package nurasoftech.gridsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        ImageView logoImageView = findViewById(R.id.logoImageView);
        ImageView bannerImageView = findViewById(R.id.bannerImageView);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(fadeIn);
        bannerImageView.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            // Start MainActivity after the splash duration
            Intent mainIntent = new Intent(Splash.this, MainActivity.class);
            startActivity(mainIntent);
            finish(); // Close the splash activity so that it's not shown on back press
        }, SPLASH_DISPLAY_DURATION);
    }
}