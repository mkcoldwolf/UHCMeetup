package net.development.meetup.enums;

import net.development.meetup.Main;

public enum Status {
    LOADING(false),
    WAITING(false),
    TELEPORT(false),
    PVP(false),
    FINISH(false);

    Main main;
    private boolean canJoin;
    private static Status currentState;

    private Status(boolean bl2) {
        this.canJoin = bl2;
    }

    public boolean canJoin() {
        return this.canJoin;
    }

    public static void setState(Status b) {
        currentState = b;
    }

    public static boolean isState(Status b) {
        if (currentState == b) {
            return true;
        }
        return false;
    }

    public static Status getState() {
        return currentState;
    }

}
