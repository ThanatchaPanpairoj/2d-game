import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

import java.net.URL;

import java.io.File;
import java.io.IOException;

/**
 * Extends the user object to inherit its fields and methods. Creates, draws, and moves an enemy object. The draw method is overridden to make the enemy look different.
 * 
 * @author (Thanatcha Panpairoj, Jack Weng) 
 * @version (6/6/15)
 */
public class Enemy extends User
{
    private double currentX, currentY, finalX, finalY, xSpeed, ySpeed, angle, speed;
    private int fWidth, fHeight;
    private Color color;
    private Image skullImage;
    private String name;

    /**
     * Initializes the enemy object with its name, dimensions, location, color, and speed.
     *
     * @param name     the name of the enemy.
     * @param radius   the radius of the enemy.
     * @param fWidth   the width of the frame.
     * @param fHeight  the height of the frame.
     * @param x        the x-coordinate of the enemy.
     * @param y        the y-coordinate of the enemy.
     * @param color    the color of the enemy.
     * @param speed    the speed of the enemy.
     * 
     */
    public Enemy(String name, int radius, int fWidth, int fHeight, int x, int y, Color color, double speed) {
        super(radius, x, y, color, 1);

        this.name = name;

        this.color = color;

        this.fWidth = fWidth;
        this.fHeight = fHeight;

        currentX = x;
        currentY = y;

        finalX = Math.random() * fWidth;
        finalY = Math.random() * (fHeight - 30);
        angle = Math.atan(Math.abs(finalY - currentY) / Math.abs(finalX - currentX));

        this.speed = speed;

        if(finalX > currentX)
            xSpeed = Math.cos(angle) * speed;
        else
            xSpeed = -Math.cos(angle) * speed;
        if(finalY > currentY)
            ySpeed =  Math.sin(angle) * speed;
        else
            ySpeed = -Math.sin(angle) * speed;

        //         if(super.getMaxHp() == 1)
        //             try {
        //                 skullImage = ImageIO.read(Game.class.getResource("skull.png"));
        //             } catch (java.io.IOException e) {
        //                 System.err.println("Could not find cursor image");
        //             }
    }

    /**
     * Draws the enemy. Overrides the draw method from UserObject to create different faces for different enemies.
     * 
     * @param g2 the graphics context
     * @return   void
     */
    public void draw(Graphics2D g2) {
        if(this.getName().equals("Points")) {
            Ellipse2D.Double circle = new Ellipse2D.Double(currentX - 100, currentY - 100, 2 * super.getRadius(), 2 * super.getRadius());
            //could be any object, doesn't have to be an ellipse
            g2.setColor(color);
            g2.draw(circle);

            g2.setPaint(color);
            g2.fill(circle);

            //         Line2D.Double segment=new Line2D.Double(xLeft-radius/2, yTop-radius/2, xLeft-radius/2+5, yTop-radius/2);
            //         Line2D.Double segment2=new Line2D.Double(xLeft+radius/2, yTop-radius/2, xLeft+radius/2-5, yTop-radius/2);

            g2.setColor(Color.WHITE);
            //         g2.draw(segment);
            //         g2.draw(segment2);
            g2.drawArc((int)currentX-super.getRadius() / 2 - 20, (int)currentY-super.getRadius() / 2 - 5, (int)super.getRadius() / 2 - 2, (int)super.getRadius() / 2 - 2, 0, 180);
            g2.drawArc((int)currentX+super.getRadius() / 2 - 30, (int)currentY-super.getRadius() / 2 - 5, (int)super.getRadius() / 2 - 2, (int)super.getRadius() / 2 - 2, 0, 180);
            g2.drawArc((int)currentX-super.getRadius() / 2, (int)currentY+super.getRadius() / 2 - 25, (int)super.getRadius(), (int)super.getRadius()/2, 180,180);
        }else if(super.getMaxHp() > 6) {
            //             Ellipse2D.Double circle = new Ellipse2D.Double(currentX - 25, currentY - 25, 2 * super.getRadius(), 2 * super.getRadius());
            //             g2.setColor(color);
            //             g2.draw(circle);
            //             g2.setPaint(color);
            //             g2.fill(circle);
            Ellipse2D.Double circle = new Ellipse2D.Double(currentX - 100, currentY - 100, 2 * super.getRadius(), 2 * super.getRadius());
            //could be any object, doesn't have to be an ellipse
            Ellipse2D.Double circle2 = new Ellipse2D.Double(currentX - getRadius()/2 - 5, currentY - 15 , 20, 20);
            Ellipse2D.Double circle3 = new Ellipse2D.Double(currentX + getRadius()/2 - 15, currentY - 15 , 20, 20);
            g2.setColor(color);
            g2.draw(circle);

            g2.setPaint(color);
            g2.fill(circle);

            g2.setColor(Color.WHITE);
            Line2D.Double segment=new Line2D.Double(getX(), getY(), getX()-getRadius()/2, getY()-getRadius()/2);
            Line2D.Double segment2=new Line2D.Double(getX(), getY(), getX()+getRadius()/2, getY()-getRadius()/2);
            g2.draw(segment);
            g2.draw(segment2);
            g2.fill(circle3);
            g2.fill(circle2);
            //         g2.drawArc((int)getX()-getRadius()/2-5, (int)getY()-getRadius()/2, (int)getRadius()/2-2, (int)getRadius()/2-2, 180, 180);
            //         g2.drawArc((int)getX()+getRadius()/2-5, (int)getY()-getRadius()/2, (int)getRadius()/2-2, (int)getRadius()/2-2, 180, 180);
            g2.drawArc((int)getX()-getRadius()/2, (int)getY()+getRadius()/2, (int)getRadius(), (int)getRadius()/2, 0,180);
        } else {
            Ellipse2D.Double circle = new Ellipse2D.Double(currentX - 25, currentY - 25, 50, 50);
            //             //could be any object, doesn't have to be an ellipse
            //             Ellipse2D.Double circle2 = new Ellipse2D.Double(currentX - getRadius()/2, currentY , 5, 5);
            //             Ellipse2D.Double circle3 = new Ellipse2D.Double(currentX + getRadius()/2-5, currentY , 5, 5);
            //             g2.setColor(color);
            // 
            g2.setPaint(color);
            g2.fill(circle);
            // 
            //             g2.setColor(Color.WHITE);
            //             Line2D.Double segment=new Line2D.Double(getX(), getY(), getX()-getRadius(), getY()-getRadius());
            //             Line2D.Double segment2=new Line2D.Double(getX(), getY(), getX()+getRadius(), getY()-getRadius());
            //             g2.draw(segment);
            //             g2.draw(segment2);
            //             g2.fill(circle3);
            //             g2.fill(circle2);
            //             //         g2.drawArc((int)getX()-getRadius()/2-5, (int)getY()-getRadius()/2, (int)getRadius()/2-2, (int)getRadius()/2-2, 180, 180);
            //             //         g2.drawArc((int)getX()+getRadius()/2-5, (int)getY()-getRadius()/2, (int)getRadius()/2-2, (int)getRadius()/2-2, 180, 180);
            //             g2.drawArc((int)getX()-getRadius()/2, (int)getY()+getRadius()/2, (int)getRadius(), (int)getRadius()/2, 0,180);

            //if(skullImage != null) g2.drawImage(skullImage, (int)(currentX - 25), (int)(currentY - 25), 50, 50, null);
        }
    }

