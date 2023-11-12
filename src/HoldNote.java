import bagel.*;


// HoldNotes extends Notes
public class HoldNote extends Note{

    private static final int HEIGHT_OFFSET = 82;
    private boolean holdStarted = false;

    public HoldNote(String dir, int appearanceFrame) {
        // call parent constructor with super
        super(dir, appearanceFrame);
        this.y = 24;
    }

    public void setImage(String dir) {
        image = new Image("res/holdNote" + dir + ".png");
    }

    public void startHold() {
        holdStarted = true;
    }

    /**
     * scored twice, once at the start of the hold and once at the end
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarte d) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return y + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return y - HEIGHT_OFFSET;
    }
}
