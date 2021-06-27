package com.sbo.bot.enums;

/**
 * @author viktar hraskou
 */
public enum Command {
    HELP("help command description"),
    EMPTY("empty command description");

    private final String commandDescription;

    Command(String s) {
        this.commandDescription = s;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public boolean equalsIgnoreCase(String command){
        return toString().equalsIgnoreCase(command);
    }
}
