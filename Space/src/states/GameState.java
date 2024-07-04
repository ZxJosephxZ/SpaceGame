package states;

import gameObject.*;
import graphics.Assets;
import math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameState {

    private Player player;
    private ArrayList<MovingObject> movingObject = new ArrayList<>();
    private int meteors;

    public GameState()
    {
        player = new Player(new Vector2D(Constants.WIDTH/2 - Assets.player.getWidth()/2,
                Constants.HEIGHT/2 - Assets.player.getHeight()/2), new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, this);
        movingObject.add(player);
        meteors = 1;

        startWave();
    }

    private void startWave()
    {
        double x, y;

        for(int i = 0; i < meteors; i++)
        {
            x = i % 2 == 0 ? Math.random()*Constants.WIDTH : 0;
            y = i % 2 == 0 ? 0 : Math.random()*Constants.HEIGHT;

            BufferedImage texture = Assets.bigs[(int)(Math.random()*Assets.bigs.length)];
            movingObject.add(new Meteor(
                    new Vector2D(x, y),
                    new Vector2D(0, 1).setDirection(Math.random()*Math.PI*2),
                    Constants.METEOR_VEL*Math.random() + 1,
                    texture,
                    this,
                    Size.BIG
            ));
        }
        meteors++;
    }

    public void update()
    {
        for(int i = 0; i < movingObject.size(); i++)
        {
            movingObject.get(i).update();
        }
    }

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for(int i = 0; i < movingObject.size(); i++)
        {
            movingObject.get(i).draw(g);
        }
    }

    public ArrayList<MovingObject> getMovingObject() {
        return movingObject;
    }
}
