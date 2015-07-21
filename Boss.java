import java.awt.Graphics2D;

/**
 * Contains the common methods of each type of boss. Allows for an ArrayList<Boss> in GameComponent.
 * 
 * @author (Thanatcha Panpairoj) 
 * @version (5/27/15)
 */
public interface Boss
{
    void draw(Graphics2D g2);
    
    void move();
    
    void move2();
    
    void moveBy(double dx, double dy);
    
    double getX();
    
    double getY();
    
    double getXSpeed();
    
    double getYSpeed();
    
    void changeHp(int dHp);
    
    int getHp();
    
    String getName();
}