package Entities;

/**
 * Hotel entity
 */
public class Hotel {
    private int id;
    private String name;
    private String address;
    private String city;
    private int rating;

    public Hotel(int id, String name, String address, String city, int rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d - %s - %s, %s - $d", id, name, address, city, rating);
    }
}
