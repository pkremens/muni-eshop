run jboss
mvn clean package -DskipTests=true jboss-as:deploy
localhost:8080/web/index.html
