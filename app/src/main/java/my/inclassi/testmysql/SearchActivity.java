package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    EditText edNo, edCat, edProNo;
    Button btn, btn2;
    RequestQueue requestQueue;
    String no, cat, proNo;
    String url = "http://140.135.112.8/CollectionSystem/app/finishedProductSearch.php";
    String url2 ="http://140.135.112.8/CollectionSystem/app/rawMateriaSearch.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //註冊btn的監聽器
        btn.setOnClickListener(new View.OnClickListener() {
            //按下時要做的事情
            @Override
            public void onClick(View v) {

                edNo = (EditText) findViewById(R.id.edNo);
                edCat = (EditText) findViewById(R.id.edCat);
                edProNo = (EditText) findViewById(R.id.edSpace);
                no = edNo.getText().toString();
                cat = edCat.getText().toString();
                proNo = edProNo.getText().toString();
                //宣告並生成JsonObjectRequest物件
                //透過JsonObjectRequest來取得PHP傳遞過來的JSONArray
                //Constructor(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener)

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                    //處理JSONArray
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response.toString());

                        //使用Try Catch是因為getJSONArray有可能抓不到值
                        try {

                            //透過response.getJSONArray()取得JSONArray
                            JSONArray data = response.getJSONArray("data");
                            int flag = 0;
                            //利用迴圈取得data[]內的資料
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jasonData = data.getJSONObject(i);
                                //取得jasonData內的資料
                                String materiaNo = jasonData.getString("materiaNo");
                                String name = jasonData.getString("name");
                                String quantity = jasonData.getString("quantity");
                                String space = jasonData.getString("space");
                                String category = jasonData.getString("category");


                                if ((no.equals(materiaNo)&&!no.equals("") )||(cat.equals(category) && !cat.equals("")) ||((name.equals(name) && !proNo.equals("")))) {
                                   /* new AlertDialog.Builder(SearchActivity.this)
                                            .setTitle("華新麗華員工系統")
                                            .setMessage("原料編號:"+materiaNo + " " +
                                                    "區域" +space+" "+
                                                    "種類 "+category + " " +
                                                    "原料名稱"+name + " ")
                                            .setPositiveButton("ok", null)
                                            .show();*/
                                             flag++;

                                }



                            }
                            if(flag==0){
                                new AlertDialog.Builder(SearchActivity.this)
                                        .setTitle("華新麗華員工系統")
                                        .setMessage("查無品項")
                                        .setPositiveButton("ok", null)
                                        .show();
                            }else{
                                //new一個intent物件，並指定Activity切換的class
                                Intent intent = new Intent();
                                intent.setClass(SearchActivity.this,showRawSearchActivity.class);
                                //new一個intent物件，並指定Activity切換的class
                                // new一個Bundle物件，並將要傳遞的資料傳入
                                Bundle bundle = new Bundle();
                                bundle.putString("no",no );//傳遞Double
                                bundle.putString("cat",cat);//傳遞String
                                bundle.putString("proNo",proNo);//傳遞String

                                //將Bundle物件傳給intent
                                intent.putExtras(bundle);
                                //切換Activity
                                startActivity(intent);

                            }

                        } catch (JSONException e) {
                            //錯誤處理
                            e.printStackTrace();
                            new AlertDialog.Builder(SearchActivity.this)
                                    .setTitle("華新麗華員工系統")
                                    .setMessage(" "+e)
                                    .setPositiveButton("ok", null)
                                    .show();
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
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            //按下時要做的事情
            @Override
            public void onClick(View v) {

                edNo = (EditText) findViewById(R.id.edNo);
                edCat = (EditText) findViewById(R.id.edCat);
                edProNo = (EditText) findViewById(R.id.edSpace);
                no = edNo.getText().toString();
                cat = edCat.getText().toString();
                proNo = edProNo.getText().toString();
                //宣告並生成JsonObjectRequest物件
                //透過JsonObjectRequest來取得PHP傳遞過來的JSONArray
                //Constructor(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener)

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    //處理JSONArray
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response.toString());

                        //使用Try Catch是因為getJSONArray有可能抓不到值
                        try {

                            //透過response.getJSONArray()取得JSONArray
                            JSONArray data = response.getJSONArray("data");
                            int flag = 0;
                            //利用迴圈取得data[]內的資料
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jasonData = data.getJSONObject(i);
                                //取得jasonData內的資料
                                String finishedNo = jasonData.getString("finishedNo");
                                String category = jasonData.getString("category");
                                String productNo = jasonData.getString("productNo");
                                String space = jasonData.getString("space");
                                String quantity = jasonData.getString("quantity");


                                if (no.equals(finishedNo)&&!no.equals("") ||(cat.equals(category) && !cat.equals("") ||(proNo.equals(productNo) && !proNo.equals("")))) {

                                    flag++;
                                }



                            }
                            if(flag==0){
                                new AlertDialog.Builder(SearchActivity.this)
                                        .setTitle("華新麗華員工系統")
                                        .setMessage("查無品項")
                                        .setPositiveButton("ok", null)
                                        .show();
                            }else{
                                //new一個intent物件，並指定Activity切換的class
                                Intent intent = new Intent();
                                intent.setClass(SearchActivity.this,showSearchActivity.class);
                                //new一個intent物件，並指定Activity切換的class
                                // new一個Bundle物件，並將要傳遞的資料傳入
                                Bundle bundle = new Bundle();
                                bundle.putString("no",no );//傳遞Double
                                bundle.putString("cat",cat);//傳遞String
                                bundle.putString("proNo",proNo);//傳遞String

                                //將Bundle物件傳給intent
                                intent.putExtras(bundle);
                                //切換Activity
                                startActivity(intent);

                            }

                        } catch (JSONException e) {
                            //錯誤處理
                            e.printStackTrace();
                            new AlertDialog.Builder(SearchActivity.this)
                                    .setTitle("華新麗華員工系統")
                                    .setMessage(" "+e)
                                    .setPositiveButton("ok", null)
                                    .show();
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
            }

        });



    }

}