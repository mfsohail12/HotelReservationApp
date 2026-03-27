package entities;

public class Suite extends Room {
    private String viewType;
    private String premiumFeatures;

    public Suite(int roomNum, int price, String bedType, String numBeds, int size, String amenities, String viewType, String premiumFeatures) {
        super(roomNum, price, bedType, numBeds, size, amenities);
        this.viewType = viewType;
        this.premiumFeatures = premiumFeatures;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", View type: " + viewType +
                ", Premium features: " + premiumFeatures;
    }
}
