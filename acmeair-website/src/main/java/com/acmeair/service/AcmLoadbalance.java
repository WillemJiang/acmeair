package com.acmeair.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import io.servicecomb.config.ConfigUtil;
import io.servicecomb.config.archaius.sources.MicroserviceConfigLoader;
import io.servicecomb.serviceregistry.RegistryUtils;
import io.servicecomb.serviceregistry.ServiceRegistry;
import io.servicecomb.serviceregistry.cache.CacheEndpoint;
import io.servicecomb.serviceregistry.cache.InstanceCache;
import io.servicecomb.serviceregistry.cache.InstanceCacheManager;
import io.servicecomb.serviceregistry.config.ServiceRegistryConfig;
import io.servicecomb.serviceregistry.definition.MicroserviceDefinition;
import io.servicecomb.serviceregistry.registry.ServiceRegistryFactory;

public class AcmLoadbalance {
  private static final Logger LOG = LoggerFactory.getLogger(AcmLoadbalance.class);

  private AtomicInteger index = new AtomicInteger(0);

  private InstanceCache instanceCache;

  private InstanceCacheManager instanceCacheManager;

  private long starttime = new Date().getTime();

  public AcmLoadbalance(String microServicename) {
    instanceCache = new InstanceCache(RegistryUtils.getMicroservice().getAppId(), microServicename, "latest", null);
    EventBus eventBus = new EventBus();
    MicroserviceConfigLoader loader = ConfigUtil.getMicroserviceConfigLoader();
    MicroserviceDefinition microserviceDefinition = new MicroserviceDefinition(loader.getConfigModels());
    ServiceRegistry serviceRegistry =
        ServiceRegistryFactory.getOrCreate(eventBus, ServiceRegistryConfig.INSTANCE, microserviceDefinition);
    serviceRegistry.init();
    instanceCacheManager = new InstanceCacheManager(eventBus, serviceRegistry);
  }

  private InstanceCache find() {
    InstanceCache newCache = instanceCacheManager.getOrCreate(instanceCache.getAppId(),
        instanceCache.getMicroserviceName(), instanceCache.getMicroserviceVersionRule());
    if (instanceCache.cacheChanged(newCache)) {
      this.instanceCache = newCache;
    }

    return instanceCache;
  }

  public String choose() {
    try {
      // 每隔30s刷新一次缓存，以免远端部署节点实例信息变化。
      long endtime = new Date().getTime();
      if (endtime - starttime / 1000 > 30 && null != instanceCacheManager) {
        // cache can not auto update,so when use it,we need clean it.
        instanceCacheManager.cleanUp();
        starttime = new Date().getTime();
      }
      Map<String, List<CacheEndpoint>> addresses = find().getOrCreateTransportMap();
      List<CacheEndpoint> address = addresses.get("rest");
      if (address == null) {
        return null;
      }

      int c = (index.getAndIncrement()) % address.size();
      if (c < 0) {
        c = -c;
      }
      String r = address.get(c).getEndpoint();
      URI uri = new URI(r);
      return uri.getAuthority();
    } catch (URISyntaxException e) {
      LOG.error("URL not valid from loadbalancer");
      return null;
    }
  }
}
