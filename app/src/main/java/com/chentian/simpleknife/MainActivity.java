package com.chentian.simpleknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.chentian.inject.InjectClick;
import com.chentian.inject.InjectKnife;
import com.chentian.inject.InjectView;

/**
 * 使用运行时注解的例子
 * module: inject
 */
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.text_hello) TextView textHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InjectKnife.inject(this, this);

        textHello.setText("Inject Success");
    }


    @InjectClick(R.id.btn_hello)
    public void onBtnHelloClick() {
        Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show();
    }
}
