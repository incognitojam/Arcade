package com.github.incognitojam.arcade.display;

import com.github.incognitojam.arcade.machine.Machine;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends MapRenderer implements IGraphics {

    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;

    private final Machine machine;
    private MapCanvas mapCanvas;

    private Graphics2D graphics;
    private BufferedImage buffer;
    private int pastBuffer;

    public Display(Machine machine, MapCanvas mapCanvas) {
        super(false);

        this.machine = machine;
        this.mapCanvas = mapCanvas;

        setup();
    }

    private void setup() {
        this.buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.graphics = this.buffer.createGraphics();
        clear(Color.BLACK);
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (isModified()) {
            canvas.drawImage(0, 0, this.buffer);
            this.pastBuffer = this.buffer.hashCode();
        }
    }

    public void dispose() {
        if (graphics != null) graphics.dispose();
    }

    private boolean isModified() {
        return this.pastBuffer == 0 || this.pastBuffer != this.buffer.hashCode();
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void clear(Color color) {
        this.graphics.setColor(Color.BLACK);
        this.graphics.drawRect(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void setColor(Color color) {
        this.graphics.setColor(color);
    }

    @Override
    public void drawPixel(int x, int y) {
        this.buffer.setRGB(x, y, this.graphics.getColor().getRGB());
    }

    @Override
    public void drawText(int x, int y, String text) {
        this.graphics.drawString(text, x, y);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        this.graphics.drawRect(x, y, width, height);
    }

    @Override
    public void drawFilledRect(int x, int y, int width, int height) {
        this.graphics.fillRect(x, y, width, height);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawImage(int x, int y, Image image) {
        this.graphics.drawImage(image, x, y, null);
    }

}
