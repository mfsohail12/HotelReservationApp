package entities;

public class Attraction {
/**
 * Attraction entity
 */
    private int id;
    private String name;
    private String address;
    private int rating;
    private int distance;

    public Attraction(int id, String name, String address, int rating, int distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d - %s - %s - %d stars - %.1f km", id, name, address, rating,(double) distance/1000);
    }
}
