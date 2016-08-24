package net.chengyujia.happysnake.enums;

/**
 * Created by CYJ on 2016/8/8.
 */
public enum Direction {
    none(0), left(-1), up(-2), right(1), down(2);

    private int value;

    private Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Direction getDirection(int value) {
        for (Direction direction : Direction.values()) {
            if (direction.getValue() == value) {
                return direction;
            }
        }
        return null;
    }
}
