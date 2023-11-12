import bagel.*;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    private static final int APPEARANCE_FREQ = 600;
    private static final int X_LOWER_BOUND = 100;
    private static final int X_UPPER_BOUND = 900;
    private static final int Y_LOWER_BOUND = 100;
    private static final int Y_UPPER_BOUND = 500;
    private static final int ENEMY_SPEED = 1;
    private boolean leftMovement = false;
    private boolean rightMovement = false;
    private int x;
    private int y;
    private boolean active = false;
    private final Random random = new Random();
    private final Image image = new Image("res/enemy.png");
    public static boolean generateEnemy = false;
    /**
     * Set up random movement and position for enemy
     */
    public Enemy() {
        this.x = X_LOWER_BOUND + random.nextInt(X_UPPER_BOUND - X_LOWER_BOUND);
        this.y = Y_LOWER_BOUND + random.nextInt(Y_UPPER_BOUND - Y_LOWER_BOUND);
        this.leftMovement = random.nextBoolean();
        if (!leftMovement) {
            this.rightMovement = true;
        }
        active = true;
    }
    /**
     * Check for collision on notes and remove them
     */
    public void attack() {
        for (int i = 0; i < ShadowDance.numLanes; i++) {
            Lane lane = ShadowDance.lanes[i];
            // Loop through the notes in the lane
            for (Note note : lane.notes) {
                if (note.isActive()){
                    // Check if the note is within the attack radius of the enemy
                    boolean collided = false;
                    collided = Accuracy.enemyCollide(lane.location, note.y, this.x, this.y);
                    if (collided  && !(note instanceof SpecialNote)) {
                        note.deactivate();
                    }
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }
    public void deactivate() {
        active = false;
    }
    /**
     * Draws the enemy on the screen.
     *
     * @param x The x-coordinate where the enemy should be drawn.
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }
    /**
     * Determines if a new enemy should be generated based on the current frame.
     *
     * @return true if a new enemy should be generated; false otherwise.
     */
    public static boolean newEnemy() {
        if (ShadowDance.getCurrFrame() % APPEARANCE_FREQ == 0) {
            generateEnemy = true;
        }
        return generateEnemy;
    }

    /**
     * Reset the enemy generation
     */
    public static void resetEnemy() {
        generateEnemy = false;
    }

    /**
     * Update movement and 'attack' on notes
     */
    public void update() {
        if (active) {
            if (leftMovement) {
                x -= ENEMY_SPEED;
                if (x <= X_LOWER_BOUND) {
                    leftMovement = false;
                    rightMovement = true;
                }
            }
            if (rightMovement) {
                x += ENEMY_SPEED;
                if (x >= X_UPPER_BOUND) {
                    rightMovement = false;
                    leftMovement = true;
                }
            }
            draw(x);
            attack();
        }
    }

}
