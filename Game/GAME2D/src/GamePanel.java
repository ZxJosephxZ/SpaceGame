import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //Pantalla
    final int originalTileSize = 32; //32*32px
    final int scale = 3;

    final int tileSize = originalTileSize; //escalado si quieres *scale
    final int maxScreenCol = 16; //ancho de escalado
    final int maxScreenRow = 12; //largo de escalado
    final int screenWidth = maxScreenCol * tileSize; //768px
    final int screenHeight = maxScreenRow * tileSize; //576px

    KeyHandler KeyH = new KeyHandler();
    Thread gameThread;

    //movimiento personaje
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

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

    @Override
    public void run() {
        while (gameThread != null)
        {
            //1. actualiza la informacion por posicion
            update();
            //2. dibuja la pantalla con los datos actualizados
            repaint();
        }
    }
    public void update()
    {
        if (KeyH.upPressed == true)
        {
            playerY -= playerSpeed;
        }
        else if (KeyH.downPressed == true)
        {
            playerY += playerSpeed;
        }
        else if (KeyH.leftPressed == true)
        {
            playerX -= playerSpeed;
        }
        else if (KeyH.rightPressed == true)
        {
            playerX += playerSpeed;
        }
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }
}
