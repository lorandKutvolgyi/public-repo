cd %~dp0
java -jar lib\liquibase.jar --driver=com.mysql.jdbc.Driver --classpath=lib\mysql-connector-java-5.1.6.jar --changeLogFile=resources\db.changelog.xml --url="jdbc:mysql://localhost:3306/Bible?useUnicode=true&characterEncoding=UTF-8" --username=root --password=root update
