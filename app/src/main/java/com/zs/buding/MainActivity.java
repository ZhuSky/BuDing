package com.zs.buding;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zs.buding.view.EyeView;

public class MainActivity extends AppCompatActivity {

    private EyeView mainEyeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainEyeView = findViewById(R.id.main_eye_view);

    }

    public void blink(View view) {
        if (!mainEyeView.isStartBlinkAnim()) {
            mainEyeView.startBlinkAnim();
        } else {
            mainEyeView.stopBlinkAnim();
        }
    }

    public void square(View  view){
        mainEyeView.square();
    }

}
