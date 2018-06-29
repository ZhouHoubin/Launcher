package z.houbin.launcher.ui;

public interface AppHelper {
    boolean open();

    boolean disable();

    boolean enable();

    String getPackageName();

    boolean gotoDetail();

    boolean uninstall();
}
