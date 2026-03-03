package me.nik.anticheatbase.wrappers;

public class WrapperPlayClientCustomPayload {

    private final String channel;
    private final byte[] contents;

    public WrapperPlayClientCustomPayload(String channel, byte[] contents) {
        this.channel = channel;
        this.contents = contents;
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getContents() {
        return contents;
    }
}
