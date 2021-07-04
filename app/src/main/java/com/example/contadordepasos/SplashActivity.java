package com.example.contadordepasos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contadordepasos.MainActivity;
import com.example.contadordepasos.R;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private AnimationDrawable animacion;
    private ImageView loading;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        loading = findViewById(R.id.loading);
        textView = findViewById(R.id.texto);
        loading.setBackgroundResource(R.drawable.cargando);
        animacion = (AnimationDrawable) loading.getBackground();
        animacion.start();
        animationSetup();
    }

    private void animationSetup(){
        Animation transicion = AnimationUtils.loadAnimation(this, R.anim.mitransicion);
        loading.startAnimation(transicion);
        textView.startAnimation(transicion);
        transicion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                stopAnimation();
                redirectUI();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void stopAnimation(){
        animacion.stop();
    }
    private void redirectUI() {
        Intent intent = new Intent(SplashActivity.this, DebugActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}