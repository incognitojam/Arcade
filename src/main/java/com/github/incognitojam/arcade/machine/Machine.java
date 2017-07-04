package com.github.incognitojam.arcade.machine;

import com.github.incognitojam.arcade.HomeApp;
import com.github.incognitojam.arcade.display.Display;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class Machine {

    private final short machineId;
    private final UUID ownerUniqueId;

    private Context context;
    private Display display;

    public Machine(short machineId, UUID ownerUniqueId) {
        this.machineId = machineId;
        this.ownerUniqueId = ownerUniqueId;
    }

    public void onCreate() {
        if (this.context.getApp() != null) {
            this.context.getApp().onCreate();
            this.context.getApp().setCreated(true);
        }
    }

    public void onUpdate() {
        if (this.context.isDisplayConnected() && this.context.getApp() != null) {
            if (!this.context.getApp().isCreated()) {
                onCreate();
            } else {
                this.context.onUpdate();
            }
        }
    }

    public void onDispose() {
        if (this.context.getApp() != null) {
            this.context.getApp().onDispose();
        }
    }

    public short getMachineId() {
        return this.machineId;
    }

    public UUID getOwnerUniqueId() {
        return this.ownerUniqueId;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Display getDisplay() {
        return this.display;
    }

    public void setDisplay(Display display) {
        if (this.display.equals(display)) return;

        if (this.display != null) {
            BufferedImage buffer = this.display.getBuffer();
            display.drawImage(0, 0, buffer);
            display.setColor(this.display.getColor());

            this.display.clear(Color.BLACK);
            this.display.setColor(Color.WHITE);
            this.display.drawText(15, 15, "No signal");

            this.display = display;
            this.context.setApp(new HomeApp())
            this.context.getApp().setDisplay(display);
        } else {

        }
    }

    @Override
    public String toString() {
        return "Machine{" +
            "machineId=" + machineId +
            ", ownerUniqueId=" + ownerUniqueId +
            '}';
    }
}
