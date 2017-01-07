package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class showSearchActivity extends AppCompatActivity {
    ListView list;
    RequestQueue requestQueue;
    private String JSON_STRING;
    public String func[];

    String no, cat, proNo;
    String url = "http://140.135.112.8/CollectionSystem/app/finishedProductSearch.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search);
        list = (ListView) findViewById(R.id.listView);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

            //取得傳遞過來的資料
        Bundle bundle = getIntent().getExtras();
        no = bundle.getString("no");
        cat = bundle.getString("cat");
        proNo = bundle.getString("proNo");

        Log.d(no,"Log"+no);
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
                    func =new String[data.length()];
                    for (int i = 0; i <data.length(); i++) {
                        JSONObject jasonData = data.getJSONObject(i);
                        //取得jasonData內的資料
                        String finishedNo = jasonData.getString("finishedNo");
                        String category = jasonData.getString("category");
                        String productNo = jasonData.getString("productNo");
                        String space = jasonData.getString("space");
                        String quantity = jasonData.getString("quantity");
                        func[i]="test";
                        //if ((no.equals(finishedNo)&&!no.equals("")) ||((cat.equals(category) && !cat.equals("")) ||(proNo.equals(productNo) && !proNo.equals("")))) {
                        if (no.equals(finishedNo)&&!no.equals("") ||(cat.equals(category) && !cat.equals("") ||(proNo.equals(productNo) && !proNo.equals("")))) {
                            func[i] = ("成品編號:" + finishedNo + "\n" + "種類: " + category + " \n" + "產品編號:" + productNo + " \n地點:"+space+" \n庫存:"+quantity);
                            Log.d("test1",func[i]);

                        }

                        Log.d("cate",cat);


                    }
                    int count=0;
                    String search[];
                    for(int i=0;i<func.length;i++){
                        if(!func[i].equals("test")){
                            count++;
                        }
                    }
                    int temp=0;
                    search=new String[count];
                    for(int i=0;i<func.length;i++){
                        if(!func[i].equals("test")){
                            search[temp]=func[i];
                            temp++;
                        }
                    }

                    ArrayAdapter adapter =new ArrayAdapter(showSearchActivity.this,android.R.layout.simple_list_item_1,search);
                    list.setAdapter(adapter);



                } catch (JSONException e) {
                    //錯誤處理
                    e.printStackTrace();
                    new AlertDialog.Builder(showSearchActivity.this)
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

        String func1[]={"0","1"};
        Button btn_last = (Button) findViewById(R.id.button_last);
        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSearchPage = new Intent().setClass(getApplicationContext(), SearchActivity.class);
                startActivity(intentToSearchPage);
                finish();
            }
        });


    }

    }



