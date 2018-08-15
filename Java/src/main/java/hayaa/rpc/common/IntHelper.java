package hayaa.rpc.common;

public class IntHelper {
    public static int byteArrayToInt(byte[] s) {
        return   s[3] & 0xFF |
                (s[2] & 0xFF) << 8 |
                (s[1] & 0xFF) << 16 |
                (s[0] & 0xFF) << 24;
    }
    public static byte[] intToByteArray(int s) {
        return new byte[] {
                (byte) ((s >> 24) & 0xFF),
                (byte) ((s >> 16) & 0xFF),
                (byte) ((s >> 8) & 0xFF),
                (byte) (s & 0xFF)
        };
    }
}
