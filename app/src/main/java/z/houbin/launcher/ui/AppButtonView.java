package z.houbin.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.AppCompatImageButton;

import z.houbin.launcher.pkg.AppInfo;
import z.houbin.launcher.pkg.AppManager;

public class AppButtonView extends AppCompatImageButton implements AppHelper {
    private AppInfo appInfo;
    private int defaultTextColor = Color.BLACK;

    public AppButtonView(Context context) {
        super(context);
        setClickable(true);
        setFocusable(true);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setPadding(getMeasuredWidth() / 6, getMeasuredWidth() / 6, getMeasuredWidth() / 6, getMeasuredWidth() / 6);
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
        setImageDrawable(appInfo.getPackageInfo().applicationInfo.loadIcon(getContext().getPackageManager()));
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setClickable(true);
        setFocusable(true);
    }

    public boolean open() {
        return this.appInfo != null && AppManager.openApp(getContext(), appInfo.getPackageName());
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
    public boolean gotoDetail() {
        if (getPackageName() == null) {
            return false;
        }
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", getPackageName(), null));
        getContext().startActivity(mIntent);
        return true;
    }


    public boolean uninstall() {
        if (this.appInfo != null) {
            AppManager.unInstall(getContext(), appInfo.getPackageName());
        }
        return true;
    }
}
