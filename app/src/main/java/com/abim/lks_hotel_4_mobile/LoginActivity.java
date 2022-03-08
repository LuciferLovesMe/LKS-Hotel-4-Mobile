package com.abim.lks_hotel_4_mobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Context ctx;
    EditText et_user, et_pass;
    Button btn;
    RequestQueue queue;
    Session s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ctx = this;
        et_user = findViewById(R.id.et_username);
        et_pass = findViewById(R.id.et_pass);
        btn = findViewById(R.id.btn);
        queue = Volley.newRequestQueue(ctx);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pass.getText().toString().equalsIgnoreCase("") || et_user.getText().toString().equalsIgnoreCase("")){
                    AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                    dialog.setTitle("Error");
                    dialog.setMessage("All fields must be filled");
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{
                    doLogin();
                }
            }
        });
    }

    void doLogin(){
        String pass = et_pass.getText().toString(), username = et_user.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, MyRequest.getLoginURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    s = new Session(ctx);
                    s.setUser(object.getInt("id"), object.getString("name"));

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception ex){
                    AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                    dialog.setTitle("Error");
                    dialog.setMessage("Can't find user");
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                dialog.setTitle("Error");
                dialog.setMessage(""+error);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", pass);
                return params;
            }
        };

        queue.add(request);
    }
}