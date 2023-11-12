import bagel.*;

import java.util.ArrayList;

/**
 * Class for stationary guardian
 */
public class Guardian {
    private static final int GUARDIAN_X = 800;
    private static final int GUARDIAN_Y = 600;
    private final Image image = new Image("res/guardian.png");
    private boolean active = false;

    public Guardian() {
        active = true;
    }
    public void draw() {
        if (active) {
            image.draw(GUARDIAN_X, GUARDIAN_Y);
        }
    }
    public void update() {
        draw();
    }
}