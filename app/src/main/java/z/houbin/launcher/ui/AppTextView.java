package z.houbin.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;

import z.houbin.launcher.pkg.AppInfo;
import z.houbin.launcher.pkg.AppManager;

public class AppTextView extends AppCompatTextView implements AppHelper {
    private AppInfo appInfo;
    private int defaultTextColor = Color.BLACK;

    public AppTextView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        setLines(1);
        setClickable(true);
        setFocusable(true);
    }

    public void setTop(Drawable drawable, int width, int height) {
        drawable.setBounds(0, 0, width, height);
        setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setPadding(getMeasuredWidth() / 10, getMeasuredWidth() / 10, getMeasuredWidth() / 10, getMeasuredWidth() / 10);
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


    public boolean uninstall() {
        if (this.appInfo != null) {
            AppManager.unInstall(getContext(), appInfo.getPackageName());
        }
        return true;
    }
}
