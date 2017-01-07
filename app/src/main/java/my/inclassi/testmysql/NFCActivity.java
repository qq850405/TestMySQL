package my.inclassi.testmysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NFCActivity extends AppCompatActivity {
    Button read_btn;
    Button write_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        read_btn = (Button) findViewById(R.id.read_btn);
        read_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(NFCActivity.this , ReadActivity.class);
                //開啟Activity
                startActivity(intent);
            }
        });
        write_btn = (Button) findViewById(R.id.write_btn);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(NFCActivity.this , WriteActivity.class);
                //開啟Activity
                startActivity(intent);
            }
        });
    }
}
