package gameObject;

import graphics.Assets;
import graphics.Sound;
import input.KeyBoard;
import math.Vector2D;
import main.Window;
import states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends MovingObject{

    private Vector2D heading;
    private Vector2D acceleration;
    private boolean accelerating = false;
    private Cronometer fireRate;

    private boolean spawing, visible;
    private Cronometer spawnTime, flickerTime;

    private Sound shoot, loose;

    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);
        heading = new Vector2D(0, 1);
        acceleration = new Vector2D();
        fireRate = new Cronometer();
        spawnTime = new Cronometer();
        flickerTime = new Cronometer();
        shoot = new Sound(Assets.playerShoot);
        loose = new Sound(Assets.playerLoose);
    }

    @Override
    public void update() {

        if(!spawnTime.isRunning())
        {
            spawing = false;
            visible = true;
        }
        if(spawing)
        {
            if(!flickerTime.isRunning())
            {
                flickerTime.run(Constants.FLICKER_TIME);
                visible = !visible;
            }
        }

        if(KeyBoard.SHOOT && !fireRate.isRunning() && !spawing)
        {
            gameState.getMovingObject().add(0, new Laser(
                    getCenter().add(heading.scale(width)),
                    heading,
                    Constants.LASER_VEL,
                    angle,
                    Assets.redLaser,
                    gameState
            ));
            fireRate.run(Constants.FIRERATE);
            shoot.play();
        }

        if(shoot.getFramePosition() > 8500)
        {
            shoot.stop();
        }

        if (KeyBoard.RIGHT)
        {
            angle += Constants.DELTAANGLE;
        }
        if (KeyBoard.LEFT)
        {
            angle -= Constants.DELTAANGLE;
        }
        if (KeyBoard.UP)
        {
            acceleration = heading.scale(Constants.ACC);
            accelerating = true;
        }else
        {
            if (velocity.getMagnitud() != 0)
            {
                acceleration = (velocity.scale(-1).normalize()).scale(Constants.ACC/2);
            }
            accelerating = false;
        }

        velocity = velocity.add(acceleration);

        velocity = velocity.limit(maxVel);

        heading = heading.setDirection(angle - Math.PI/2);

        position = position.add(velocity);

        if(position.getX() > Constants.WIDTH)
        {
            position.setX(0);
        }
        if(position.getY() > Constants.HEIGHT)
        {
            position.setY(0);
        }

        if(position.getX() < 0)
        {
            position.setX(Constants.WIDTH);
        }
        if(position.getY() < 0)
        {
            position.setY(Constants.HEIGHT);
        }

        fireRate.update();
        spawnTime.update();
        flickerTime.update();
        collidesWith();
    }

    @Override
    public void Destroy()
    {
        spawing = true;
        spawnTime.run(Constants.SPAWING_TIME);
        loose.play();
        if(!gameState.subtracLife())
        {
            gameState.gameOver();
            super.Destroy();
        }
        resetValues();
    }

    private void resetValues()
    {
        angle = 0;
        velocity = new Vector2D();
        position = GameState.PLAYER_START_POSITION;
    }

    @Override
    public void draw(Graphics g) {

        if(!visible)
        {
            return;
        }

        Graphics2D g2d = (Graphics2D)g;
        AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() -20, position.getY()-12);
        at1.rotate(angle, width/2 + 20, height/2 +12);
        if(accelerating)
        {
            g2d.drawImage(Assets.speed, at1, null);
        }

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        at.rotate(angle, width/2, height/2);
        g2d.drawImage(texture, at, null);
    }

    public boolean isSpawing(){return spawing;}

}
