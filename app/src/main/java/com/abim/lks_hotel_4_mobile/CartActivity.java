package com.abim.lks_hotel_4_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    Context ctx;
    Button btn;
    AlertDialog dialog;
    List<Cart> carts;
    Adapter adapter;
    RecyclerView rv;
    int fdid, reservationRoomID, qty, total, employeeId;
    String fdName;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle("Cart");

        ctx = this;
        btn = findViewById(R.id.btn_checkout);
        carts = new ArrayList<>();
        carts.clear();
        rv = findViewById(R.id.rv);
        queue = Volley.newRequestQueue(ctx);

        load();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ctx).create();
                dialog.setTitle("Confirmation");
                dialog.setMessage("Are you sure to checkout?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getSharedPreferences("my_key", MODE_PRIVATE).getInt("size", 0) == 0){
                            dialog.dismiss();
                        }
                        else {
                            sendData();
                        }
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void load(){
        if (getSharedPreferences("my_key", MODE_PRIVATE).getInt("size", 0) == 0){
            rv.setVisibility(View.GONE);
        }else {
            rv.setVisibility(View.VISIBLE);
            int size = getSharedPreferences("my_key", MODE_PRIVATE).getInt("size", 0);
            for (int i = 0; i < size; i++){
                fdid = getSharedPreferences("my_key", MODE_PRIVATE).getInt("fdid"+i, 0);
                reservationRoomID = getSharedPreferences("my_key", MODE_PRIVATE).getInt("reservationRoom"+i, 0);
                qty = getSharedPreferences("my_key", MODE_PRIVATE).getInt("qty"+i, 0);
                total = getSharedPreferences("my_key", MODE_PRIVATE).getInt("total"+i, 0);
                employeeId = getSharedPreferences("my_key", MODE_PRIVATE).getInt("employeeId"+i, 0);
                fdName = getSharedPreferences("my_key", MODE_PRIVATE).getString("fdname"+i, "");

                carts.add(new Cart(employeeId, reservationRoomID, qty, total, fdid, fdName));
            }

            rv.setLayoutManager(new LinearLayoutManager(ctx));
            adapter = new Adapter(ctx, carts);
            rv.setAdapter(adapter);
        }
    }

    void sendData(){
        try {
            int size = getSharedPreferences("my_key", MODE_PRIVATE).getInt("size", 0);
            JSONObject obj = new JSONObject();
            for (int i = 0; i < size; i++){
                obj.put("fdid", getSharedPreferences("my_key", MODE_PRIVATE).getInt("fdid"+i, 0));
                obj.put("reservationRoomId", getSharedPreferences("my_key", MODE_PRIVATE).getInt("reservationRoom"+i, 0));
                obj.put("qty", getSharedPreferences("my_key", MODE_PRIVATE).getInt("qty"+i, 0));
                obj.put("price", getSharedPreferences("my_key", MODE_PRIVATE).getInt("total"+i, 0));
                obj.put("employeeId", getSharedPreferences("my_key", MODE_PRIVATE).getInt("employeeId"+i, 0));

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MyRequest.getCheckOutURL(), obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog = new AlertDialog.Builder(ctx).create();
                        dialog.setTitle("Information");
                        dialog.setMessage("Check Out success");
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        SharedPreferences.Editor editor = getSharedPreferences("my_key", MODE_PRIVATE).edit().putInt("size", 0);
                        editor.commit();
                        load();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog = new AlertDialog.Builder(ctx).create();
                        dialog.setTitle("Information");
                        dialog.setMessage("Check Out success");
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        SharedPreferences.Editor editor = getSharedPreferences("my_key", MODE_PRIVATE).edit().putInt("size", 0);
                        editor.commit();
                        load();
                    }
                });

                queue.add(request);
            }
        }catch (Exception e){
            Toast.makeText(ctx, ""+e, Toast.LENGTH_SHORT).show();
        }
    }
}