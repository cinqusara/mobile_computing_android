package com.example.maledettatreest2;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CommunicationController {

    private static final String MTE_LOG = "MTE_LOG";
    private static String BASE_URL="https://ewserver.di.unimi.it/mobicomp/treest/";

    private static String REGISTER_URL=BASE_URL+"/register.php";
    private static String GET_PROFILE_URL=BASE_URL+"/getProfile.php"; //sid
    private static String SET_PROFILE_URL=BASE_URL+"/setProfile.php"; //sid, name, picture
    private static String GET_USER_PICTURE_URL=BASE_URL+"/getUserPicture.php"; //sid, uid

    private static String GET_LINES_URL=BASE_URL+"/getLines.php"; //sid
    private static String GET_STATIONS_URL=BASE_URL+"/getStations.php"; //sid, did (identificativo tratta)

    private static String GET_POSTS_URL=BASE_URL+"/getPosts.php"; //sid, did (identificativo tratta)
    private static String ADD_POSTS_URL=BASE_URL+"/addPost.php"; //sid, did, delay, status, comment

    private static String FOLLOW_URL=BASE_URL+"/follow.php"; //sid, uid
    private static String UNFOLLOW_URL=BASE_URL+"/unfollow.php"; //sid, uid


    private RequestQueue queue = null;
    public CommunicationController(Context context){
        queue= Volley.newRequestQueue(context);
    }



    public void register(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                null,
                responseListener,
                errorListener
        );
        queue.add(req);
    }


    public void getProfile(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                GET_PROFILE_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }

    public void setProfile(String sid, String name, String picture, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("name", name);
            objParam.put("picture", picture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                SET_PROFILE_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }


    public void getUserPicture(String sid,String uidUtenteDaCercare, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("uid", uidUtenteDaCercare);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                GET_USER_PICTURE_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }



    public void getLines(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                GET_LINES_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }


    public void getStations(String sid, String didTratta, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("did", didTratta);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                GET_STATIONS_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }


    public void getPosts(String sid, String didTratta, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("did", didTratta);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                GET_POSTS_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }

    public void addPost(String sid, String didTratta, JSONObject campiPost, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) throws JSONException {
        Log.d(MTE_LOG, "add post");
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("did", didTratta);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator itr = campiPost.keys();

        while(itr.hasNext()) {
            String key = (String) itr.next();
            objParam.put(key, campiPost.getString(key));
        }
        Log.d(MTE_LOG, "objParam    "+ objParam.toString());


        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                ADD_POSTS_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }

    public void follow(String sid,String uidUtenteDaSeguire, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("uid", uidUtenteDaSeguire);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                FOLLOW_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }


    public void unfollow(String sid,String uidUtenteDaSmettereDiSeguire, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject objParam = new JSONObject();
        try {
            objParam.put("sid", sid);
            objParam.put("uid", uidUtenteDaSmettereDiSeguire);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                UNFOLLOW_URL,
                objParam,
                responseListener,
                errorListener
        );
        queue.add(req);
    }

}
