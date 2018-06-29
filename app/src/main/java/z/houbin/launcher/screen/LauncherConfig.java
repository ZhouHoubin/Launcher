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

    private static final String key_launcher_config = "launcher_config";


    public LauncherConfig(Context context) {
        this.context = context;
        this.cache = ACache.get(context);
    }

    public void addToLauncher(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        try {
            JSONObject homeJson = getHomeJson();
            JSONArray jsonArray = getHomeJsonArray(homeJson);

            if (jsonArray == null) {
                jsonArray = new JSONArray();
            }

            JSONObject item = new JSONObject();
            item.put("package", packageName);
            item.put("page", 0);
            jsonArray.put(item);
            cache.put(key_launcher_config, homeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addToLauncherBottom(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        try {
            JSONObject homeJson = getHomeJson();
            JSONArray jsonArray = getHomeButtomJsonArray(homeJson);

            if (jsonArray == null) {
                jsonArray = new JSONArray();
            }

            JSONObject item = new JSONObject();
            item.put("package", packageName);
            item.put("page", 0);
            jsonArray.put(item);
            cache.put(key_launcher_config, homeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray getHomeJsonArray(JSONObject configObject) {
        if (configObject == null) {
            configObject = new JSONObject();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = configObject.getJSONArray("home_apps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            jsonArray = new JSONArray();
            try {
                configObject.put("home_apps", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    private JSONArray getHomeButtomJsonArray(JSONObject configObject) {
        if (configObject == null) {
            configObject = new JSONObject();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = configObject.getJSONArray("home_bottom_apps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            jsonArray = new JSONArray();
            try {
                configObject.put("home_bottom_apps", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    private JSONObject getHomeJson() {
        JSONObject configObject = cache.getAsJSONObject(key_launcher_config);
        if (configObject == null) {
            configObject = new JSONObject();
        }
        return configObject;
    }

    public void deleteFromLauncher(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        try {
            JSONObject homeJson = getHomeJson();
            JSONArray jsonArray = getHomeJsonArray(homeJson);
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
            cache.put(key_launcher_config, homeJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFromLauncherBottom(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        try {
            JSONObject homeJson = getHomeJson();
            JSONArray jsonArray = getHomeButtomJsonArray(homeJson);
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
            cache.put(key_launcher_config, homeJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getLauncherPackages() {
        List<String> packages = new ArrayList<>();
        JSONArray jsonArray = getHomeJsonArray(getHomeJson());
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

    public List<String> getLauncherBottomPackages() {
        List<String> packages = new ArrayList<>();
        JSONArray jsonArray = getHomeButtomJsonArray(getHomeJson());
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
