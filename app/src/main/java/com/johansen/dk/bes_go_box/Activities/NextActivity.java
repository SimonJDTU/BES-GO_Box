package com.johansen.dk.bes_go_box.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.johansen.dk.bes_go_box.R;

public class NextActivity extends AppCompatActivity implements View.OnClickListener {

    Button overview, recieve, give;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        overview = (Button) findViewById(R.id.overview);
        give = (Button) findViewById(R.id.give);
        recieve = (Button) findViewById(R.id.recieve);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.overview:
                startActivity(new Intent(NextActivity.this, OverviewActivity.class));
                break;
            case R.id.give:
                break;
            case R.id.recieve:
                break;
            default:
                Toast.makeText(this,"DEFAULT HIT IN SWITCH ONLCICK",Toast.LENGTH_SHORT).show();
        }
    }
}
