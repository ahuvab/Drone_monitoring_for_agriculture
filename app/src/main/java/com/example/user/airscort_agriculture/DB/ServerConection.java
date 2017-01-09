package com.example.user.airscort_agriculture.DB;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ServerConection {

    private final String URL="http://agri-server.cloudapp.net/";
    private RequestQueue queue;
    private Context context;
    private ProgressDialog progressDialog;
    private boolean emailExist;
    private LatLng droneLocation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private LocalDB localDB;
    private boolean successDeleteing, successStopScan,successStartScan, successAdd, successLogin;


    public ServerConection(Context c) {
        context=c;
        queue= Volley.newRequestQueue(c);
        emailExist=false;
        sharedPreferences = c.getSharedPreferences(c.getString(R.string.user_details), c.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        localDB=new LocalDB(c);
    }


    /* return the current drone location */
    public LatLng getDronrLocation(int userId){
        progressDialog = ProgressDialog.show(context, "Connecting", "Please wait", true);
        String currUrl = URL+"api/agri/app/user/"+userId+"/drone_location";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, currUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double lat=response.getJSONObject("drone_home").getDouble("latitude");
                            double lon=response.getJSONObject("drone_home").getDouble("longitude");
                            droneLocation=new LatLng(lat,lon);
                            Log.e("ahuva", droneLocation.latitude+", "+ droneLocation.longitude);
                        }
                        catch (JSONException e) {
                            Log.e("ahuva", "exeption");
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        queue.add(request);
        request.setTag("GET_DRONE_LOCATOIN");

        return droneLocation;
    }


    /* add new field to user*/
    public boolean addField(final int userId, final String fieldName, final int distance,  final ArrayList<LatLng> frameVector,
                                                    final ArrayList<LatLng> dronePathVector){
        progressDialog = ProgressDialog.show(context, "Add field", "Please wait", true);
        String currUrl = URL+"api/agri/app/field";
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("success").equals("true")){
                                Log.e("ahuva", "success2");
                                successAdd=true;
                            }
                            else{
                                Log.e("ahuva", "success3");
                                successAdd=false;
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successAdd=false;
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return paramForAddField(userId,fieldName, distance, frameVector,dronePathVector);
            }
        };
        queue.add(postRequest);
        Log.e("ahuva", successAdd+"");
        return successAdd;
    }

    /* order the parameters for adding field in currect pattern*/
    private Map<String, String> paramForAddField(final int userId, final String fieldName, final int distance,
                                                 final ArrayList<LatLng> frameVector,final ArrayList<LatLng> dronePathVector){
        Map<String, String>  params = new HashMap<>();
        params.put("user_id", String.valueOf(userId));
        params.put("field_name", fieldName);
        params.put("field_area", String.valueOf(distance));

        JSONArray jsonArrScan=new JSONArray();
        try {
            for(int i=0; i<dronePathVector.size(); i++) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("latitude",dronePathVector.get(i).latitude );
                jsonObject.put("longitude",dronePathVector.get(i).longitude );
                jsonArrScan.put(jsonObject);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        params.put("scan_vector", jsonArrScan.toString());

        JSONArray jsonArr=new JSONArray();
        try {
            for(int i=0; i<frameVector.size(); i++) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("latitude",frameVector.get(i).latitude );
                jsonObject.put("longitude",frameVector.get(i).longitude );
                jsonArr.put(jsonObject);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        params.put("field_vector", jsonArr.toString());
        Log.e("ahuva", params.toString());

        return params;
    }

    /* delete field from db*/
    public boolean deleteField(int fieldID){
        String currUrl = URL+"api/agri/app/field/"+fieldID+"/delete ";
        return delete(currUrl);
    }

    /* add new user*/
    public boolean addUser(final String email, final String fName, final String lName, final String password, final String stationId){
        progressDialog = ProgressDialog.show(context, "Connecting", "Please wait", true);
        String currUrl = URL+"api/agri/app/add_user";
        StringRequest postRequest = new StringRequest(Request.Method.POST, currUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                            //TODO: get user_id & home_point and save in shared preferences
                        //  spEditor.putFloat(context.getString(R.string.latitude_drone_home), 0);
                    //    spEditor.putFloat(context.getString(R.string.longtitude_drone_home), 0);
                    //    spEditor.apply();
                          Log.e("ahuva", response.toString());
                        successAdd=true;
                        progressDialog.dismiss();


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return paramForAddUser(email, fName, lName, password, stationId);
            }
        };
        queue.add(postRequest);
        return successAdd;

    }

    /* order the parameters for adding user in currect pattern*/
    private Map<String, String> paramForAddUser(final String email, final String fName, final String lName, final String password, final String stationId){
        Map<String, String>  params = new HashMap<>();
        params.put("email", email);
        params.put("firstName", fName);
        params.put("lastName", lName);
        params.put("password", password);
        params.put("stationId", stationId);

        Log.e("ahuva", params.toString());

        return params;
    }

    /* delete user */
    public void deleteUser(){

    }


    /* start new scanning. sending user id and which fields to scan*/
    public boolean startScanning(final int userID, final ArrayList<Integer> fieldsId){
        progressDialog = ProgressDialog.show(context, "Connecting", "Please wait", true);
        String currUrl = URL+"api/agri/startMission";
        StringRequest postRequest = new StringRequest(Request.Method.POST, currUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("success").equals("true")){
                                successStartScan=true;
                            }
                            else{
                                successStartScan=false;
                            }
                            progressDialog.dismiss();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successStartScan=false;
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return paramForStartScan(userID, fieldsId);
            }
        };
        queue.add(postRequest);
        return successStartScan;
    }

    /* order the parameters for adding field in currect pattern*/
    private Map<String, String> paramForStartScan(final int userId,  final ArrayList<Integer> fieldsId){
        Map<String, String>  params = new HashMap<>();
        params.put("user_id", String.valueOf(userId));
        JSONArray jsonArr=new JSONArray();
        try {
            for(int i=0; i<fieldsId.size(); i++) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("field_id", fieldsId.get(i));
                jsonArr.put(jsonObject);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        params.put("field_vector", jsonArr.toString());
        Log.e("ahuva", params.toString());

        return params;
    }


    /* stop the current scanning */
    public boolean stopScanning(int missionID){
        String currUrl = URL+"api/agri/mission/"+missionID+"/delete";
        return delete(currUrl);
    }

    /* return if the email has already exist*/
    public boolean ifEmailExist(String email){
        progressDialog = ProgressDialog.show(context, "Connecting", "Please wait", true);
        String currUrl = URL+"api/agri/app/is_mail_exists/:"+email;
        StringRequest request = new StringRequest(Request.Method.GET, currUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.equals("false")){
                            emailExist=false;
                        }
                        else{
                            emailExist=true;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                emailExist=false;
                Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        queue.add(request);
        request.setTag("IF_EMAIL_EXIST");
        return emailExist;
    }


    private boolean delete(String CurrUrl){
        progressDialog = ProgressDialog.show(context, "Connecting", "Please wait", true);
        StringRequest postRequest = new StringRequest(Request.Method.POST, CurrUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")){
                            successDeleteing=false;
                            Log.e("ahuva", "0");
                        }
                        else if(response.equals("1")){
                            successDeleteing=true;
                            Log.e("ahuva", "1");
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successDeleteing=false;
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        );
        queue.add(postRequest);
        return successDeleteing;
    }

    /* confirm the login details and return all user details to save in local DB*/
    public boolean login(String email, String password){
        progressDialog = ProgressDialog.show(context, "Connecting", "Please wait", true);
        String currUrl = URL+"api/agri/app/login/"+email+"/"+password;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, currUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (!response.isNull("error")) {       //if the login failed
                            successLogin = false;
                            Toast.makeText(context, context.getText(R.string.error_login), Toast.LENGTH_LONG).show();                        }
                        else{
                            successLogin=true;
                            getLoginParmeters(response);
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                successLogin = false;
                Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        queue.add(request);
        request.setTag("LOGIN");
        return successLogin;
    }

    public void getLoginParmeters(JSONObject response){
        try {
            JSONObject user=response.getJSONObject("user");       //save user detail in shared preferences
            login_getUserDetails(user);

            JSONObject drone=response.getJSONObject("drone");    //save home point in shared preferences
            login_getDroneDetails(drone);

            JSONArray fields=response.getJSONArray("fields");    //save user's fields in local DB
            login_getUserFields(fields);

            JSONArray history=response.getJSONArray("missions");   //save history and current mission in local DB
            login_getHistoryDetails(history);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void login_getUserDetails(JSONObject user){
        try{
            spEditor.putString(context.getString(R.string.f_name), user.getString("first_name"));
            spEditor.putString(context.getString(R.string.l_name), user.getString("last_name"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void login_getDroneDetails(JSONObject drone){
    }

    public void login_getUserFields(JSONArray fields){
        try {
            for (int i = 0; i < fields.length(); i++) {
                JSONObject json=fields.getJSONObject(i);
                String name=json.getString("field_name");


                float distance=(float)json.getDouble("field_area");

                JSONArray arr=json.getJSONArray("field_vector");
                ArrayList <Double> latFrame=new ArrayList();
                ArrayList <Double> lonFrame=new ArrayList();

                for(int j=0; j<arr.length(); j++){
                    latFrame.add(arr.getJSONObject(j).getDouble("latitude"));
                    lonFrame.add(arr.getJSONObject(j).getDouble("longitude"));
                }
                int id=json.getInt("field_id");

                //TODO: add drone path
//                localDB.addField(name,latFrame.toString(), lonFrame.toString(), ???,???, distance);     //add user fields to local DB
//                localDB.setFieldID(id, name);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void login_getHistoryDetails(JSONArray history){
        try {
            for (int i = 0; i < history.length(); i++) {
                JSONObject json=history.getJSONObject(i);
                int missionID=json.getInt("mission_id");

                JSONArray arr=json.getJSONArray("field_vector");
                ArrayList <Integer> fieldsList=new ArrayList();
                for(int j=0; j<arr.length(); j++){
                    fieldsList.add(arr.getJSONObject(j).getInt("field_id"));
                }

                String date=json.getString("creation_date");

                //TODO:
//                localDB.addHistory(date,fieldsList.toString());                //add history to local DB
//                localDB.setMissionId(missionID,date,fieldsList.toString());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
