package net.chengyujia.happysnake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import net.chengyujia.happysnake.R;
import net.chengyujia.happysnake.androidTool.TextTool;
import net.chengyujia.happysnake.bll.GameBll;
import net.chengyujia.happysnake.entity.GameData;
import net.chengyujia.happysnake.entity.Tile;
import net.chengyujia.happysnake.enums.GameState;

import java.util.List;

/**
 * Created by CYJ on 2016/8/6.
 */
public class SnakeView extends View implements GameBll.OnDisplayListener {
    private boolean initDone = false;
    private Paint paint = new Paint();
    private GameData gameData;
    private static final int WALL_COLOR = Color.GREEN;
    private static final int FOOD_COLOR = Color.RED;
    private static final int SNAKE_COLOR = Color.MAGENTA;
    private static final int SNAKE_HEAD_COLOR = Color.BLUE;

    public SnakeView(Context context) {
        super(context);
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void drawTile(Tile tile, Canvas canvas) {
        if (tile == null && canvas == null) return;
        canvas.drawRect(tile.getLeft(), tile.getTop(), tile.getRight(), tile.getBottom(), paint);
    }

    private void drawWalls(Canvas canvas) {
        Tile[] walls = gameData.getWalls();
        if (walls == null) return;
        paint.setColor(WALL_COLOR);
        for (Tile t : walls) {
            drawTile(t, canvas);
        }
    }

    private void drawSnake(Canvas canvas) {
        List<Tile> snake = gameData.getSnake();
        if (snake == null) return;
        paint.setColor(SNAKE_HEAD_COLOR);
        drawTile(snake.get(0), canvas);
        paint.setColor(SNAKE_COLOR);
        for (int i = 1; i < snake.size(); i++) {
            drawTile(snake.get(i), canvas);
        }
    }

    private void drawFood(Canvas canvas) {
        paint.setColor(FOOD_COLOR);
        drawTile(gameData.getFood(), canvas);
    }

    private void drawScore(Canvas canvas) {
        int score = gameData.getScore();
        String text = getResources().getString(R.string.over_msg, score);
        TextTool.drawTextInCenter(canvas, text, getWidth() * 0.1f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (gameData == null) return;

        drawWalls(canvas);
        drawSnake(canvas);
        drawFood(canvas);

        if (gameData.getGameState() == GameState.over) {
            drawScore(canvas);
        }
    }

    @Override
    public void onDisplay(GameData viewData) {
        this.gameData = viewData;
        postInvalidate();
    }
}