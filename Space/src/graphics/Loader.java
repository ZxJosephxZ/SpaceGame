package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Loader {

    //Metodo que nos permite leer las imagenes requeridas y retornarla para su uso
    public static BufferedImage ImageLoader(String path)
    {
        try
        {
            return ImageIO.read(Loader.class.getResource(path));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
