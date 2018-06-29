package z.houbin.launcher.ui.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
    public static Bitmap zoomImage(Bitmap bmp, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bmp.getWidth();
        float height = bmp.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

}
