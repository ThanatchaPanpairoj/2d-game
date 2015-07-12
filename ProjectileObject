import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Color;

/**
 * Describes the projectiles that will be used in the game. Contains different overloaded constructors with different parameters.
 * Used by both the user and enemy.
 * Keeps track of location, radius, color, damage, and speed.
 * 
 * @author (Thanatcha Panpairoj) 
 * @version (6/4/15)
 */
public class ProjectileObject
{
    private double currentX, currentY, finalX, finalY, xSpeed, ySpeed, angle, dAmplitude, maxRange, displacement;
    private int damage, radius, amplitude, amplitudeClock = 0;
    private Ellipse2D projectile;
    private Color color;

    /**
     *Constructs a projectile object with a given starting point and ending point. 
     *Calculates the angle and sets the vertical and horizontal speed of the projectile.
     *
     *@param radius the radius of the projectile
     *@param damage the damage of the projectile
     *@param color the color of the projectile
     *@param startingX the x coordinate of the center of the user
     *@param startingY the y coordinate of the center of the user
     *@param endingX the x coordinate of the mouse
     *@param endingY the y coordinate of the mouse
     *@param speed the speed of the projectile
     */
    public ProjectileObject(int radius, int damage, Color color, double startingX, double startingY, double endingX, double endingY, double speed) {
        this.damage = damage;
        this.radius = radius;
        this.color = color;

        currentX = startingX;
        currentY = startingY;

        finalX = endingX;
        finalY = endingY;

        maxRange = 10000;
        displacement = 0;

        angle = Math.atan(Math.abs(endingY - startingY) / Math.abs(endingX - startingX));
        if(finalX > currentX)
            xSpeed = Math.cos(angle) * speed;
        else
            xSpeed = -Math.cos(angle) * speed;
        if(finalY > currentY)
            ySpeed =  Math.sin(angle) * speed;
        else
            ySpeed = -Math.sin(angle) * speed;
    }

    /**
     *Constructs a projectile object with a given starting point and ending point. 
     *Calculates the angle and sets the vertical and horizontal speed of the projectile.
     *
     *@param radius the radius of the projectile
     *@param damage the damage of the projectile
     *@param color the color of the projectile
     *@param startingX the x coordinate of the center of the user
     *@param startingY the y coordinate of the center of the user
     *@param angle the angle of the projectile
     *@param speed the speed of the projectile
     */
    public ProjectileObject(int radius, int damage, Color color, double startingX, double startingY, double angle, double speed) {
        this.damage = damage;
        this.radius = radius;
        this.color = color;

        currentX = startingX;
        currentY = startingY;

        maxRange = 30000;
        displacement = 0;

        angle *= Math.PI / 180;

        xSpeed = Math.cos(angle) * speed;
        ySpeed = Math.sin(angle) * speed;
    }

    /**
     *Constructs a projectile object with a given starting point firing angle. 
     *Calculates the angle and sets the vertical and horizontal speed of the projectile.
     *
     *@param maxRange the maximum range of the projectile
     *@param limitedRange whether the projectile has limited range
     *@param radius the radius of the projectile
     *@param damage the damage of the projectile
     *@param color the color of the projectile
     *@param startingX the x coordinate of the center of the user
     *@param startingY the y coordinate of the center of the user
     *@param angle the angle of the projectile
     *@param speed the speed of the projectile
     */
    public ProjectileObject(double maxRange, boolean limitedRange, int radius, int damage, Color color, double startingX, double startingY, double angle, double speed) {
        this.damage = damage;
        this.radius = radius;
        this.color = color;

        currentX = startingX;
        currentY = startingY;

        if(limitedRange)
            this.maxRange = maxRange;
        displacement = 0;

        angle *= Math.PI / 180;

        xSpeed = Math.cos(angle) * speed;
        ySpeed = Math.sin(angle) * speed;
    }

    /**
     *Constructs a projectile object with a given starting point and ending point. 
     *Calculates the angle and sets the vertical and horizontal speed of the projectile.
     *
     *@param amplitude the amplitude of the projectile
     *@param radius the radius of the projectile
     *@param damage the damage of the projectile
     *@param color the color of the projectile
     *@param startingX the x coordinate of the center of the user
     *@param startingY the y coordinate of the center of the user
     *@param endingX the x coordinate of the mouse
     *@param endingY the y coordinate of the mouse
     *@param speed the speed of the projectile
     */
    public ProjectileObject(int amplitude, int radius, int damage, Color color, double startingX, double startingY, double endingX, double endingY, double speed) {
        this.amplitude = amplitude;
        this.damage = damage;
        this.radius = radius;
        this.color = color;

        currentX = startingX;
        currentY = startingY;

        finalX = endingX;
        finalY = endingY;

        maxRange = 10000;
        displacement = 0;

        angle = Math.atan(Math.abs(endingY - startingY) / Math.abs(endingX - startingX));
        if(finalX > currentX)
            xSpeed = Math.cos(angle) * speed;
        else
            xSpeed = -Math.cos(angle) * speed;
        if(finalY > currentY)
            ySpeed =  Math.sin(angle) * speed;
        else
            ySpeed = -Math.sin(angle) * speed;
    }

