package com.chatapp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class ServerServices {
	private static final OkHttpClient client= new OkHttpClient();
    public static DefaultListModel<User>  sendGetRequest() throws MalformedURLException, IOException, ClassNotFoundException{
        Request request = new Request.Builder()
                .url("https://srv-gei-tomcat.insa-toulouse.fr/serverPaul-1.0-SNAPSHOT/paul")
                .build();
        Call call= client.newCall(request);
        Response response= call.execute();
        ObjectInputStream in = new ObjectInputStream(response.body().byteStream());
        System.out.println(response.body().string());
        return (DefaultListModel<User>) in.readObject();
}
    public static void sendPostRequest(User u) throws JsonProcessingException, IOException{
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      ObjectMapper mapper = new ObjectMapper();
      String json= mapper.writeValueAsString(u);
      System.out.println(json);
        RequestBody body = RequestBody.create(JSON, json);
        Request resquest = new Request.Builder()
                .url("https://srv-gei-tomcat.insa-toulouse.fr/serverPaul-1.0-SNAPSHOT/paul")
                .post(body)
                .build();
        Call call = client.newCall(resquest);
        Response response=call.execute();
    }
    //Not used yet
    public static void sendPutRequest(User u) throws JsonProcessingException{
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      ObjectMapper mapper = new ObjectMapper();
      String json= mapper.writeValueAsString(u);
        RequestBody body = RequestBody.create(JSON, json);
        Request resquest = new Request.Builder()
                .url("https://srv-gei-tomcat.insa-toulouse.fr/serverPaul-1.0-SNAPSHOT/paul")
                .put(body)
                .build();
    }
    public static void sendDeleteRequest(User u) throws JsonProcessingException{
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            ObjectMapper mapper = new ObjectMapper();
            String json= mapper.writeValueAsString(u);
            System.out.println(json);
            RequestBody body = RequestBody.create(JSON, json);
            Request resquest = new Request.Builder()
                    .url("https://srv-gei-tomcat.insa-toulouse.fr/serverPaul-1.0-SNAPSHOT/paul")
                    .delete(body)
                    .build();
            Call call = client.newCall(resquest);
            Response response=call.execute();
        } catch (IOException ex) {
            Logger.getLogger(ServerServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
