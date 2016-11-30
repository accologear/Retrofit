package com.tikerdev.retrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tikerdev.retrofit.actions.GetDataActivity;
import com.tikerdev.retrofit.actions.PostDataActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void get(View view) {
        startActivity(new Intent(this, GetDataActivity.class));
    }

    public void post(View view) {
        startActivity(new Intent(this, PostDataActivity.class));
    }
}
