import bagel.*;

/**
 * Class for normal notes
 */
public class Note {
    protected Image image;
    protected int appearanceFrame;
    protected int speed = 2;
    protected int y = 100;
    protected boolean active = false;
    protected boolean completed = false;
    protected int scoringFactor = 1;

    public Note(String dir, int appearanceFrame) {
        setImage(dir);
        this.appearanceFrame = appearanceFrame;
    }

    public void setImage(String dir) {
        image = new Image("res/note" + dir + ".png");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getSpeed() {
        return speed;
    }
    public int getY() {
        return y;
    }
    public int getAppearanceFrame() {
        return appearanceFrame;
    }

    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {return completed;}

    public void deactivate() {
        active = false;
        completed = true;
    }

    public void setScoringFactor(int factor) {
        scoringFactor = factor;
    }

    // Update the status of the note
    public void update() {
        if (active) {
            y += speed;
        }
        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey));
            // The scoring of note is detected, deactivate the note
            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score * scoringFactor;
            }

        }

        return 0;
    }


}
