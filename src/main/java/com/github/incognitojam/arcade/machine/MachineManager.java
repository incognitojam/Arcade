package com.github.incognitojam.arcade.machine;

import com.github.incognitojam.arcade.Arcade;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MachineManager {

    private final Arcade arcade;
    private Map<Integer, Machine> machineMap;

    public MachineManager(Arcade arcade) {
        this.arcade = arcade;

        loadMachines();
        verifyMachines();
    }

    void dispose() {
        saveMachines();
    }

    private void loadMachines() {
        this.machineMap = new HashMap<>();
    }

    private void verifyMachines() {

    }

    private void saveMachines() {

    }

    boolean doesMachineExist(int machineId) {
        return this.machineMap.containsKey(machineId);
    }

    Machine getMachine(int machineId) {
        return this.machineMap.get(machineId);
    }

    Machine createMachine(UUID ownerUniqueId) {
        int machineId = getFirstAvailableId();
        Machine machine = new Machine(machineId, ownerUniqueId);
        return machine;
    }

    MachineContext createContext() {
        MachineContext context = new MachineContext()
    }

    private int getFirstAvailableId() {
        int id = 0;
        while (doesMachineExist(id)) id++;
        return id;
    }

}
