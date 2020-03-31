# DC2060
DC2060 Team Project

Tech Stack:

- XAMPP Web Server (primarily for the tomcat host) https://www.apachefriends.org/index.html
- MySQL Database https://dev.mysql.com/downloads/installer/ (You can make use of the packaged mysql/maria db in XAMPP, I just like MySQL Workbench as a db access tool and installed it all in one go)
- Java 8
- Maven WebApp

Tools Used:
- Eclipse IDE
  - Maven M2E plugin
  - SonarLint plugin
- GitHub Desktop
- MySQL Workbench
- Apache Maven (installed for the cli compliation) https://maven.apache.org/install.html


Installation:

- Install Apache Maven as per the instructions at the address listed
(you can test it is correctly installed by opening CMD and running the command 'mvn -version'
(example output:
	
	D:\Development\Aston\DC2060\DC2060\reachout>mvn -version
	Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T20:00:29+01:00)
	Maven home: C:\Users\John\apache-maven-3.6.1\bin\..
	Java version: 1.8.0_212, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk1.8.0_212\jre
	Default locale: en_GB, platform encoding: Cp1252
	OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
)

- Install XAMMP
- Install MySQL Workbench (optional)
- Install whatever IDE you are happy with for Java development

== Database ==
- Setup a mysql database with root user. The mysql installer should guide you through this.
- Use the packaged sql file databaseCreate to build the application's schema, user and tables.
(That can be done through the CLI or mysql workbench, whatever suits you)

== Compilation ==
- Clone the github repo locally.
- Open cmd and navigate to the application
- Compile the war file with the command 'mvn clean install -DskipTests'

(example output:
	D:\Development\Aston\DC2060\DC2060\reachout>dir                           <-- executing 'dir' to show you where I am in my directory and what files I can see'
	
	Volume in drive D has no label.
	Volume Serial Number is 1800-472A

	Directory of D:\Development\Aston\DC2060\DC2060\reachout

	29/03/2020  17:41    <DIR>          .
	29/03/2020  17:41    <DIR>          ..
	29/03/2020  17:40             1,366 .classpath
	25/03/2020  01:05                40 .gitignore
	29/03/2020  17:31             1,084 .project
	24/03/2020  21:52    <DIR>          .settings
	29/03/2020  17:34             4,367 pom.xml
	24/03/2020  21:24    <DIR>          src
	29/03/2020  17:41    <DIR>          target
	24/03/2020  21:53    <DIR>          WebContent
               4 File(s)          6,857 bytes
               6 Dir(s)  380,064,186,368 bytes free

	D:\Development\Aston\DC2060\DC2060\reachout>mvn clean install -DskipTests  				<-- executing maven compile
	
	....... BUNCH OF STUFF REMOVED FOR BREVITY .......
	[INFO] --- maven-install-plugin:2.4:install (default-install) @ reachout ---
    [INFO] Installing D:\Development\Aston\DC2060\DC2060\reachout\target\ReachOut.war to C:\Users\John\.m2\repository\com\aston\dc2060\reachout\0.0.1-SNAPSHOT\reachout-0.0.1-SNAPSHOT.war
	[INFO] Installing D:\Development\Aston\DC2060\DC2060\reachout\pom.xml to C:\Users\John\.m2\repository\com\aston\dc2060\reachout\0.0.1-SNAPSHOT\reachout-0.0.1-SNAPSHOT.pom
	[INFO] Installing D:\Development\Aston\DC2060\DC2060\reachout\target\ReachOut-classes.jar to C:\Users\John\.m2\repository\com\aston\dc2060\reachout\0.0.1-SNAPSHOT\reachout-0.0.1-SNAPSHOT-classes.jar
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD SUCCESS
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time:  3.599 s
	[INFO] Finished at: 2020-03-29T17:53:31+01:00
	[INFO] ------------------------------------------------------------------------
)


- Navigate into new target directory and check there are two compiled files. ReachOut.war and Reachout-class.jar 
We only need the file ReachOut.war later

== Deployment ==
- Start XAMMP
- Start Apache Tomcat
- Configure tomcat for first startup. ( you need to create an admin user, although it has been a while since I did this hopefully it guides you.)
- Possible reboot of XAMMP for changes to take effect
- With Tomcat running, click Admin
- Click 'Manager App'
- Scroll down to Deploy
- Use 'WAR file to deploy'
- Find the file ReachOut.war seen earlier.
- Deploy that file.
- A new entry will appear in the table above.
- Click the link /ReachOut
- Will probably 404 as there is nothing at the address 'http://localhost:8080/Reachout/'
- Change the URL to 'http://localhost:8080/ReachOut/home'
- Should get a pretty generic looking landing page with a bunch of lorem ipsum text filler and links that go nowhere.
- Check the url 'http://localhost:8080/ReachOut/signup'
- The registration form will accept any input and then validate against some prebuilt checks in the class SignupValidator but not result in a user being created yet.




== Misc ==

- If you receive the following exception from the mysql database then you need to edit your mysql config file.
java.sql.SQLException: The server timezone value <Whatever your pc is set to> is unrecognized or represents more than one timezone. You must configure either the server or JDBC driver.... etc.
Mine was stored at C:\ProgramData\MySQL\MySQL Server 8.0\my.ini
Add the following line to the bottom and reboot mysql
default-time-zone='+00:00'




== Development ==

- Logging in jUnits
Log at INFO level. This will be caught by the test log4j config, everything else is suppressed as the supporting libraries like spring and hibernate log a LOT of stuff we don't care about for tests

