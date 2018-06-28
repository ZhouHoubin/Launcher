package z.houbin.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import z.houbin.launcher.screen.Launcher;
import z.houbin.launcher.screen.LauncherConfig;
import z.houbin.launcher.ui.StatusBarTransparentActivity;
import z.houbin.launcher.ui.WallpaperUtil;

public class MainActivity extends StatusBarTransparentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LauncherConfig.screenWidth = metrics.widthPixels;
        LauncherConfig.screenHeight = metrics.heightPixels;
        LauncherConfig.screenCount = 2;

        getWindow().setBackgroundDrawable(WallpaperUtil.getWallpaper(getApplicationContext(), new Launcher()));

        findViewById(R.id.main_allapps).setOnClickListener(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_allapps:
                Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
