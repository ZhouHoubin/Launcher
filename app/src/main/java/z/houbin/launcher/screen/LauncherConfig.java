package z.houbin.launcher.screen;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import z.houbin.launcher.util.ACache;

public class LauncherConfig {
    public static int screenWidth;
    public static int screenHeight;
    public static int screenCount;

    private Context context;

    private final ACache cache;

    private static final String key_launcher_app = "launcher_apps";

    public LauncherConfig(Context context) {
        this.context = context;
        this.cache = ACache.get(context);
    }

    public void addToLauncher(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        JSONArray jsonArray = cache.getAsJSONArray(key_launcher_app);
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }

        JSONObject item = new JSONObject();
        try {
            item.put("package", packageName);
            item.put("page", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(item);
        cache.put(key_launcher_app, jsonArray);
    }

    public void deleteFromLauncher(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        JSONArray jsonArray = cache.getAsJSONArray(key_launcher_app);
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pkg = jsonObject.getString("package");
                if (pkg.equalsIgnoreCase(packageName)) {
                    jsonArray.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cache.put(key_launcher_app, jsonArray);
    }

    public List<String> getLauncherPackages() {
        List<String> packages = new ArrayList<>();
        JSONArray jsonArray = cache.getAsJSONArray(key_launcher_app);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    packages.add(jsonObject.getString("package"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return packages;
    }
}
