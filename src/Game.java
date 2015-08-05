import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Point;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import java.net.URL;

import java.io.File;
import java.io.IOException;

/**
 * This is the game class. This includes the JFrame, listeners, buttons, and the GameComponent which includes all the objects. The buttons and the components are all added to a panel, which is added to the frame.
 * The main method starts the game and sets everything up. 
 *
 * @author (Thanatcha Panpairoj, Justin Liu)
 * @version (6/8/15)
 */

public class Game extends JFrame
{
    private static boolean left, right, up, down, firing, space, autofire, autospeed, pause, zeroScoreMessageDisplayed, menu;
    private static double diagonalMoveDistance = 7 / Math.sqrt(2), mouseX, mouseY;
    private static Image cursorImage;

    /**
     * Main method that starts the game. Sets up the JFrame, GameComponent, GridComponent, buttons, and listeners.
     * Sets the size to fullscreen and the cursor to a crosshair.
     * Starts a timer with a 1000/60 millisecond delay. The timer calls the ActionPerformed method every 1000 / 60 of a second.
     * This updates and repaints the GameComponent which contains all the objects each time.
     * Checks to see if the game has ended and offers to restart. Can also pause the game.
     * 
     * @param  args  default parameter for main method
     * @return       void
     */
    public static void main(String[] args)
    {
        Game game = new Game();
    }

    public Game() {
        super();

        this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        this.setTitle("Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        final int width = this.getWidth();
        final int height = this.getHeight();

        left = false;
        right = false;
        up = false;
        down = false;
        firing = false;
        space = false;
        autofire = false;
        autospeed = false;
        pause = false;
        zeroScoreMessageDisplayed = false;
        menu = true;

        //         frame.setUndecorated(true);
        //         frame.setShape(new Ellipse2D.Double(0,0, 800, 800));//circle frame?

        JPanel panel = new JPanel();
        panel.setDoubleBuffered(true);

        final GridComponent gridComponent = new GridComponent(width, height);
        final GameComponent component = new GameComponent(width, height, gridComponent);
        pause = true;

        final JButton startButton = new JButton ("Start Game");
        final JButton optionsButton = new JButton ("Options");
        final JButton aboutButton = new JButton("About");
        final JButton menuBackToMenuButton = new JButton ("Back to Menu");

        final JButton exitButton = new JButton ("Exit Game");

        final JButton restartButton = new JButton ("Restart");

        final JButton gameBackToMenuButton = new JButton ("Back to Menu");

        final JRadioButton easyButton = new JRadioButton("Easy");
        final JRadioButton mediumButton = new JRadioButton("Medium");
        final JRadioButton hardButton = new JRadioButton("Hard");

        final ButtonGroup group = new ButtonGroup();

        final Toolkit toolkit = Toolkit.getDefaultToolkit();

        try {
            cursorImage = ImageIO.read(Game.class.getResource("images/crosshair.png"));
        } catch (java.io.IOException e) {
            System.err.println("Could not find cursor image");
        }

        try {
            Image icon = ImageIO.read(Game.class.getResource("images/blue circle.jpg"));
            this.setIconImage(icon);
        } catch (java.io.IOException e) {
            System.err.println("Could not find cursor image");
        }

        double x = this.getLocation().getX();
        double y = this.getLocation().getY();

        class TimeListener implements ActionListener {
            /**
             * Updates information for the game at a constant rate.
             * This includes:
             * Starting difficulty, menus of the game, pause state, mouse location, user controls, enemies, frame, and button visibility.
             * 
             * @param  e  Used by the timer.
             * @return    void
             */
            public void actionPerformed(ActionEvent e) {
                if (easyButton.isSelected()) { 
                    component.changeStartingDifficulty(0);
                    easyButton.setText("Easy - for beginners");
                    mediumButton.setText("Medium");
                    hardButton.setText("Hard");
                } else if (mediumButton.isSelected()) { 
                    component.changeStartingDifficulty(1100000000);
                    easyButton.setText("Easy");
                    mediumButton.setText("Medium - for experienced");
                    hardButton.setText("Hard");
                } else if (hardButton.isSelected()) { 
                    component.changeStartingDifficulty(2000000000);
                    easyButton.setText("Easy");
                    mediumButton.setText("Medium");
                    hardButton.setText("Hard - high difficulty high score");
                }

                if(!pause) {
                    mouseX = MouseInfo.getPointerInfo().getLocation().getX() - x - 3;
                    mouseY = MouseInfo.getPointerInfo().getLocation().getY() - y - 25;

                    boolean boosting = space || autospeed;

                    if(component.isGame()) {
                        if(gridComponent.getXShift() > 10000 + width / 2)
                            left = false;
                        if(gridComponent.getXShift() < -10000 + width / 2)
                            right = false;
                        if(gridComponent.getYShift() > 10000  + height / 2)
                            up = false;
                        if(gridComponent.getYShift() < -10000 + height / 2)
                            down = false;

                        if (right && up && !left) {
                            component.moveBy(boosting, diagonalMoveDistance, -diagonalMoveDistance);
                        } else if (right && down && !left) {
                            component.moveBy(boosting, diagonalMoveDistance, diagonalMoveDistance);
                        } else if (left && up && !right) {
                            component.moveBy(boosting, -diagonalMoveDistance, -diagonalMoveDistance);
                        } else if (left && down && !right) {
                            component.moveBy(boosting, -diagonalMoveDistance, diagonalMoveDistance);
                        } else {
                            if (right) {
                                component.moveBy(boosting, 7, 0);
                            } else if (left) {
                                component.moveBy(boosting, -7, 0);
                            } else if (up) {
                                component.moveBy(boosting, 0, -7);
                            } else if (down) {
                                component.moveBy(boosting, 0, 7);
                            }
                        }
                    }

                    if(firing || autofire)
                        component.userShoot(mouseX, mouseY);

                    component.spawnEnemy(2, 5, 240);

                    component.updateKeys(boosting, left, right, up, down, firing, autofire);
                    component.updateSize(width, height);
                    component.updateMouse(mouseX, mouseY);

                    if (!component.isGame()) {
                        restartButton.setVisible(true); 
                        gameBackToMenuButton.setVisible(true);
                        if(component.getScore() == 0 && !zeroScoreMessageDisplayed) {
                            JOptionPane.showMessageDialog(null, "Unjust life", "0 score", JOptionPane.OK_OPTION);
                            zeroScoreMessageDisplayed = true;
                        }
                    } else {
                        restartButton.setVisible(false); 
                        gameBackToMenuButton.setVisible(false);
                        zeroScoreMessageDisplayed = false;
                    }

                    repaint();
                } else if (menu) {
                    gridComponent.shift(-1, 0);
                    if(gridComponent.getXShift() -100 < 0.0001)
                        gridComponent.shift(100, 0);
                    repaint();
                }
            }
        }

        class MotionWithKeyListener implements KeyListener {
            /**
             * Updates which keys are currently pressed.
             * 
             * @param  e  key pressed on the keyboard
             * @return    void
             */
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    left = true;
                else if (e.getKeyCode() == KeyEvent.VK_D)
                    right = true;
                else if (e.getKeyCode() == KeyEvent.VK_W)
                    up = true; 
                else if (e.getKeyCode() == KeyEvent.VK_S)
                    down = true;
                else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                    space = true;
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !menu)
                    if(pause)
                        pause = false;
                    else
                        pause = true;
            }

            /**
             * Updates when a key is released.
             * 
             * @param  e  key released from the keyboard
             * @return    void
             */
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    left = false;
                else if (e.getKeyCode() == KeyEvent.VK_D)
                    right = false;
                else if (e.getKeyCode() == KeyEvent.VK_W)
                    up = false;
                else if (e.getKeyCode() == KeyEvent.VK_S)
                    down = false;
                else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                    space = false;
            }

