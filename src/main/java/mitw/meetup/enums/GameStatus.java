package mitw.meetup.enums;

public enum GameStatus {
    LOADING(false),
    WAITING(false),
    TELEPORT(false),
    PVP(false),
    FINISH(false);

    private boolean canJoin;
    private static GameStatus currentState;

    private GameStatus(boolean bl2) {
        this.canJoin = bl2;
    }

    public boolean canJoin() {
        return this.canJoin;
    }

    public static void set(GameStatus b) {
        currentState = b;
    }

    public static boolean is(GameStatus b) {
        return currentState == b;
    }

    public static GameStatus get() {
        return currentState;
    }

}
