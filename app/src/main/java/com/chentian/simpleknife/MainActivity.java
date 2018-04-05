package com.chentian.simpleknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chentian.inject.InjectKnife;
import com.chentian.inject.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.text_hello) TextView textHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InjectKnife.Inject(this, this);

        textHello.setText("Inject Success");
    }
}
