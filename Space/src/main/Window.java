package main;

import gameObject.Constants;
import graphics.Assets;
import input.KeyBoard;
import states.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Window extends JFrame implements Runnable{


    private Canvas canvas;
    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    private final int FPS = 60;
    private double TARGETTIME = 1000000000/FPS;
    private double delta = 0;
    private int AVERAGEFPS = FPS;

    private GameState gameState;
    private KeyBoard keyBoard;

    public Window()
    {
        setTitle("Space ship game");
        //Definir el tamaño de la pantalla
        setSize(Constants.WIDTH,Constants.HEIGHT);
        //Habilita la opcion de cerrado de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Desactiva la opcion para maximizar o minizar la pantalla
        setResizable(false);
        //El posicionamiento a la hora de mostrar la pantalla
        setLocationRelativeTo(null);
        //Muestra la pantalla


        // canvas objeto que nos permite definir un recuadro para dibujar o capturar un input
        canvas = new Canvas();
        // keyboard es un objeto que nos permite manejar los input por teclado
        keyBoard = new KeyBoard();
        //Establece el tamaño preferido para el recuadro del canvas
        canvas.setPreferredSize(new Dimension(Constants.WIDTH,Constants.HEIGHT));
        //Establece el tamaño maximo
        canvas.setMaximumSize(new Dimension(Constants.WIDTH,Constants.HEIGHT));
        //Establece el tamaño minimo
        canvas.setMinimumSize(new Dimension(Constants.WIDTH,Constants.HEIGHT));
        //Nos permite interactuar con el canvas
        canvas.setFocusable(true);
        //Añadimos el canvas
        add(canvas);
        canvas.addKeyListener(keyBoard);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new Window().start();
    }


    private void update()
    {
        keyBoard.update();
        gameState.update();
    }

    private void draw()
    {
        //Obtiene la estrategia del buffer predefinido
        bs = canvas.getBufferStrategy();
        // Si esta null definimos los buffer
        if (bs == null)
        {
            //Creamos la estrategia del buffer para el manejo de imagenes para evitar el parpadeo
            canvas.createBufferStrategy(3);
            return;
        }
        //
        g = bs.getDrawGraphics();
        //--------------------------------
        //Establece el color en negro
        g.setColor(Color.BLACK);
        //Nos dibuja un rectangulo lleno
        g.fillRect(0,0,Constants.WIDTH,Constants.HEIGHT);
        //Pasamos los graficos a el objeto que tiene el metodo para dibujar (draw)
        gameState.draw(g);
        //Dibuja los string (parecido a un print pero es en un canvas)
        g.drawString(""+AVERAGEFPS,10,20);
        //--------------------------------
        //Limpia la pantalla
        g.dispose();
        //
        bs.show();
    }

    public void init() {
        Assets.init();
        gameState = new GameState();
    }


    //Hilo encargado de el dibujado y actualizacion por segundo (fps)

    @Override
    public void run() {
        //Logica para calcular los frames por segundo
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
        //Crea y inicializa el hilo
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    private void stop()
    {
        //Se encarga de detener el hilo
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

