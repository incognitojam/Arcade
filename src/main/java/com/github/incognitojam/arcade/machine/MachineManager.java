package com.github.incognitojam.arcade.machine;

import com.github.incognitojam.arcade.Arcade;
import com.github.incognitojam.arcade.display.Display;

import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MachineManager {

    private final Arcade arcade;
    private ScriptEngineManager manager;
    private Map<Short, Machine> machineMap;

    public MachineManager(Arcade arcade) {
        this.arcade = arcade;
        this.manager = new ScriptEngineManager();

        loadMachines();
        verifyMachines();
    }

    public void onUpdate() {
        machineMap.values().forEach(Machine::onUpdate);
    }

    public void dispose() {
        saveMachines();
    }

    private void loadMachines() {
        this.machineMap = new HashMap<>();
    }

    private void verifyMachines() {

    }

    private void saveMachines() {

    }

    public boolean doesMachineExist(short machineId) {
        return this.machineMap.containsKey(machineId);
    }

    public Machine getMachine(short machineId) {
        return this.machineMap.get(machineId);
    }

    public Machine findMachine(Display display) {
        for (Machine machine : machineMap.values()) {
            if (machine.getContext() != null && display.equals(machine.getContext().getDisplay())) {
                return machine;
            }
        }
        return null;
    }

    public ArrayList<Machine> getMachines() {
        return new ArrayList<>(machineMap.values());
    }

    public Machine createMachine(UUID ownerUniqueId) {
        short machineId = getFirstAvailableId();
        Machine machine = new Machine(machineId, ownerUniqueId);
        Context context = createContext(machine);
        machine.setContext(context);

        machineMap.put(machineId, machine);

        return machine;
    }

    public void removeMachine(short machineId) {
        Machine machine = machineMap.remove(machineId);
        // TODO
//        machine.dispose();
    }

    public Context createContext(Machine machine) {
//        ScriptEngine engine = manager.getEngineByExtension("kotlin");
        return new Context(machine);
    }

    private short getFirstAvailableId() {
        short id = 0;
        while (doesMachineExist(id)) id++;
        return id;
    }
}
