/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leapproject;

import com.leapmotion.leap.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SampleListener extends Listener {

    private static long lastFrameID = 0;
    private static int xPos[] = new int[3];
    private static int yPos[] = new int[3];
    private static int zPos[] = new int[3];

    private int x;
    private int z;
    private int xf;
    private int zf;

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {

        fingerCoord(controller);

        //checks if the index finger is above the middle finger in a 'gun' formation
        //gets the distance between the tip position of the index figner to TP of the middle finger
        double dist;
        int deltX;
        int deltZ;

        //distances between index finger and mid finger
        deltX = xPos[2] - xPos[1];
        deltZ = zPos[2] - zPos[1];
        dist = Math.sqrt(Math.pow(deltZ, 2) + Math.pow(deltX, 2));

        //if the distance between the index finger, mid finger is less than 3cm, they are touching
        if (dist <= 30) {
            //they are slashing
            xf += 250;
            zf += 250;
            //otherwise they are not slashing, set coordinates of fingers to 0
        } else {
            xf = 0;
            zf = 0;
        }

        //System.out.println(x + " " + z);
    }

    public void fingerCoord(Controller controller) {

        Frame frame = controller.frame();
        FingerList fingerList = frame.fingers();

        //finger list which goes from thumb to pinky
        Iterator<Finger> fingerIterator = fingerList.iterator();

        int totalX = 0, totalY = 0, totalZ = 0;

        //loops through thumb to middle finger
        for (int i = 0; i < 3; i++) {
            Finger finger = fingerIterator.next();
            Vector fingerPosition = finger.tipPosition();
            xPos[i] = (int) fingerPosition.getX();
            zPos[i] = (int) fingerPosition.getZ();
            //gets average of only middle and index fingers
            if (i > 0) {
                totalX += xPos[i];
                totalZ += zPos[i];
            }
        }
        xf = (totalX / 2);
        zf = totalZ / 2;
    }

    public int getX() {
        return xf;
    }

    public int getZ() {
        return zf;
    }
}
