import bagel.*;
/**
 * Class for SpeedUp Special note
 */
public class SpeedUpNote extends SpecialNote{
    private static final int SPEED_ALTER = 1;
    private static final String TYPE = "Speed Up";
    public SpeedUpNote(String type, int appearanceFrame) {
        super(type, appearanceFrame);
    }

    /**
     * triggers the special note's special effect
     */
    public void applyEffect() {
        for (int i = 0; i < ShadowDance.numLanes; i++) {
            Lane lane = ShadowDance.lanes[i];
            lane.setNoteSpeed(lane.getNoteSpeed() + SPEED_ALTER);
        }
    }

    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.specialScoring(y, targetHeight, input.wasPressed(relevantKey), TYPE);

            // The special note is scored
            // deactivate the note and apply affect
            if (score == Accuracy.SPECIAL_NOTE_SCORE) {
                deactivate();
                applyEffect();
                return score;
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
