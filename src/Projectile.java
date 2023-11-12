import bagel.*;

public class Projectile {
  private static final int PROJECTILE_X = 800;
  private static final int PROJECTILE_Y = 600;
  private final Image image = new Image("res/arrow.png");
  private boolean active = false;

  public Projectile() {
    active = true;
  }
  public void draw() {
    if (active) {
      image.draw(PROJECTILE_X, PROJECTILE_Y);
    }
  }
  public void update() {
    draw();
  }
}

