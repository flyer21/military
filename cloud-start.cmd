start mvn spring-boot:run -f eureka-server
ping 127.0.0.1 -n 5 > null
start mvn spring-boot:run -f config-server
ping 127.0.0.1 -n 15 > null
start mvn spring-boot:run -f oauth2-server
start mvn spring-boot:run -f zuul-server
