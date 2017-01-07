package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FuncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);

        Button btn_next = (Button)findViewById(R.id.btn1);
        Button btn_next2 = (Button)findViewById(R.id.btn2);
        Button btn_next3 = (Button)findViewById(R.id.btn3);


        //跳轉按鈕
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), SearchActivity.class);
                startActivity(intentToInsertPage);

            }
        });


        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), InsertActivity.class);
                startActivity(intentToInsertPage);

            }
        });

        btn_next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), InsertActivity.class);
                startActivity(intentToInsertPage);

            }
        });

    }
}
