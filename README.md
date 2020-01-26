# ProjetJava: a chat App made in java
# Description 
This is a project of an application for chating in local network. Each active user can discover connected users in his local network and exchange text or file with them. The historic of the conversations are stored in a database. The system is totally decentralised except of the database that is common to all users. The project has an extension using servlet in other to allow a user use system without being in the local network. For more detail follow this link [specification.pdf](https://moodle.insa-toulouse.fr/pluginfile.php/127921/mod_resource/content/1/INSA_COO_POO_URD_v3.1.pdf).
## Contents
The project contains: 
* The source code of chat app: **chatApp**
* The source code of servlets: **servlet**
* The jar executable file of the chat app: **chatApp.jar**
* The war file of servlet that can be deployed in a tomcat server: **servlet.war**
* The documentation of the project: **chat.pdf**
* The file to import to create the database structure: **sql.txt**
## Compilation 
To make this app runnable, we need to compile the source code of the app and create the executable jar file. Maven has been used to manage dependencies so the command are: 
```
maven clean 
maven build
maven assembly:single
```
## Execution
To run the app, all we need is jdk > 1.8 and the command is:
```
java -jar chatApp.jar
```
The app can work with or without servlet.
To run the project with servlets, we just need set up the global variable subcription to true in the Global.java. This file contains global configurations of the project.
> subcription = true<br>

## Test
You can test the app on two (or more) machines by following these steps:
* Install java in the machine
* Download the chatApp.jar file
* In your terminal, navigate to the folder that contains the downloaded chatApp.jar file and type the command
```java -jar chatApp.jar```
The app will be lunched in a new windows, you can then choose your login. The app will show connected users. You can select any of them and start chatting with him. You will get the history if your old converstions with that user.
You can also exchange files.
## Question
For any question or suggestion contact [Paul Etse](mailto:etse@etud.insa-toulouse.fr) & [Chaaran](nalakala@etud.insa-toulouse.fr)
