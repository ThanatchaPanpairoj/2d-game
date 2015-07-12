import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.Color;

import javax.swing.JComponent;

/**
 * This draws a background grid on the frame.
 *
 * @author (Thanatcha Panpairoj)
 * @version (6/8/15)
 */
public class GridComponent extends JComponent 
{
    private int fWidth, fHeight;
    private double xShift, yShift;

    /**
     * Initializes grid.
     *
     * @param frameWidth   the width of the frame.
     * @param frameHeight  the height of the frame.
     * 
     */
    public GridComponent(int frameWidth, int frameHeight) {
        fWidth = frameWidth;
        fHeight = frameHeight;
        xShift = 0;
        yShift = 0;
    }

    /**
     * Draws the grid. Each box is 100x100 pixels. Number of lines drawn depends on the frame width and frame height. 
     *
     * @param  g  Graphics
     * @return    void
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.GRAY);

        //horizontl lines
        for(int i = -10000; i <= 10000; i += 100)
            g2.draw(new Line2D.Double(-10000 + xShift, i + yShift, 10000 + xShift, i + yShift));

        //vertical lines
        for(int i = -10000; i <= 10000; i += 100)
            g2.draw(new Line2D.Double(i + xShift, -10000 + yShift, i + xShift, 10000 + yShift));
    }
    
    public void shift(double dx, double dy) {
        xShift += dx;
        yShift += dy;
    }
    
    public void reset() {
        xShift = 0;
        yShift = 0;
    }
    
    public double getXShift() {
        return xShift;
    }
    
    public double getYShift() {
        return yShift;
    }
}
