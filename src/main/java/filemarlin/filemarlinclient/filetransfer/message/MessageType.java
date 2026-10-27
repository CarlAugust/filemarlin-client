package filemarlin.filemarlinclient.filetransfer.message;

public enum MessageType {
    TEST(0),
    UPLOAD_REQUEST(1),
    DOWNLOAD_REQUEST(2),
    ACCEPT_TRANSFER(3),
    PAYLOAD(4),
    RECEIVED(5),
    COMPLETE(6),
    CANCEL(7),
    ERROR(8);

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