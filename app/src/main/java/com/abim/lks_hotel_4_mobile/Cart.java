package com.abim.lks_hotel_4_mobile;

public class Cart {
    private int employeeId, reservationRoomId, qty, total, fdId;
    private String fdName;

    public Cart(int employeeId, int reservationRoomId, int qty, int total, int fdId, String fdName) {
        this.employeeId = employeeId;
        this.reservationRoomId = reservationRoomId;
        this.qty = qty;
        this.total = total;
        this.fdId = fdId;
        this.fdName = fdName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getReservationRoomId() {
        return reservationRoomId;
    }

    public int getQty() {
        return qty;
    }

    public int getTotal() {
        return total;
    }

    public int getFdId() {
        return fdId;
    }

    public String getFdName() {
        return fdName;
    }
}
