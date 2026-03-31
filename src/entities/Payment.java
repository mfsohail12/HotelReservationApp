package entities;

import java.time.LocalDate;

/**
 * Payment entity
 */
public class Payment {
    private int paymentId;
    private int reservationId;
    private int amount;
    private String method;
    private String email;
    private LocalDate paymentDate;

    public Payment(int paymentId, int reservationId, int amount, String method, String email, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.email = email;
        this.paymentDate = paymentDate;
    }

    public Payment(int reservationId, int amount, String method, String email, LocalDate paymentDate) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.email = email;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", reservationId=" + reservationId +
                ", amount=" + amount +
                ", method='" + method + '\'' +
                ", email='" + email + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}


