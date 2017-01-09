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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class checkTaskActivity extends AppCompatActivity {

    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mFilters;
    Context context;
    TextView mNfcContent;
    String activityNo,status,manuNo,nextNo;
    RequestQueue requestQueue;
    String url = "http://140.135.112.8/CollectionSystem/app/updateStatus.php";
    boolean passByOnNewIntent = false;
    boolean enabledNFC = false;


    private boolean isEnabledNFC()
    {
        NfcManager manager = (NfcManager)context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        if(adapter != null && adapter.isEnabled())
        {
            //Toast.makeText(context, "NFC已經開啟！", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_check_task);
        context = this;
        Bundle bundle = getIntent().getExtras();
        activityNo = bundle.getString("activityNo");
        status = bundle.getString("status");
        manuNo = bundle.getString("manuNo");
        nextNo = bundle.getString("nextNo");
        Log.d("activityNo",activityNo);
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
        mNfcContent = (TextView)findViewById(R.id.textView_show);

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
        mNfcContent.setText("敢硬");

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
            String value = new String(payload);
            //顯示NFC內容
            mNfcContent.setText(value+""+activityNo );
            //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
            if(value.equals(manuNo)) {
                Toast.makeText(this, "讀取成功", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent();
                intent.setClass(checkTaskActivity.this, updateStatusActivity.class);
                //new一個intent物件，並指定Activity切換的class
                // new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("activityNo", activityNo);//傳遞Double
                bundle.putString("status", status);//傳遞Double
                bundle.putString("nextNo", nextNo);//傳遞Double
                bundle.putString("manuNo", manuNo);//傳遞Double
                //將Bundle物件傳給intent
                intent.putExtras(bundle);
                //切換Activity
                startActivity(intent);

                finish();





            }else{
                new AlertDialog.Builder(checkTaskActivity.this)
                        .setTitle("華新麗華員工系統")
                        .setMessage("料件錯誤")
                        .setPositiveButton("ok", null)
                        .show();
            }



        }



        }
}

