package net.chengyujia.happysnake.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.chengyujia.happysnake.Config;
import net.chengyujia.happysnake.R;
import net.chengyujia.happysnake.androidTool.LogTool;
import net.chengyujia.happysnake.enums.Direction;

/**
 * Created by CYJ on 2016/8/4.
 */
public class DirectionKeys extends View {
    private static final boolean DEBUG = Config.DEBUG;
    private static final float colorRatio = 0.8f;
    private static final int ARROW_COLOR = 0xFFFFFFFF;
    private int leftColor = 0xFFFF0000;
    private int upColor = 0xFF0000FF;
    private int rightColor = 0xFF00FF00;
    private int downColor = 0xFFFFFF00;

    private float[] colorMatrixSrc =
            {
                    colorRatio, 0, 0, 0, 0,
                    0, colorRatio, 0, 0, 0,
                    0, 0, colorRatio, 0, 0,
                    0, 0, 0, 1, 0
            };
    private ColorMatrix colorMatrix = new ColorMatrix(colorMatrixSrc);
    private ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);

    private Direction currentDirection = Direction.none;
    private OnClickListener onClickListener = null;
    private boolean initDone = false;
    //本View的宽和高
    private int width = 0;
    private int height = 0;

    private Paint paint = new Paint();
    private Path pathLeft = new Path();
    private Path pathUp = new Path();
    private Path pathRight = new Path();
    private Path pathDown = new Path();
    private Path pathLeftArrow = new Path();
    private Path pathUpArrow = new Path();
    private Path pathRightArrow = new Path();
    private Path pathDownArrow = new Path();


    public DirectionKeys(Context context) {
        super(context);
    }

    public DirectionKeys(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DirectionKeys(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DirectionKeys, defStyle, 0);
        leftColor = a.getColor(R.styleable.DirectionKeys_leftColor, leftColor);
        upColor = a.getColor(R.styleable.DirectionKeys_upColor, upColor);
        rightColor = a.getColor(R.styleable.DirectionKeys_rightColor, rightColor);
        downColor = a.getColor(R.styleable.DirectionKeys_downColor, downColor);
        a.recycle();
    }

    private void init() {
        paint.setAntiAlias(true);//抗锯齿

        width = getWidth();
        height = getHeight();
        //左上点 0，0
        //左下点 0，height
        //右上点 width,0
        //右下点 width,height
        //中心点 width/2,height/2

        initButton();
        initArrow();
    }

    private void initButton() {
        pathLeft.moveTo(0, 0);
        pathLeft.lineTo(width / 2, height / 2);
        pathLeft.lineTo(0, height);
        pathLeft.close();

        pathUp.moveTo(0, 0);
        pathUp.lineTo(width / 2, height / 2);
        pathUp.lineTo(width, 0);
        pathUp.close();

        pathRight.moveTo(width, 0);
        pathRight.lineTo(width / 2, height / 2);
        pathRight.lineTo(width, height);
        pathRight.close();

        pathDown.moveTo(width, height);
        pathDown.lineTo(width / 2, height / 2);
        pathDown.lineTo(0, height);
        pathDown.close();
    }

    //每个箭头由一个三角形和一个矩形组成
    private void initArrow() {
        //每个箭头整体的宽和高
        final int arrowWidth = width / 4;
        final int arrowHeight = height / 4;

        //左箭头
        //箭头尖的坐标
        int arrowStartX = width / 16;
        int arrowStartY = height / 2;
        //画三角形
        int arrowX = arrowStartX;
        int arrowY = arrowStartY;
        pathLeftArrow.moveTo(arrowX, arrowY);
        pathLeftArrow.lineTo(arrowX += arrowWidth / 2, arrowY -= arrowHeight / 2);
        pathLeftArrow.lineTo(arrowX, arrowY += arrowHeight);
        pathLeftArrow.close();
        //画矩形
        arrowX = arrowStartX;
        arrowY = arrowStartY;
        pathLeftArrow.addRect(arrowX += arrowWidth / 2, arrowY -= arrowHeight / 4, arrowX += arrowWidth / 2, arrowY += arrowHeight / 2, Path.Direction.CW);

        //右箭头
        Matrix matrix = new Matrix();
        matrix.setRotate(180, width / 2, height / 2);
        pathLeftArrow.transform(matrix, pathRightArrow);

        //上箭头
        arrowStartX = width / 2;
        arrowStartY = height / 16;
        arrowX = arrowStartX;
        arrowY = arrowStartY;
        pathUpArrow.moveTo(arrowX, arrowY);
        pathUpArrow.lineTo(arrowX += arrowWidth / 2, arrowY += arrowHeight / 2);
        pathUpArrow.lineTo(arrowX - +arrowWidth, arrowY);
        pathUpArrow.close();
        arrowX = arrowStartX;
        arrowY = arrowStartY;
        pathUpArrow.addRect(arrowX -= arrowWidth / 4, arrowY += arrowHeight / 2, arrowX += arrowWidth / 2, arrowY += arrowHeight / 2, Path.Direction.CW);

        //下箭头
        pathUpArrow.transform(matrix, pathDownArrow);
    }

    private void drawPath(Path path, int color, boolean isPressed, Canvas canvas) {
        if (!isPressed) {
            paint.setColorFilter(colorMatrixColorFilter);
        }
        paint.setColor(color);
        canvas.drawPath(path, paint);
        paint.setColorFilter(null);//去除颜色过滤
    }

    private void drawLeftNormal(Canvas canvas) {
        drawPath(pathLeft, leftColor, false, canvas);
        drawPath(pathLeftArrow, ARROW_COLOR, false, canvas);
    }

    private void drawLeftPressed(Canvas canvas) {
        drawPath(pathLeft, leftColor, true, canvas);
        drawPath(pathLeftArrow, ARROW_COLOR, true, canvas);
    }

    private void drawUpNormal(Canvas canvas) {
        drawPath(pathUp, upColor, false, canvas);
        drawPath(pathUpArrow, ARROW_COLOR, false, canvas);
    }

    private void drawUpPressed(Canvas canvas) {
        drawPath(pathUp, upColor, true, canvas);
        drawPath(pathUpArrow, ARROW_COLOR, true, canvas);
    }

    private void drawRightNormal(Canvas canvas) {
        drawPath(pathRight, rightColor, false, canvas);
        drawPath(pathRightArrow, ARROW_COLOR, false, canvas);
    }

    private void drawRightPressed(Canvas canvas) {
        drawPath(pathRight, rightColor, true, canvas);
        drawPath(pathRightArrow, ARROW_COLOR, true, canvas);
    }

    private void drawDownNormal(Canvas canvas) {
        drawPath(pathDown, downColor, false, canvas);
        drawPath(pathDownArrow, ARROW_COLOR, false, canvas);
    }

    private void drawDownPressed(Canvas canvas) {
        drawPath(pathDown, downColor, true, canvas);
        drawPath(pathDownArrow, ARROW_COLOR, true, canvas);
    }

    private void reset(Canvas canvas) {
        drawLeftNormal(canvas);
        drawUpNormal(canvas);
        drawRightNormal(canvas);
        drawDownNormal(canvas);
    }

    private void drawWhenLeftPressed(Canvas canvas) {
        drawLeftPressed(canvas);
        drawUpNormal(canvas);
        drawRightNormal(canvas);
        drawDownNormal(canvas);
    }

    private void drawWhenUpPressed(Canvas canvas) {
        drawLeftNormal(canvas);
        drawUpPressed(canvas);
        drawRightNormal(canvas);
        drawDownNormal(canvas);
    }

    private void drawWhenRightPressed(Canvas canvas) {
        drawLeftNormal(canvas);
        drawUpNormal(canvas);
        drawRightPressed(canvas);
        drawDownNormal(canvas);
    }

    private void drawWhenDownPressed(Canvas canvas) {
        drawLeftNormal(canvas);
        drawUpNormal(canvas);
        drawRightNormal(canvas);
        drawDownPressed(canvas);
    }

    //父类View的draw方法调用onDraw
    //父类中调用draw方法的方法有：updateDisplayListIfDirty、buildDrawingCacheImpl、createSnapshot、draw(Canvas, ViewGroup, long)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!initDone) {
            init();
            initDone = true;
        }

        switch (currentDirection) {
            case none:
                reset(canvas);
                break;
            case left:
                drawWhenLeftPressed(canvas);
                break;
            case up:
                drawWhenUpPressed(canvas);
                break;
            case right:
                drawWhenRightPressed(canvas);
                break;
            case down:
                drawWhenDownPressed(canvas);
                break;
            default:
                reset(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (DEBUG) LogTool.log("downColor=" + downColor);
        if (action == MotionEvent.ACTION_DOWN) {
            currentDirection = getDirection(event.getX(), event.getY());
            invalidate();
            if (onClickListener != null) {
                onClickListener.onClick(currentDirection);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            currentDirection = Direction.none;
            invalidate();
        } else {
            return super.onTouchEvent(event);
        }
        return true;
    }


    private Direction getDirection(float x, float y) {
        //经过坐标转换，统一成边长为1的正方形处理。对角线分割形成的4个区域，分别代表4个方向。
        float relativeX = x / getWidth();//0<=relativeX<=1
        float relativeY = y / getHeight();//0<=relativeY<=1
        if (relativeX > relativeY) {
            if (relativeX > 1 - relativeY) {//右
                return Direction.right;
            } else {//上
                return Direction.up;
            }
        } else {
            if (relativeX > 1 - relativeY) {//下
                return Direction.down;
            } else {//左
                return Direction.left;
            }
        }
    }

    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }

    public interface OnClickListener {
        void onClick(Direction direction);
    }
}