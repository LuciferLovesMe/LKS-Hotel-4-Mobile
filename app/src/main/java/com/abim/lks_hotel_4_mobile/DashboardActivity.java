package com.abim.lks_hotel_4_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.card_order){
            Intent intent = new Intent(DashboardActivity.this, OrderActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.card_cart){
            Intent intent = new Intent(DashboardActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.card_info){
            Intent intent = new Intent(DashboardActivity.this, InfoActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.card_logout){
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}