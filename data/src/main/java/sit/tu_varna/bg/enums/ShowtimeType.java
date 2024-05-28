package sit.tu_varna.bg.enums;

public enum ShowtimeType {
    TWO_D("2D"),

    THREE_D("3D"),

    IMAX("IMAX"),

    FOUR_DX("4DX");

    private final String displayName;

    ShowtimeType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
