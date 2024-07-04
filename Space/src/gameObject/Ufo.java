package gameObject;

import math.Vector2D;
import states.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ufo extends MovingObject{

    private ArrayList<Vector2D> path;

    private Vector2D currentNode;
    private int index;
    private boolean following;

    public Ufo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture,
               ArrayList<Vector2D> path, GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);
        this.path = path;
        index = 0;
        following = true;
    }

    private Vector2D pathFollowing()
    {
        currentNode = path.get(index);

        double distanceToNode = currentNode.subtract(getCenter()).getMagnitud();
        if(distanceToNode < Constants.NODE_RADIUS)
        {
            index++;
            if(index >= path.size())
            {
                following = false;
            }
        }
        return seekForce(currentNode);
    }

    private Vector2D seekForce(Vector2D target)
    {
        Vector2D desiredVelocity = target.subtract(getCenter());
        desiredVelocity = desiredVelocity.normalize().scale(maxVel);
        return desiredVelocity.subtract(velocity);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void draw(Graphics g) {

    }
}
