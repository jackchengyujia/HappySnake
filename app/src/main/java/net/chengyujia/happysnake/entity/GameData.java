package net.chengyujia.happysnake.entity;

import net.chengyujia.happysnake.enums.GameState;

import java.util.List;

/**
 * Created by CYJ on 2016/8/8.
 */
public class GameData {
    //得分
    private int score = 0;
    //围墙
    private Tile[] walls;
    //蛇
    private List<Tile> snake;
    //食物
    private Tile food;

    private GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Tile getFood() {
        return food;
    }

    public void setFood(Tile food) {
        this.food = food;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Tile> getSnake() {
        return snake;
    }

    public void setSnake(List<Tile> snake) {
        this.snake = snake;
    }

    public Tile[] getWalls() {
        return walls;
    }

    public void setWalls(Tile[] walls) {
        this.walls = walls;
    }
}
