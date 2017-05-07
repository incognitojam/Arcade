package com.github.incognitojam.arcade.display;

import java.awt.*;

public interface IGraphics {

    int getWidth();

    int getHeight();

    void clear(Color color);

    void setColor(Color color);

    void drawPixel(int x, int y);

    void drawText(int x, int y, String text);

    void drawRect(int x, int y, int width, int height);

    void drawFilledRect(int x, int y, int width, int height);

    void drawLine(int x1, int y1, int x2, int y2);

    void drawImage(int x, int y, Image image);

}
