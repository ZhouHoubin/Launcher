package z.houbin.launcher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import z.houbin.launcher.pkg.AppInfo;
import z.houbin.launcher.pkg.AppManager;
import z.houbin.launcher.screen.Launcher;
import z.houbin.launcher.screen.LauncherConfig;
import z.houbin.launcher.ui.AppButtonView;
import z.houbin.launcher.ui.AppHelper;
import z.houbin.launcher.ui.AppTextView;
import z.houbin.launcher.ui.GridLinearLayout;
import z.houbin.launcher.ui.StatusBarTransparentActivity;
import z.houbin.launcher.ui.WallpaperUtil;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends StatusBarTransparentActivity implements View.OnClickListener {
    private GridLinearLayout gridBottom, gridMain;

    private Handler handler = new Handler();

    private AppHelper mFocusChild;

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

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });

        gridMain = findViewById(R.id.main_grid);
        gridBottom = findViewById(R.id.main_bottom);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "删除图标");
        menu.add(0, 2, 0, "应用信息");
        menu.add(0, 3, 0, "卸载");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mFocusChild != null) {
            switch (item.getItemId()) {
                case 1:
                    LauncherConfig config = new LauncherConfig(getApplicationContext());
                    config.deleteFromLauncher(mFocusChild.getPackageName());
                    onResume();
                    mFocusChild = null;
                    break;
                case 2:
                    mFocusChild.gotoDetail();
                    break;
                case 3:
                    mFocusChild.uninstall();
                    break;
                case 4:
                    LauncherConfig config2 = new LauncherConfig(getApplicationContext());
                    config2.deleteFromLauncherBottom(mFocusChild.getPackageName());
                    onResume();
                    mFocusChild = null;
                    break;
                case 5:
                    mFocusChild.gotoDetail();
                    break;
            }
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, List<AppInfo>>() {
            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                LauncherConfig config = new LauncherConfig(getApplicationContext());
                List<String> launcherPackages = config.getLauncherPackages();
                List<AppInfo> appInfos = new ArrayList<>();
                for (int i = 0; i < launcherPackages.size(); i++) {
                    PackageInfo packageInfo = AppManager.getPackageActivity(getPackageManager(), launcherPackages.get(i));
                    AppInfo appInfo = new AppInfo();
                    appInfo.setPackageInfo(packageInfo);
                    appInfo.setEnabled(AppManager.isEnable(getPackageManager(), packageInfo.packageName));
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfos.add(appInfo);
                }
                return appInfos;
            }

            @Override
            protected void onPostExecute(List<AppInfo> data) {
                super.onPostExecute(data);
                gridMain.removeAllViews();
                gridMain.setColumnCount(4);

                int childWidth = ((ViewGroup) gridMain.getParent()).getMeasuredWidth() / gridMain.getColumnCount();

                for (int i = 0; i < data.size(); i++) {
                    AppTextView appView = new AppTextView(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(gridMain.getMeasuredWidth() / gridMain.getColumnCount(), gridMain.getMeasuredWidth() / gridMain.getColumnCount());

                    AppInfo packageInfo = data.get(i);

                    appView.setText(packageInfo.getPackageInfo().applicationInfo.loadLabel(getPackageManager()));
                    appView.setTop(packageInfo.getPackageInfo().applicationInfo.loadIcon(getPackageManager()), childWidth / 2, childWidth / 2);
                    appView.setAppInfo(packageInfo);
                    appView.setOnClickListener(onIconClickListener);
                    appView.setOnLongClickListener(onIconLongClickListener);
                    appView.setEnabled(packageInfo.isEnabled());
                    appView.setDefaultTextColor(Color.WHITE);

                    appView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            menu.add(0, 1, 0, "移除");
                            menu.add(0, 2, 0, "应用信息");
                            menu.add(0, 3, 0, "卸载");
                        }
                    });
                    try {
                        gridMain.addView(appView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute();

        new AsyncTask<Void, Void, List<AppInfo>>() {
            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                LauncherConfig config = new LauncherConfig(getApplicationContext());
                List<String> launcherPackages = config.getLauncherBottomPackages();
                List<AppInfo> appInfos = new ArrayList<>();
                for (int i = 0; i < launcherPackages.size(); i++) {
                    PackageInfo packageInfo = AppManager.getPackageActivity(getPackageManager(), launcherPackages.get(i));
                    AppInfo appInfo = new AppInfo();
                    appInfo.setPackageInfo(packageInfo);
                    appInfo.setEnabled(AppManager.isEnable(getPackageManager(), packageInfo.packageName));
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfos.add(appInfo);
                }
                return appInfos;
            }

            @Override
            protected void onPostExecute(List<AppInfo> appInfos) {
                super.onPostExecute(appInfos);
                gridBottom.removeAllViews();
                gridBottom.setColumnCount(5);
                for (int i = 0; i < appInfos.size(); i++) {
                    AppButtonView appView = new AppButtonView(getApplicationContext());
                    gridBottom.addView(appView);

                    AppInfo packageInfo = appInfos.get(i);
                    appView.setAppInfo(packageInfo);
                    appView.setOnClickListener(onIconClickListener);
                    appView.setOnLongClickListener(onIconLongClickListener);

                    appView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            menu.add(0, 4, 0, "移除");
                            menu.add(0, 5, 0, "应用信息");
                        }
                    });
                }

                AppButtonView child = new AppButtonView(getApplicationContext());
                for (int i = 0; i < gridBottom.getColumnCount() - gridBottom.getChildCount() - 1; i++) {
                    gridBottom.addView(new TextView(getApplicationContext()));
                }

                gridBottom.addView(child, 2);

                child.setImageResource(R.mipmap.ic_allapps_color);
                child.setBackgroundColor(Color.TRANSPARENT);
                child.setOnClickListener(MainActivity.this);
                child.setClickable(true);
                child.setFocusable(true);
                child.setId(R.id.main_allapps);
            }
        }.execute();
    }

    public View.OnClickListener onIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AppHelper appHelper = (AppHelper) v;
            if (!appHelper.open()) {
                Toast.makeText(MainActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
                if (appHelper.enable()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!appHelper.open()) {
                                Toast.makeText(MainActivity.this, "第二次打开失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);
                }
            }
            mFocusChild = null;
        }
    };

    public View.OnLongClickListener onIconLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            v.showContextMenu();
            mFocusChild = (AppHelper) v;
            return true;
        }
    };

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
