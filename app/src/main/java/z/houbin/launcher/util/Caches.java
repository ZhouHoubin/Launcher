package z.houbin.launcher.util;

import android.content.Context;

public class Caches {
    private static Caches cache;
    private ACache aCache;

    private Caches(Context context) {
        aCache = ACache.get(context);
    }

    public static Caches getInstance(Context context) {
        if (cache == null) {
            cache = new Caches(context);
        }
        return cache;
    }

    public ACache getCache() {
        return aCache;
    }
}
