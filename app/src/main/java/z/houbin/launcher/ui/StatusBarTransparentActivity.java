package z.houbin.launcher.ui;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏透明
 */

public class StatusBarTransparentActivity extends AppCompatActivity {
    protected static final int FLAG_SHOW_STATUS_BAR = 0;
    protected static final int FLAG_HIDE_STATUS_BAR = 1;

    protected static final int FLAG_SHOW_NAVIGATION = 2;
    protected static final int FLAG_HIDE_NAVIGATION = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Android 4.4 statusBar半透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Android 5.0以上 statusBar背景全透明
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setNavigationBarColor(Color.TRANSPARENT);
    }

    protected void setNavigationBarColor(int color) {
        Window window;
        if ((window = getWindow()) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setNavigationBarColor(color);
            }
        }
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | decorView.getSystemUiVisibility();

            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    protected void setFlag(int flag) {
        switch (flag) {
            case FLAG_SHOW_NAVIGATION:
                break;
            case FLAG_HIDE_NAVIGATION:
                hideBottomUIMenu();
                break;
        }
    }

    protected void setWindowWidthHeight(float percentageWidth, float percentageheight) {
        Display mDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        mDisplay.getMetrics(outMetrics);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (outMetrics.widthPixels * percentageWidth);
        layoutParams.height = (int) (outMetrics.heightPixels * percentageheight);
        getWindow().setAttributes(layoutParams);
    }
}
