package com.ohio.grand_hotel_ohio.domain;

public enum Roles {
    ADMIN, USER, STAFF;

    public String asAuthority() {
        return "ROLE_" + name();
    }
}
