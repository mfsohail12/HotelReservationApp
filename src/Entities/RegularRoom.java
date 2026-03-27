package Entities;

/**
 * Room entity
 */
public class RegularRoom extends Room {
    private String standardCategory;

    public RegularRoom(int roomNum, int price, String bedType, String numBeds, int size, String amenities, String standardCategory) {
        super(roomNum, price, bedType, numBeds, size, amenities);
        this.standardCategory = standardCategory;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Standard Category: " + standardCategory;
    }
}
