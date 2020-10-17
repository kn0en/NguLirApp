package com.kn0en.ngulirapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.kn0en.ngulirapp.fragments.OnPageOneFragment;
import com.kn0en.ngulirapp.fragments.OnPageThreeFragment;
import com.kn0en.ngulirapp.fragments.OnPageTwoFragment;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("ALL")
public class AfterSplashActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager2 viewPager2;

    private FragmentStateAdapter pagerAdapter;

    private LinearLayout sliderIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_splash);

        Window window = AfterSplashActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sliderIndicator = findViewById(R.id.slideIndicators);
        viewPager2 = findViewById(R.id.viewPager2);
        pagerAdapter = new ViewPagerFragmentAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        setupTutorialIndicators();
        setCurrentTutorialIndicator(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentTutorialIndicator(position);
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 3000);

    }

    public void clickGetStarted(View view) {
        Intent i = new Intent(AfterSplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setupTutorialIndicators() {
        ImageView[] indicators = new ImageView[pagerAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.tutorial_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            sliderIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentTutorialIndicator(int index) {
        int childCount = sliderIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) sliderIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.tutorial_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.tutorial_indicator_inactive)
                );
            }
        }
    }

    private static class ViewPagerFragmentAdapter extends FragmentStateAdapter {
        public ViewPagerFragmentAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new OnPageOneFragment();
                case 1:
                    return new OnPageTwoFragment();
                case 2:
                    return new OnPageThreeFragment();
            }
            return new OnPageOneFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            AfterSplashActivity.this.runOnUiThread(() -> {
                if (viewPager2.getCurrentItem() < NUM_PAGES - 1) {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                } else {
                    viewPager2.setCurrentItem(0);
                }
            });
        }
    }
}