package gameObject;

import graphics.Assets;

import java.awt.image.BufferedImage;

public enum Size {
    BIG(2, Assets.bigs), MED(2, Assets.smalls), SMALL(2, Assets.tinies), TINY(0, null);

    public int quantitty;

    public BufferedImage[] textures;

    private Size(int quantitty, BufferedImage[] textures)
    {
        this.quantitty = quantitty;
        this.textures = textures;
    }
}
