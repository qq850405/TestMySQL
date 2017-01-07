package my.inclassi.testmysql;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    //宣告元件
    Button btn;
    //宣告RequestQueue
    RequestQueue requestQueue;
    //宣告PHP的URL
    String showUri = "http://140.135.112.8/CollectionSystem/app/login.php";
    String position;
    EditText edUserid,edPassword;
    String pw ,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         edUserid =(EditText)findViewById(R.id.userid);
         edPassword = (EditText)findViewById(R.id.password);
        //找到元件
         btn = (Button)findViewById(R.id.btn1);

        //生成RequestQueue物件
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //註冊btn的監聽器
        btn.setOnClickListener(new View.OnClickListener() {
            //按下時要做的事情
            @Override
            public void onClick(View v) {
                pw=edPassword.getText().toString();
                uid=edUserid.getText().toString();
                //宣告並生成JsonObjectRequest物件
                //透過JsonObjectRequest來取得PHP傳遞過來的JSONArray
                //Constructor(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener)

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, showUri, null, new Response.Listener<JSONObject>() {

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
                                String id = jasonData.getString("id");
                                String password = jasonData.getString("password");
                                 position = jasonData.getString("position");

                                if(uid.equals(id)&&pw.equals(password)){
                                    logon=true;
                                    Log.d("Login_position",position);
                                    getIntent().putExtra("LOGIN_USERID",uid);
                                    getIntent().putExtra("POSITION",position);
                                    setResult(RESULT_OK,getIntent());
                                    finish();

                                }



                                if(!logon){
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("華新麗華員工系統")
                                            .setMessage("登入失敗")
                                            .setPositiveButton("ok",null)
                                            .show();
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
            }


        });


    }
}
