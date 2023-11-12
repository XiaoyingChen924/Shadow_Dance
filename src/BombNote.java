import bagel.*;

/**
 * Represents a BombNote in the game. When activated, it clears all notes in the lane of its direction.
 */
public class BombNote extends SpecialNote{
    private static final String TYPE = "Lane Clear";
    private final String direction;

    /**
     * Constructor for the BombNote class.
     *
     * @param type The type of the note.
     * @param appearanceFrame The frame in which the note will appear.
     */
    public BombNote(String type, int appearanceFrame) {
        super(type, appearanceFrame);
        this.direction = type;
    }
    /**
     * Applies the bomb effect, clearing all notes in the lane of its direction.
     */
    public void applyEffect() {
        for (int i = 0; i < ShadowDance.numLanes; i++) {
            Lane lane = ShadowDance.lanes[i];
            if (lane.getType().equals(this.direction)) {
                lane.clearNotes();
            }
        }
    }
    /**
     * Checks the score for the BombNote.
     *
     * @param input The input from the user.
     * @param accuracy The accuracy with which the note was hit.
     * @param targetHeight The target height for the note.
     * @param relevantKey The key that corresponds to the note.
     * @return The score for the note.
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.specialScoring(y, targetHeight, input.wasPressed(relevantKey), TYPE);

            // The special note is scored
            // deactivate the note and apply affect
            if (score == Accuracy.BOMB_NOTE_ACTIVATE) {
                deactivate();
                applyEffect();
                return Accuracy.NOT_SCORED;
            }
            // The special note is missed, deactivate the note
            if (score == Accuracy.SPECIAL_MISS) {
                deactivate();
                return Accuracy.NOT_SCORED;
            }
        }
        return 0;
    }

}
