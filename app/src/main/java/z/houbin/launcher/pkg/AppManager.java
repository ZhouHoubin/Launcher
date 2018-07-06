package z.houbin.launcher.pkg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class AppManager {
    private static HashMap<String, String> adMap = new HashMap<>();

    static {
        adMap.put("com.adsmogo.adview.AdsMogoWebView", "芒果广告");
        adMap.put("com.lmmob.ad.sdk.LmMobAdWebView", "力美");
        adMap.put("com.mobisage.android.MobiSageActivity", "艾德思奇");
        adMap.put("net.youmi.android.AdActivity", "有米");
        adMap.put("net.youmi.android.AdBrowser", "有米");
        adMap.put("cn.domob.android.ads.DomobActivity", "多盟");
        adMap.put("com.adwo.adsdk.AdwoAdBrowserActivity", "安沃");
        adMap.put("com.vpon.adon.android.WebInApp", "Vpo");
        adMap.put("com.google.ads.AdActivity", "AdMob");
        adMap.put("com.adchina.android.ads.views.AdBrowserView", "AdChina");
        adMap.put("com.winad.android.ads.VideoPlayerActivity", "赢告");
        adMap.put("com.wiyun.common.SimpleBrowserActivity", "微云");
        adMap.put("com.greystripe.android.sdk.AdContentProvider", "Greystripe");
        adMap.put("com.mdotm.android.ads.MdotmLandingPage", "MdotM");
        adMap.put("com.millennialmedia.android.MMAdViewOverlayActivity", "Millennial");
        adMap.put("com.mt.airad.MultiAD", "AirAD");
        adMap.put("com.wooboo.adlib_android.AdActivity", "哇棒");
        adMap.put("com.tencent.mobwin.MobinWINBrowserActivity", "聚赢");
        adMap.put("com.baidu.mobads.AppActivity", "百度");
        adMap.put("com.umengAd.android.UmengAdDetailActivity", "友盟");
        adMap.put("com.fractalist.sdk.base.sys.FtActivity", "飞云");
        adMap.put("net.miidi.ad.banner.AdBannerActivity", "米迪");
        adMap.put("cn.appmedia.ad.AdActivity", "Appmedia");
        adMap.put("com.suizong.mobplate.ads.AdActivity", "随踪");
        adMap.put("com.inmobi.androidsdk.IMBrowserActivity", "InMobi");
        adMap.put("com.telead.adlib_android.AdActivity", "天翼");
        adMap.put("com.l.adlib_android.AdBrowseActivity", "百分联通");
        adMap.put("com.donson.momark.activity.AdActivity", "Momark");
        adMap.put("com.doumob.main.WebViewActivity", "Doumob");
        adMap.put("com.mobile.app.adlist", "第七传媒");
        adMap.put("com.adzhidian.view.WebViewActivity", "指点传媒");
        adMap.put("com.huawei.hiad.core.BrowserActivity", "华为聚点");
        adMap.put("com.adchina.android.ads.views.FullScreenAdActivity", "AdChina");
        adMap.put("com.lmmob.ad.sdk.LmMobFullImageActivity", "力美");
        adMap.put("com.wooboo.adlib_android.FullActivity", "哇棒");
        adMap.put("com.adwo.adsdk.AdwoSplashAdActivity", "安沃");
        adMap.put("com.telead.adlib_android.FullActivity", "天翼");
        adMap.put("cn.aduu.adsdk.AdSpotActivity", "优友");
        adMap.put("net.cavas.show.MainLoadCavasActivity", "芒果");
        adMap.put("com.adsmogo.offers.adapters.WanpuAdView", "万普");
        adMap.put("net.miidi.ad.wall.AdWallActivity", "米迪");
        adMap.put("com.dianle.DianleOfferActivity", "点乐");
        adMap.put("com.lmmob.sdk.AdListActivity", "力美");
        adMap.put("com.yjfsdk.advertSdk.AdverWallActivity", "易积分");
        adMap.put("com.winad.android.wall.MoreAdActivity", "赢告");
        adMap.put("com.telead.adlib.adwall.TeleadWallActivity", "天翼");
        adMap.put("com.mobile.app.adlist.GEList", "第七传媒");
        adMap.put("com.juzi.main.TheAdVirtualGoods", "桔子平台");
        adMap.put("com.waps.OffersWebView", "万普世纪");
        adMap.put("com.wiyun.offer.OfferLis", "微云");
        adMap.put("com.etonenet.pointwall.EtonenetPointWallActivity", "移通");
        adMap.put("com.winad.android.adwall.push.PushContentActivity", "赢告");
        adMap.put("com.bypush.PushActivity", "艾普");
        adMap.put("com.kuguo.ad.MainActivity", "酷果");
        adMap.put("cn.aduu.adsdk.AduuPushAdReceiver", "优友");
        adMap.put("com.iadpush.adp.IA", "IadPush");
        adMap.put("com.mobile.app.adpush.GELaunchNotify", "第七传媒");
        adMap.put("com.longmob.service.LongActivity", "掌龙广告平台");
        adMap.put("cn.jpush.android.ui.PushActivity", "极光推送");
        adMap.put("com.airpush.", "AirPush");
        adMap.put("com.LeadBolt.", "LeadBolt");
        adMap.put("com.appenda.", "Appenda");
        adMap.put("com.iac.notification", "IAC");
        adMap.put("com.tapit.adview.notify.", "TapIt");
        adMap.put("com.adnotify", "Moolah Media");
        adMap.put("com.senddorid", "SendDroid");
        adMap.put("cn.kuguo", "Kuguo");
        adMap.put("com.applovin", "Applovin");
        adMap.put("xx.ii.qq.Ad", "xq");
        adMap.put("com.tgszgo.android.gms.ads.AdActivity", "tgszgo");
        adMap.put("com.adpooh.adscast.AdsWebViewActivity", "adpooh");
        adMap.put("com.google.android.gms.ads.AdActivity", "google");
        adMap.put("com.qq.e.ads.AdActivity", "腾讯广点通");
        adMap.put("com.qq.e.ads.ADActivity", "腾讯广点通");
        adMap.put("com.nd.dianjin.", "点金推荐墙广告");
        adMap.put("com.baidu.ops.appunion.", "百通");
        adMap.put("com.baidu.appx.", "appX插屏广告");
    }

    public static String isAd(ComponentName componentName) {
        StringBuilder ad = new StringBuilder();
        String name = componentName.getPackageName() + componentName.getClassName();
        Iterator<String> iterator = adMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (name.contains(key)) {
                ad.append(adMap.get(key));
            }
        }
        return ad.toString();
    }

    /**
     * 启动程序
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean openApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent LaunchIntent = packageManager.getLaunchIntentForPackage(packageName);
        try {
            context.startActivity(LaunchIntent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 卸载
     *
     * @param context
     * @param packageName
     * @return
     */
    public static void unInstall(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    public static List<PackageInfo> getThirdPackage(PackageManager manager) {
        List<PackageInfo> thirdPackages = new ArrayList<>();
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        for (PackageInfo installedPackage : installedPackages) {
            if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                thirdPackages.add(installedPackage);
            }
        }
        return thirdPackages;
    }

    public static List<PackageInfo> getInstallPackages(PackageManager manager) {
        List<PackageInfo> thirdPackages = new ArrayList<>();
        List<PackageInfo> installedPackages = manager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.MATCH_DISABLED_COMPONENTS);
        for (PackageInfo installedPackage : installedPackages) {
            if (installedPackage.activities != null) {
                thirdPackages.add(installedPackage);
            }
            if (manager.getLaunchIntentForPackage(installedPackage.packageName) != null) {

            }
        }
        //thirdPackages.addAll(installedPackages);
        return thirdPackages;
    }

    public static List<AppInfo> getAvaiablePackages(PackageManager manager) {
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = manager.queryIntentActivities(resolveIntent, PackageManager.MATCH_DISABLED_COMPONENTS);

        HashMap<String, String> keyMap = new HashMap<>();

        List<AppInfo> appInfoList = new ArrayList<>();

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppInfo info = new AppInfo();
            info.setPackageName(resolveInfo.activityInfo.packageName);
            info.setEnabled(isEnable(manager, info.getPackageName()));
            info.setActivityInfo(resolveInfo.activityInfo);
            PackageInfo packageInfo = getPackageActivity(manager, info.getPackageName());
            if (packageInfo != null) {
                info.setIcon(packageInfo.applicationInfo.loadIcon(manager));
                info.setName(packageInfo.applicationInfo.loadLabel(manager).toString());
            } else {
                info.setIcon(resolveInfo.loadIcon(manager));
                info.setName(resolveInfo.loadLabel(manager).toString());
            }
            info.setPackageInfo(packageInfo);
            if (!keyMap.containsKey(info.getPackageName())) {
                appInfoList.add(info);
                keyMap.put(info.getPackageName(), null);
            }
        }

        return appInfoList;
    }

    public static PackageInfo getPackageActivity(PackageManager manager, String packageName) {
        try {
            return manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES | PackageManager.MATCH_DISABLED_COMPONENTS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PackageInfo getPackageReceiver(PackageManager manager, String packageName) {
        try {
            return manager.getPackageInfo(packageName, PackageManager.GET_RECEIVERS | PackageManager.MATCH_DISABLED_COMPONENTS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PackageInfo getPackageService(PackageManager manager, String packageName) {
        try {
            return manager.getPackageInfo(packageName, PackageManager.GET_SERVICES | PackageManager.MATCH_DISABLED_COMPONENTS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEnable(PackageManager manager, String packageName) {
        Intent launchIntentForPackage = manager.getLaunchIntentForPackage(packageName);
        return launchIntentForPackage != null;
    }

    public static boolean isDisable(Context context, String packageName, ComponentName componentName) {
        boolean disable = false;
        try {
            Context c = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            int status = c.getPackageManager().getComponentEnabledSetting(componentName);
            if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER) {
                disable = true;
            }
            c = null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return disable;
    }


    public static boolean disableComponent(ComponentName componentName) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            String cmd = "pm disable " + componentName.getPackageName() + "/" + componentName.getClassName() + "\n";
            DataOutputStream dataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean enableComponent(ComponentName componentName) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            String cmd = "pm enable " + componentName.getPackageName() + "/" + componentName.getClassName() + "\n";
            DataOutputStream dataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized boolean enable(String packageName) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            String cmd = "pm enable " + packageName + "\n";
            DataOutputStream dataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized boolean disable(String packageName) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            String cmd = "pm disable " + packageName + "\n";
            DataOutputStream dataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
