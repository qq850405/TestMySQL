package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class TaskActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String uid, position;
    ListView list;
    RequestQueue requestQueue;
    private String JSON_STRING;
    public String func[];
    String search[];
    String tmepActivityNo[];
    String tmepStatus[];
    String tmepNextNo[];
    String tmepManuNo[];
    String arrayManuNo[];
    String arrayActivityNo[];
    String arrayStatus[];
    String arrayNextNo[];
    String ch_position;
    String url = "http://140.135.112.8/CollectionSystem/app/manuActivityShow.php";
    /*@Override
    protected void onResume() {

        super.onResume();

        onCreate(null);

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        position = bundle.getString("POSITION");
        ch_position=position;
        Log.d("session", uid + "");
        Log.d("Taskposition", position + "");
        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(this);



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
                    func =new String[data.length()];
                    tmepActivityNo =new String[data.length()];
                    tmepStatus =new String[data.length()];
                    tmepNextNo =new String[data.length()];
                    tmepManuNo =new String[data.length()];

                    for (int i = 0; i <data.length(); i++) {
                        JSONObject jasonData = data.getJSONObject(i);
                        //取得jasonData內的資料
                        String activityNo = jasonData.getString("activityNo");
                        String manuNo = jasonData.getString("manuNo");
                        String _position = jasonData.getString("position");
                        String startTime = jasonData.getString("startTime");
                        String endTime = jasonData.getString("endTime");
                        String status = jasonData.getString("status");
                        String nextNo = jasonData.getString("nextNo");
                        func[i]="test";
                        tmepActivityNo[i]="test";
                        tmepNextNo[i]="test";
                        tmepStatus[i]="test";
                        tmepManuNo[i]="test";
                        int viewFlag=0;
                        //if ((no.equals(finishedNo)&&!no.equals("")) ||((cat.equals(category) && !cat.equals("")) ||(proNo.equals(productNo) && !proNo.equals("")))) {
                        if (position.equals(_position) && (status.equals("ready") || status.equals("in progress"))) {


                            func[i] = ("製令編號:" + manuNo + "\n" + "工作站: " + _position + " \n" + "開始時間:" + startTime + " \n結束時間:"+endTime+" \n狀態:"+status);
                            tmepActivityNo[i]=activityNo;
                            tmepStatus[i]=status;
                            tmepNextNo[i]=nextNo;
                            tmepManuNo[i]=manuNo;
                            Log.d("test1",func[i]);
                        }
                    }
                    int count=0;

                    for(int i=0;i<func.length;i++){
                        if(!func[i].equals("test")){
                            count++;
                        }
                    }
                    int temp=0;
                    search=new String[count];
                    arrayActivityNo=new String[count];
                    arrayStatus=new String[count];
                    arrayNextNo=new String[count];
                    arrayManuNo=new String[count];
                    for(int i=0;i<func.length;i++){
                        if(!func[i].equals("test")){
                            search[temp]=func[i];
                            arrayActivityNo[temp]=tmepActivityNo[i];
                            arrayStatus[temp]=tmepStatus[i];
                            arrayNextNo[temp]=tmepNextNo[i];
                            arrayManuNo[temp]=tmepManuNo[i];
                            Log.d("showSearch",search[temp]);
                            Log.d("showstastus",arrayStatus[temp]);
                            Log.d("showManuNo",arrayManuNo[temp]);
                            temp++;

                        }
                    }

                    ArrayAdapter adapter =new ArrayAdapter(TaskActivity.this,android.R.layout.simple_list_item_1,search);
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    //錯誤處理
                    e.printStackTrace();
                    new AlertDialog.Builder(TaskActivity.this)
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





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Log.d("clicked", String.valueOf(arrayActivityNo[position]));


        Intent intent = new Intent();
        if(ch_position.equals("picking")&&!String.valueOf(arrayStatus[position]).equals("in progress")){
            intent.setClass(TaskActivity.this,BindingRFIDActivity.class);
        }else{
            intent.setClass(TaskActivity.this,showTaskActivity.class);
        }
        //new一個intent物件，並指定Activity切換的class
        // new一個Bundle物件，並將要傳遞的資料傳入
            Bundle bundle = new Bundle();
            bundle.putString("activityNo",String.valueOf(arrayActivityNo[position]));//傳遞Double
            bundle.putString("status",String.valueOf(arrayStatus[position]));//傳遞Double
            bundle.putString("manuNo",String.valueOf(arrayManuNo[position]));//傳遞Double
            bundle.putString("nextNo",String.valueOf(arrayNextNo[position]));//傳遞Double
        //將Bundle物件傳給intent
            intent.putExtras(bundle);
        //切換Activity
            startActivity(intent);
            finish();

    }
}
