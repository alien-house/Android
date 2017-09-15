package com.alienhouse.kitten;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Created by shinji on 2017/09/13.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setImageDrawable(R.drawable.intro01);
        sliderPage1.setBgColor(Color.TRANSPARENT);

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Create developer account");
        sliderPage2.setDescription("Make your profile and tell me what position you want to get.");
        sliderPage2.setImageDrawable(R.drawable.intro02);
        sliderPage2.setBgColor(Color.TRANSPARENT);

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Getting information");
        sliderPage3.setDescription("Getting information that it's suited for you.");
        sliderPage3.setImageDrawable(R.drawable.intro03);
        sliderPage3.setBgColor(Color.TRANSPARENT);

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Finding a job");
        sliderPage4.setDescription("Finding job opportunities around you.");
        sliderPage4.setImageDrawable(R.drawable.intro04);
        sliderPage4.setBgColor(Color.TRANSPARENT);

        addSlide(AppIntroFragment.newInstance(sliderPage1));
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        addSlide(AppIntroFragment.newInstance(sliderPage3));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance(title, description, image, backgroundColor));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#2b64f5"));
        setSeparatorColor(Color.parseColor("#333333"));

        // Hide Skip/Done button.
//        showSkipButton(false);
//        setProgressButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
