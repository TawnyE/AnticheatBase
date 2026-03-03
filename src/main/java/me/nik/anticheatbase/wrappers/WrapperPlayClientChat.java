package me.nik.anticheatbase.wrappers;

public class WrapperPlayClientChat {
    private final String message;

    public WrapperPlayClientChat(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
