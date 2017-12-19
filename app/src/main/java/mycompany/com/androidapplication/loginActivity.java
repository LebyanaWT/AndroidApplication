package mycompany.com.androidapplication;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    private String username,password;
    Button  btnLogin,btnRegister;
    EditText edusername,edPassword;

    public static final String TAG =  "EVENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edusername = findViewById(R.id.usernameLogin);
        edPassword = findViewById(R.id.passwordLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loginDialog = new ProgressDialog(loginActivity.this);
                loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loginDialog.setTitle("Account Verification");
                loginDialog.setMessage("Please wait...");
                loginDialog.setProgress(0);
                loginDialog.setMax(100);
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        int progress=0;
                        while(progress <=100){
                            try{
                                loginDialog.setProgress(progress);
                                progress++;
                                Thread.sleep(20);
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        loginDialog.dismiss();
                        loginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username = edusername.getText().toString();
                                password = edPassword.getText().toString();
                                if(Utility.isNotNull(username) && Utility.isNotNull(password)){
                                    LoginCustomer();
                                    Toast.makeText(getApplicationContext(), "Successfully Logged In", Toast.LENGTH_LONG).show();
                                    Intent fragment = new Intent(loginActivity.this,MainActivity.class);
                                    startActivity(fragment);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please fill in all fields to proceed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
                loginDialog.show();
            }
        });
        /**
         * Assigning a function on the Register button from the clients login View to navigate to the register Activity
         * **/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intentRegister = new Intent(loginActivity.this,registerActivity.class);
            startActivity(intentRegister);
            }
        });
    }
    private void LoginCustomer(){
        String HttpUrl ="http://192.168.2.162:8081/login/user";
        Map<String,String> jsonParams = new HashMap<String,String>();
        jsonParams.put("username" ,username);
        jsonParams.put("password" ,password);

        Log.d(TAG , "Json" + new JSONObject(jsonParams));

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,HttpUrl, new JSONObject(jsonParams)
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               if(response.equals("success")){
                   Toast.makeText(getApplicationContext(), "Access Granted", Toast.LENGTH_LONG).show();
                   Intent home = new Intent(loginActivity.this,MainActivity.class);
                   startActivity(home);
               }else{
                   Toast.makeText(getApplicationContext(),"Invalid" , Toast.LENGTH_LONG).show();
               }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue requestQ = Volley.newRequestQueue(loginActivity.this);
        requestQ.add(loginRequest);
    }
}
