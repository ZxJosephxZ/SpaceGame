package main;

import graphics.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Window extends JFrame implements Runnable{

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private Canvas canvas;
    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    private final int FPS = 60;
    private double TARGETTIME = 1000000000/FPS;
    private double delta = 0;
    private int AVERAGEFPS = FPS;

    public Window()
    {
        setTitle("Space ship game");
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        canvas.setMaximumSize(new Dimension(WIDTH,HEIGHT));
        canvas.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
    }

    public static void main(String[] args)
    {
        new Window().start();
    }


    private void update()
    {

    }

    private void draw()
    {
        bs = canvas.getBufferStrategy();

        if (bs == null)
        {
            canvas.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //--------------------------------
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.drawImage(Assets.player,100,100,null);
        g.drawString(""+AVERAGEFPS,10,20);
        //--------------------------------
        g.dispose();
        bs.show();
    }

    public void init() {
        Assets.init();
    }

    @Override
    public void run() {

        long now = 0;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;

        init();
        while(running)
        {
            now = System.nanoTime();
            delta += (now - lastTime)/TARGETTIME;
            time += (now - lastTime);
            lastTime = now;
            if (delta >= 1)
            {
                update();
                draw();
                delta --;
                frames++;
            }
            if (time >= 1000000000)
            {
                AVERAGEFPS = frames;
                frames = 0;
                time = 0;
            }

        }

        stop();
    }

    private void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    private void stop()
    {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

