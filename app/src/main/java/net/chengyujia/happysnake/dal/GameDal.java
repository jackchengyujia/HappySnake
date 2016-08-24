package net.chengyujia.happysnake.dal;

import net.chengyujia.happysnake.androidTool.SharedPreferenceTool;
import net.chengyujia.happysnake.entity.Tile;
import net.chengyujia.happysnake.enums.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChengYuJia on 2016/8/10.
 */
public class GameDal {

    private static final String SCORE = "score";
    private static final String FOOD = "food";
    private static final String SNAKE = "snake";
    private static final String DIRECTION = "direction";

    public void saveScore(int score) {
        SharedPreferenceTool.putInt(SCORE, score);
    }

    public int getScore() {
        return SharedPreferenceTool.getInt(SCORE);
    }

    public void saveFood(Tile food) {
        if (food != null) {
            String foodValue = food.getX() + "," + food.getY();
            SharedPreferenceTool.putString(FOOD, foodValue);
        }
    }

    public Tile getFood() {
        String foodValue = SharedPreferenceTool.getString(FOOD);
        String[] strings = foodValue.split(",");
        int x = Integer.parseInt(strings[0]);
        int y = Integer.parseInt(strings[1]);
        Tile food = new Tile(x, y);
        return food;
    }

    public void saveSnake(List<Tile> snake) {
        if (snake != null) {
            StringBuilder snakeValue = new StringBuilder();
            for (Tile t : snake) {
                snakeValue.append(t.getX() + "," + t.getY() + ",");
            }
            snakeValue.deleteCharAt(snakeValue.length() - 1);//删除最后一个分隔符。
            SharedPreferenceTool.putString(SNAKE, snakeValue.toString());
        }
    }

    public List<Tile> getSnake() {
        String snakeValue = SharedPreferenceTool.getString(SNAKE);
        String[] strings = snakeValue.split(",");
        List<Tile> snake = new ArrayList<Tile>();
        for (int i = 0; i < strings.length; i += 2) {
            int x = Integer.parseInt(strings[i]);
            int y = Integer.parseInt(strings[i + 1]);
            Tile tile = new Tile(x, y);
            snake.add(tile);
        }
        return snake;
    }

    public void saveDirection(Direction direction) {
        SharedPreferenceTool.putInt(DIRECTION, direction.getValue());
    }

    public Direction getDirection() {
        int value = SharedPreferenceTool.getInt(DIRECTION);
        return Direction.getDirection(value);
    }
}