package my.inclassi.testmysql;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mFilters;
    Context context;

    boolean passByOnNewIntent = false;
    boolean enabledNFC = false;

    private boolean isEnabledNFC()
    {
        NfcManager manager = (NfcManager)context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        if(adapter != null && adapter.isEnabled())
        {
            Toast.makeText(context, "NFC已經開啟！", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            //開啟NFC功能設定的頁面
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivityForResult(intent, 101);
        }
        return  false;
    }



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




                            }

                            if(!logon){
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("華新麗華員工系統")
                                        .setMessage("登入失敗")
                                        .setPositiveButton("ok",null)
                                        .show();
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

        context = this;

        //1.建立NFC Adapter
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        //2.創造一個PendingIntent
        //當掃描NFC Tag時，Android可以讀取Tag的資訊
        mPendingIntent = PendingIntent.getActivity(this, 200, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),PendingIntent.FLAG_UPDATE_CURRENT);

        //3.宣告IntentFilter用來監聽讀取Tag資訊的Intent
        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        //4.定義IntentFilter[]
        //遊於每次發現Tag的過程都會收到TAG_DISCOVERED intent
        //因此可以藉由定義一個IntentFilter[]來存放所有的IntentFilter物件
        mFilters = new IntentFilter[]{tag};

        //找元件


    }
    //APP一啟動就立刻判斷NFC功能是否有開啟
    @Override
    protected void onStart() {
        super.onStart();
        enabledNFC = isEnabledNFC();
    }

    //喚醒再度進入APP
    @Override
    protected void onResume() {
        super.onResume();

        if(!enabledNFC)
        {
            return;
        }

        //啟動前景Activity調度
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);

        if(!passByOnNewIntent)
        {
            //判斷Tag的模式
            if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction()) ||
                    NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()))
            {
                //呼叫顯示Tag內容的方法
                showNFCTagContent(getIntent());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    //取得最新的Intent資訊
    @Override
    protected void onNewIntent(Intent intent) {
        if(!enabledNFC)
        {
            return;
        }

        //初始化UI的資料
        passByOnNewIntent = true;


        //判斷Tag的模式
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            //呼叫顯示Tag內容的方法
            showNFCTagContent(intent);
        }
    }

    //顯示Ndef Tag內容
    private void showNFCTagContent(Intent intent)
    {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msgs = null;

        if(rawMsgs != null)
        {
            msgs = new NdefMessage[rawMsgs.length];
            for(int i=0; i<rawMsgs.length; i++)
            {
                msgs[i] = (NdefMessage)rawMsgs[i];
            }
            byte[] payload = msgs[0].getRecords()[0].getPayload();
             final String value = new String(payload);


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
                            position = jasonData.getString("position");

                            if(new String(value).equals(id)){
                                logon=true;
                                Log.d("Login_position",position);
                                getIntent().putExtra("LOGIN_USERID",id);
                                getIntent().putExtra("POSITION",position);
                                setResult(RESULT_OK,getIntent());
                                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                                finish();

                            }




                        }

                        if(!logon){
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("華新麗華員工系統")
                                    .setMessage("卡片錯誤，登入失敗！")
                                    .setPositiveButton("ok",null)
                                    .show();
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
    }


   }