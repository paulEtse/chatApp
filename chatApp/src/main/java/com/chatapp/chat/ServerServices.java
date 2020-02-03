package com.chatapp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class ServerServices {
	private static final OkHttpClient client= new OkHttpClient();
    public static User[]  sendGetRequest() throws MalformedURLException, IOException, ClassNotFoundException{
        Request request = new Request.Builder()
                .url(Global.ServletUrl)
                .build();
        Call call= client.newCall(request);
        Response response= call.execute();
        String json= response.body().string();
        Gson gson = new Gson();
        NewsDTO data= gson.fromJson(json, NewsDTO.class);
        if (data.delegate!=null)
        	return data.delegate;
        else 
        	return null;
}
    public static void sendPostRequest(User u) throws JsonProcessingException, IOException{
      MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      ObjectMapper mapper = new ObjectMapper();
      String json= mapper.writeValueAsString(u);
      System.out.println(json);
        RequestBody body = RequestBody.create(JSON, json);
        Request resquest = new Request.Builder()
                .url(Global.ServletUrl)
                .post(body)
                .build();
        Call call = client.newCall(resquest);
        Response response=call.execute();
    }
    public static void sendPutRequest(User u) throws JsonProcessingException{
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
      ObjectMapper mapper = new ObjectMapper();
      String json= mapper.writeValueAsString(u);
        RequestBody body = RequestBody.create(JSON, json);
        Request resquest = new Request.Builder()
                .url(Global.ServletUrl)
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
class NewsDTO
{
  String status; 
  int totalResults;
  public User[] delegate;
}
