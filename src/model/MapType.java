package model;

public enum MapType {
    SMALL("materials/maps/small.txt"),
    MEDIUM("materials/maps/medium.txt"),
    LARGE("materials/maps/large.txt");

    private final String path;

    MapType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
