package com.github.incognitojam.arcade.display;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class Display extends MapRenderer {

    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;

    private final short displayId;
    private final UUID ownerUniqueId;
    private final MapView mapView;

    Block attachedBlock;
    ItemFrame frame;
    Vector planeOrigin;
    Vector planeXAxis;
    Vector planeYAxis;

    private Graphics2D graphics;
    private BufferedImage buffer;
    private boolean invalidated;

    public Display(short displayId, UUID ownerUniqueId, MapView mapView) {
        super(false);

        this.displayId = displayId;
        this.ownerUniqueId = ownerUniqueId;
        this.mapView = mapView;
    }

    public void onCreate() {
        this.mapView.getRenderers().forEach(mapView::removeRenderer);
        this.mapView.addRenderer(this);

        this.buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.graphics = this.buffer.createGraphics();

        disconnect();
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (this.invalidated) {
            this.invalidated = false;
            canvas.drawImage(0, 0, this.buffer);
        }
    }

    public void disconnect() {
        clear(Color.BLACK);
        drawText(15, 15, "No signal!");
    }

    public void dispose() {
        if (this.graphics != null) this.graphics.dispose();
    }

    public short getDisplayId() {
        return this.displayId;
    }

    public UUID getOwnerUniqueId() {
        return this.ownerUniqueId;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setupFrame(ItemFrame frame) {
        if (this.frame != null && this.frame.getEntityId() == frame.getEntityId()) {
            return;
        }

        if (frame == null) {
            this.frame = null;
            this.attachedBlock = null;

            this.planeOrigin = null;
            this.planeXAxis = null;
            this.planeYAxis = null;

            return;
        }

        Location location = frame.getLocation();
        World world = location.getWorld();

        Block frameBlock = world.getBlockAt(location);
        BlockFace attachedFace = frame.getAttachedFace();
        Block attachedBlock = frameBlock.getRelative(attachedFace);

        // Debugging
        attachedBlock.setType(Material.DIAMOND_BLOCK);

        // Top left of the face, X axis vector pointing to top right, Z axis vector pointing to bottom left
        Vector planeOrigin, planeXAxis, planeYAxis;
        switch (attachedFace) {
            case NORTH: { // z-
                planeOrigin = new Vector(attachedBlock.getX(), attachedBlock.getY() + 1, attachedBlock.getZ() + (17f / 16f));
                planeXAxis = new Vector(1, 0, 0);
            }
            break;
            case SOUTH: { // z+
                planeOrigin = new Vector(attachedBlock.getX() + 1, attachedBlock.getY() + 1, attachedBlock.getZ() - (1f / 16f));
                planeXAxis = new Vector(-1, 0, 0);
            }
            break;
            case EAST: { // x+
                planeOrigin = new Vector(attachedBlock.getX() - (1f / 16f), attachedBlock.getY() + 1, attachedBlock.getZ());
                planeXAxis = new Vector(0, 0, 1);
            }
            break;
            default:
            case WEST: { // x-
                planeOrigin = new Vector(attachedBlock.getX() + (17f / 16f), attachedBlock.getY() + 1, attachedBlock.getZ() + 1);
                planeXAxis = new Vector(0, 0, -1);
            }
            break;
        }

        // This is always the same
        planeYAxis = new Vector(0, -1, 0);

        this.planeOrigin = planeOrigin;
        this.planeXAxis = planeXAxis;
        this.planeYAxis = planeYAxis;
        this.frame = frame;
        this.attachedBlock = attachedBlock;
    }

    public boolean isFrameSetup() {
        return this.frame != null;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public Block getAttachedBlock() {
        return attachedBlock;
    }

    public void clear(Color color) {
        setColor(color);
        drawFilledRect(0, 0, WIDTH, HEIGHT);
        setColor(Color.WHITE);
    }

    public void setColor(Color color) {
        this.graphics.setColor(color);
    }

    public void setColor(Color color, Runnable runnable) {
        final Color oldColor = this.graphics.getColor();
        this.graphics.setColor(color);
        runnable.run();
        this.graphics.setColor(oldColor);
    }

    public Color getColor() {
        return this.graphics.getColor();
    }

    public void drawPixel(int x, int y) {
        drawRect(x, y, 1, 1);
    }

    public Color getPixel(int x, int y) {
        return new Color(this.buffer.getRGB(x, y));
    }

    public void drawText(int x, int y, String text) {
        this.graphics.drawString(text, x, y);
        this.invalidated = true;
    }

    public void drawRect(int x, int y, int width, int height) {
        this.graphics.drawRect(x, y, width, height);
        this.invalidated = true;
    }

    public void drawFilledRect(int x, int y, int width, int height) {
        this.graphics.fillRect(x, y, width, height);
        this.invalidated = true;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        this.graphics.drawLine(x1, y1, x2, y2);
        this.invalidated = true;
    }

    public void drawImage(int x, int y, Image image) {
        this.graphics.drawImage(image, x, y, null);
        this.invalidated = true;
    }

    public void drawImage(Point point, Image image) {
        drawImage(point.x, point.y, image);
    }

    public BufferedImage getBuffer() {
        return this.buffer;
    }

    @Override
    public String toString() {
        return "Display{" +
                "displayId=" + displayId +
                ", ownerUniqueId=" + ownerUniqueId +
                ", invalidated=" + invalidated +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Display && ((Display) obj).displayId == this.displayId;
    }
}