            /**
             * Updates when a key is typed.
             * 
             * @param  e  key typed on the keyboard
             * @return    void
             */
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((c == 'f' || c == 'F') && !autofire)
                    autofire = true;
                else if((c == 'f' || c == 'F') && autofire)
                    autofire = false;
                else if ((c == 'v' || c == 'V') && !autospeed)
                    autospeed = true;
                else if((c == 'v' || c == 'V') && autospeed)
                    autospeed = false;
                else if(c == 'l' || c == 'L')
                    component.changeShotMode(2);
                else if(c == 'i' || c == 'I')
                    if(!component.getAutoaim())
                        component.autoaim(true);
                    else
                        component.autoaim(false);
                else if(c == 'o' || c == 'O')
                    if(!component.getAutomove())
                        component.automove(true);
                    else
                        component.automove(false);
                else if(c == 'k' || c == 'K')
                    component.changeShotMode(3);
            }
        }

        class MousePressListener implements MouseListener
        {
            /**
             * Updates when the mouse button is pressed.
             * 
             * @param  event  mouse button press
             * @return        void
             */
            public void mousePressed(MouseEvent event)
            {
                firing = true;
            }

            /**
             * Updates when the mouse button is released.
             * 
             * @param  event  mouse button is released
             * @return        void
             */
            public void mouseReleased(MouseEvent event) {
                firing = false;
            }

            /**
             * Updates when the mouse button is pressed and released, as a "click."
             * 
             * @param  event  mouse button is clicked
             * @return        void
             */
            public void mouseClicked(MouseEvent event) {
                component.userShoot(mouseX, mouseY);
            }

            public void mouseEntered(MouseEvent event) {}

            public void mouseExited(MouseEvent event) {}
        }

        class ButtonMouseListener implements MouseListener
        {
            public void mousePressed(MouseEvent event){}

            public void mouseReleased(MouseEvent event) {}

            public void mouseClicked(MouseEvent event) {}

            public void mouseEntered(MouseEvent event) {
                JButton b = (JButton)event.getComponent();
                b.setFont(new Font(event.getComponent().getFont().getFamily(), Font.BOLD, 17));
                b.setBounds((int)b.getLocation().getX() - 10, (int)b.getLocation().getY() - 10, 220, 100);
            }

            public void mouseExited(MouseEvent event) {
                JButton b = (JButton)event.getComponent();
                b.setFont(new Font(event.getComponent().getFont().getFamily(), Font.BOLD, 14));
                b.setBounds((int)b.getLocation().getX() + 10, (int)b.getLocation().getY() + 10, 200, 80);
            }
        }

        class ScrollListener implements MouseWheelListener
        {
            /**
             * Changes the shooting mode of the user when the mouse wheel is scrolled.
             * 
             * @param  e  action of scrolling the mouse wheel
             * @return    void
             */
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(component.getShotMode() == 0)
                    component.changeShotMode(1);
                //else if(component.getShotMode() == 1)
                //    component.changeShotMode(2);
                else
                    component.changeShotMode(0);
            }
        }

        class StartGameListener implements ActionListener
        {
            /**
             * Starts the game when its button is pressed. Hides the menu and its buttons and focuses the game component. Makes the mouse a crosshair.
             * 
             * @param  event  Start Game button press
             * @return        void
             */
            public void actionPerformed(ActionEvent event) {
                pause = false;
                menu = false;
                component.setVisible(true);
                component.requestFocus();

                gridComponent.reset();

                startButton.setVisible(false);
                optionsButton.setVisible(false);
                aboutButton.setVisible(false);
                exitButton.setVisible(false);

                autofire = false;

                setCursor(toolkit.createCustomCursor(cursorImage, new Point(15, 15), "Crosshair"));
            }
        }

        class ExitGameListener implements ActionListener
        {
            /**
             * Closes the game when its button is pressed.
             * 
             * @param  event  Exit Game button press
             * @return        void
             */
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        }

        class OptionsListener implements ActionListener
        {
            /**
             * Goes to the options screen when the options button is pressed. Hides the menu and its buttons and makes the difficulty radio buttons visible. Makes visible the button to go back to the main menu.
             * 
             * @param  event  Options button press
             * @return        void
             */
            public void actionPerformed(ActionEvent event) {
                startButton.setVisible(false);
                optionsButton.setVisible(false);
                aboutButton.setVisible(false);
                exitButton.setVisible(false);
                menuBackToMenuButton.setVisible(true);
                easyButton.setVisible(true);
                mediumButton.setVisible(true);
                hardButton.setVisible(true);
            }
        }

        class AboutListener implements ActionListener
        {
            /**
             * Goes to the options screen when the options button is pressed. Hides the menu and its buttons and makes the difficulty radio buttons visible. Makes visible the button to go back to the main menu.
             * 
             * @param  event  Options button press
             * @return        void
             */
            public void actionPerformed(ActionEvent event) {
                startButton.setVisible(false);
                optionsButton.setVisible(false);
                aboutButton.setVisible(false);
                exitButton.setVisible(false);
                menuBackToMenuButton.setVisible(true);
            }
        }

        class RestartGameListener implements ActionListener
        {
            /**
             * Restarts the game when the button is made visible after losing.
             * 
             * @param  event  Restart button press
             * @return        void
             */
            public void actionPerformed(ActionEvent event) {
                left = false;
                right = false;
                up = false;
                down = false;
                firing = false;
                space = false;
                autofire = false;
                autospeed = false;
                pause = false;
                zeroScoreMessageDisplayed = false;
                component.restart();
                gridComponent.reset();
            }
        }

        class BackToMenuListener implements ActionListener
        {
            /**
             * Goes back to the main menu from the options screen. Makes the options buttons invisible and the main menu visible.
             * 
             * @param  event  Back to Menu button press
             * @return        void
             */
            public void actionPerformed(ActionEvent event) {
                menu = true;
                component.setVisible(false);
                restartButton.setVisible(false);
                gameBackToMenuButton.setVisible(false);
                startButton.setVisible(true);
                optionsButton.setVisible(true);
                aboutButton.setVisible(true);
                menuBackToMenuButton.setVisible(false);
                easyButton.setVisible(false);
                mediumButton.setVisible(false);
                hardButton.setVisible(false);
                exitButton.setVisible(true);
                setCursor(Cursor.getDefaultCursor());

                gridComponent.reset();
                component.restart();
                pause = true;
            }
        }

        component.setPreferredSize(new Dimension(width, height));
        component.setBounds(0, 0, width, height);
        component.setFocusable(true);
        component.setVisible(false);

        gridComponent.setPreferredSize(new Dimension(width, height));
        gridComponent.setBounds(0, 0, width, height);
        gridComponent.setVisible(true);

        ActionListener tListener = new TimeListener();
        KeyListener kListener = new MotionWithKeyListener();
        component.addKeyListener(kListener);

        MouseListener mListener = new MousePressListener();
        component.addMouseListener(mListener);

        MouseWheelListener mwListener = new ScrollListener();
        component.addMouseWheelListener(mwListener);

        final int DELAY = 1000 / 60;//60 frames per second
        Timer t = new Timer(DELAY, tListener);
        t.start();

        ActionListener sListener = new StartGameListener();
        startButton.setFont(new Font(startButton.getFont().getFamily(), Font.BOLD, 14));
        startButton.addMouseListener(new ButtonMouseListener());
        startButton.addActionListener(sListener);
        startButton.setBounds(width / 2 - 100, height / 2 - 200, 200, 80);
        startButton.setVisible(true);

        ActionListener oListener = new OptionsListener();
        optionsButton.setFont(new Font(startButton.getFont().getFamily(), Font.BOLD, 14));
        optionsButton.addMouseListener(new ButtonMouseListener());
        optionsButton.addActionListener(oListener);
        optionsButton.setBounds(width / 2 - 100, height / 2 - 100, 200, 80);
        optionsButton.setVisible(true);

        ActionListener aListener = new AboutListener();
        aboutButton.setFont(new Font(startButton.getFont().getFamily(), Font.BOLD, 14));
        aboutButton.addMouseListener(new ButtonMouseListener());
        aboutButton.addActionListener(aListener);
        aboutButton.setBounds(width / 2 - 100, height / 2, 200, 80);
        aboutButton.setVisible(true);

        ActionListener mBTMListener = new BackToMenuListener();
        menuBackToMenuButton.addActionListener(mBTMListener);
        menuBackToMenuButton.setBounds(50, 50, 150, 40);
        menuBackToMenuButton.setVisible(false);

        ActionListener eListener = new ExitGameListener();
        exitButton.setFont(new Font(startButton.getFont().getFamily(), Font.BOLD, 14));
        exitButton.addMouseListener(new ButtonMouseListener());
        exitButton.addActionListener(eListener);
        exitButton.setBounds(width / 2 - 100, height / 2 + 100, 200, 80);
        exitButton.setVisible(true);

        easyButton.setBounds(width / 2 - 100, height / 2 - 150, 200, 25);
        easyButton.setVisible(false);
        easyButton.setOpaque(false);
        easyButton.setSelected(true);

        mediumButton.setBounds(width / 2 - 100, height / 2 - 50, 200, 25);
        mediumButton.setVisible(false);
        mediumButton.setOpaque(false);

        hardButton.setBounds(width / 2 - 100, height / 2 + 50, 200, 25);
        hardButton.setVisible(false);
        hardButton.setOpaque(false);

        ActionListener rListener = new RestartGameListener();
        restartButton.addActionListener(rListener);
        restartButton.setBounds(width / 2 - 135, height / 2 - 20, 100, 40);
        restartButton.setVisible(false);

        ActionListener gBTMListener = new BackToMenuListener();
        gameBackToMenuButton.addActionListener(gBTMListener);
        gameBackToMenuButton.setBounds(width / 2 - 15, height / 2 - 20, 150, 40);
        gameBackToMenuButton.setVisible(false);

        group.add(easyButton);
        group.add(mediumButton);
        group.add(hardButton); 

        panel.setLayout(null);
        panel.add(startButton);
        panel.add(optionsButton);
        panel.add(aboutButton);
        panel.add(menuBackToMenuButton);
        panel.add(easyButton);
        panel.add(mediumButton);
        panel.add(hardButton);
        panel.add(exitButton);
        panel.add(restartButton);
        panel.add(gameBackToMenuButton);
        panel.add(component);
        panel.add(gridComponent);
        this.add(panel);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        this.setVisible(true);
        this.setResizable(false);

        gridComponent.repaint();
    }
}
