package entities;

/**
 * Room entity parent class
 */
public class Room {
    private int roomNum;
    private int price;
    private String bedType;
    private int numBeds;
    private int size;
    private String amenities;

    public Room(int roomNum, int price, String bedType, int numBeds, int size, String amenities) {
        this.roomNum = roomNum;
        this.price = price;
        this.bedType = bedType;
        this.numBeds = numBeds;
        this.size = size;
        this.amenities = amenities;
    }

    public int getRoomNumber() {
        return roomNum;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Room number: " + roomNum +
                ", Price: $" + price + "/night" +
                ", Included bed type: " + bedType +
                ", Number of beds: " + numBeds +
                ", Room size: " + size + " Sqft" +
                ", Amenities included: " + amenities;
    }
}
