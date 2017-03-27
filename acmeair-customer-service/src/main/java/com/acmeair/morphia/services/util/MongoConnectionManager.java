package com.acmeair.morphia.services.util;

import com.acmeair.morphia.BigDecimalConverter;
import com.acmeair.morphia.MorphiaConstants;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class MongoConnectionManager implements MorphiaConstants{

	private final static Logger logger = Logger.getLogger(MongoConnectionManager.class.getName());
	
	@Resource(name = JNDI_NAME)
	protected DB db;
	private static Datastore datastore;


	MongoConnectionManager(String host, String port, String database){

		Morphia morphia = new Morphia();
		// Set default client options, and then check if there is a properties file.
		boolean fsync = false;
		int w = 0;
		int connectionsPerHost = 5;
		int threadsAllowedToBlockForConnectionMultiplier = 10;
		int connectTimeout= 0;
		int socketTimeout= 0;
		boolean socketKeepAlive = true;
		int maxWaitTime = 2000;


		Properties prop = new Properties();
		URL mongoPropertyFile = MongoConnectionManager.class.getResource("/com/acmeair/morphia/services/util/mongo.properties");
		if(mongoPropertyFile != null){
			try {
				logger.info("Reading mongo.properties file");
				prop.load(mongoPropertyFile.openStream());
				fsync = new Boolean(prop.getProperty("mongo.fsync"));
				w = new Integer(prop.getProperty("mongo.w"));
				connectionsPerHost = new Integer(prop.getProperty("mongo.connectionsPerHost"));
				threadsAllowedToBlockForConnectionMultiplier = new Integer(prop.getProperty("mongo.threadsAllowedToBlockForConnectionMultiplier"));
				connectTimeout= new Integer(prop.getProperty("mongo.connectTimeout"));
				socketTimeout= new Integer(prop.getProperty("mongo.socketTimeout"));
				socketKeepAlive = new Boolean(prop.getProperty("mongo.socketKeepAlive"));
				maxWaitTime =new Integer(prop.getProperty("mongo.maxWaitTime"));
			}catch (IOException ioe){
				logger.severe("Exception when trying to read from the mongo.properties file" + ioe.getMessage());
			}
		}
		
		// Set the client options
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
			.writeConcern(new WriteConcern(w, 0, fsync))
			.connectionsPerHost(connectionsPerHost)
			.connectTimeout(connectTimeout)
			.socketTimeout(socketTimeout)
			.socketKeepAlive(socketKeepAlive)
			.maxWaitTime(maxWaitTime)
			.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);


		//Check if VCAP_SERVICES exist, and if it does, look up the url from the credentials.
		String vcapJSONString = System.getenv("VCAP_SERVICES");
		if (vcapJSONString != null) {
            logger.info("Reading VCAP_SERVICES");
            Object jsonObject = JSONValue.parse(vcapJSONString);
            JSONObject vcapServices = (JSONObject)jsonObject;
            JSONArray mongoServiceArray =null;
            for (Object key : vcapServices.keySet()){
                if (key.toString().startsWith("mongo")){
                    mongoServiceArray = (JSONArray) vcapServices.get(key);
                    break;
                }
            }

            if (mongoServiceArray == null) {
                logger.severe("VCAP_SERVICES existed, but a mongo service was not definied.");
            } else {
                JSONObject mongoService = (JSONObject)mongoServiceArray.get(0);
                JSONObject credentials = (JSONObject)mongoService.get("credentials");
                String url = (String) credentials.get("url");
                logger.fine("service url = " + url);
                MongoClientURI mongoURI = new MongoClientURI(url, builder);
                MongoClient mongo = new MongoClient(mongoURI);

                morphia.getMapper().getConverters().addConverter(new BigDecimalConverter());
                datastore = morphia.createDatastore( mongo ,mongoURI.getDatabase());
            }

        } else {
            //VCAP_SERVICES don't exist, so use the DB resource
            logger.fine("No VCAP_SERVICES found");
            if(db == null){
                try {
                    logger.warning("Resource Injection failed. Attempting to look up " + JNDI_NAME + " via JNDI.");
                    db = (DB) new InitialContext().lookup(JNDI_NAME);
                } catch (NamingException e) {
                    logger.severe("Caught NamingException : " + e.getMessage() );
                }
            }

            if(db == null){
                logger.info("Creating the MongoDB Client connection. Looking up host and port information " );
                try {
                    ServerAddress server = new ServerAddress(host, Integer.parseInt(port));
                    MongoClient mongo = new MongoClient(server);
                    db = mongo.getDB(database);
                } catch (Exception e) {
                    logger.severe("Caught Exception : " + e.getMessage() );
                }
            }

            if(db == null){
                logger.severe("Unable to retreive reference to database, please check the server logs.");
            } else {

                morphia.getMapper().getConverters().addConverter(new BigDecimalConverter());
                datastore = morphia.createDatastore(new MongoClient(db.getMongo().getConnectPoint(),builder.build()), db.getName());
            }
        }

		logger.info("created mongo datastore with options:"+datastore.getMongo().getMongoClientOptions());
	}
	
	public DB getDB(){
		return db;
	}
	
	public Datastore getDatastore(){
		return datastore;
	}

	public String getMongoVersion(){
		return datastore.getDB().command("buildInfo").getString("version");
	}
}
