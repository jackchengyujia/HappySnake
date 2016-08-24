package net.chengyujia.happysnake.bll;

import net.chengyujia.happysnake.Config;
import net.chengyujia.happysnake.androidTool.LogTool;
import net.chengyujia.happysnake.dal.GameDal;
import net.chengyujia.happysnake.entity.GameData;
import net.chengyujia.happysnake.entity.Tile;
import net.chengyujia.happysnake.enums.Direction;
import net.chengyujia.happysnake.enums.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by CYJ on 2016/8/6.
 */
public class GameBll {
    private static final boolean DEBUG = Config.DEBUG;
    //初始化时蛇的长度
    private static final int SNAKE_INIT_LENGTH = 5;
    //每得一分，蛇移动的时间间隔按此比例缩小一次，让蛇加速。
    private static final double DELAY_RATIO = 0.95;
    //蛇移动的时间间隔初始值
    private static final int DELAY_INIT = 500;
    //蛇每次移动的时间间隔(毫秒)
    private int moveDelay = DELAY_INIT;

    //画布宽度
    private int canvasWidth;
    //画布高度
    private int canvasHeight;

    //水平方向最多能放瓷片的个数
    private int tileXCount;
    //垂直方向最多能放瓷片的个数
    private int tileYCount;


    //围墙
    private Tile[] walls;
    //蛇
    private List<Tile> snake;
    //食物
    private Tile food;
    //得分
    private int score = 0;
    //游戏状态
    private GameState gameState = GameState.ready;
    //界面上蛇当前前进的方向
    private Direction currentDirection = Direction.none;
    //蛇下一步要前进的方向
    private Direction nextDirection = Direction.none;

    private OnDisplayListener onDisplayListener;

    private Random random = new Random();
    private GameData viewData = new GameData();
    private GameDal gameDal = new GameDal();


    public GameBll(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        init();
    }

    private void init() {
        tileXCount = canvasWidth / Tile.SIZE;
        tileYCount = canvasHeight / Tile.SIZE;

        Tile.xOffset = (canvasWidth - tileXCount * Tile.SIZE) / 2;
        Tile.yOffset = (canvasHeight - tileYCount * Tile.SIZE) / 2;

        initWalls();
        if (!getPauseData()) {
            initSnake();
            randomNewFood();
        }
    }

    private boolean getPauseData() {
        int scoreTemp = gameDal.getScore();
        if (scoreTemp <= 0) return false;
        score = scoreTemp;
        food = gameDal.getFood();
        snake = gameDal.getSnake();
        nextDirection = gameDal.getDirection();
        gameDal.saveScore(0);//上次暂停存储的数据，获取一次后作废。
        moveDelay *= Math.pow(DELAY_RATIO, score);//加速
        return true;
    }

    private void savePauseData() {
        if (gameState != GameState.running || score <= 0) return;

        gameDal.saveScore(score);
        gameDal.saveFood(food);
        gameDal.saveSnake(snake);
        gameDal.saveDirection(currentDirection);
    }

    public void pause() {
        savePauseData();
        gameState = GameState.pause;
    }

    public void resume() {
        start();
    }


    //围墙是固定的，计算一次即可。
    private void initWalls() {
        int length = (tileXCount + tileYCount) * 2 - 4;
        walls = new Tile[length];

        int index = 0;
        for (int i = 0; i < tileXCount; i++, index++) {
            walls[index] = new Tile(i, 0);
        }

        for (int i = 0; i < tileXCount; i++, index++) {
            walls[index] = new Tile(i, tileYCount - 1);
        }

        for (int j = 1; j < tileYCount - 1; j++, index++) {
            walls[index] = new Tile(0, j);
        }

        for (int j = 1; j < tileYCount - 1; j++, index++) {
            walls[index] = new Tile(tileXCount - 1, j);
        }
    }

    private void initSnake() {
        nextDirection = Direction.up;//初始方向
        moveDelay = DELAY_INIT;//初始速度
        snake = new ArrayList<Tile>();
        int x = tileXCount / 2;
        int y = (tileYCount - SNAKE_INIT_LENGTH) / 2;//初始蛇头位置
        for (int i = 0; i < SNAKE_INIT_LENGTH; i++) {
            snake.add(new Tile(x, y + i));
        }
    }

    private boolean isCoordInSnake(int x, int y) {
        for (Tile t : snake) {
            if (t.getX() == x && t.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private void randomNewFood() {
        int x = 0;
        int y = 0;
        while (true) {
            x = random.nextInt(tileXCount - 2) + 1;
            y = random.nextInt(tileYCount - 2) + 1;
            if (!isCoordInSnake(x, y)) {
                break;
            }
        }
        food = new Tile(x, y);
    }

    //蛇向前移动一格
    private boolean move() {
        Tile tile = snake.get(0);
        int x = tile.getX();
        int y = tile.getY();

        currentDirection = nextDirection;
        // 根据方向增减坐标值
        switch (currentDirection) {
            case up:
                y--;
                break;
            case down:
                y++;
                break;
            case left:
                x--;
                break;
            case right:
                x++;
                break;
        }

        if (x < 1 || y < 1 || x > tileXCount - 2 || y > tileYCount - 2) {//撞到墙
            if (DEBUG) LogTool.log("撞到墙");
            return false;
        }

        if (isCoordInSnake(x, y)) {//撞到自己
            if (DEBUG) LogTool.log("撞到自己");
            return false;
        }

        if (x == food.getX() && y == food.getY()) {//吃到食物
            if (DEBUG) LogTool.log("吃到食物");
            snake.add(0, food);//从蛇头增长
            score++;//加分
            moveDelay *= DELAY_RATIO;//加快蛇移动速度
            randomNewFood();
        } else {//空白位置
            snake.add(0, new Tile(x, y));//蛇头增长
            snake.remove(snake.size() - 1);//蛇尾移除
        }

        return true;
    }

    //再来一次
    public void again() {
        if (gameState == GameState.running) return;
        initSnake();
        randomNewFood();
        score = 0;
        start();
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onDisplayListener != null) {
                    gameState = GameState.running;
                    do {
                        long start = System.currentTimeMillis();
                        viewData.setWalls(walls);
                        viewData.setSnake(snake);
                        viewData.setFood(food);
                        viewData.setScore(score);
                        viewData.setGameState(gameState);
                        onDisplayListener.onDisplay(viewData);
                        long end = System.currentTimeMillis();
                        if (DEBUG) LogTool.log("重绘耗时=" + (end - start));
                        if (gameState != GameState.running) {
                            break;
                        }
                        try {
                            Thread.sleep(moveDelay);
                        } catch (InterruptedException e) {
                            LogTool.exception(e);
                        }
                        start = System.currentTimeMillis();
                        if (!move()) {
                            gameState = GameState.over;
                        }
                        end = System.currentTimeMillis();
                        if (DEBUG) LogTool.log("移动耗时=" + (end - start));
                    } while (true);
                }
            }
        }).start();
    }

    public void changeDirection(Direction newDirection) {
        if (newDirection == Direction.none || currentDirection == newDirection || currentDirection.getValue() + newDirection.getValue() == 0 || gameState != GameState.running)
            return;
        nextDirection = newDirection;
    }

    public void setOnDisplayListener(OnDisplayListener l) {
        onDisplayListener = l;
    }

    public interface OnDisplayListener {
        void onDisplay(GameData viewData);
    }
}