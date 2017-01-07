package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

public class MainActivity extends AppCompatActivity {
    private static final int FUNC_LOGIN =1 ;
    //宣告RequestQueue
    RequestQueue requestQueue;
    //宣告PHP的URL
    String showUri = "http://140.135.112.8/CollectionSystem/app/login.php";
    String task[];
    public String uid,position;

    boolean logon =false;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("result", requestCode+"");
        if(requestCode==FUNC_LOGIN){
            if(resultCode==RESULT_OK) {
                 uid = data.getStringExtra("LOGIN_USERID");
                 position = data.getStringExtra("POSITION");
                Log.d("result", uid + "");
            }else{
                Log.d("result", "null");
                finish();


            }

        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn_next = (Button)findViewById(R.id.btn1);
        Button btn_next2 = (Button)findViewById(R.id.btn2);
        Button btn_next3 = (Button)findViewById(R.id.btn3);


        if(!logon){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,FUNC_LOGIN);
        }






        //跳轉按鈕
        assert btn_next != null;
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), FuncActivity.class);
                getIntent().putExtra("LOGIN_USERID",uid);
                startActivity(intentToInsertPage);

            }
        });

        //跳轉按鈕
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Log.d("uid",uid+"");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,TaskActivity.class);
                //new一個intent物件，並指定Activity切換的class
                // new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("uid",uid );//傳遞Double
                bundle.putString("POSITION",position );//傳遞Double
               Log.d("POSITION",position+"");

                //將Bundle物件傳給intent
                intent.putExtras(bundle);
                //切換Activity
                startActivity(intent);

            }
        });


        btn_next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), NFCActivity.class);



                startActivity(intentToInsertPage);

            }
        });



    }


    }

