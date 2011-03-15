package FIT_8201_Sviridov_Flt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author alstein
 */
public class LittleEndianDataOutputStream {

    private DataOutputStream _dos;

    public LittleEndianDataOutputStream(DataOutputStream dos){
        _dos = dos;
    }

    private void writeBytes(byte[] b) throws IOException {
        _dos.write(b);
    }

    public final void writeUTF(String str) throws IOException {
        _dos.writeUTF(str);
    }

    public final void writeShort(int v) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort((short) v);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        _dos.write(bb.array());
    }

    public final void writeLong(long v) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(v);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        _dos.write(bb.array());
    }

    public final void writeInt(int v) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(v);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        _dos.write(bb.array());
    }

    public final void writeChars(String s) throws IOException {
        for(int i = 0; i < s.length(); ++i){
            this.writeChar(s.charAt(i));
        }
    }

    public final void writeChar(int v) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putChar((char) v);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        _dos.write(bb.array());
    }

    public final void writeBytes(String s) throws IOException {
        _dos.writeBytes(s);
    }

    public final void writeByte(int v) throws IOException {
        _dos.writeByte(v);
    }

    public final void writeBoolean(boolean v) throws IOException {
        _dos.writeBoolean(v);
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        _dos.write(b, off, len);
    }

    public synchronized void write(int b) throws IOException {
        _dos.write(b);
    }

    public final int size() {
        return _dos.size();
    }

    public void flush() throws IOException {
        _dos.flush();
    }
}
