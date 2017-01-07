package my.inclassi.testmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class InsertActivity extends AppCompatActivity{
    Button btn, btn2;
    EditText edNo,edCat,edSpace,edName,edQuantity;
    RequestQueue requestQueue;
    String url = "http://140.135.112.8/CollectionSystem/app/rawInsert.php";
    String url2 = "http://140.135.112.8/CollectionSystem/app/finishedProductInsert.php";
    public void refresh(){

        onCreate(null);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_layout);

        btn = (Button)findViewById(R.id.btn2);
        btn2 = (Button)findViewById(R.id.btn1);
        edNo = (EditText)findViewById(R.id.edNo);
        edCat = (EditText)findViewById(R.id.edCat);
        edSpace = (EditText)findViewById(R.id.edSpace);
        edName = (EditText)findViewById(R.id.edName);
        edQuantity = (EditText)findViewById(R.id.edQuantity);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("orderNo", edNo.getText().toString());
                        params.put("category", edCat.getText().toString());
                        params.put("space", edSpace.getText().toString());
                        params.put("productNo", edName.getText().toString());
                        params.put("quantity", edQuantity.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), InsertActivity.class);
                startActivity(intentToInsertPage);
                finish();


            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String,String> params = new HashMap<String, String>();
                        //params.put("name", edNo.getText().toString());
                        params.put("name", edName.getText().toString());
                        params.put("quantity", edQuantity.getText().toString());
                        params.put("space", edSpace.getText().toString());
                        params.put("category", edCat.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                Intent intentToInsertPage = new Intent().setClass(getApplicationContext(), InsertActivity.class);
                startActivity(intentToInsertPage);



            }
        });

        /*btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToReadPage = new Intent().setClass(getApplicationContext(), MainActivity.class);
                startActivity(intentToReadPage);
                finish();
            }
        });*/
    }

}
