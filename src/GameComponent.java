import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import java.util.ArrayList;

import javax.swing.JComponent;

import java.text.DecimalFormat;

/**
 * Draws the game, which includes the user, enemies, bosses, and projectiles. This will be added onto the frame in the main method of the Game class.
 *
 * @author (Thanatcha Panpairoj, Justin Liu)
 * @version (6/8/15)
 */
public class GameComponent extends JComponent 
{
    private User user;

    private boolean boosting, left, right, up, down, firing, autofire, game, newShotModeDisplayed, autoaim, automove;
    private int fWidth, fHeight, framesToDisplayInstructions, framesToDisplayShotMode, framesToDisplayNewShotMode, shotClock, enemyClock, previousTCount, score, startingDifficulty, difficulty, lastShotAngle, enemiesDestroyed, bossesDestroyed, shotsFired, shotsHit, shotMode;
    private double mouseX, mouseY, time;
    private String hint;

    private ArrayList<Projectile> userProjectiles;
    private ArrayList<Projectile> enemyProjectiles;
    private ArrayList<Enemy> enemies;
    private ArrayList<Boss> bosses;
    private ArrayList<Integer> enemyShotClock;
    private ArrayList<Integer> bossShotClock;

    private GridComponent grid;

    /**
     * Initializes the game component. Sets the time to display the control instructions to 10 seconds (600 frames).
     * Sets counters for shooting at 0, creates empty ArrayLists to store projectiles and enemies.
     * Starts the game with a specified difficulty and creates the user object.
     * 
     * @param frameWidth   the width of the frame.
     * @param frameHeight  the height of the frame.
     *
     */
    public GameComponent(int frameWidth, int frameHeight, GridComponent grid) {
        user = new User(25, frameWidth >> 1, frameHeight >> 1, Color.BLUE, 6);
        this.grid = grid;
        score = 0;

        fWidth = frameWidth;
        fHeight = frameHeight;

        framesToDisplayInstructions = 600;
        framesToDisplayShotMode = 0;
        framesToDisplayNewShotMode = 0;

        shotClock = 0;
        enemyClock = 0;
        previousTCount = 0;
        time = 0;
        lastShotAngle = 0;
        shotsFired = 0;
        shotsHit = 0;
        shotMode = 0;

        hint = "";

        startingDifficulty = 0;

        difficulty = startingDifficulty;

        userProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();
        enemies = new ArrayList<Enemy>();
        bosses = new ArrayList<Boss>();
        enemyShotClock = new ArrayList<Integer>();
        bossShotClock = new ArrayList<Integer>();

        game = true;
        autoaim = false;
        automove = false;
    }

