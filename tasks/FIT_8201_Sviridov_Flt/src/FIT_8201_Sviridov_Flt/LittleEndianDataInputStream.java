package FIT_8201_Sviridov_Flt;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author alstein
 */
public class LittleEndianDataInputStream {

    private DataInputStream _dis;

    private byte[] readBytes(int n) throws IOException {
        byte[] buffer = new byte[n];
        int bytesRead = _dis.read(buffer);
        if (bytesRead != n) {
            throw new IOException("Unexpected End of Stream");
        }
        return buffer;
    }

    public LittleEndianDataInputStream(DataInputStream dis) {
        _dis = dis;
    }

    public final int skipBytes(int n) throws IOException {
        return _dis.skipBytes(n);
    }

    public final int readUnsignedShort() throws IOException {
        return ByteBuffer.wrap(readBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public final int readUnsignedByte() throws IOException {
        return _dis.readUnsignedByte();
    }

    public final short readShort() throws IOException {
        return ByteBuffer.wrap(readBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public final long readLong() throws IOException {
        return ByteBuffer.wrap(readBytes(8)).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }
    public final int readInt() throws IOException {
        return ByteBuffer.wrap(readBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        _dis.readFully(b, off, len);
    }

    public final void readFully(byte[] b) throws IOException {
        _dis.readFully(b);
    }

    public final char readChar() throws IOException {
        return ByteBuffer.wrap(readBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getChar();
    }

    public final byte readByte() throws IOException {
        return _dis.readByte();
    }

    public final boolean readBoolean() throws IOException {
        return _dis.readBoolean();
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        return _dis.read(b, off, len);
    }

    public final int read(byte[] b) throws IOException {
        return _dis.read(b);
    }
}
