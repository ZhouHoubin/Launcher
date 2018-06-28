package z.houbin.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;

import z.houbin.launcher.pkg.AppInfo;
import z.houbin.launcher.pkg.AppManager;

public class AppView extends AppCompatTextView {
    private AppInfo appInfo;
    private int defaultTextColor = Color.BLACK;

    public AppView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        setLines(1);
        setPadding(10, 10, 10, 10);
        setClickable(true);
        setFocusable(true);
    }

    public void setTop(Drawable drawable, int width, int height) {
        drawable.setBounds(0, 0, width, height);
        setCompoundDrawables(null, drawable, null, null);
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
        setTextColor(defaultTextColor);
    }

    public boolean open() {
        if (this.appInfo != null) {
            return AppManager.openApp(getContext(), appInfo.getPackageName());
        } else {
            return false;
        }
    }

    public boolean disable() {
        return AppManager.disable(getPackageName());
    }

    public boolean enable() {
        return AppManager.enable(getPackageName());
    }

    public String getPackageName() {
        if (this.appInfo != null) {
            return appInfo.getPackageName();
        } else {
            return null;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setPaintFlags(getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            setTextColor(defaultTextColor);
        } else {
            setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            setTextColor(Color.GRAY);
        }
        setClickable(true);
        setFocusable(true);

    }

    public void gotoDetailActivity() {
        if (getPackageName() == null) {
            return;
        }
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        getContext().startActivity(mIntent);
    }

    public void uninstall() {
        if (this.appInfo != null) {
            AppManager.unInstall(getContext(), appInfo.getPackageName());
        }
    }
}
