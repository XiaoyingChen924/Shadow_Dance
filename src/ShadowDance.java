import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Sample solution for SWEN20003 Project 1, Semester 2, 2023
 *
 * @author Stella Li
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final static String LEVEL_ONE_FILE = "res/level1.csv";
    private final static String LEVEL_TWO_FILE = "res/level2.csv";
    private final static String LEVEL_THREE_FILE = "res/level3.csv";
    /**
     * The font used to draw the text.
     */
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final static int INS_LINE_X_OFFSET = 60;
    private final static int INS_LINE_Y_OFFSET = 30;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTION_LINE_ONE = "SELECT LEVELS WITH";
    private static final String INSTRUCTION_LINE_TWO = "NUMBER KEYS";
    private static final String INSTRUCTION_LINE_THREE = "1  2  3";
    private static final String END_INSTRUCTION = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private static final int END_INSTRUCTION_Y = 500;
    private static final int END_TITLE_Y = 300;
    private static final int LEVEL_ONE_TARGET = 150;
    private static final int LEVEL_TWO_TARGET = 400;
    private static final int LEVEL_THREE_TARGET = 350;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private final Accuracy accuracy = new Accuracy();
    public static final Lane[] lanes = new Lane[5];
    public static int numLanes = 0;
    private static final ArrayList<Enemy> enemies = new ArrayList<>();
    private int score = 0;
    private static int currFrame = 0;
    private int targetScore = 0;
    private Track track = new Track("res/track1.wav");
    private boolean started = false;
    private boolean finished = false;
    private boolean paused = false;

    /**
     * The Game Shadow Dance
     */
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Reads the csv file and creates the lanes and notes
     */
    private void readCsv(String level_file) {
        try (BufferedReader br = new BufferedReader(new FileReader(level_file))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane;
                    if (laneType.equals("Special")) {
                        lane = new SpecialLane(laneType, pos);
                        lanes[numLanes++] = lane;
                    } else {
                        lane = new Lane(laneType, pos);
                        lanes[numLanes++] = lane;
                    }
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                Note note = new Note(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                Note holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(holdNote);
                                break;
                            case "Bomb":
                                Note bomb = new BombNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(bomb);
                                break;
                            case "DoubleScore":
                                Note doubleScore = new DoubleScoreNote("DoubleScore",
                                        Integer.parseInt(splitText[2]));
                                lane.addNote(doubleScore);
                                break;
                            case "SlowDown":
                                Note slowDown = new SlowDownNote("SlowDown", Integer.parseInt(splitText[2]));
                                lane.addNote(slowDown);
                                break;
                            case "SpeedUp":
                                Note speedUp = new SpeedUpNote("SpeedUp", Integer.parseInt(splitText[2]));
                                lane.addNote(speedUp);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }



    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // Draw Starting Screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION_LINE_ONE,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
            INSTRUCTION_FONT.drawString(INSTRUCTION_LINE_TWO,
                    TITLE_X + INS_X_OFFSET + INS_LINE_X_OFFSET, TITLE_Y + INS_LINE_Y_OFFSET + INS_Y_OFFSET);
            INSTRUCTION_FONT.drawString(INSTRUCTION_LINE_THREE,
                    TITLE_X + INS_X_OFFSET + 2 * INS_LINE_X_OFFSET ,
                    TITLE_Y + 3 * INS_LINE_Y_OFFSET + INS_Y_OFFSET);

            // Detect level selection and load the game
            if (input.wasPressed(Keys.NUM_1)) {
                targetScore = LEVEL_ONE_TARGET;
                started = true;
                readCsv(LEVEL_ONE_FILE);
                track = new Track("res/track1.wav");
                track.start();
            }
            if (input.wasPressed(Keys.NUM_2)) {
                targetScore = LEVEL_TWO_TARGET;
                started = true;
                readCsv(LEVEL_TWO_FILE);
                track = new Track("res/track2.wav");
                track.start();
            }
            if (input.wasPressed(Keys.NUM_3)) {
                targetScore = LEVEL_THREE_TARGET;
                started = true;
                readCsv(LEVEL_THREE_FILE);
                track = new Track("res/track3.wav");
                track.start();
            }
        } else if (finished) {
            // end screen
            if (score >= targetScore) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        END_TITLE_Y);
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        END_TITLE_Y);
            }
            INSTRUCTION_FONT.drawString(END_INSTRUCTION,
                    WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth(END_INSTRUCTION)/2,
                    END_INSTRUCTION_Y);
            if (input.wasPressed(Keys.SPACE)) {
                reset();
            }
        } else {
            // gameplay

            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    track.run();
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {
                currFrame++;
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].update(input, accuracy);
                }
                accuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    track.pause();
                }
                if (targetScore == LEVEL_THREE_TARGET) {
                    Projectile projectile = new Projectile();
                    Guardian guardian = new Guardian();
                    if (Enemy.newEnemy()) {
                        enemies.add(new Enemy());
                        Enemy.resetEnemy();
                    }
                    for (Enemy enemy : enemies) {
                        enemy.update();
                    }
                    guardian.update();
                    projectile.update();
                }
            }
        }

    }
    /**
     *  Returns the current frame
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    /**
     * Game finishes when all lanes no longer
     * have any activated notes
     */
    private boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }

    /**
     * reset the game
     */
    public void reset() {
        // Reset game state variables
        score = 0;
        currFrame = 0;
        started = false;
        finished = false;
        paused = false;

        // Clear the lanes
        for (int i = 0; i < numLanes; i++) {
            lanes[i].clearAllNotes();
            lanes[i] = null;
        }
        numLanes = 0;
        // Reset accuracy
        accuracy.reset();
        // Clear enemies
        enemies.clear();
    }

}