    /**
     * Draws all of the game objects, including the user, projectiles, and enemies. This paintComponent
     * method gets called every 1000 / 60 millisecond so it updates 60 times per 1000 millisecond.
     * It also determines the projectile collisions, and draws the instructions, statistics, and game over state.
     *
     * @param  g  Graphics
     * @return    void
     * 
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        //         g2.translate(fWidth * 3 / 8, fHeight * 3 / 8);
        //         g2.scale(1.0 / 4, 1.0 / 4);
        game = user.getHp() != 0;

        try{
            int userLevel = user.getLevel();
            if(score >= 10000000)
                changeShotMode(2);
            if(!newShotModeDisplayed && userLevel == 10) {
                newShotModeDisplayed = true;
                framesToDisplayNewShotMode = 500;
            }
            if(game) {
                time += 1.0 / 60;
                if((int)(time % (11 - (userLevel >> 1))) == 0 && (int)(time) != previousTCount) {
                    user.changeHp(1 + (userLevel >> 3));
                    previousTCount = (int)time;
                }
                if(time % (int)time < userLevel * 1.0 / 60) {
                    user.changeStamina(1);
                }
            }

            if(difficulty < 1000000000)
                difficulty += 400000;
            else if(difficulty < 1500000000)
                difficulty += 100000;
            else if(difficulty < 1750000000)
                difficulty += 80000;
            else if(difficulty < 2147400000)
                difficulty += 50000;

            Enemy closestE = null;
            int shortestEnemyDistance = 10001;
            if(autoaim) {
                for(int e = 0; e < enemies.size(); e++) {
                    Enemy enemy = enemies.get(e);
                    double x = enemy.getX();
                    double y = enemy.getY();
                    if(x > -200 && x < fWidth + 200 && y > -200 && y < fHeight + 200)
                        enemy.draw(g2);
                    if(game) {
                        enemyShoot(e, enemy.getX(), enemy.getY(), 60);
                        if(enemy.getName().equals("e1"))
                            enemy.move();
                        else 
                            enemy.move2();
                    }

                    int distance = (int)Math.sqrt(Math.pow(x - user.getX(), 2) + Math.pow(y - user.getY(), 2));
                    if(distance < shortestEnemyDistance) {
                        closestE = enemy;
                        shortestEnemyDistance = distance;
                    }
                }
            } else
                for(int e = 0; e < enemies.size(); e++) {
                    Enemy enemy = enemies.get(e);
                    double x = enemy.getX();
                    double y = enemy.getY();
                    if(x > -200 && x < fWidth + 200 && y > -200 && y < fHeight + 200)
                        enemy.draw(g2);
                    if(game) {
                        enemyShoot(e, enemy.getX(), enemy.getY(), 60);
                        if(enemy.getName().equals("e1"))
                            enemy.move();
                        else 
                            enemy.move2();
                    }
                }

            for(int p = 0; p < userProjectiles.size(); p++) {
                Projectile projectile = userProjectiles.get(p);
                double x = projectile.getX();
                double y = projectile.getY();

                if(x > 0 && x < fWidth && y > 0 && y < fHeight)
                    projectile.draw(g2);
                if(x >= -10000 && x <= 10000 && y >= -10000 && y <= 10000 && projectile.getDisplacement() <= projectile.getRange()) {
                    boolean hit = false;
                    for(int e = 0; e < enemies.size(); e++) {
                        Enemy enemy = enemies.get(e);
                        if(Math.pow(enemy.getX() - x, 2) + Math.pow(enemy.getY() - y, 2) < 625) {//25^2 = 625, 25 is the radius of the Enemy
                            shotsHit++;
                            enemies.remove(e);
                            enemyShotClock.remove(e--);
                            userProjectiles.remove(p--);
                            int points = 600;
                            user.addXp(points);
                            updateScore(points  * (int)(1 + (startingDifficulty / 1000000000 >> 2)));
                            enemiesDestroyed++;
                            hit = true;
                            break;
                        }
                    }
                    if(!hit) {
                        for(int b = 0; b < bosses.size(); b++) {
                            Boss boss = bosses.get(b);
                            if(Math.pow(boss.getX() - x, 2) + Math.pow(boss.getY() - y, 2) < 10000) {//100^2 = 10000, 100 is the radius of the Enemy
                                boss.changeHp(-projectile.getDamage());
                                userProjectiles.remove(p--);
                                shotsHit++;
                                if(boss.getHp() == 0) {
                                    int points = 50 * (difficulty / 1000000);
                                    if(boss.getName().equals("Points"))
                                        points *= 2;
                                    user.addXp(points);
                                    updateScore(points * (int)(1 + (startingDifficulty / 1000000000 >> 2)));
                                    bossShotClock.remove(b);
                                    bosses.remove(b--);
                                    bossesDestroyed++;
                                    break;
                                }
                            }
                        }
                    }
                    if(x > 0 && x < fWidth && y > 0 && y < fHeight)
                        projectile.draw(g2);
                    if(game)
                        if(projectile.getAmplitude() != 0)
                            projectile.move2();
                        else
                            projectile.move();
                } else
                    userProjectiles.remove(p--);
            }

            for(int p = 0; p < enemyProjectiles.size(); p++) {
                Projectile projectile = enemyProjectiles.get(p);
                double x = projectile.getX();
                double y = projectile.getY();
                boolean blocked = false;

                double lightsaberAngle = 0;
                double reflectedAngle = 0;
                if(shotMode == 2)
                    for(Projectile userProjectile : userProjectiles)
                        if(userProjectile.getColor().equals(new Color(99, 184, 255)) && Math.abs(userProjectile.getX() - x) < 10 && Math.abs(userProjectile.getY() - y) < 10) {
                            blocked = true;
                            lightsaberAngle = userProjectile.getAngle() * 180 / Math.PI;
                            reflectedAngle = lightsaberAngle - 180 + lightsaberAngle - (projectile.getAngle() * 180 / Math.PI + 180);
                            if(userProjectile.getDisplacement() > 85)
                                reflectedAngle = projectile.getAngle() * 180 / Math.PI + 180;
                        }

                if(!blocked && x >= -10000 && x <= 10000 && y >= -10000 && y <= 10000 && projectile.getDisplacement() <= projectile.getRange()) {
                    boolean absorbed = false;
                    if(x > 0 && x < fWidth && y > 0 && y < fHeight)
                        projectile.draw(g2);
                    if(game)
                        if(projectile.getAmplitude() != 0)
                            projectile.move2();
                        else
                            projectile.move();
                    if(x > 0 && x < fWidth && y > 0 && y < fHeight)
                        projectile.draw(g2);
                    if(game)
                        projectile.move();
                    for(int b = 0; b < bosses.size(); b++) {
                        Boss boss = bosses.get(b);
                        if(boss.getName() .equals("Points") && Math.pow(boss.getX() - x, 2) + Math.pow(boss.getY() - y, 2) < 10000) {//100^2 = 10000, 100 is the radius of the Enemy
                            enemyProjectiles.remove(p--);
                            absorbed = true;
                        }
                    }
                    if(!absorbed && Math.pow(user.getX() - x, 2) + Math.pow(user.getY() - y, 2) < Math.pow(projectile.getRadius(), 2)) {//distance formula to check if the user is within the projectile radius
                        user.changeHp(-projectile.getDamage());
                        if(game)
                            enemyProjectiles.remove(p--);
                    }
                } else
                    enemyProjectiles.remove(p--);

                if(blocked) {
                    userProjectiles.add(new Projectile(projectile.getRadius(), projectile.getDamage(), projectile.getColor(), x, y, reflectedAngle, 2 * projectile.getSpeed()));
                }
            }

            Boss closestB = null;
            int shortestBossDistance = 10001;
            if(autoaim) {
                for(int b = 0; b < bosses.size(); b++) {
                    Boss boss = bosses.get(b);
                    double x = boss.getX();
                    double y = boss.getY();
                    if(x > -200 && x < fWidth + 200 && y > -200 && y < fHeight + 200)
                        boss.draw(g2);
                    if(game) {
                        if(bossShotClock.get(b) < 15)
                            if(boss.getName() .equals("Jack Weng"))
                                bossShoot(b, boss.getX(), boss.getY(), 14);
                            else if(boss.getName() .equals("Ms Jaime"))
                                bossShoot(b, boss.getX(), boss.getY(), 2);
                            else
                                bossShoot(b, boss.getX(), boss.getY(), 3);
                        else
                            bossShotClock.set(b, bossShotClock.get(b) - 1);

                        String bossName = boss.getName();
                        if(bossName.equals("Jack Weng") || bossName.equals("Justin Liu"))
                            boss.move2();
                        else
                            boss.move();
                    }
                    int distance = (int)Math.sqrt(Math.pow(x - user.getX(), 2) + Math.pow(y - user.getY(), 2));
                    if(distance < shortestBossDistance) {
                        closestB = boss;
                        shortestBossDistance = distance;
                    }
                }
            } else 
                for(int b = 0; b < bosses.size(); b++) {
                    Boss boss = bosses.get(b);
                    double x = boss.getX();
                    double y = boss.getY();
                    if(x > -200 && x < fWidth + 200 && y > -200 && y < fHeight + 200)
                        boss.draw(g2);
                    if(game) {
                        if(bossShotClock.get(b) < 15)
                            if(boss.getName() .equals("Jack Weng"))
                                bossShoot(b, boss.getX(), boss.getY(), 14);
                            else if(boss.getName() .equals("Ms Jaime"))
                                bossShoot(b, boss.getX(), boss.getY(), 2);
                            else
                                bossShoot(b, boss.getX(), boss.getY(), 3);
                        else
                            bossShotClock.set(b, bossShotClock.get(b) - 1);

                        String bossName = boss.getName();
                        if(bossName.equals("Jack Weng") || bossName.equals("Justin Liu"))
                            boss.move2();
                        else
                            boss.move();
                    }
                }

            if(game && (closestE != null || closestB != null))
                if(shortestEnemyDistance < shortestBossDistance) {
                    //                     double multiplier = shortestEnemyDistance / (15 + user.getLevel() * 1.5);
                    //                     int nextDistance = (int)Math.sqrt(Math.pow(closestE.getX() + closestE.getXSpeed() - user.getX(), 2) + Math.pow(closestE.getY() + closestE.getYSpeed() - user.getY(), 2));
                    //                     if(nextDistance > shortestEnemyDistance)
                    //                         multiplier *= 1.15;
                    //                     else
                    //                         multiplier *= 0.85;
                    //                     userShoot(closestE.getX() + multiplier * closestE.getXSpeed(), closestE.getY() + multiplier * closestE.getYSpeed());
                    double userProjectileSpeed = (15 + user.getLevel() * 1.5);
                    double enemyDistance = Math.sqrt(Math.pow(closestE.getX() - user.getX(), 2) + Math.pow(closestE.getY() - user.getY(), 2));
                    for(int t = 0; t < 500; t++) {
                        double userProjectileDistance =  t * userProjectileSpeed;
                        double enemyX = closestE.getX() + t * closestE.getXSpeed();
                        double ememyY = closestE.getY() + t * closestE.getYSpeed();
                        if(Math.abs(userProjectileDistance - Math.sqrt(Math.pow(enemyX - user.getX(), 2) + Math.pow(ememyY - user.getY(), 2))) < 25) {
                            //System.out.println(userProjectileDistance + "    " + Math.sqrt(Math.pow(enemyX - user.getX(), 2) + Math.pow(ememyY - user.getY(), 2)));
                            userShoot(enemyX, ememyY);
                            break;
                        }
                    }
                } else if (closestB != null){
                    double multiplier = shortestBossDistance / (15 + user.getLevel() * 1.5);
                    int nextDistance = (int)Math.sqrt(Math.pow(closestB.getX() + closestB.getXSpeed() - user.getX(), 2) + Math.pow(closestB.getY() + closestB.getYSpeed() - user.getY(), 2));
                    if(nextDistance > shortestBossDistance)
                        multiplier *= 1.15;
                    else
                        multiplier *= 0.85;
                    userShoot(closestB.getX() + multiplier * closestB.getXSpeed(), closestB.getY() + multiplier * closestB.getYSpeed());
                }

            if(automove) {
                if(closestB != null && shortestBossDistance < 240) {
                    double dx = user.getX() - closestB.getX();
                    double dy = user.getY() - closestB.getY();
                    double v = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    moveBy(true,7 * dx / v, 7 * dy / v);
                } else {
                    Projectile closestP = null;
                    int closestPDistance = 10001;
                    for(Projectile p : enemyProjectiles) {
                        int distance = (int)Math.sqrt(Math.pow(p.getX() - user.getX(), 2) + Math.pow(p.getY() - user.getY(), 2));
                        if(distance < closestPDistance) {
                            closestPDistance = distance;
                            closestP = p;
                        }
                    }
                    if(game && closestP != null && closestPDistance < 600) {
                        Color pColor = closestP.getColor();
                        if(pColor == Color.BLACK || (pColor.equals(new Color(128, 0, 128)) && closestPDistance < 45)) {
                            double dx = user.getX() - closestP.getX();
                            double dy = user.getY() - closestP.getY();
                            double v = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                            moveBy(closestPDistance < 55 || (double)user.getHp() / user.getMaxHp() < 0.3,7 * dx / v, 7 * dy / v);
                        } else {
                            double enemyPXV = closestP.getXSpeed();
                            double enemyPYV = closestP.getYSpeed();
                            double enemyPV = closestP.getSpeed();
                            moveBy(closestPDistance < 132 || (double)user.getHp() / user.getMaxHp() < 0.3,7 * enemyPYV / enemyPV, -7 * enemyPXV / enemyPV);
                        }
                    }
                }
            }

            if(game)
                user.draw(g2);
            else {
                framesToDisplayInstructions = 0;

                Rectangle back = new Rectangle(0, 0, fWidth, fHeight);
                g2.setColor(new Color(1f, 1f, 1f, 0.4f));
                g2.fill(back);

                g2.setPaint(Color.BLACK);
                g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 100));
                drawCenteredText(g2, "GAME OVER", -50);

                g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 40));
                drawCenteredText(g2, "SCORE: " + score, 80);

                g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 20));
                drawCenteredText(g2, "Enemies destroyed: " + enemiesDestroyed, 110);
                drawCenteredText(g2, "Bosses destroyed: " + bossesDestroyed, 140);

                int accuracy = 0;
                if(shotsFired!= 0 && shotsFired == shotsHit)
                    accuracy = 100;
                else if(shotsFired != 0)
                    accuracy = (int)(shotsHit * 100.0 / shotsFired);
                drawCenteredText(g2, "Acurracy: " + accuracy + "%", 170);

                String message = "YOU'RE A GOD";
                if(score < 100000)
                    message = "DIDN'T GET FAR, TRY AGAIN?";
                else if(score < 2000000)
                    message = "GREAT SCORE";
                g2.setPaint(new Color(200, 200, 200));
                g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, fWidth / 15));
                drawCenteredText(g2, message, -160);

                g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 20));
                drawCenteredText(g2, hint, 270);
            }

            //paintStats(g2);

            if(userLevel != 20) {
                Rectangle xpBar = new Rectangle(25, 11, (int)(600 * (((double)user.getXp()) / user.getMaxXp())), 3);
                g2.setPaint(new Color(0, 191, 255));
                g2.fill(xpBar);
                g2.draw(xpBar);
            } else if(userLevel == 20 && shotMode != 2) {
                Rectangle xpBar = new Rectangle(25, 11, (int)(600 * ((score) / 10000000.0)), 3);
                g2.setPaint(new Color(0, 191, 255));
                g2.fill(xpBar);
                g2.draw(xpBar);
            }

            Rectangle hpBar = new Rectangle(25, 15, (int)(600 * (((double)user.getHp()) / user.getMaxHp())), 30);
            g2.setPaint(new Color(210, 0, 0));
            g2.fill(hpBar);
            g2.draw(hpBar);

            //Rectangle staminaBar = new Rectangle(25, 60, (int)(600 * (((double)user.getStamina()) / user.getMaxStamina())), 30);
            Rectangle staminaBar = new Rectangle(25, 46, (int)(600 * (((double)user.getStamina()) / user.getMaxStamina())), 6);
            g2.setPaint(new Color(255, 215, 0));
            g2.fill(staminaBar);
            g2.draw(staminaBar);

            g2.setPaint(Color.BLACK);
            g2.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 25));
            g2.drawString("SCORE: " + score, 30, fHeight - 50);

            g2.drawString("HP: " + user.getHp() + " / " + user.getMaxHp(), 30, 40);

            //g2.drawString("Stamina: " + user.getStamina() + " / " + user.getMaxStamina(), 30, 85);

            g2.drawString((shotMode == 2) ? "Level: 21" : "Level: " + userLevel, fWidth - 123, 40);

            DecimalFormat df = new DecimalFormat("00");
            g2.drawString(df.format((int)time / 60) + ":" + df.format((int)time % 60), fWidth - 100, fHeight - 50);

            if(framesToDisplayInstructions > 0) {
                if(framesToDisplayInstructions <= 210) {
                    int colorNum = 210 - framesToDisplayInstructions;
                    g2.setColor(new Color(colorNum, colorNum, colorNum));
                }
                drawCenteredText(g2, "WASD to move, F or hold mouse to shoot", -70);
                drawCenteredText(g2, "V or hold SPACEBAR for quadruple speed. Uses stamina.", 70);
                drawCenteredText(g2, "ESC to pause and unpause.", 120);
                framesToDisplayInstructions--;
            }
            if(framesToDisplayShotMode > 0) {
                int colorNum = 210 - framesToDisplayShotMode;
                g2.setColor(new Color(colorNum, colorNum, colorNum));
                if(shotMode == 0)
                    drawCenteredText(g2, "Regular shots", 0);
                else if(shotMode == 1)
                    drawCenteredText(g2, "3 shots at 50% fire rate", 0);
                else if(shotMode == 2)
                    drawCenteredText(g2, "Experimental Lightsaber", 0);
                framesToDisplayShotMode--;
            }
            g2.setColor(Color.RED);
            if(framesToDisplayNewShotMode > 0) {
                g2.drawString("New firing mode, scroll to switch", fWidth - 400, 75);
                framesToDisplayNewShotMode--;
            }
            if(autoaim)
                drawCenteredText(g2, "AUTOAIM", - fHeight / 2 + 50);
            if(automove)
                drawCenteredText(g2, "AUTOMOVE", - fHeight / 2 + 90);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {

        }
    }

    /**
     * Draws text that is centered and shifted horizontally by heightFromCenter
     *
     * @param g2                        the graphics context
     * @param text                      the string to be drawn
     * @param heightFromCenter          the shift in horizontal position
     * @return                          void
     */
    public void drawCenteredText(Graphics2D g2, String text, int heightFromCenter) {
        g2.drawString(text, fWidth / 2 - (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() / 2, fHeight / 2 + heightFromCenter);
    }

    /**
     * Moves user object by an amount. Prevents going off screen. Adjusts stamina if user is boosting.
     *
     * @param isSpacebar  boolean for using spacebar to boost
     * @param dx          change in x
     * @param dy          change in y
     * @return            void
     */
    public void moveBy(boolean isSpacebar, double dx, double dy) {
        //         double newX = user.getX() + dx;
        //         double newY = user.getY() + dy;
        //         if(newX <= getWidth() - 25 && newX >= 25) 
        //             user.moveBy(dx, 0);
        //         if(newY <= getHeight() - 25 && newY >= 25) 
        //             user.moveBy(0, dy);
        if(isSpacebar && user.getStamina() > 0) {
            user.changeStamina(-1);
            dx *= 4;
            dy *= 4;
        }

        for(Enemy e : enemies)
            e.moveBy(-dx, -dy);
        for(Boss b : bosses)
            b.moveBy(-dx, -dy);
        for(Projectile p : userProjectiles)
            p.moveBy(-dx, -dy);
        for(Projectile p : enemyProjectiles)
            p.moveBy(-dx, -dy);
        grid.shift(-dx, -dy);
    }

    /**
     * Updates the known location of the mouse. Used to test shooting.
     * 
     * @param mouseX the x coordinate of the mouse
     * @param mouseY the y coordinate of the mouse
     * @return       void
     */
    public void updateMouse(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    /**
     * For testing movement keys. Updates which keys are pressed.
     *
     * @param left   boolean indicating if the left arrow key is pressed
     * @param right  boolean indicating if the right arrow key is pressed
     * @param up     boolean indicating if the up arrow key is pressed
     * @param down   boolean indicating if the down arrow key is pressed
     * @return void
     */
    public void updateKeys(boolean boosting, boolean left, boolean right, boolean up, boolean down, boolean firing, boolean autofire) {
        this.boosting = boosting;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.firing = firing;
        this.autofire = autofire;
    }

    /**
     * Updates the known size of the frame.
     * 
     * @param frameWidth    width of the frame
     * @param frameHeight   height of the frame
     * @return              void
     */
    public void updateSize(int frameWidth, int frameHeight) {
        fWidth = frameWidth;
        fHeight = frameHeight;
    }

    /**
     * Updates the player's score by dScore amount.
     * 
     * @param dScore the change in score
     * @return       void
     */
    public void updateScore(int dScore) {
        score += dScore;
    }

    /**
     * For testing movement and shooting. Displays which keys are pressed and the mouse coordinates.
     * Also displays game info.
     * @param g2  Graphics context
     * @return    void
     */
    public void paintStats(Graphics g2) {
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 12)); 
        g2.setColor(Color.BLACK);
        g2.drawString("Left: " + left, 125, 150);
        g2.drawString("Right: " + right, 125, 250);
        g2.drawString("Up: " + up, 125, 350);
        g2.drawString("Down: " + down, 125, 450);
        g2.drawString("Firing: " + firing, 125, 550);
        g2.drawString("Autofire: " + autofire, 125, 650);
        g2.drawString("MouseX: " + mouseX, 210, 150);
        g2.drawString("MouseY: " + mouseY, 210, 250);

        DecimalFormat df = new DecimalFormat("0,000,000,000");

        g2.drawString("Difficulty: " + df.format(difficulty) + "/ 2,147,400,000", 210, 350);
        g2.drawString("Projectiles: " + (enemyProjectiles.size() + userProjectiles.size()), 210, 450);
        g2.drawString("Enemies: " + enemies.size(), 210, 550);
    }

