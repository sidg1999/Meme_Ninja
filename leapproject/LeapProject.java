/* scoring system : wont draw string?
 * --blued out dead bodies 
 * --more memes
 * sounds: more research
 * --bouncers
 * levels?????
 * for kristy: resize the dead sprites
* NEW IDEA: give the memes a time to be displayed, where they get smaller and smoler until they dissapear. as levels increase, their speed incr
and they get smoler faster. if you get the 5 dead memes you're done
 */
package leapproject;

import com.leapmotion.leap.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LeapProject extends JPanel implements ActionListener{

    static int d = 0;//for drawing dead bodies
    static Target[] t = new Target[3];
    static int xVel = 1;
    static int zVel = 2;
    //60fps
    Timer tm = new Timer(15, this);
    static Controller controller = new Controller();
    static SampleListener listener = new SampleListener();
    static int x, z;
    static boolean annie = true;
    static int score = 0;
    Color col = new Color(255, 0, 0);
    ArrayList<Integer> xQueue = new ArrayList<Integer>();
    ArrayList<Integer> zQueue = new ArrayList<Integer>();

    boolean drawBG = false;
    boolean targKilled = true;

    int lvl = 1;
    int miss = 0;

    float vShrink = (float) 0.1;

    Target target;
    ArrayList<Target> qTargets = new ArrayList<Target>();

    public LeapProject() {
        tm.start();
    }

    public static void main(String args[]) {
        LeapProject t = new LeapProject();
        JFrame jf = new JFrame();
        jf.setLocation(500, 10);
        jf.setSize(700, 500);
        jf.setVisible(true);
        jf.add(t);
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);

        controller.addListener(listener);
    }

    @Override
    public void paintComponent(Graphics g) {

        if (miss == 5) {
            tm.stop();
            //red, size 20 bold in the bottom right corner
            g.setColor(Color.RED);
            Font font = new Font("Helvetica", Font.PLAIN, 40);
            g.setFont(font);
            //display score
            g.drawString(("GAME OVER"), 30, 60);
            Scanner sc = new Scanner(System.in);
            
            

        } else {
            //draws bg when told 
            if (drawBG == false) {
                drawBG(g);
                drawBG = true;
            }

            //creates a new target if the previous was killed
            if (targKilled == true) {
                createTarget(g);
                targKilled = false;
            }

            //check if the frame of the leap has collided with the target. 
            leapCollide(g);

            //check if the animated target hits one of the walls. if so, 'bounce' them so they move in the opposite direction
            wallCollide(g);

            //before you animate the target, check if it's already shrunk to 0
            //if so, the target will disappear and the user will have one miss
            if (target.getWidth() == 0 || target.getHeight() == 0) {
                miss++;
                targKilled = true;
            } else { //otherwise animate the target
                //animates the target
                target.animate(g, xVel, zVel, vShrink);
            }

            //draws all of the dead targets
            for (int i = 0; i < qTargets.size(); i++) {
                qTargets.get(i).kill(g);
            }

            //draws leading red line (from leap)
            col = new Color(255, 0, 0);
            g.setColor(col);
            g.fillOval(x, z, 5, 5);

            //if the size arraylists are not filled with positions
            if (xQueue.size() != 10) {
                //fill it with the position
                xQueue.add(x);
                zQueue.add(z);
            } else {
                //otherwise, they are.
                //remove the first elements in the queue and erase those circles
                //set the color of the dot to white
                col = new Color(102, 180, 235);
                //add the x,y positions to the end of the queue
                xQueue.add(x);
                zQueue.add(z);
                //if the first elements in the queue are equal to the x,y points, don't erase. otherwise erase that circle
                g.setColor(col);
                g.fillOval(xQueue.get(0), zQueue.get(0), 5, 5);
                //remove those dots from the queue
                xQueue.remove(0);
                zQueue.remove(0);
            }

            //outputs the score in the bottom left and the level on the bottom right
            //everytime it iterates, draw a blue square in place of the old number so it can draw the new number over it
            //102, 180, 235
            col = new Color(102,180,235);
            g.setColor(col);
            g.fillRect(60, 410, 50, 30);
            g.fillRect( 560, 410, 50, 30);
            g.fillRect( 660, 410, 50, 30);
            //red, size 20 bold in the bottom right corner
            g.setColor(Color.ORANGE);
            Font font = new Font("Helvetica", Font.PLAIN, 20);
            g.setFont(font);
            //display score
            g.drawString(("SCORE: " + score), 10, 430);
            //display level
            g.drawString(("LEVEL: " + lvl), 500, 430);
            //display num dead memes
            g.drawString(("MISS: " + miss), 600, 430);

            if (qTargets.size() == 5) {
                drawBG = false;
                //clear the dead targets list
                qTargets.clear();
                lvl++;
                vShrink += 0.1;
                xVel++;
                zVel++;
            }
        }

    }

    public void drawBG(Graphics g) {
        g.setColor(new Color(102, 180, 235));
        g.fillRect(0, 0, 700, 500);
    }

    public void createTarget(Graphics g) {
        //spawn a random target, with a random x,y position if a target is killed
        target = new Target((int) (Math.random() * 390 + 1), (int) (Math.random() * 350 + 1), ((int) (Math.random() * 4)) + 0);
        target.draw(g);
    }

    public void leapCollide(Graphics g) {

        int frameX, frameZ;
        int targX, targZ;
        float targW, targH;

        frameX = listener.getX();
        frameZ = listener.getZ();
        targX = target.getX();
        targZ = target.getZ();
        targW = target.getWidth();
        targH = target.getHeight();

        //to make a more realistic 'slash', have the user be within the 50x50 square within the center the 100x100 one
        //checks if the user's within the x limitations
        if ((frameX >= targX ) && frameX < (targX + targW)) {
            //if the listener is between the target's square y positions
            if (frameZ >= targZ && frameZ < (targZ + targH)) {
                System.out.println("Attack!");
                target.kill(g);
                qTargets.add(target);
                targKilled = true;
                System.out.println("TARGET X: " + target.getX() + " Z: " + target.getZ() + "\nPOINT x: " + listener.getX() + " z: " + listener.getZ());
                //increments their score
                score++;
            }
        }
    }

    public void wallCollide(Graphics g) {
        if (target.getX() < -10 || target.getX() > (700 - target.getWidth())) {
            xVel = -xVel;
        }
        if (target.getZ() < 5  || target.getZ() >= (500 - target.getHeight()-15)) {
            zVel = -zVel;
        }
    }

    public void actionPerformed(ActionEvent e) {
        listener.onFrame(controller);
        x = listener.getX();
        z = listener.getZ();
        repaint();
    }
    
}
