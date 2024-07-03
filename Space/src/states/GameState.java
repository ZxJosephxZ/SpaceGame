package states;

import gameObject.MovingObject;
import gameObject.Player;
import graphics.Assets;
import math.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class GameState {

    private Player player;
    private ArrayList<MovingObject> movingObject = new ArrayList<>();

    public GameState()
    {
        player = new Player(new Vector2D(100,500), new Vector2D(),7, Assets.player, this);
        movingObject.add(player);
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
        for(int i = 0; i < movingObject.size(); i++)
        {
            movingObject.get(i).draw(g);
        }
    }

    public ArrayList<MovingObject> getMovingObject() {
        return movingObject;
    }
}
