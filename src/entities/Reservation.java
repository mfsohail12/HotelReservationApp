package entities;

import java.time.LocalDate;

/**
 * Reservation entity
 */
public class Reservation {
    private int reservationId;
    private String email;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private boolean isPaidCompletely;

    public Reservation(int reservationId, String email, LocalDate checkIn, LocalDate checkOut, boolean isPaidCompletely) {
        this.reservationId = reservationId;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isPaidCompletely = isPaidCompletely;
    }

    public Reservation(String email, LocalDate checkIn, LocalDate checkOut) {
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isPaidCompletely = false;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public boolean isPaidCompletely() {
        return isPaidCompletely;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", email='" + email + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", isPaidCompletely=" + isPaidCompletely +
                '}';
    }
}


