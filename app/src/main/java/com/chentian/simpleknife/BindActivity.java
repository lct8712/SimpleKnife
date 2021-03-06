package com.chentian.simpleknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.chentian.bind.BindKnife;
import com.chentian.bind_annotation.BindClick;
import com.chentian.bind_annotation.BindString;
import com.chentian.bind_annotation.BindView;

/**
 * 使用编译时注解的例子
 * module: inject
 *
 * @author chentian
 */
public class BindActivity extends AppCompatActivity {

    @BindView(R.id.text_hello) TextView textHello;
    @BindString(R.string.app_name) String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindKnife.bind(this, this);

        textHello.setText("Bind Success");
    }

    @BindClick(R.id.btn_hello)
    public void onBtnHelloClick() {
        Toast.makeText(this, "Button Clicked: " + appName, Toast.LENGTH_SHORT).show();
    }
}