    /**
     * Shoots user projectiles and adjusts the fire rate.
     * 
     * @param mouseX      the x-coordinate of the mouse
     * @param mouseY      the y-coordinate of the mouse
     * @param frameDelay  shoots a projectile every frameDelay frames
     * @return            void
     */
    public void userShoot(double mouseX, double mouseY) {
        if(game) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            if(shotMode == 0) {
                int frameDelay = 10 - (user.getLevel() - 2) / 2;
                shotClock++;
                shotClock %= frameDelay;
                if(shotClock == 0) {
                    //                 double userX = user.getX();
                    //                 double userY = user.getY();
                    //                 Projectile middleProjectile = new Projectile(Color.GREEN, userX, userY, mouseX, mouseY, 45);
                    //                 double angle = middleProjectile.getAngle() * 180 / Math.PI;
                    //                 userProjectiles.add(middleProjectile);
                    //                 userProjectiles.add(new Projectile(Color.GREEN, userX, userY, angle + 15, 45));
                    //                 userProjectiles.add(new Projectile(Color.GREEN, userX, userY, angle - 15, 45));

                    //                 userProjectiles.add(new Projectile(50, 10, 1, Color.GREEN, user.getX(), user.getY(), mouseX, mouseY, 15 + user.getLevel() * 1.5));
                    //                 userProjectiles.add(new Projectile(-50, 10, 1, Color.GREEN, user.getX(), user.getY(), mouseX, mouseY, 15 + user.getLevel() * 1.5));
                    userProjectiles.add(new Projectile(10, 1, Color.GREEN, user.getX(), user.getY(), mouseX, mouseY, 15 + user.getLevel() * 1.5));
                    shotsFired++;
                }
            } else if(shotMode == 1) {
                int frameDelay = 10 - (user.getLevel() - 2) / 3 + 2;
                shotClock++;
                shotClock %= frameDelay;
                if(shotClock == 0) {
                    int range = (int)(fWidth >> 1);
                    Projectile middleProjectile = new Projectile(true, range, 10, 1, Color.GREEN, user.getX(), user.getY(), mouseX, mouseY, 15 + user.getLevel() * 1.5);
                    userProjectiles.add(middleProjectile);
                    double angle = middleProjectile.getAngle() * 180 / Math.PI;
                    double speed = 15 + user.getLevel() * 1.5;
                    userProjectiles.add(new Projectile(range, true, 10, 1, Color.GREEN, user.getX(), user.getY(), angle + 18, speed));
                    userProjectiles.add(new Projectile(range, true, 10, 1, Color.GREEN, user.getX(), user.getY(), angle - 18, speed));
                    shotsFired += 3;
                }
            } else if (shotMode == 2) {
                int frameDelay = 1;
                shotClock++;
                shotClock %= frameDelay;
                if(shotClock == 0)
                    for(int i = 10; i < 26; i++) {
                        userProjectiles.add(new Projectile(true, 110, 10, 1, new Color(99, 184, 255), user.getX(), user.getY(), mouseX, mouseY, i));
                        shotsFired++;;
                    } 
            } else if (shotMode == 3) {
                int frameDelay = 1;
                shotClock++;
                shotClock %= frameDelay;
                if(shotClock == 0) {
                    double speed = 15 + user.getLevel() * 1.5;
                    userProjectiles.add(new Projectile(20, 10, 2, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()), user.getX(), user.getY(), mouseX, mouseY, speed));
                    userProjectiles.add(new Projectile(-20, 10, 2, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()), user.getX(), user.getY(),  mouseX, mouseY, speed));
                    shotsFired += 2;
                }
            }
        }
    }

    /**
     * Shoots enemy projectiles and adjusts the fire rate.
     * 
     * @param enemyIndex   the enemy index in the enemies ArrayList 
     * @param enemyX       the x-coordinate of the enemy
     * @param enemyY       the y-coordinate of the enemy
     * @param frameDelay   shoots a projectile every frameDelay frames
     * @return             void
     */
    public void enemyShoot(int enemyIndex, double enemyX, double enemyY, int frameDelay) {
        int i = (enemyShotClock.get(enemyIndex) + 1) % (frameDelay - (difficulty / 40000000));
        enemyShotClock.set(enemyIndex, new Integer(i));

        if(i == 0)
            if(enemies.get(enemyIndex).getName().equals("e1"))
                enemyProjectiles.add(new Projectile(10, 1, Color.RED, enemyX, enemyY, user.getX(), user.getY(), 5 + difficulty / 400000000));
            else {
                Projectile middleProjectile = new Projectile(true, 100, 10, 5, Color.RED, enemyX, enemyY, user.getX(), user.getY(), 5 + difficulty / 400000000);
                enemyProjectiles.add(middleProjectile);
                double angle = middleProjectile.getAngle() * 180 / Math.PI;
                for(int p = 1; p < 6; p++)
                    enemyProjectiles.add(new Projectile(100, true, 10, 1, Color.RED, enemyX, enemyY, angle + p * 360 / 6, 5 + difficulty / 400000000)); 
            }
    }

    /**
     * Shoots boss projectiles and adjusts the fire rate.
     * 
     * @param bossIndex    the boss index in the boss ArrayList 
     * @param bossX        the x-coordinate of the boss
     * @param bossY        the y-coordinate of the boss
     * @param frameDelay   shoots a projectile every frameDelay frames
     * @return             void
     */
    public void bossShoot(int bossIndex, double bossX, double bossY, int frameDelay) {
        int i = ((bossShotClock.get(bossIndex) + 1) % frameDelay);
        bossShotClock.set(bossIndex, new Integer(i));

        if (bosses.get(bossIndex).getName() .equals("Justin Liu") && i == 0) {
            int speed = 5 + difficulty / 400000000;
            double angle = (new Projectile(13, 3, Color.ORANGE, bossX, bossY, user.getX(), user.getY(), speed)).getAngle() * 180 / Math.PI;
            for(int a = -20; a <= 20; a += 10)
                enemyProjectiles.add(new Projectile(13, 3, Color.ORANGE, bossX, bossY, angle + a, speed));
        } else if(bosses.get(bossIndex).getName() .equals("Thanatcha Panpairoj") && i == 0) {
            lastShotAngle -= 4;
            lastShotAngle %= 360;
            for(int p = 0; p < 6; p++)
                enemyProjectiles.add(new Projectile(15, 5, new Color(128, 0, 128), bossX, bossY, 360 / 6 * p + lastShotAngle, 5));
        } else if(bosses.get(bossIndex).getName() .equals("Jack Weng") && i == 0) {
            int speed = 2 + difficulty / 600000000;
            double angle = (new Projectile(13, 15, Color.PINK, bossX, bossY, user.getX(), user.getY(), speed)).getAngle() * 180 / Math.PI;
            for(double a = -12; a <= 12; a += 2.4) 
                enemyProjectiles.add(new Projectile(13, 3, Color.PINK, bossX, bossY, angle + a, speed));
        } else if (bosses.get(bossIndex).getName() .equals("James Gosling") && i == 0) {
            int speed = 5 + difficulty / 400000000;
            for(int p = 0; p < 5; p++)
                enemyProjectiles.add(new Projectile(16, 6, Color.BLACK, bossX, bossY, (int)(Math.random() * 360), speed));
        } else if (bosses.get(bossIndex).getName() .equals("Ms Jaime") && i == 0) {
            enemyProjectiles.add(new Projectile(30, 14, 4, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()), bossX, bossY, user.getX(), user.getY(), 10));
            enemyProjectiles.add(new Projectile(-30, 14, 4, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()), bossX, bossY, user.getX(), user.getY(), 10));
        }
    }

    /**
     * Spawns a random enemy according to the current difficulty of the game.
     * 
     * @param maxBosses    the maximum currently allowed number of bosses
     * @param maxEnemies   the maximum currently allowed number of enemies
     * @param frameDelay   number of frames before spawning
     * @return             void
     */
    public void spawnEnemy(int maxBosses, int maxEnemies, int frameDelay) {
        if(game) {
            maxEnemies += difficulty / 50000000;
            enemyClock++;
            enemyClock %= (frameDelay - (difficulty / 10000000));
            if(enemyClock == 0 && enemies.size() < maxEnemies) {
                int x = 0;
                int y = 0;
                int ranNum = (int)(Math.random() * 4) + 1;//random number 1-4
                if(ranNum == 1)//spawns north
                    x = (int)(Math.random() * fWidth);
                else if(ranNum == 2) {//spawns south
                    x = (int)(Math.random() * fWidth);
                    y = fHeight - 30;
                } else if(ranNum == 3) {//spawns east
                    y = (int)(Math.random() * fHeight);
                    x = fWidth;
                } else// spawns west
                    y = (int)(Math.random() * fHeight);

                if(difficulty > 1000000000 && (int)(Math.random() * 100) > 79)
                    enemies.add(new Enemy("e2", 125, fWidth, fHeight, x, y, new Color(255, 255, 51), 9 + difficulty / 400000000));
                else 
                    enemies.add(new Enemy("e1", 125, fWidth, fHeight, x, y, Color.BLACK, 3 + difficulty / 200000000));

                enemyShotClock.add(0);
            }

            if((int)(Math.random() * difficulty) > 1200000000 && bosses.size() < maxBosses + score / 100000) {
                int bossSpawnFrameDelay = (user.getLevel() == 20) ? 155:180;
                if((int)(time / 60) < 150)
                    bossSpawnFrameDelay -= (int)(time / 60);

                if((int)(Math.random() * bossSpawnFrameDelay) == 0 && (int)(Math.random() * (2147400001 - difficulty) / 100000000) < 2) {
                    boolean JL = false;
                    boolean TP = false;
                    boolean J = false;
                    boolean JG = false;
                    boolean P = false;
                    boolean MJ = false;

                    for(int b = 0; b < bosses.size(); b++) {
                        String bossName = bosses.get(b).getName();
                        if(bossName .equals("Justin Liu") )
                            JL = true;
                        else if (bossName .equals("Thanatcha Panpairoj") )
                            TP = true;
                        else if (bossName .equals("Jack Weng"))
                            J = true;
                        else if (bossName .equals("James Gosling"))
                            JG = true;
                        else if (bossName .equals("Points"))
                            P = true;
                        else if (bossName .equals("Ms Jaime"))
                            MJ = true;
                    }

                    boolean spawned = false;
                    while(!spawned) {
                        int bossNum = (int)(Math.random() * 12) + 1;
                        if((bossNum == 1 || bossNum == 2 || bossNum == 3) && !JL) {
                            bosses.add(new JustinLiu(40 + score / 6000, fWidth, fHeight));
                            spawned = true;
                        } else if((bossNum == 4 || bossNum == 5 || bossNum == 6) && !TP) {
                            bosses.add(new ThanatchaPanpairoj(40 + score / 6000, fWidth, fHeight));
                            spawned = true;
                        } else if((bossNum == 7 || bossNum == 8 || bossNum == 9) && !J) {
                            bosses.add(new JackWeng(40 + score / 6000, fWidth, fHeight));
                            spawned = true;
                        } else if (bossNum == 10 && !JG) {
                            bosses.add(new JamesGosling(45 + score / 4500, fWidth, fHeight));
                            spawned = true;
                        } else if (bossNum == 11 && !P) {
                            bosses.add(new Points(45 + score / 4500, fWidth, fHeight));
                            spawned = true;
                        } else if (bossNum == 12 && !MJ) {
                            bosses.add(new MsJaime(45 + score / 4500, fWidth, fHeight));
                            spawned = true;
                        } else if (JL && TP && J&& JG && P && MJ)
                            spawned = true;
                    }

                    bossShotClock.add(33);
                }
            }
        }
    }

    /**
     * Returns whether the game is in progress.
     * 
     * @return whether game is on
     */
    public boolean isGame() {
        return game;
    }

    /**
     * Returns the current score.
     * 
     * @return the current player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the current stamina of the user.
     * 
     * @return stamina of user
     */
    public int getUserStamina()
    {
        return user.getStamina();
    }

    /**
     * Returns the current shot mode of the user.
     * 
     * @return user's shooting mode
     */
    public int getShotMode() {
        return shotMode;
    }

    /**
     * Changes to different shooting modes. Also sets the number of frames to display the shot mode.
     * 
     * @param mode   the mode to change to
     * @return       void
     */
    public void changeShotMode(int mode) {
        if(mode == 1 && user.getLevel() > 9)
            shotMode = 1;
        else if(mode == 2)
            shotMode = 2;
        else if(mode == 3)
            shotMode = 3;
        else
            shotMode = 0;
        framesToDisplayShotMode = 150;
    }

    public void autoaim(boolean autoaim) {
        this.autoaim = autoaim;
    }

    public boolean getAutoaim() {
        return autoaim;
    }

    public void automove(boolean automove) {
        this.automove = automove;
    }

    public boolean getAutomove() {
        return automove;
    }

    /**
     * Changes the starting difficulty of the game.
     * 
     * @param startingDifficulty   the starting difficulty to be used in the game
     * @return                     void
     */
    public void changeStartingDifficulty(int startingDifficulty) {
        this.startingDifficulty = startingDifficulty;
    }

    /**
     * Restarts the game. This creates a new user object and brand new ArrayLists for the enemies and projectiles.
     * Sets statistics back to zero, and redisplays the instructions.
     * 
     * @return void
     */
    public void restart() {
        user = new User(25, fWidth >> 1, fHeight >> 1, Color.BLUE, 6);
        score = 0;

        framesToDisplayInstructions = 600;
        framesToDisplayNewShotMode = 0;

        shotClock = 0;
        enemyClock = 0;
        previousTCount = 0;
        time = 0;
        lastShotAngle = 0;
        shotsFired = 0;
        shotsHit = 0;
        shotMode = 0;

        int hintNumber = (int)(Math.random() * 7) + 1;
        if(hintNumber == 1)
            hint = "Use the space bar to speed up and dodge projectiles.";
        else if (hintNumber == 2)
            hint = "The space bar costs stamina to use, which is shown by the yellow bar.";
        else if (hintNumber == 3)
            hint = "Level 20 is NOT the last level. Get 10,000,000 points to reach Level 21";
        else if (hintNumber == 4)
            hint = "You unlock a special weapon at Level 21. But you'll never get there.";
        else if (hintNumber == 5)
            hint = "Enemies spawn, move and shoot faster as the game goes on.";
        else if (hintNumber == 6)
            hint = "The blue bar represnts the xp needed to get to the next level.";
        else if (hintNumber == 7)
            hint = "You can safely hide inside the 'points' boss.";
        //         startingDifficulty = 2147400000;
        //         for(int i = 1; i < 20; i++)
        //             user.addXp(500000);//sets the user level to max(for testing)
        //         user.changeHp(200);
        //         user.changeStamina(10000);
        //         score = 100000;
        // 
        //         startingDifficulty = 1000000000;

        difficulty = startingDifficulty;

        userProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();
        enemies = new ArrayList<Enemy>();
        bosses = new ArrayList<Boss>();
        enemyShotClock = new ArrayList<Integer>();
        bossShotClock = new ArrayList<Integer>();

        game = true;
        autoaim = false;
        automove = false;
    }
}
