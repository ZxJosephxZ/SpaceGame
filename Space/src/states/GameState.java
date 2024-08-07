package states;

import gameObject.*;
import graphics.Animation;
import graphics.Assets;
import graphics.Sound;
import graphics.Text;
import io.JSONParser;
import io.ScoreData;
import math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class GameState extends State{

    public static final Vector2D PLAYER_START_POSITION = new Vector2D(Constants.WIDTH/2 - Assets.player.getWidth()/2,
            Constants.HEIGHT/2 - Assets.player.getHeight()/2);
    private Player player;
    private ArrayList<MovingObject> movingObject = new ArrayList<>();
    private ArrayList<Animation> explosions = new ArrayList<Animation>();
    private ArrayList<Message> messages = new ArrayList<Message>();
    private int meteors;
    private int lives=3;
    private int waves = 1;


    //Preferible crear una clase con estos datos (en este caso al ser pequeño el programa no hay necesidad)
    private int score = 0;
    private Sound backgroundMusic;

    private Cronometer ufoSpawner;
    private Cronometer gameOverTimer;
    private boolean gameOver;

    public GameState()
    {
        player = new Player(PLAYER_START_POSITION, new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, this);

        gameOverTimer = new Cronometer();
        gameOver = false;
        movingObject.add(player);
        meteors = 1;

        startWave();
        backgroundMusic = new Sound(Assets.backgroundMusic);
        backgroundMusic.loop();
        backgroundMusic.changeVolume(-10.0f);
        ufoSpawner = new Cronometer();
        ufoSpawner.run(Constants.UFO_SPAWN_RATE);
    }

    public void addScore(int value, Vector2D position)
    {
        score += value;
        messages.add(new Message(position, true, "+"+value+" score", Color.WHITE, false, Assets.fontMed));
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void divideMeteor(Meteor meteor)
    {
        Size size = meteor.getSize();

        BufferedImage[] textures = size.textures;
        Size newSize = null;

        switch (size)
        {
            case BIG:
                newSize = Size.MED;
                break;
            case MED:
                newSize = Size.SMALL;
                break;
            case SMALL:
                newSize = Size.TINY;
                break;
            default:
                return;
        }

        for(int i = 0; i < size.quantitty; i++)
        {
            movingObject.add(new Meteor(
                    meteor.getPosition(),
                    new Vector2D(0, 1).setDirection(Math.random()*Math.PI*2),
                    Constants.METEOR_VEL*Math.random() + 1,
                    textures[(int)(Math.random()*textures.length)],
                    this,
                    newSize
                ));
        }
    }

    private void startWave()
    {
        messages.add(new Message(new Vector2D(Constants.WIDTH/2, Constants.HEIGHT/2),false,
                "WAVE "+waves, Color.WHITE, true, Assets.fontBig));

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
        waves++;
    }

    public void playExplosion(Vector2D position)
    {
        explosions.add(new Animation(
                Assets.exp,
                50,
                position.subtract(new Vector2D(Assets.exp[0].getWidth()/2,Assets.exp[0].getHeight()/2))
        ));
    }

    private void spawnUfo()
    {
        int rand = (int) (Math.random()*2);

        double x = rand == 0 ? (Math.random()*Constants.WIDTH) : 0;
        double y = rand == 0 ? 0 : (Math.random()*Constants.HEIGHT);

        ArrayList<Vector2D> path = new ArrayList<Vector2D>();

        double posX, posY;

        posX = Math.random()*Constants.WIDTH/2;
        posY = Math.random()*Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random()*(Constants.WIDTH/2) + Constants.WIDTH/2;
        posY = Math.random()*Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random()*Constants.WIDTH/2;
        posY = Math.random()*(Constants.HEIGHT/2) + Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random()*(Constants.WIDTH/2) + Constants.WIDTH/2;
        posY = Math.random()*(Constants.HEIGHT/2) + Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));

        movingObject.add(new Ufo(
                new Vector2D(x, y),
                new Vector2D(),
                Constants.UFO_MAX_VEL,
                Assets.player1,
                path,
                this
        ));
    }

    public void update()
    {
        for(int i = 0; i < movingObject.size(); i++)
        {
            MovingObject mo = movingObject.get(i);
            mo.update();
            if(mo.isDead())
            {
                movingObject.remove(i);
                i--;
            }
        }

        for(int i = 0; i < explosions.size(); i++)
        {
            Animation anim = explosions.get(i);
            anim.update();
            if(!anim.isRunning())
            {
                explosions.remove(i);
            }
        }


        if(gameOver && !gameOverTimer.isRunning())
        {
            try{
                ArrayList<ScoreData> dataList = JSONParser.readFile();
                dataList.add(new ScoreData(score));
                JSONParser.writeFile(dataList);
            }catch (IOException e)
            {
                e.printStackTrace();
            }

            State.changeState(new MenuState());
        }
        if(!ufoSpawner.isRunning())
        {
            ufoSpawner.run(Constants.UFO_SPAWN_RATE);
            spawnUfo();
        }
        gameOverTimer.update();
        ufoSpawner.update();

        for(int i = 0; i < movingObject.size(); i++)
        {
            if(movingObject.get(i) instanceof Meteor)
            {
                return;
            }
        }

        startWave();
    }

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for(int i = 0; i < messages.size(); i++)
        {
            messages.get(i).draw(g2d);
            if(messages.get(i).isDead())
            {
                messages.remove(i);
            }
        }

        for(int i = 0; i < movingObject.size(); i++)
        {
            movingObject.get(i).draw(g);
        }

        for(int i = 0; i < explosions.size(); i++)
        {
            Animation anim = explosions.get(i);
            g2d.drawImage(anim.getCurrentFrame(), (int)anim.getPosition().getX() , (int)anim.getPosition().getY(), null);
        }
        drawScore(g);
        drawLives(g);
    }

    private void drawLives(Graphics g)
    {
        if(lives < 1)
            return;
        Vector2D livePosition = new Vector2D(25, 25);
        g.drawImage(Assets.others, (int)livePosition.getX(), (int)livePosition.getY(), null);
        g.drawImage(Assets.numbers[10], (int)livePosition.getX()+40,
                (int)livePosition.getY()+5, null);
        String liveToString = Integer.toString(lives);
        Vector2D pos = new Vector2D(livePosition.getX(), livePosition.getY());
        for(int i = 0; i < liveToString.length(); i++)
        {
            int number = Integer.parseInt(liveToString.substring(i, i+1));
            if(number <= 0)
            {
                break;
            }
            g.drawImage(Assets.numbers[number],
                    (int)pos.getX()+60,
                    (int)pos.getY()+5,
                    null);
            pos.setX(pos.getX()+20);
        }
    }

    private void drawScore(Graphics g)
    {
        Vector2D pos = new Vector2D(1800,25);
        String scoreToString = Integer.toString(score);
        for(int i = 0; i < scoreToString.length(); i++)
        {
            g.drawImage(Assets.numbers[Integer.parseInt(scoreToString.substring(i, i+1))],
                    (int)pos.getX(), (int)pos.getY(), null);
            pos.setX(pos.getX() + 20);
        }
    }

    public ArrayList<MovingObject> getMovingObject() {
        return movingObject;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean subtracLife(){
        lives--;
        return lives > 0;
    }

    public void gameOver()
    {
        Message gameOverMsg = new Message(
                PLAYER_START_POSITION,
                true,
                "GAME OVER",
                Color.WHITE,
                true,
                Assets.fontBig
        );
        this.messages.add(gameOverMsg);
        gameOverTimer.run(Constants.GAME_OVER_TIME);
        gameOver = true;
    }

}
