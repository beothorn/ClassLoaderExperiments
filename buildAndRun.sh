cd server
mvn clean package
cp target/server-1.0-SNAPSHOT-jar-with-dependencies.jar ../bridge/src/main/resources/
cd ..

cd client
mvn clean package
cp target/client-1.0-SNAPSHOT-jar-with-dependencies.jar ../bridge/src/main/resources/