    /**
     * Moves the enemy object along its path. Can move at an angle.
     *
     * @return void
     */
    public void move() {
        currentX += xSpeed;
        currentY += ySpeed;
        super.moveBy(xSpeed, ySpeed);
        if(Math.abs(currentX - finalX) < 10 && Math.abs(currentY - finalY) < 10) {
            finalX = Math.random() * fWidth;
            finalY = Math.random() * fHeight;
            angle = Math.atan(Math.abs(finalY - currentY) / Math.abs(finalX - currentX));
            if(finalX > currentX)
                xSpeed = Math.cos(angle) * speed;
            else
                xSpeed = -Math.cos(angle) * speed;
            if(finalY > currentY)
                ySpeed =  Math.sin(angle) * speed;
            else
                ySpeed = -Math.sin(angle) * speed;
        }
    }

    public void move2() {
        finalX = fWidth / 2;
        finalY = fHeight / 2;
        angle = Math.atan(Math.abs(finalY - currentY) / Math.abs(finalX - currentX));
        if(finalX > currentX)
            xSpeed = Math.cos(angle) * speed;
        else
            xSpeed = -Math.cos(angle) * speed;
        if(finalY > currentY)
            ySpeed =  Math.sin(angle) * speed;
        else
            ySpeed = -Math.sin(angle) * speed;
        if(Math.abs(currentX - finalX) > 5 || Math.abs(currentY - finalY) > 5) {
            currentX += xSpeed;
            currentY += ySpeed;
            super.moveBy(xSpeed, ySpeed);
        }
    }

    /**
     * Moves enemy object by an amount.
     *
     * @param dx change in x
     * @param dy change in y
     * @return void
     */
    public void moveBy(double dx, double dy) {
        super.moveBy(dx, dy);
        currentX += dx;
        currentY += dy;
        finalX += dx;
        finalY += dy;
    }

    /**
     * Returns the name of the enemy.
     *
     * @return name of the boss
     */
    public String getName() {
        return name;
    }

    public double getX() {
        return currentX;
    }

    public double getY() {
        return currentY;
    }

    public double getFinalX() {
        return finalX;
    }

    public double getFinalY() {
        return finalY;
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() {
        return ySpeed;
    }
}
