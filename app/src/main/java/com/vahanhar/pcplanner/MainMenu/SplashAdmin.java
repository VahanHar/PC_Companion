package com.vahanhar.pcplanner.MainMenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.MainMenu.Admin.AdminActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieTask;
import com.airbnb.lottie.LottieCompositionFactory;
import com.vahanhar.pcplanner.R;

public class SplashAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_admin);

        LottieAnimationView animationView = findViewById(R.id.animationView1);

        LottieTask<LottieComposition> lottieTask = LottieCompositionFactory.fromRawRes(this, R.raw.admin_an);
        lottieTask.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {
                animationView.setComposition(result);
                animationView.setRepeatCount(LottieDrawable.INFINITE);
                animationView.playAnimation();

                // Add zoom in animation effect
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(animationView, View.ALPHA, 0f, 1f);
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(animationView, View.SCALE_X, 0f, 1f);
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(animationView, View.SCALE_Y, 0f, 1f);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
                animatorSet.start();
            }
        });

        // Start the main activity with fade zoom animation after a delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashAdmin.this , AdminActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.zoom_out);
                finish();
            }
        }, 2500);
    }

}
