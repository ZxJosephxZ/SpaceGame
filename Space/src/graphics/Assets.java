package graphics;

import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage player;
    public static BufferedImage speed;

    //Nos permite cargar la imagen en una variable para su posterior leida y uso
    public static void init()
    {
        player = Loader.ImageLoader("/ships/player.png");
        speed = Loader.ImageLoader("/effects/fire08.png");
    }
}
