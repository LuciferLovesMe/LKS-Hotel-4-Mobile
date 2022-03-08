package com.abim.lks_hotel_4_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    Context ctx;
    Session s;
    Spinner spin_room, spin_fd;
    EditText et_qty;
    Button btn_inc, btn_dec, btn_add;
    TextView tv_price, tv_total;
    RequestQueue queue;
    List<FD> fds;
    List<String> fdName;
    List<String> roomNumber;
    List<Integer> roomId;
    List<Cart> carts;
    int qty, fdid, total, reservationRoomId, price;
    String fdname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setTitle("Order Foods or Drinks");

        ctx = this;
        s = new Session(ctx);
        spin_room = findViewById(R.id.spin_room);
        spin_fd = findViewById(R.id.spin_fd);
        et_qty = findViewById(R.id.et_qty);
        btn_inc = findViewById(R.id.btn_inc);
        btn_add = findViewById(R.id.btn_add);
        btn_dec = findViewById(R.id.btn_dec);
        tv_price = findViewById(R.id.tv_price);
        tv_total = findViewById(R.id.tv_total);
        queue = Volley.newRequestQueue(ctx);

        fds = new ArrayList<>();
        fds.clear();
        fdName = new ArrayList<>();
        fdName.clear();
        roomNumber = new ArrayList<>();
        roomNumber.clear();
        roomId = new ArrayList<>();
        roomId.clear();
        carts = new ArrayList<>();
        carts.clear();

        loadFd();
        loadRoom();

        qty = 0;

        spin_fd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FD fd = fds.get(i);
                fdname = fd.getName();
                fdid = fd.getId();
                total = fd.getPrice() * Integer.parseInt(et_qty.getText().toString());
                tv_price.setText(String.valueOf(fd.getPrice()));
                tv_total.setText(String.valueOf(total));
                price = fd.getPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reservationRoomId = Integer.valueOf(roomId.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.valueOf(et_qty.getText().toString());
                qty++;
                et_qty.setText(String.valueOf(qty));
                total = Integer.valueOf(qty * price);
                tv_total.setText(String.valueOf(total));
            }
        });

        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.valueOf(et_qty.getText().toString());
                if (qty < 0){
                    qty = 0;
                }
                else if (qty > 0){
                    qty--;
                }
                et_qty.setText(String.valueOf(qty));
                total = Integer.valueOf(qty * price);
                tv_total.setText(String.valueOf(total));
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_qty.getText().toString().equalsIgnoreCase("") || et_qty.getText().toString().equalsIgnoreCase("0")){
                    AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                    dialog.setTitle("Error");
                    dialog.setMessage("Please insert quantity");
                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{
                    add();
                }
            }
        });
    }

    void loadFd(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MyRequest.getFdURL(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        fds.add(new FD(obj.getInt("id"), obj.getInt("price"), obj.getString("name")));
                    }

                    for (FD fd: fds){
                        fdName.add(fd.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.support_simple_spinner_dropdown_item, fdName);
                    spin_fd.setAdapter(adapter);
                }catch (Exception ex){
                    Toast.makeText(ctx, "fd"+ex, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                dialog.setMessage(error.getMessage());
                dialog.show();
            }
        });

        queue.add(request);
    }

    void loadRoom(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, MyRequest.getRoomURL(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        roomNumber.add(obj.getString("RoomNumber"));
                        roomId.add(obj.getInt("id"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String >(ctx, R.layout.support_simple_spinner_dropdown_item, roomNumber);
                    spin_room.setAdapter(adapter);
                }catch (Exception ex){
                    Toast.makeText(ctx, "fd"+ex, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                dialog.setMessage(error.getMessage());
                dialog.show();
            }
        });

        queue.add(request);
    }

    void add(){
        carts.add(new Cart(s.getId(), reservationRoomId, qty, total, fdid, fdname));
        SharedPreferences.Editor editor = getSharedPreferences("my_key", MODE_PRIVATE).edit();
        editor.putInt("size", carts.size());
        editor.commit();

        for (int i = 0; i < carts.size(); i++){
            Cart cart = carts.get(i);
            SharedPreferences.Editor edit = getSharedPreferences("my_key", MODE_PRIVATE).edit();
            edit.putInt("employeeId"+i, cart.getEmployeeId());
            edit.putInt("reservationRoom"+i, cart.getReservationRoomId());
            edit.putInt("qty"+i, cart.getQty());
            edit.putInt("total"+i, cart.getTotal());
            edit.putInt("fdid"+i, cart.getFdId());
            edit.putString("fdname"+i, cart.getFdName());
            edit.commit();
        }

        Toast.makeText(ctx, "Successfully added to Cart", Toast.LENGTH_SHORT).show();
    }
}