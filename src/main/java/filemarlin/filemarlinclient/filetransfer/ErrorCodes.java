package filemarlin.filemarlinclient.filetransfer;

public enum ErrorCodes {
    DECODE_ERROR_LOCAL(0);

    private final byte value;

    ErrorCodes(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return this.value;
    }

    public static ErrorCodes fromByte(byte value) throws IllegalArgumentException {
        for (var type : values()) {
            if (type.value == value) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid Error Code");
    }
}
