package z.houbin.launcher;

import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.List;

import z.houbin.launcher.pkg.AppManager;
import z.houbin.launcher.ui.AppView;
import z.houbin.launcher.ui.StatusBarTransparentActivity;

public class AppListActivity extends StatusBarTransparentActivity {

    private GridLayout gridLayout;

    private AppView mFocusChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        setWindowWidthHeight(1f, 1f);

        gridLayout = findViewById(R.id.apps_grid);

        new AsyncTask<Void, Void, List<PackageInfo>>() {
            @Override
            protected List<PackageInfo> doInBackground(Void... voids) {
                return AppManager.getThirdPackage(getPackageManager());
            }

            @Override
            protected void onPostExecute(List<PackageInfo> thirdPackage) {
                super.onPostExecute(thirdPackage);
                for (int i = 0; i < thirdPackage.size(); i++) {
                    AppView appView = new AppView(getApplicationContext());

                    GridLayout.Spec rowSpec = GridLayout.spec(i / 4, 1f);
                    GridLayout.Spec columnSpec = GridLayout.spec(i % 4, 1f);
                    //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    layoutParams.height = gridLayout.getMeasuredWidth() / 4;
                    layoutParams.width = gridLayout.getMeasuredWidth() / 4;

                    PackageInfo packageInfo = thirdPackage.get(i);

                    appView.setText(packageInfo.applicationInfo.loadLabel(getPackageManager()));
                    appView.setTop(packageInfo.applicationInfo.loadIcon(getPackageManager()), 100, 100);
                    appView.setPackageInfo(packageInfo);
                    appView.setOnClickListener(onIconClickListener);
                    appView.setOnLongClickListener(onIconLongClickListener);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "添加到桌面");
        menu.add(0, 2, 0, "应用信息");
        menu.add(0, 3, 0, "卸载");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mFocusChild != null) {
            switch (item.getItemId()) {
                case 1:
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

    public View.OnClickListener onIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppView view = (AppView) v;
            if (!view.open()) {
                Toast.makeText(AppListActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
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
}
