import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;

/**
 * Creates the Ms. Jaime boss that can spawn. Has a unique shooting pattern in GameComponent.
 * 
 * @author (Thanatcha Panpairoj) 
 * @version (6/2/15)
 */
public class MsJaime extends EnemyObject implements Boss
{
    /**
     * Initializes the boss object with its hp and dimensions.
     *
     * @param hp       the hit points of the boss.
     * @param fWidth   the width of the frame.
     * @param fHeight  the height of the frame.
     */
    public MsJaime(int hp, int fWidth, int fHeight) {
        super("Ms Jaime", 100, fWidth, fHeight, (int)(Math.random() * fWidth), (int)(Math.random() * fHeight), new Color((float)Math.random(), (float)Math.random(), (float)Math.random()), 2);
        changeMaxHp(hp);
        changeHp(hp);
    }

    /**
     * Draws the boss. Overrides the draw method from EnemyObject to create different names for every boss.
     * 
     * @param g2 the graphics context
     * @return   void
     */
    public void draw(Graphics2D g2) {
        super.draw(g2);

        //         g2.setColor(Color.GREEN);
        //         g2.fillArc((int)(super.getX() - 25), (int)(super.getY() - 25), 2 * getRadius(), 2 * getRadius(), 0, (int)(360 - 360.0 * getHp() / getMaxHp())); 

        g2.setColor(Color.BLACK);
        g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 25));

        int x = (int)super.getX();
        int y = (int)super.getY();
        g2.drawString("Ms Jaime", x - 52, y + 10);
        g2.drawString("" + super.getHp(), x - 10, y + 60);
    }
}
