package com.visualipcv.core.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompoundCommand extends Command {
    private List<Command> commands = new ArrayList<>();

    public CompoundCommand(Command... commands) {
        Collections.addAll(this.commands, commands);
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        for(Command command : commands) {
            command.execute();
        }
    }

    @Override
    public void rollback() {
        for(int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).rollback();
        }
    }
}
