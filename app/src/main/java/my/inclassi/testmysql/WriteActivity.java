package my.inclassi.testmysql;

        import android.app.AlertDialog;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.nfc.NdefMessage;
        import android.nfc.NdefRecord;
        import android.nfc.NfcAdapter;
        import android.nfc.NfcManager;
        import android.nfc.Tag;
        import android.nfc.tech.Ndef;
        import android.nfc.tech.NdefFormatable;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.io.IOException;

public class WriteActivity extends AppCompatActivity {
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mFilters;
    EditText mNote;
    Button mBtn;
    AlertDialog dialog;
    Context context;

    boolean writeMode = false;
    boolean passByOnNewIntent = false;
    boolean enabledNFC = false;

    //關閉NFC Tag的寫入模式
    private void disableTagWriteMode()
    {
        mAdapter.disableForegroundDispatch(this);
        writeMode = false;
    }

    //判斷是否有開起NFC功能
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
        setContentView(R.layout.activity_write);
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
        mNote = (EditText)findViewById(R.id.edit_msg);
        mBtn = (Button)findViewById(R.id.writing);

        //mBtn的監聽器
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMode = true;
                //當Dialog出現時，就要進行寫入
                //enableTagWriteMode()
                dialog = new AlertDialog.Builder(context)
                        .setTitle("請將NFC Tag放到NFC手機感應處！")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                disableTagWriteMode();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
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
                Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

                if(writeMode)
                {
                    //寫入Tag，呼叫writeTag(NdefMessage message, Tag tag)
                    writeTag(getNoteAsNdef(), detectedTag);
                }
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
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if(writeMode)
            {
                //寫入Tag，呼叫writeTag(NdefMessage message, Tag tag)
                writeTag(getNoteAsNdef(), detectedTag);
            }
        }
    }

    //取得NdefMessage
    private NdefMessage getNoteAsNdef()
    {
        //1.將輸入的字串轉換成Byte[]
        byte[] textBytes = mNote.getText().toString().getBytes();
        //2.將byte[]放入NdefRecord
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(), new byte[]{}, textBytes);
        //3.包裝成NdefMessage並且回傳
        return new NdefMessage(new NdefRecord[]{textRecord});
    }

    //寫入標籤
    //要區分成Before Format和After Format的寫入邏輯
    boolean writeTag(NdefMessage message, Tag tag)
    {
        //1.訊息長度大小
        int size = message.toByteArray().length;
        try
        {
            //2.取得Ndef
            Ndef ndef = Ndef.get(tag);

            //判斷Tag是否已格式化
            if(ndef != null)
            {
                //這邊是已經格式化

                //連接開啟I/O通道
                ndef.connect();

                //判斷此NFC卡是否為Read Only
                if(!ndef.isWritable())
                {
                    Toast.makeText(context, "此Tag為Read-Only！",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ndef.close();
                    return false;
                }
                //判斷是否有足夠的空間可以寫入資訊
                if(ndef.getMaxSize() < size)
                {
                    Toast.makeText(context, "此Tag的容量不足！\nTag容量：" + ndef.getMaxSize() + "bytes\n寫入資訊容量：" + size + "bytes",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ndef.close();
                    return false;
                }
                //寫入
                ndef.writeNdefMessage(message);
                Toast.makeText(context, "NFC Tag資訊寫入成功(pre-formatted)！",Toast.LENGTH_SHORT).show();
                dialog.cancel();
                dialog.dismiss();
                writeMode = false;
                ndef.close();
                return true;
            }
            else
            {
                //這邊是未格式化

                //格式化NFC卡
                NdefFormatable format = NdefFormatable.get(tag);
                if(format != null)
                {
                    try
                    {
                        //連接開啟I/O通道
                        format.connect();
                        //格式化完成後即寫入資料
                        format.format(message);
                        Toast.makeText(context, "格式化成功！\nNFC Tag資訊寫入成功！",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        dialog.dismiss();
                        format.close();
                        return true;
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(context, "格式化失敗！",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        format.close();
                        return false;
                    }
                }
                else
                {
                    Toast.makeText(context, "此NFC Tag不支援NDEF讀寫格式！",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            if(dialog != null)
            {
                Toast.makeText(context, "寫入失敗！",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }

        return false;
    }
}




