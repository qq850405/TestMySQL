package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class updateStatusActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String url ;
    public String flag;
    public String activityNo,status,nextNo,manuNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);


        final Bundle[] bundle = {getIntent().getExtras()};
        activityNo = bundle[0].getString("activityNo");
        status = bundle[0].getString("status");
        manuNo = bundle[0].getString("manuNo");
        nextNo = bundle[0].getString("nextNo");
        Log.d("activityNo",activityNo);
        Log.d("00status",status);



        if(status.equals("ready")) {
            url = "http://140.135.112.8/CollectionSystem/app/updateStatus.php";
        }else{
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "123");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "你好"+"\n製令編號:"+manuNo+"\n請查看任務清單，有問題回電，謝謝");
            startActivity(Intent.createChooser(sharingIntent, "1234"));
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
        if(!nextNo.equals("end")&& status.equals("ready")){
            final String[] flag_new = {""};
            flag="";
            Log.d("",flag+"有進來");
            url = "http://140.135.112.8/CollectionSystem/app/finishedStoreSearch.php";

            //生成RequestQueue物件
            requestQueue = Volley.newRequestQueue(getApplicationContext());

            //註冊btn的監聽器


            //宣告並生成JsonObjectRequest物件
            //透過JsonObjectRequest來取得PHP傳遞過來的JSONArray
            //Constructor(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener)

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                //處理JSONArray
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());

                    //使用Try Catch是因為getJSONArray有可能抓不到值
                    try
                    {
                        //透過response.getJSONArray()取得JSONArray
                        JSONArray data = response.getJSONArray("data");

                        boolean logon= false;

                        //利用迴圈取得data[]內的資料
                        for(int i=0; i<data.length(); i++)
                        {
                            //取得data[]內的JSONObject
                            JSONObject jasonData = data.getJSONObject(i);
                            //取得jasonData內的資料
                            String finishedStoreNo = jasonData.getString("finishedStoreNo");
                            String space = jasonData.getString("space");
                            String manuNo = jasonData.getString("manuNo");

                            if(manuNo.equals("No thing")) {

                                flag=space;
                                Log.d("",flag+"有進來2");
                                break;

                            }
                        }

                    }
                    catch(JSONException e)
                    {
                        //錯誤處理
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.append(error.getMessage());
                }
            });

            //最後一定將JsonObjectRequest物件加入至requestQueue內
            //這樣才能Work

            requestQueue.add(jsonObjectRequest);

            Log.d("", flag_new[0] +"有進來4");


        }

        if(nextNo.equals("end")&& status.equals("ready")){
            final String[] flag_new = {""};
            flag="";
            Log.d("",flag+"有進來");
            url = "http://140.135.112.8/CollectionSystem/app/finishedStoreSearch.php";

            //生成RequestQueue物件
            requestQueue = Volley.newRequestQueue(getApplicationContext());

            //註冊btn的監聽器


                    //宣告並生成JsonObjectRequest物件
                    //透過JsonObjectRequest來取得PHP傳遞過來的JSONArray
                    //Constructor(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener)

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                        //處理JSONArray
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());

                            //使用Try Catch是因為getJSONArray有可能抓不到值
                            try
                            {
                                //透過response.getJSONArray()取得JSONArray
                                JSONArray data = response.getJSONArray("data");

                                boolean logon= false;

                                //利用迴圈取得data[]內的資料
                                for(int i=0; i<data.length(); i++)
                                {
                                    //取得data[]內的JSONObject
                                    JSONObject jasonData = data.getJSONObject(i);
                                    //取得jasonData內的資料
                                    String finishedStoreNo = jasonData.getString("finishedStoreNo");
                                    String space = jasonData.getString("space");
                                    String manuNo = jasonData.getString("manuNo");
                                    
                                    if(manuNo.equals("No thing")) {

                                        flag=space;
                                        Log.d("",flag+"有進來2");
                                        break;

                                    }
                                }
                                url = "http://140.135.112.8/CollectionSystem/app/updateFinishedStoreSpace.php";

                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                                        params.put("space", flag);
                                        params.put("manuNo", manuNo);
                                        params.put("activityNo", activityNo);
                                        Log.d("space", flag + " " + manuNo + " " + activityNo + " ");
                                        return params;
                                    }
                                };
                                requestQueue.add(stringRequest1);

                                Log.d("",flag+"有進來3");


                                flag_new[0] =flag;





                            }
                            catch(JSONException e)
                            {
                                //錯誤處理
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.append(error.getMessage());
                        }
                    });

                    //最後一定將JsonObjectRequest物件加入至requestQueue內
                    //這樣才能Work

            requestQueue.add(jsonObjectRequest);

            Log.d("", flag_new[0] +"有進來4");


        }

        finish();
    }





        }













    

