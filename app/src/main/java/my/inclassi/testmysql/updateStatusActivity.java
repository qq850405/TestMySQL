package my.inclassi.testmysql;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class updateStatusActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String url ;

    String activityNo,status,nextNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);




        Bundle bundle = getIntent().getExtras();
        activityNo = bundle.getString("activityNo");
        status = bundle.getString("status");
        nextNo = bundle.getString("nextNo");
        Log.d("activityNo",activityNo);
        Log.d("00status",status);



        if(status.equals("ready")) {
            url = "http://140.135.112.8/CollectionSystem/app/updateStatus.php";
        }else{
            url = "http://140.135.112.8/CollectionSystem/app/updateFinishedStatus.php";

        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("activityNo", activityNo);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        if(!nextNo.equals("end")&&status.equals("in progress")){
            url = "http://140.135.112.8/CollectionSystem/app/updateReadyStatus.php";



            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nextNo", nextNo);

                    return params;
                }
            };
            requestQueue.add(stringRequest);

        }

        finish();


    }












    }
