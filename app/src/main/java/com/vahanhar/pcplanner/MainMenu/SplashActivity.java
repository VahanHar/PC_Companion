package com.vahanhar.pcplanner.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieTask;
import com.airbnb.lottie.LottieCompositionFactory;
import com.vahanhar.pcplanner.R;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        LottieAnimationView animationView = findViewById(R.id.animationView1);

        LottieTask<LottieComposition> lottieTask = LottieCompositionFactory.fromRawRes(this, R.raw.splash_an);
        lottieTask.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {
                animationView.setComposition(result);
                animationView.setRepeatCount(LottieDrawable.INFINITE);
                animationView.playAnimation();
            }
        });
        LottieAnimationView logoAnimationView = findViewById(R.id.animationView2);
        LottieTask<LottieComposition> logoAnimationTask = LottieCompositionFactory.fromRawRes(this, R.raw.logopc);
        logoAnimationTask.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {
                logoAnimationView.setComposition(result);
                logoAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                logoAnimationView.playAnimation();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this , Login.class));
                finish();
            }
        }, 2800);
    }

}
