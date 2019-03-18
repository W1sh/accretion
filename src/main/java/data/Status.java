package data;

import java.awt.*;

public enum Status {

    ONGOING("Ongoing", Color.GREEN),
    PLANNED("Planned", Color.GRAY),
    DROPPED("Dropped", Color.RED),
    COMPLETED("Completed", Color.BLUE);

    private String text;
    private Color color;

    Status(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public String toString() {
        return text;
    }

    public Color getColor() {
        return color;
    }
}