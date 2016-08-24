package net.chengyujia.happysnake.androidTool;

import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * Created by ChengYuJia on 2016/8/12.
 */
public class TextTool {

    public static void drawTextInCenter(Canvas canvas, String text, float textSize) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        StaticLayout layout = new StaticLayout(text, textPaint, width,
                Layout.Alignment.ALIGN_CENTER,//水平居中
                1.0F, 0.0F, true);
        canvas.save();//保存canvas的状态
        canvas.translate(0, (height - layout.getHeight()) / 2);//垂直居中
        layout.draw(canvas);
        canvas.restore();//还原canvas的状态
    }
}
