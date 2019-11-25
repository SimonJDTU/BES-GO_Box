package com.johansen.dk.bes_go_box.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.johansen.dk.bes_go_box.R;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    Button overview, recieve, give;
    String QRvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        QRvalue = getIntent().getStringExtra("QRvalue");


        overview = (Button) findViewById(R.id.overviewBtn);
        give = (Button) findViewById(R.id.giveBtn);
        recieve = (Button) findViewById(R.id.recieveBtn);

        recieve.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.overviewBtn:
                Intent overviewIntent = new Intent(this, OverviewActivity.class);
                overviewIntent.putExtra("QRvalue", QRvalue);
                startActivity(overviewIntent);
                break;
            case R.id.giveBtn:
                break;
            case R.id.recieveBtn:
                Intent recieveIntent = new Intent(this, RecieveActivity.class);
                recieveIntent.putExtra("QRvalue", QRvalue);
                startActivity(recieveIntent);
                break;
            default:
                Toast.makeText(this,"DEFAULT HIT IN SWITCH ONLCICK",Toast.LENGTH_SHORT).show();
        }
    }
}
