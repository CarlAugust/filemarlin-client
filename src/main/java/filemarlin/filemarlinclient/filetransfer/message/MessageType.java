package filemarlin.filemarlinclient.filetransfer.message;

public enum MessageType {
    TEST(0),

    UPLOAD_REQUEST(1),
    DOWNLOAD_REQUEST(2),

    ACCEPT_TRANSFER(3),
    START_TRANSFER(4),
    COMPLETE_TRANSFER(5),
    CANCEL_TRANSFER(6),

    PAYLOAD(7),
    RECEIVED(8),
    ERROR(9);

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