import bagel.*;
import java.util.ArrayList;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    protected static final int HEIGHT = 384;
    protected static final int TARGET_HEIGHT = 657;
    protected static final int DEFAULT_SCORING_FACTOR = 1;
    protected int scoringFactor = DEFAULT_SCORING_FACTOR;
    protected int doubleScoreFrame = 0;
    protected final String type;
    protected Image image;
    protected Keys relevantKey;
    protected final int location;
    protected final ArrayList<Note> notes = new ArrayList<>();


    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    public String getType() {
        return type;
    }
    /**
     * updates all the notes in the lane
     */
    public int update(Input input, Accuracy accuracy) {
        //draw the lane
        draw();

        // update the status of the notes
        for (Note note : notes) {
            note.update();
        }
        // Update the frameNumber for double score note
        if (doubleScoreFrame > 0) {
            doubleScoreFrame--;
            if (doubleScoreFrame == 0) {
                resetScoringFactor();
            }
        }

        // calculate and return the scoring of the first entering note
        int score = 0;
        if (!notes.isEmpty()) {
            Note enterNote = notes.get(0);
            score += enterNote.checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);

            if (enterNote.isCompleted()) {
                removeNote(enterNote);
            }
            // makes sure for hold note, the score
            // is also calculated twice
            return score;
        }
        return Accuracy.NOT_SCORED;
    }


    public void addNote(Note n) {
        notes.add(n);
    }

    public void removeNote(Note n) {
        notes.remove(n);
    }

    public void clearNotes() {
        for (Note note : notes) {
            if (note.getAppearanceFrame() < ShadowDance.getCurrFrame()) {
                note.deactivate();
            }
        }
    }

    public void setNoteSpeed(int new_speed) {
        for (Note note : notes) {
            note.setSpeed(new_speed);
        }
    }
    //set scoring factor (doubled) for all notes
    public void setScoringFactor(int doubledFactor) {
        for (Note note : notes) {
            note.setScoringFactor(doubledFactor);
        }
    }
    public void resetScoringFactor() {
        for (Note note : notes) {
            note.setScoringFactor(DEFAULT_SCORING_FACTOR);
        }
    }
    public void setDoubleScoreCounter(int duration) {
        this.doubleScoreFrame = duration;
    }

    public int getNoteSpeed() {
        return notes.get(0).getSpeed();
    }

    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {

        for (Note note : notes) {
            if (!note.isCompleted()) {
                return false;
            }
        }

        return true;
    }

    public void clearAllNotes() {
        notes.clear();
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (Note note : notes) {
            note.draw(location);
        }
    }

}
