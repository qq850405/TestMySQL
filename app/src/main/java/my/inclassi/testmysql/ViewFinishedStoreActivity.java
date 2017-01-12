package my.inclassi.testmysql;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class ViewFinishedStoreActivity extends AppCompatActivity {
    String url = "http://140.135.112.8/CollectionSystem/app/finishedStoreSearch.php";
    String no, cat, proNo;
    String[] func;
    RequestQueue requestQueue;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_finished_store);
       list = (ListView) findViewById(R.id.listView);
       requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                    func = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasonData = data.getJSONObject(i);
                        //取得jasonData內的資料
                        String finishedStoreNo = jasonData.getString("finishedStoreNo");
                        String space = jasonData.getString("space");
                        String manuNo = jasonData.getString("manuNo");

                        func[i] = "test";
                        //if ((no.equals(finishedNo)&&!no.equals("")) ||((cat.equals(category) && !cat.equals("")) ||(proNo.equals(productNo) && !proNo.equals("")))) {

                            func[i] = ( "成品倉號: " + space + "\n" + "製令號:" + manuNo );
                            Log.d("test1", func[i]);



                      //  Log.d("cate", cat);


                    }
                    int count = 0;
                    String search[];
                    for (int i = 0; i < func.length; i++) {
                        if (!func[i].equals("test")) {
                            count++;
                        }
                    }
                    int temp = 0;
                    search = new String[count];
                    for (int i = 0; i < func.length; i++) {
                        if (!func[i].equals("test")) {
                            search[temp] = func[i];
                            temp++;
                            Log.d("search",search[i]);
                        }
                    }

                   ArrayAdapter adapter = new ArrayAdapter(ViewFinishedStoreActivity.this, android.R.layout.simple_list_item_1, search);
                   list.setAdapter(adapter);


                } catch (JSONException e) {
                    //錯誤處理
                    e.printStackTrace();
                    new AlertDialog.Builder(ViewFinishedStoreActivity.this)
                            .setTitle("華新麗華員工系統")
                            .setMessage(" " + e)
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
        requestQueue.add(jsonObjectRequest);
    }
}
