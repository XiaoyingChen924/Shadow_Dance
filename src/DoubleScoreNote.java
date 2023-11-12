import bagel.*;
public class DoubleScoreNote extends SpecialNote{
    private static final int DOUBLE_FACTOR = 2;
    private static final int DURATION = 480;
    private static final String TYPE = "Double Score";

    /**
     * Constructor for the DoubleScoreNote class.
     *
     * @param type The type of the note.
     * @param appearanceFrame The frame in which the note will appear.
     */
    public DoubleScoreNote(String type, int appearanceFrame) {
        super(type, appearanceFrame);
    }

    /**
     * apply effect to all lanes
     */
    public void applyEffect() {
        for (int i = 0; i < ShadowDance.numLanes; i++) {
            Lane lane = ShadowDance.lanes[i];
            // set scoring factor into 2
            lane.setScoringFactor(DOUBLE_FACTOR);
            lane.setDoubleScoreCounter(DURATION);
        }
    }
    /**
     * Checks the score for the DoubleScoreNote.
     *
     * @param input The input from the user.
     * @param accuracy The accuracy with which the note was hit.
     * @param targetHeight The target height for the note.
     * @param relevantKey The key that corresponds to the note.
     * @return The score for the note.
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.specialScoring(y, targetHeight, input.wasPressed(relevantKey), TYPE);

            // The special note is scored
            // deactivate the note and apply affect
            if (score == Accuracy.DOUBLE_SCORE_ACTIVATE) {
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
