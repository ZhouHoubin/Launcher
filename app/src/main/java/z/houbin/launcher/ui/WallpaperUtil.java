package z.houbin.launcher.ui;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import z.houbin.launcher.screen.Launcher;
import z.houbin.launcher.screen.LauncherConfig;

public class WallpaperUtil {

    public static Drawable getWallpaper(Context context, Launcher launcher) {
// 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();

        // 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别
        //对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下
        float step = 0;
        // 计算出屏幕的偏移量
        step = (bm.getWidth() - LauncherConfig.screenWidth) / (LauncherConfig.screenCount - 1);
        // 截取相应屏幕的Bitmap
        Bitmap pbm = Bitmap.createBitmap(bm, (int) (launcher.getCurrentWorkspaceScreen() * step), 0,
                (int) (LauncherConfig.screenWidth),
                (int) (LauncherConfig.screenHeight));
        return new BitmapDrawable(pbm);
    }
}
