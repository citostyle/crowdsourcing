* Assume that Maven 3 is installed
* Assume that Apache Tomcat 7 is installed and running on `localhost:8080`
* Assume Postgres 9.2 is installed
* Other versions of postgres might work. With Maven, Tomcat I'm not so sure

* `cd ~/.m2`
* Edit settings.xml
* Insert the following:

    <settings>
	  <servers>
        <server>
          <id>localhost-tomcat</id>
          <username>admin</username>
          <password>adminadmin</password>
        </server>
	  </servers>
	</settings>


* `cd ApacheTomcatInstallationDirectory/conf`
* Edit tomcat-users.xml
* Insert

    <role rolename="system"/>
    <role rolename="admin"/>
    <role rolename="admin-gui"/>
    <role rolename="manager"/>
    <role rolename="manager-gui"/>
    <user username="admin" password="adminadmin" roles="system,manager,manager-gui,admin,admin-gui"/>
  
* If tomcat is not running locally or you would like to change the deployment URL, change the pom.xml and change the lines

    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>tomcat-maven-plugin</artifactId>
      <configuration>
        <url>http://localhost:8080/</url>
        <server>localhost-tomcat</server>
        <path>/aic-crowdsourcing</path>
        </configuration>
      </plugin>

* Open a postgres administration console and execute our `create-db.sql` script line by line. But **attention**: Read it first! It drops a database and recreates it!
* If there is already an application running with the name `aic-crowdsourcing` on Tomcat, go to `localhost:8080/manager/html` with credentials `admin:adminadmin` and undeploy it
* Go to the directory with our `pom.xml` and type `mvn tomcat:deploy`
* To undeploy the application from Tomcat, goto `localhost:8080/manager/html` with credentials `admin:adminadmin` and undeploy it