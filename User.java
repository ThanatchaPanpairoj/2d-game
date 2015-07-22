import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Color;

/**
 * Describes the user object, which is used as a superclass for the enemies and bosses.
 * Methods to return/change stats (level, xp, hp, max hp, stamina, max stamina, speed, movement, size, location).
 *
 * @author (Thanatcha Panpairoj, Jack Weng)
 * @version (6/4/15)
 */
public class User
{
    private double currentX;
    private double currentY;
    private Color color;
    private int hp, maxHp, stamina, maxStamina, radius, level, xp;

    /**
    Constructs an user object with a given center.

    @param radius the radius of the user object
    @param x the x coordinate of the center
    @param y the y coordinate of the center
    @param color the color of the user object
     */
    public User(int radius, int x, int y, Color color, int maxHp)
    {
        this.radius = radius;
        this.maxHp = maxHp;
        hp = maxHp;
        stamina = 180;
        maxStamina = 180;
        currentX = x;
        currentY = y;
        level = 1;
        this.color = color;
    }

    /**
    Draws the object.
    @param g2 the graphics context
    @return void
     */
    public void draw(Graphics2D g2)
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(currentX - 25, currentY - 25, 2 * radius, 2 * radius);
        //could be any object, doesn't have to be an ellipse
        g2.setColor(color);
        g2.draw(circle);

        g2.setPaint(color);
        g2.fill(circle);
    }

    /**
     * Moves user object to (x,y)
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return void
     */
    public void moveTo(double x, double y) {
        currentX = x;
        currentY = y;
    }

    /**
     * Moves user object by an amount.
     *
     * @param dx change in x
     * @param dy change in y
     * @return void
     */
    public void moveBy(double dx, double dy) {
        currentX += dx;
        currentY += dy;
    }

    /**
     * Returns x-coordinate of user object.
     *
     * @return currentX
     */
    public double getX() {
        return currentX;
    }

    /**
     * Returns y-coordinate of user object.
     *
     * @return currentY
     */
    public double getY() {
        return currentY;
    }

    /**
     * Returns radius of user object.
     *
     * @return radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Returns hp of user object.
     *
     * @return hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * Returns max hp of user object.
     *
     * @return maxHp
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Returns level of user object.
     *
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the stamina of the user.
     *
     * @return stamina
     */
    public int getStamina()
    {
        return stamina;
    }

    /**
     * Returns the maximum stamina of the user.
     *
     * @return maxStamina
     */
    public int getMaxStamina()
    {
        return maxStamina;
    }

    /**
     * Adds xp to the user. Levels up if enough xp.
     *
     * @param dXp change in xp
     * @return void
     */
    public void addXp(int dXp) {
        if(level < 20) {
            xp += dXp;
            if((level < 10 && xp > level * 1000) || (level < 20 && xp > level * 4000)) {
                level++;
                xp = 0;
                if(level < 11)
                    maxHp += 1 + level / 4;
                else 
                    maxHp += 2 + level / 2;
                changeHp(1000);
                maxStamina += 30;
                changeStamina(2000);
            }
        }
    }

    /**
     * Returns the xp of the user.
     *
     * @return xp
     */
    public int getXp()
    {
        return xp;
    }

    /**
     * Returns the maximum xp for a level.
     *
     * @return maxXp
     */
    public int getMaxXp()
    {
        if(level < 10)
            return 1000 * level;
        else
            return 4000 * level;
    }

    /**
     * Changes user's hp by an amount.
     *
     * @param dHp change in Hp
     * @return void
     */
    public void changeHp(int dHp) {
        if(hp > 0)
            hp += dHp;
        if(hp > maxHp)
            hp = maxHp;
        if(hp < 0)
            hp = 0;
    }

    /**
     * Changes user's max hp by an amount.
     *
     * @param dMaxHp change in MaxHp
     * @return void
     */
    public void changeMaxHp(double dMaxHp) {
        maxHp += dMaxHp;
    }

    /**
     * Returns the name of the user object.
     *
     * @return "User"
     */
    public String getName() {
        return "User";
    }

    /**
     * Changes user's stamina by an amount.
     *
     * @param dStamina change in stamina
     * @return void
     */
    public void changeStamina(int dStamina)
    {
        if(stamina >= 0)
            stamina += dStamina;
        if(stamina > maxStamina)
            stamina = maxStamina;
        if(stamina < 0)
            stamina = 0;
    }

    /**
     * Changes user's max stamina by an amount.
     *
     * @param dMaxStamina change in MaxStamina
     * @return void
     */
    public void changeMaxStamina(double dMaxStamina) {
        maxStamina += dMaxStamina;
    }
}
