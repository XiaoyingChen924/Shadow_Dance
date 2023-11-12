import bagel.*;

import java.util.ArrayList;

/**
 * Special Lane Extends from Lane Class
 */
public class SpecialLane extends Lane {

    public SpecialLane(String dir, int location) {
        super(dir, location);
        image = new Image("res/laneSpecial.png");
    }


}
