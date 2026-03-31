package entities;

/**
 * Represents a reservation with its hotel and room details
 */
public class Reservation {
    public int reservationId;
    public String checkIn;
    public String checkOut;
    public int isPaid;
    public String hotelName;
    public String city;
    public int roomNumber;
    public int price;
    public String bedType;
    public String numBeds;

    public Reservation(int reservationId, String checkIn, String checkOut, int isPaid, String hotelName, String city, int roomNumber, int price, String bedType, String numBeds) {
        this.reservationId = reservationId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isPaid = isPaid;
        this.hotelName = hotelName;
        this.city = city;
        this.roomNumber = roomNumber;
        this.price = price;
        this.bedType = bedType;
        this.numBeds = numBeds;
    }

    public String getPaidStatus() {
        return (isPaid == 1) ? "Paid" : "Not Fully Paid";
    }
}