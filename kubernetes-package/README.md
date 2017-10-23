  Install fabric8 first
        
      mvn fabric8:install
  
  After fabric8 & minikube download and installed,convert docker compose yaml to kubernetes deploy yaml
      
      mvn package

  can find convert file in target\classed\META-INF\fabric8\kubernetes.yml