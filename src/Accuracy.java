import bagel.*;

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {

    // Constants defining the scores for various accuracy levels
    public static final int PERFECT_SCORE = 10;
    public static final int GOOD_SCORE = 5;
    public static final int BAD_SCORE = -1;
    public static final int MISS_SCORE = -5;
    public static final int NOT_SCORED = 0;
    public static final int SPECIAL_NOTE_SCORE = 15;

    // Constants defining string representations of accuracy levels
    public static final String PERFECT = "PERFECT";
    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String MISS = "MISS";
    public static final String DOUBLE_SCORE = "Double Score";
    public static final String SPEED_UP = "Speed Up";
    public static final String SLOW_DOWN = "Slow Down";
    public static final String BOMB_NOTE = "Lane Clear";
    public static final int SPECIAL_MISS = -1;
    public static final int DOUBLE_SCORE_ACTIVATE = -2;
    public static final int BOMB_NOTE_ACTIVATE = -3;
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final int SPECIAL_RADIUS = 50;
    private static final int ENEMY_RADIUS = 104;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;

    /**
     * Sets the current accuracy level for feedback display.
     *
     * @param accuracy accuracy of score.
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * Resets the current accuracy and frame count to their default values.
     */
    public void reset() {
        currAccuracy = null;
        frameCount = 0;
    }

    /**
     * Evaluates the player's score based on the accuracy of their input.
     *
     * @param height       The height at which the note was pressed.
     * @param targetHeight The target height for the note.
     * @param triggered    Whether the note was triggered by the player.
     * @return The score corresponding to the player's accuracy.
     */
    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE;
        }

        return NOT_SCORED;

    }

    /**
     * Evaluates the score for special notes based on player input.
     *
     * @param height       The height at which the special note was pressed.
     * @param targetHeight The target height for the special note.
     * @param triggered    Whether the special note was triggered by the player.
     * @param event        The type of special note.
     * @return The score or event corresponding to the player's accuracy.
     */
    public int specialScoring(int height, int targetHeight, boolean triggered, String event) {
        int distance = Math.abs(height - targetHeight);
        if (triggered) {
            if (distance <= SPECIAL_RADIUS) {
                setAccuracy(event);
                switch (event) {
                        case DOUBLE_SCORE:
                            setAccuracy(DOUBLE_SCORE);
                            return DOUBLE_SCORE_ACTIVATE;
                        case SPEED_UP:
                            setAccuracy(SPEED_UP);
                            return SPECIAL_NOTE_SCORE;
                        case SLOW_DOWN:
                            setAccuracy(SLOW_DOWN);
                            return SPECIAL_NOTE_SCORE;
                        case BOMB_NOTE:
                            setAccuracy(BOMB_NOTE);
                            return BOMB_NOTE_ACTIVATE;
                }
            }
        } else if (height >= (Window.getHeight())) {
            return SPECIAL_MISS;
        }
        return NOT_SCORED;
    }

    /**
     * Checks if a note collides with an enemy.
     *
     * @param note_x   The x-coordinate of the note.
     * @param note_y   The y-coordinate of the note.
     * @param enemy_x  The x-coordinate of the enemy.
     * @param enemy_y  The y-coordinate of the enemy.
     * @return {@code true} if the note collides with the enemy, {@code false} otherwise.
     */
    public static boolean enemyCollide(int note_x, int note_y, int enemy_x, int enemy_y) {
        double distance;
        distance = Math.sqrt(Math.pow(enemy_x - note_x, 2) + Math.pow(enemy_y - note_y, 2));
        return distance <= ENEMY_RADIUS;
    }
    /**
     * Updates the display of the current accuracy based on frame counts.
     */
    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
        }
    }
}
