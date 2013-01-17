* Assume that Maven 3 is installed
* Assume that Apache Tomcat 7 is installed and running on `localhost:8080`
* Assume Postgres 9.2 is installed and running on `localhost:5432`
* Other versions of Postgres might work. Maven, Tomcat: Other versions will probably not work.

* `cd ~/.m2`
* (Create and) edit `settings.xml`
* Insert the following:

		<settings>
		  <servers>
			<server>
			  <id>tomcat</id>
			  <username>admin</username>
			  <password>adminadmin</password>
			</server>
		  </servers>
		</settings>


* `cd ApacheTomcatInstallationDirectory/conf`
* Edit `tomcat-users.xml`
* Insert

		<role rolename="system"/>
		<role rolename="admin"/>
		<role rolename="admin-gui"/>
		<role rolename="manager"/>
		<role rolename="manager-gui"/>
		<user username="admin" password="adminadmin" roles="system,manager,manager-gui,admin,admin-gui"/>
  
* If Tomcat is not running locally or you would like to change the deployment URL, change the following lines in `pom.xml`

		<plugin>
		  <groupId>org.codehaus.mojo</groupId>
		  <artifactId>tomcat-maven-plugin</artifactId>
		  <version>1.1</version>
		  <configuration>
			<url>http://localhost:8080/manager/html</url>
			<server>tomcat</server>
			<path>/crowdsourcing</path>
			</configuration>
		  </plugin>
	  
* Beware that for reasons of simplicity and testing, we deployed a static rss file called `yahoofeed` on `crowdsourcing/resources/yahoofeed.rss`
* Therefore, if you change the deployment path from `/crowdsourcing`, be aware that this file will not be found by the application. The reason for inserting this fake RSS feed is that sometimes, there aren't any companies found in the new articles. So we made a feed that would make sure we would find companies.

* Open a Postgres administration console and execute our `create-db.sql` script line by line. But **attention**: Read it first! It drops a database and recreates it!
* If there is already an application running with the name `crowdsourcing` on Tomcat, go to `localhost:8080/manager/html` with credentials `admin:adminadmin` and undeploy it
* Go to the directory with our `pom.xml` and type `mvn tomcat:deploy`
* To undeploy the application from Tomcat, goto `localhost:8080/manager/html` with credentials `admin:adminadmin` and undeploy it
* To open the project in Eclipse, type `mvn eclipse:eclipse` and then import it using the Eclipse Import Wizard