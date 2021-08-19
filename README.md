# Payment-Management-System

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)

---

## Used tools and patterns for project implementation

`Primary:` SpringBoot 2.5, MySQL 5.7, FlyWay 7.5.1, Java 15 (Core + Servlets + JDBC + JSP), JSTL, Maven 3, HTML CSS, JS, jQuery v3.4, Bootstrap v4.4.    
`Secondary:` Apache Tomcat v9.0, Apache Commons Lang v2.6, Apache Log4j v1.2, JUnit v4.13, Mockito v1.10.    
<i>*For more information on dependencies and plugins, see the pom.xml file.</i>

When implementing business logic algorithms, I used GoF patterns (Singleton, Builder, Factory Method, Command, Strategy), as well as MVC and DAO patterns.

## Initialization and launch of the project

For the correct launch and stable operation of the site (locally) you need to:
- Download and uncompress the .zip archive of this repository. I recommend cloning a project (if you know Git):  
  `$ git clone https://github.com/kuan1701/Payment-Management-System`
- Specify the correct path to the JDK home folder in the IDE.
- Connect, configure and start your database server.
- Import the database dump from the /resources/sql/dump.sql file (substituting your DB name).
- Specify the correct database connection parameters in the /webapp/META-INF/context.xml file
- Uncomment the block of code needed for your purposes in the /persistence/dao/impl/QueryExecutor.java file (contains the basic methods for working with the DB).
- Run the project build command from the root folder: `$ mvn clean package`
- Connect and configure your web server to deploy the project locally.
- Verify that all components are configured correctly and that all services are running.
- In the browser, go to this address: `http://localhost:8080/`

If you know Docker, use these commands by running them from the root folder:  

Running it from the root folder:  
&nbsp;&nbsp;&nbsp;&nbsp;`$ docker-compose up --build -V`

I advise you to use the first option with manual installation and configuration.

## Support

Patches are welcome and can be submitted by forking this project and submitting a pull request via GitHub.  
Please see [CONTRIBUTING.md](../master/CONTRIBUTING.md) for more details.

---  

I hope my project will help you! Communication with me: https://t.me/kuan1701
