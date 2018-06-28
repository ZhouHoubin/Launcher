package z.houbin.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;

import z.houbin.launcher.pkg.AppManager;

public class AppView extends AppCompatTextView {
    private PackageInfo packageInfo;

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

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public boolean open() {
        if (this.packageInfo != null) {
            return AppManager.openApp(getContext(), packageInfo.packageName);
        } else {
            return false;
        }
    }

    private String getPackageName() {
        if (this.packageInfo != null) {
            return packageInfo.packageName;
        } else {
            return null;
        }
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
        if (this.packageInfo != null) {
            AppManager.unInstall(getContext(), packageInfo.packageName);
        }
    }
}
