package graphics;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage player;
    public static BufferedImage player1;
    public static BufferedImage speed;
    public static BufferedImage blueLaser, greenLaser, redLaser;

    public static BufferedImage[] bigs = new BufferedImage[4];
    public static BufferedImage[] meds = new BufferedImage[2];
    public static BufferedImage[] smalls = new BufferedImage[2];
    public static BufferedImage[] tinies = new BufferedImage[2];

    public static BufferedImage[] exp = new BufferedImage[9];

    public static BufferedImage[] numbers = new BufferedImage[11];

    public static BufferedImage others;

    public static Font fontBig;
    public static Font fontMed;

    public static Clip backgroundMusic, explosion, playerLoose, playerShoot, ufoShoot;

    public static BufferedImage greyBtn, blueBtn;

    public static boolean loaded = false;
    public static float count  = 0;
    //Esto dependera de cuantos objetos cargues
    public static float MAX_COUNT = 46;

    //Nos permite cargar la imagen en una variable para su posterior leida y uso
    public static void init()
    {
        player = loadImage("/ships/player.png");
        speed = loadImage("/effects/fire08.png");
        blueLaser = loadImage("/lasers/laserBlue01.png");
        greenLaser = loadImage("/lasers/laserGreen11.png");
        redLaser = loadImage("/lasers/laserRed01.png");
        others = loadImage("/others/life.png");
        fontBig = loadFont("/fonts/futureFont.ttf", 62);
        fontMed = loadFont("/fonts/futureFont.ttf", 40);

        for(int i = 0; i < bigs.length; i++)
        {
            bigs[i] = loadImage("/meteors/big"+(i+1)+".png");
        }
        for(int i = 0; i < meds.length; i++)
        {
            meds[i] = loadImage("/meteors/med"+(i+1)+".png");
        }
        for(int i = 0; i < smalls.length; i++)
        {
            smalls[i] = loadImage("/meteors/small"+(i+1)+".png");
        }
        for(int i = 0; i < tinies.length; i++)
        {
            tinies[i] = loadImage("/meteors/tiny"+(i+1)+".png");
        }
        for(int i = 0; i < exp.length; i++)
        {
            exp[i] = loadImage("/explosion/"+(i)+".png");
        }
        player1 = loadImage("/ships/ufo.png");

        for(int i = 0; i < numbers.length; i++)
        {
            numbers[i] = loadImage("/numbers/"+(i)+".png");
        }
        backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
        playerShoot = loadSound("/sounds/playerShoot.wav");
        ufoShoot = loadSound("/sounds/ufoShoot.wav");
        playerLoose = loadSound("/sounds/playerLoose.wav");
        explosion = loadSound("/sounds/explosion.wav");

        greyBtn = loadImage("/ui/blue_button.png");
        blueBtn = loadImage("/ui/grey_button.png");
        loaded = true;
    }
    public static BufferedImage loadImage(String path)
    {
        count++;
        return Loader.ImageLoader(path);
    }
    public static Font loadFont(String path, int size)
    {
        count++;
        return Loader.loadFont(path, size);
    }
    public static Clip loadSound(String path)
    {
        count++;
        return Loader.loadSound(path);
    }
}
