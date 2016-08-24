package net.chengyujia.happysnake.entity;

/**
 * Created by CYJ on 2016/8/6.
 * 界面上的蛇和围墙都是由若干个连续的瓷片组成，食物由一个瓷片组成。
 */
public class Tile {
    //每个瓷片都是大小相等的正方形，此字段表示瓷片的长和宽(像素)。
    public static final int SIZE = 30;

    //由于画布的长宽不一定是瓷片长宽的整数倍，所以围墙和画布边缘之间可能会有间隙。如有间隙，围墙居中显示。这里用xOffset和yOffset分别表示每个瓷片横向和纵向的偏移。
    public static int xOffset;
    public static int yOffset;

    //表示瓷片在网格中的坐标，单位是一个瓷片的大小。游戏界面其实是一个二维网格，虽然网格线不需要显示，网格中每一个格子正好放一个瓷片或为空。
    private int x;
    private int y;

    //瓷片左上角和右下角在画布中的坐标，单位是像素。
    private int left;
    private int top;
    private int right;
    private int bottom;

    private static void init(Tile tile) {
        tile.left = tile.x * SIZE + xOffset;
        tile.top = tile.y * SIZE + yOffset;
        tile.right = tile.left + SIZE - 1;
        tile.bottom = tile.top + SIZE - 1;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        init(this);
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
