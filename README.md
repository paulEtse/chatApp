# ProjetJava : a chat App made in java
## Contents
The project contains : 
* The source code of chat app : chatApp
* The source code of servlets : servlet
* The jar executable file of the chat app : chatApp.jar
* The war file of servlet that can be deployed in a tomcat server : servlet.war
* The documentation of the project : chat.pdf
* The file to import to create the database structure : sql.txt

## Compilation 
To make this app runnable, we need to compile sources codes. We use maven to manage dependencies so the command are
```
maven clean 
maven build
maven assembly:single **For the chatApp**
maven install **For the servlet**
```

## Execution
To run the app, all we need is jdk > 1.8 and the command is 
```
java -jar chatApp.jar
```
The app can work with or without servlet.
To run the project with servlets, we just need set up the global variable subcription to true in the Global.java. This file contains global configurations of the project.

## Test
You can test the app on two (or more) machines by following these steps:
* install java in the machin
* download the chatApp.jar file
* In your terminal, navigate to the folder that contains the downloaded chatApp.jar file and type the command
```java -jar chatApp.jar```
The app will be lunched on the machine in a new windows, you can then choose your login. The app will show you connected users. You can select any of them and start chatting with him. You will get the history if your old converstations with that user.
You can also exchange files.
