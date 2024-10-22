package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //Pantalla
    final int originalTileSize = 16; //32*32px
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //escalado si quieres *scale
    final int maxScreenCol = 16; //ancho de escalado
    final int maxScreenRow = 12; //largo de escalado
    final int screenWidth = maxScreenCol * tileSize; //768px
    final int screenHeight = maxScreenRow * tileSize; //576px

    KeyHandler KeyH = new KeyHandler();
    Thread gameThread;

    int FPS = 60;

    //movimiento personaje
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    Player player = new Player(this, KeyH);

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
/* forma sencilla de ordenar los fps
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null)
        {
            //1. actualiza la informacion por posicion
            update();
            //2. dibuja la pantalla con los datos actualizados
            repaint();
            try{
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                if (remainingTime < 0)
                {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }*/
    public void update()
    {
        player.update();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.dispose();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1)
            {
                update();
                repaint();
                delta--;
            }
        }
    }
}
