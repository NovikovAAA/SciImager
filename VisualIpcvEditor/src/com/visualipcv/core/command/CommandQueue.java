package com.visualipcv.core.command;

import java.util.LinkedList;
import java.util.List;

public class CommandQueue {
    private List<Command> commands = new LinkedList<>();
    private List<Command> rollbackCommands = new LinkedList<>();

    public void add(Command command) {
        commands.add(command);
        rollbackCommands.clear();
    }

    public void undo() {
        Command lastCommand = commands.get(commands.size() - 1);
        commands.remove(lastCommand);
        lastCommand.rollback();
        rollbackCommands.add(lastCommand);
    }

    public void redo() {
        Command lastCommand = rollbackCommands.get(rollbackCommands.size() - 1);
        rollbackCommands.remove(lastCommand);
        lastCommand.execute();
        commands.add(lastCommand);
    }
}
