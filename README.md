
##
How to use this app:

**1. Open the project using the IDE:**
```
   git clone https://github.com/JolaPsh/forum-ua.git
```
**2. Execute initial scripts to create database schema and generate test data:**
```
   - /resources/initial.sql;
   - /resources/populate.sql;
   - /resources/functions.sql.
```
**3. To integrate Facebook and Google login into your app, configure your social-cfg.properties:**
 - to obtain client credentials for Facebook OAuth2 authentication, go to <a href="https://developers.facebook.com, add a new application">Facebook Graph API</a>. Expand the Setting menu and select Basic. Here you can find the App ID and  App Secret.
 Copy app-id and app-secret field and paste it in the social-cfg.properties;
 - to obtain client credentials for Google OAuth2 authentication, go to <a href="https://console.developers.google.com">Google API Console</a>, section “Credentials”. Do the same mentioned above.
 
**4. Set up SSL for Tomcat server:**

```
   - create the keystore (use Java Keytool - open cmd, go to ~java/jdk/bin - enter keytool);
   - create the certificate signing request;
   - install your certificate;
   - configure Tomcat to use SSL, find server.xml and change your connector settings:

     <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" SSLEnabled="true" 
     scheme="https" secure="true" clientAuth="false" keystoreFile="conf/localhost.jsk" keystorePass="password" sslProtocol="TLS"
     redirectPort="8443" />
```
**5. Run your program or just execute it with Maven. After launching the program, open https://localhost:8080/.**

**6. Use Postman to test REST API. See also examples of curl commands REST_req.txt.**
