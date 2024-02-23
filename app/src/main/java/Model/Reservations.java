package Model;

import androidx.annotation.NonNull;

public class Reservations {
    private String User;
    private int RoomNumber;
    private String CheckInDate;
    private String CheckOutDate;

    public Reservations(String user, int roomNumber, String checkInDate, String checkOutDate) {
        User = user;
        RoomNumber = roomNumber;
        CheckInDate = checkInDate;
        CheckOutDate = checkOutDate;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public int getRoomdId() {
        return RoomNumber;
    }

    public void setRoomdId(int roomNumber) {
        RoomNumber = roomNumber;
    }

    public String getReservationDate() {
        return CheckInDate;
    }

    public void setReservationDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public String getExpectedCheckout() {
        return CheckOutDate;
    }

    public void setExpectedCheckout(String checkOutDate) {
        CheckOutDate = checkOutDate;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