    /**
     *Constructs a projectile object with a given starting point and ending point. 
     *Calculates the angle and sets the vertical and horizontal speed of the projectile.
     *
     *@param limitedRange whether the projectile has limited range
     *@param maxRange the maximum range of the projectile
     *@param radius the radius of the projectile
     *@param damage the damage of the projectile
     *@param color the color of the projectile
     *@param startingX the x coordinate of the center of the user
     *@param startingY the y coordinate of the center of the user
     *@param endingX the x coordinate of the mouse
     *@param endingY the y coordinate of the mouse
     *@param speed the speed of the projectile
     */
    public ProjectileObject(boolean limitedRange, double maxRange, int radius, int damage, Color color, double startingX, double startingY, double endingX, double endingY, double speed) {
        this.damage = damage;
        this.radius = radius;
        this.color = color;

        currentX = startingX;
        currentY = startingY;

        finalX = endingX;
        finalY = endingY;

        if(limitedRange)
            this.maxRange = maxRange;
        displacement = 0;

        angle = Math.atan(Math.abs(endingY - startingY) / Math.abs(endingX - startingX));
        if(finalX > currentX)
            xSpeed = Math.cos(angle) * speed;
        else
            xSpeed = -Math.cos(angle) * speed;
        if(finalY > currentY)
            ySpeed =  Math.sin(angle) * speed;
        else
            ySpeed = -Math.sin(angle) * speed;
    }

    /**
     *Draws a projectile object. 
     *Checks if the projectile is within the frame by comparing current coordinates to fWidth and fHeight.
     *If it isn't, set existing to false so it will not be moved by the move() method.
     *
     *@param g2 the Graphics2D
     *@return void
     */
    public void draw(Graphics2D g2) {
        projectile = new Ellipse2D.Double(currentX - 10, currentY - 10, 2 * radius, 2 * radius);
        g2.setPaint(color);
        g2.fill(projectile);
        g2.draw(projectile);
    }

    /**
     *Moves a projectile object along its path. 
     *
     *@return void
     */
    public void move() {
        if(maxRange < 10000)
            displacement += Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
        currentX += xSpeed;
        currentY += ySpeed;
    }

    /**
     *Moves a projectile object along its path. 
     *
     *@return void
     */
    public void move2() {
        amplitudeClock += 1;
        dAmplitude = amplitude * Math.cos(amplitudeClock * Math.PI / 18);
        currentX += (dAmplitude) * Math.cos(getAngle() + Math.PI / 2) + xSpeed;
        currentY += (dAmplitude) * Math.sin(getAngle() + Math.PI / 2) + ySpeed;
        amplitudeClock += 1;
    }

        /**
     * Moves proectile object by an amount.
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
     * Returns the current x-coordinate of the projectile.
     *
     * @return the current x-coordinate
     */
    public double getX() {
        return currentX;
    }

    /**
     * Returns the current y-coordinate of the projectile.
     *
     * @return the current y-coordinate
     */
    public double getY() {
        return currentY;
    }

    /**
     * Returns the angle of the projectile.
     *
     * @return the angle of the projectile
     */
    public double getAngle() {
        if(xSpeed > 0 && ySpeed < 0)
            return -angle;
        else if (xSpeed < 0 && ySpeed < 0)
            return Math.PI + angle;
        else if (xSpeed < 0 && ySpeed > 0)
            return Math.PI - angle;
        return angle;
    }

    /**
     * Returns the damage of the projectile.
     *
     * @return the damage of the projectile
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the radius of the projectile.
     *
     * @return the radius of the projectile
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Returns the amplitude of the projectile.
     *
     * @return the amplitude of the projectile
     */
    public double getAmplitude() {
        return amplitude;
    }

    /**
     * Returns the range of the projectile.
     *
     * @return the range of the projectile
     */
    public double getRange() {
        return maxRange;
    }

    /**
     * Returns the displacement of the projectile.
     *
     * @return the displacement of the projectile
     */
    public double getDisplacement() {
        return displacement;
    }

    /**
     * Returns the color of the projectile.
     *
     * @return the color of the projectile
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the speed of the projectile.
     *
     * @return the speed of the projectile
     */
    public double getSpeed() {
        return Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
    }
    
    /**
     * Returns the xspeed of the projectile.
     *
     * @return the xspeed of the projectile
     */
    public double getXSpeed() {
        return xSpeed;
    }
    
    /**
     * Returns the yspeed of the projectile.
     *
     * @return the yspeed of the projectile
     */
    public double getYSpeed() {
        return ySpeed;
    }
}
