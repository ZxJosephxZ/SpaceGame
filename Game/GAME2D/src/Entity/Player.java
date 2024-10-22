package Entity;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static Utils.Constants.PlayerConstants.*;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler KeyH;
    BufferedImage imgPlayer;
    BufferedImage animations[][];
    int aniTick, aniIndex, aniSpeed = 15;
    int playerAction = IDLE;
    public Player(GamePanel gp, KeyHandler KeyH)
    {
        this.gp = gp;
        this.KeyH = KeyH;
        importImage();
        loadAnimations();
        setDefaultValues();
    }

    public void setDefaultValues()
    {
        x = 100;
        y = 100;
        speed = 4;
    }

    private void loadAnimations()
    {
        animations = new BufferedImage[4][4];
        for (int j = 0; j < animations.length;j++)
        {
            for (int i = 0; i < animations[j].length;i++)
            {
                animations[j][i] = imgPlayer.getSubimage(i*32,j*32,32,32);
            }
        }
    }

    private void importImage()
    {
        InputStream is = getClass().getResourceAsStream("/Player/spriteCompletoPlayer.png");
        try{
            imgPlayer = ImageIO.read(is);
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            try{
                is.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void update()
    {
        if (KeyH.upPressed == true)
        {
             y -= speed;
             playerAction = DOWN;
        }
        else if (KeyH.downPressed == true)
        {
            y += speed;
            playerAction = IDLE;
        }
        else if (KeyH.leftPressed == true)
        {
            x -= speed;
            playerAction = LEFT;
        }
        else if (KeyH.rightPressed == true)
        {
            x += speed;
            playerAction = RIGHT;
        }
    }

    private void updateAnimationTick()
    {
        aniTick++;
        if (aniTick >= aniSpeed)
        {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction))
            {
                aniIndex = 0;
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        updateAnimationTick();
        g2.drawImage(animations[playerAction][aniIndex], x,y,64,64,null);
    }

}
