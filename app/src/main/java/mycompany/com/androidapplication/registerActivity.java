package mycompany.com.androidapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    EditText firstname,lastname,email,username,password;
    Button btnRegister,btnLogin;
    String firstnameReg,lastnameReg,emailReg,usernameReg,passwordReg;

    public static final String TAG =  "EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.regBtn);
        firstname = findViewById(R.id.regFirstname);
        lastname = findViewById(R.id.regLastname);
        email = findViewById(R.id.regEmail);
        username = findViewById(R.id.regUsername);
        password = findViewById(R.id.regPassword);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                firstnameReg = firstname.getText().toString();
                lastnameReg  =lastname.getText().toString();
                emailReg =email.getText().toString();
                usernameReg =username.getText().toString();
                passwordReg =password.getText().toString();

                final ProgressDialog regDialog = new ProgressDialog(registerActivity.this);
                regDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                regDialog.setTitle("ACCOUNT CREATION");
                regDialog.setMessage("Please wait...");
                regDialog.setProgress(0);
                regDialog.setMax(100);
            Thread thread = new Thread(new Runnable(){
            @Override
             public void run(){
                int progress=0;
                while(progress <=100){
                    try{
                        regDialog.setProgress(progress);
                        progress++;
                        Thread.sleep(10);

                    }catch(Exception ex){
                    }
                }
                regDialog.dismiss();
                registerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
            });
            thread.start();
            regDialog.show();
                if(Utility.isNotNull(firstnameReg) && Utility.isNotNull(lastnameReg) && Utility.isNotNull(emailReg)
                        && Utility.isNotNull(usernameReg) && Utility.isNotNull(passwordReg)){
                        if(Utility.validate(emailReg)){
                            /**
                             * Invoking the Webservice Method to interact with the register Webservice
                             * */
                            volleyCallWebService(firstnameReg,lastnameReg,emailReg,usernameReg,passwordReg);
                        }else {
                            Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
                        }
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void volleyCallWebService(String firstnameReg,String lastnameReg,String emailReg,String usernameReg,String passwordReg){

        RequestQueue requestQ = Volley.newRequestQueue(this);

        String serviceUrl = "http://192.168.2.162:8081/register";
        Map<String,String> jsonParams = new HashMap<String,String>();
        jsonParams.put("firstname" ,firstnameReg);
        jsonParams.put("lastname" ,lastnameReg);
        jsonParams.put("email" ,emailReg);
        jsonParams.put("username" ,usernameReg);
        jsonParams.put("password" ,passwordReg);
        Log.d(TAG , "Json" + new JSONObject(jsonParams));

        JsonObjectRequest reqRequest = new JsonObjectRequest(Request.Method.POST,serviceUrl, new JSONObject(jsonParams)
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String msg = "success";
                Log.d(TAG , "Json" +response.toString());
                try{
                    msg = (String)response.get("msg");
                } catch (JSONException e){
                    e.printStackTrace();
                }
                if(msg.equals("success")){
                    Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
                    Intent intentLogin = new Intent(registerActivity.this,loginActivity.class);
                    startActivity(intentLogin);
                }else{
                    Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_LONG).show();
                    Log.d(TAG , "Json" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError Error) {
                Log.d(TAG , "Error" + Error);
            }
        });
        requestQ.add(reqRequest);
    }
}
