import bagel.*;

public abstract class SpecialNote extends Note{
    public SpecialNote(String type, int appearanceFrame) {
        super(type, appearanceFrame);
    }
    public void setImage(String type) {
        if (type.equals("DoubleScore")) {
            image = new Image("res/note2x.png");
        } else if (type.equals("SlowDown")) {
            image = new Image("res/noteSlowDown.png");
        } else if (type.equals("SpeedUp")) {
            image = new Image("res/noteSpeedUp.png");
        } else {
            image = new Image("res/noteBomb.png");
        }
    }
    protected abstract void applyEffect();
    public abstract int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey);
}

