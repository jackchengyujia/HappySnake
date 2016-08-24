package net.chengyujia.happysnake.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import net.chengyujia.happysnake.Config;
import net.chengyujia.happysnake.R;
import net.chengyujia.happysnake.androidTool.LogTool;
import net.chengyujia.happysnake.bll.GameBll;
import net.chengyujia.happysnake.enums.Direction;
import net.chengyujia.happysnake.view.DirectionKeys;
import net.chengyujia.happysnake.view.SnakeView;

public class MainActivity extends BaseActivity implements View.OnTouchListener {
    private static final boolean DEBUG = Config.DEBUG;
    private SnakeView snakeView;
    private DirectionKeys directionKeys;
    private GameBll gameBll;
    private boolean initDone = false;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snakeView = (SnakeView) findViewById(R.id.snakeView);
        directionKeys = (DirectionKeys) findViewById(R.id.directionKeys);
        snakeView.setOnTouchListener(this);
        snakeView.setClickable(true);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (DEBUG) LogTool.log("双击了");
                gameBll.again();
                return true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        init();
    }

    private void init() {
        if (initDone) return;

        gameBll = new GameBll(snakeView.getWidth(), snakeView.getHeight());
        gameBll.setOnDisplayListener(snakeView);

        directionKeys.setOnClickListener(new DirectionKeys.OnClickListener() {
            @Override
            public void onClick(Direction direction) {
                gameBll.changeDirection(direction);
            }
        });

        gameBll.start();
        initDone = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (gameBll != null) {
            gameBll.resume();
        }
    }

    @Override
    protected void onPause() {
        if (gameBll != null) {
            gameBll.pause();
        }
        super.onPause();
    }
}