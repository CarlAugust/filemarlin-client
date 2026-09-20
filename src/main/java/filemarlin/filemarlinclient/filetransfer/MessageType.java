package filemarlin.filemarlinclient.filetransfer;

public enum MessageType {
    MESSAGE(0),
    FILE(1),
    ACCEPT(2),
    PAYLOAD(3),
    RECEIVED(4),
    COMPLETE(5),
    CANCEL(6),
    ERROR(7);

    private final byte value;

    MessageType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return this.value;
    }
}