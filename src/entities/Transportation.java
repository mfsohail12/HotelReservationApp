package entities;

public class Transportation {
/**
 * Transport entity
 */
    private String name;
    private String type;
    private int distance;

    public Transportation(String name, String type, int distance) {
        this.name = name;
        this.type = type;
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        // if (type.equals("Metro Station")){
        //     return String.format("%s - %s", name, type);
            
        // }
        // return String.format("%s", name);
        return String.format("%s - %s - %.1f km", name, type,(double) distance/1000);
    }

}
