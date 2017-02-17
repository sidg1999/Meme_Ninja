/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leapproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author anish
 */
public class Target {

    public int rnd;
    public int x;
    public int z;
    public Image img;
    public String[] normSprite;
    public String[] dedSprite = new String[3];
    public boolean tf = false;
    public float width = 110;
    public float height;

    public Target() {
        x = 0;
        z = 0;
        normSprite = new String[]{"C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\doge.png",
            "C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\feels.png",
            "C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\pepe.png",
            "C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\dat.png"};
        dedSprite = new String[]{"C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\fdoge.png",
            "C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\ffeels.png",
            "C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\fpepe.png",
            "C:\\Users\\Kristy Xue Gao\\Documents\\Hackathons\\ECHacks\\LeapProject\\LeapProject\\src\\leapproject\\fdat.png"};
        // loadImage();
    }

    public Target(int x, int z, int rnd) {
        this();
        this.x = x;
        this.z = z;
        this.rnd = rnd;
        if (rnd == 0) {//doge
            height = 121;
        } else if (rnd == 1) {//feels
            height = 132;
        } else if (rnd == 2) {//eppe
            height = 95;
        }else if(rnd == 3){//boi
            height = 175;
        }
    }

    public void draw(Graphics g) {
        BufferedImage img = null;
        if (tf == false) {
            try {
                img = ImageIO.read(new File(normSprite[rnd]));
            } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
                System.out.println(e);
            }
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, x, z, (int)width, (int)height, null);
    }

    public void kill(Graphics g) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(dedSprite[rnd]));
            tf = true;
        } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
            System.out.println(e);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, x, z, (int)width, (int)height, null);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public void setWidth(float width){
        this.width = width;
    }
    public void setHeight(float height){
        this.height = height;
    }
    public void shrink(float vShrink){
        //if their heights will become negative if shrunk, simple set their heights to 0
        if(width-vShrink<0 || height-vShrink<0 ){
            this.setHeight(0);
            this.setWidth(0);
        }else{
        this.setWidth(width-vShrink);
        this.setHeight(height-vShrink);
        }
        
    }

    /*  private void setSurfaceSize() {
        Dimension d = new Dimension();
        d.width = (img.getWidth(null))/2;
        d.height = (img.getHeight(null)/2);
        setPreferredSize(d);
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);6
        g.drawImage(img, 0, 0, this); // see javadoc for more info on the parameters            
    }*/
    public void animate(Graphics g, int xVel, int zVel, float vShrink) {//the animation of the meme
        //draw a blue rect in place of the old sprite
        g.setColor(new Color(102, 180, 235));
        g.fillRect(x-5, z-5,(int)this.getWidth()+10, (int)this.getHeight()+10);
        
        //increment the x,y values
        x += xVel;
        z += zVel;
        //draw a smaller version of the last sprite
        this.shrink(vShrink);
        //draw sprite
        draw(g);
    }
}
