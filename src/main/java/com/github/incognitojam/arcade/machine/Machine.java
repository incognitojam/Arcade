package com.github.incognitojam.arcade.machine;

import java.util.UUID;

public class Machine {

    private final int machineId;
    private final UUID ownerUniqueId;

    private MachineContext context;

    public Machine(int machineId, UUID ownerUniqueId, MachineContext context) {
        this.machineId = machineId;
        this.ownerUniqueId = ownerUniqueId;
        this.context = context;
    }

    public int getMachineId() {
        return this.machineId;
    }

    public UUID getOwnerUniqueId() {
        return this.ownerUniqueId;
    }

    public MachineContext getContext() {
        return this.context;
    }

    public void setContext(MachineContext context) {
        this.context = context;
    }

}
