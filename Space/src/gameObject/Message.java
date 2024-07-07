package gameObject;

import graphics.Text;
import math.Vector2D;
import states.GameState;

import java.awt.*;

public class Message {
    private GameState gameState;
    private String text;
    private float alpha;
    private Vector2D position;
    private Color color;
    private boolean center;
    private boolean fade;
    private Font font;
    private final float deltaAlpha = 0.01f;

    public Message(Vector2D position, boolean fade, String text, Color color, boolean center,
                   Font font, GameState gameState) {
        this.gameState = gameState;
        this.text = text;
        this.position = position;
        this.color = color;
        this.center = center;
        this.fade = fade;
        this.font = font;

        if(fade)
            alpha = 1;
        else
            alpha = 0;
    }

    public void draw(Graphics2D g2d)
    {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Text.drawText(g2d, text, position, center, color, font);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        position.setY(position.getY() - 1);
        if(fade)
            alpha -= deltaAlpha;
        else
            alpha += deltaAlpha;

        if(fade && alpha < 0 || !fade && alpha > 1)
            gameState.getMessages().remove(this);
    }
}