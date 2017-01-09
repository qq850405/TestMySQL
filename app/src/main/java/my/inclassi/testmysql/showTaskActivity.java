package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class showTaskActivity extends AppCompatActivity {
    String activityNo,status,manuNo,nextNo;
    RequestQueue requestQueue;
    String url = "http://140.135.112.8/CollectionSystem/app/manuActivityShow.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        Bundle bundle = getIntent().getExtras();
        activityNo = bundle.getString("activityNo");
        status = bundle.getString("status");
        manuNo = bundle.getString("manuNo");
        nextNo = bundle.getString("nextNo");
        Log.d("activityNo",activityNo);
        Log.d("status",status);
        Button btn_check =(Button) findViewById(R.id.btn_check);



        final TextView manuNoText = (TextView) findViewById(R.id.manuNoText);
        final TextView statusText =(TextView) findViewById(R.id.statusText);
        final TextView maunInfoText =(TextView) findViewById(R.id.manuInfoText);
        final TextView machineText =(TextView) findViewById(R.id.machineText);

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

                    for (int i = 0; i <data.length(); i++) {

                        JSONObject jasonData = data.getJSONObject(i);
                        //取得jasonData內的資料
                        String _activityNo = jasonData.getString("activityNo");
                        String manuNo = jasonData.getString("manuNo");
                        String position = jasonData.getString("position");
                        String manuStepInfo = jasonData.getString("manuStepInfo");
                        String status = jasonData.getString("status");

                        if ((activityNo.equals(_activityNo))) {

                            manuNoText.setText(manuNo);
                            statusText.setText(status);
                            maunInfoText.setText(manuStepInfo);
                            machineText.setText(position);
                            Log.d("activityNoIn",activityNo);
                        }
                    }



                } catch (JSONException e) {
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



        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Log.d("uid",uid+"");
                Intent intent = new Intent();
                intent.setClass(showTaskActivity.this,checkTaskActivity.class);
                //new一個intent物件，並指定Activity切換的class
                // new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("activityNo",activityNo );//傳遞Double
                bundle.putString("status",status );//傳遞Double
                bundle.putString("manuNo",manuNo );//傳遞Double
                bundle.putString("nextNo",nextNo );//傳遞Double
                Log.d("activityNo",activityNo+"");

                //將Bundle物件傳給intent
                intent.putExtras(bundle);
                //切換Activity
                startActivity(intent);
                finish();
            }
        });






    }
}
