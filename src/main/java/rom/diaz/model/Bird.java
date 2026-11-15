package rom.diaz.model;

import lombok.Getter;

@Getter

public class Bird {

    private double x;
    private double y;
    private double velocityY;

    private static final double GRAVITY = 0.18;
    private static final double JUMP_STRENGTH = -4.5;

    public Bird(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityY = 0;
    }

    public void jump() {
        velocityY = JUMP_STRENGTH;
    }

    public void update() {
        velocityY += GRAVITY;
        y += velocityY;
    }
}