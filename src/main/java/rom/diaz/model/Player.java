package rom.diaz.model;

import lombok.Getter;

@Getter

public class Player {

    private final int playerId;
    private final Bird bird;

    private int score;
    private PlayerState state;

    public Player(int playerId, Bird bird) {
        this.playerId = playerId;
        this.bird = bird;
        this.score = 0;
        this.state = PlayerState.ALIVE;
    }

    public void jump() {
        if (state == PlayerState.ALIVE) {
            bird.jump();
        }
    }

    public void update() {
        if (state == PlayerState.ALIVE) {
            bird.update();
        }
    }

    public void increaseScore() {
        if (state == PlayerState.ALIVE) {
            score++;
        }
    }

    public void finishRun() {
        if (state == PlayerState.ALIVE) {
            state = PlayerState.GAME_OVER;
        }
    }
}