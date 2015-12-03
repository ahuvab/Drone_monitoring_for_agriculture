package com.example.user.airscort_agriculture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button reg;
    private Button log;
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        logo=(ImageView)findViewById(R.id.ImageLogo);
        reg = (Button) findViewById(R.id.buttonReg);
        log = (Button) findViewById(R.id.buttonLog);
        reg.setOnClickListener(this);
        log.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (reg.getId() == v.getId()){
            Intent intent=new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        if (log.getId() == v.getId()){
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
