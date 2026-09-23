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

    public static MessageType fromByte(byte value) throws IllegalArgumentException {
        for (var type : values()) {
            if (type.value == value) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid Message Type");
    }
}