package com.visualipcv.core.command;

public abstract class Command {
    public abstract void execute();
    public abstract void rollback();
}
