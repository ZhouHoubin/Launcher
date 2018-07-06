package z.houbin.launcher;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import z.houbin.launcher.pkg.AppInfo;
import z.houbin.launcher.pkg.AppManager;
import z.houbin.launcher.screen.LauncherConfig;
import z.houbin.launcher.ui.AppHelper;
import z.houbin.launcher.ui.AppTextView;
import z.houbin.launcher.ui.StatusBarTransparentActivity;

public class AppListActivity extends StatusBarTransparentActivity implements LoaderManager.LoaderCallbacks<List<AppInfo>> {
    private GridLayout gridLayout;

    private AppTextView mFocusChild;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        setWindowWidthHeight(1f, 1f);

        gridLayout = findViewById(R.id.apps_grid);

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).startLoading();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "添加到桌面");
        menu.add(0, 2, 0, "添加到导航");
        menu.add(0, 3, 0, "应用信息");
        menu.add(0, 4, 0, "卸载");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mFocusChild != null) {
            LauncherConfig config = new LauncherConfig(getApplicationContext());
            switch (item.getItemId()) {
                case 1:
                    config.addToLauncher(mFocusChild.getPackageName());
                    break;
                case 2:
                    config.addToLauncherBottom(mFocusChild.getPackageName());
                    break;
                case 3:
                    mFocusChild.gotoDetail();
                    break;
                case 4:
                    mFocusChild.uninstall();
                    break;
            }
        }

        return super.onContextItemSelected(item);
    }

    public View.OnClickListener onIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AppHelper appHelper = (AppHelper) v;
            if (!appHelper.open()) {
                Toast.makeText(AppListActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
                if (appHelper.enable()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!appHelper.open()) {
                                Toast.makeText(AppListActivity.this, "第二次打开失败", Toast.LENGTH_SHORT).show();
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
            mFocusChild = (AppTextView) v;
            return true;
        }
    };

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<AppInfo>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<AppInfo>>(getApplicationContext()) {
            @Override
            public List<AppInfo> loadInBackground() {
                List<AppInfo> appInfos = new ArrayList<>();
//                List<PackageInfo> thirdPackage = AppManager.getInstallPackages(getPackageManager());
//                for (int i = 0; i < thirdPackage.size(); i++) {
//                    PackageInfo packageInfo = thirdPackage.get(i);
//                    AppInfo appInfo = new AppInfo();
//                    appInfo.setPackageInfo(packageInfo);
//                    appInfo.setEnable(AppManager.isEnable(getApplicationContext(), packageInfo.packageName));
//                    appInfo.setPackageName(packageInfo.packageName);
//                    appInfos.add(appInfo);
//                    System.out.println(packageInfo.packageName + "----" + packageInfo.applicationInfo.loadLabel(getPackageManager()));
//                }
                List<AppInfo> thirdPackage = AppManager.getAvaiablePackages(getPackageManager());
                return thirdPackage;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<List<AppInfo>> loader, List<AppInfo> data) {
        gridLayout.removeAllViews();
        gridLayout.setRowCount((data.size() / 4) + 1);
        gridLayout.setColumnCount(4);

        int childWidth = ((ViewGroup) gridLayout.getParent()).getMeasuredWidth() / gridLayout.getColumnCount();

        for (int i = 0; i < data.size(); i++) {
            AppTextView appView = new AppTextView(getApplicationContext());

            GridLayout.Spec rowSpec = GridLayout.spec(i / 4, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 4, 1f);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.height = gridLayout.getMeasuredWidth() / 4;
            layoutParams.width = gridLayout.getMeasuredWidth() / 4;

            AppInfo packageInfo = data.get(i);

            appView.setText(packageInfo.getPackageInfo().applicationInfo.loadLabel(getPackageManager()));
            appView.setTop(packageInfo.getPackageInfo().applicationInfo.loadIcon(getPackageManager()), childWidth / 2, childWidth / 2);
            appView.setAppInfo(packageInfo);
            appView.setOnClickListener(onIconClickListener);
            appView.setOnLongClickListener(onIconLongClickListener);
            appView.setEnabled(packageInfo.isEnabled());

            registerForContextMenu(appView);

            try {
                gridLayout.addView(appView, layoutParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<List<AppInfo>> loader) {

    }
}
