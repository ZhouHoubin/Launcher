package z.houbin.launcher;

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
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import z.houbin.launcher.pkg.AppInfo;
import z.houbin.launcher.pkg.AppManager;
import z.houbin.launcher.screen.Launcher;
import z.houbin.launcher.screen.LauncherConfig;
import z.houbin.launcher.ui.AppView;
import z.houbin.launcher.ui.StatusBarTransparentActivity;
import z.houbin.launcher.ui.WallpaperUtil;

public class MainActivity extends StatusBarTransparentActivity implements View.OnClickListener {

    private GridLayout gridLayout;

    private Handler handler = new Handler();

    private AppView mFocusChild;

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
                e.printStackTrace();
            }
        });

        gridLayout = findViewById(R.id.main_grid);
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
                    gridLayout.removeView(mFocusChild);
                    gridLayout.invalidate();
                    mFocusChild = null;
                    break;
                case 2:
                    mFocusChild.gotoDetailActivity();
                    break;
                case 3:
                    mFocusChild.uninstall();
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
                    appInfo.setEnable(AppManager.isEnable(getApplicationContext(), packageInfo.packageName));
                    appInfo.setPackageName(packageInfo.packageName);
                    appInfos.add(appInfo);
                }
                return appInfos;
            }

            @Override
            protected void onPostExecute(List<AppInfo> data) {
                super.onPostExecute(data);
                gridLayout.removeAllViews();
                gridLayout.setRowCount((data.size() / 4) + 1);
                for (int i = 0; i < data.size(); i++) {
                    AppView appView = new AppView(getApplicationContext());

                    GridLayout.Spec rowSpec = GridLayout.spec(i / 4, 1f);
                    GridLayout.Spec columnSpec = GridLayout.spec(i % 4, 1f);
                    //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    layoutParams.height = gridLayout.getMeasuredWidth() / 4;
                    layoutParams.width = gridLayout.getMeasuredWidth() / 4;

                    AppInfo packageInfo = data.get(i);

                    appView.setText(packageInfo.getPackageInfo().applicationInfo.loadLabel(getPackageManager()));
                    appView.setTop(packageInfo.getPackageInfo().applicationInfo.loadIcon(getPackageManager()), 100, 100);
                    appView.setAppInfo(packageInfo);
                    appView.setOnClickListener(onIconClickListener);
                    appView.setOnLongClickListener(onIconLongClickListener);
                    appView.setEnabled(packageInfo.isEnable());
                    appView.setDefaultTextColor(Color.WHITE);

                    registerForContextMenu(appView);
                    try {
                        gridLayout.addView(appView, layoutParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute();
    }

    public View.OnClickListener onIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AppView view = (AppView) v;
            if (!view.open()) {
                Toast.makeText(MainActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
                if (view.enable()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!view.open()) {
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
            mFocusChild = (AppView) v;
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
