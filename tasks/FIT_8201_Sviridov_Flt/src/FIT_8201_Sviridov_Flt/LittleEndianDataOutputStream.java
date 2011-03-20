package FIT_8201_Sviridov_Flt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Wrapper for DataOutputStream convering values to little endian format
 * @author alstein
 */
public class LittleEndianDataOutputStream {

	private DataOutputStream _dos;

	public LittleEndianDataOutputStream(DataOutputStream dos) {
		_dos = dos;
	}

	public final void write(byte[] b) throws IOException {
		_dos.write(b);
	}

	public final void writeUTF(String str) throws IOException {
		_dos.writeUTF(str);
	}

	public final void writeShort(int v) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort((short) v);
		bb.position(0);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		_dos.writeShort(bb.getShort());
	}

	public final void writeLong(long v) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(v);
		bb.position(0);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		_dos.writeLong(bb.getLong());
	}

	public final void writeInt(int v) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(v);
		bb.position(0);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		_dos.writeInt(bb.getInt());
	}

	public final void writeChars(String s) throws IOException {
		for (int i = 0; i < s.length(); ++i) {
			this.writeChar(s.charAt(i));
		}
	}

	public final void writeChar(int v) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putChar((char) v);
		bb.position(0);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		_dos.writeChar(bb.getChar());
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

	public synchronized void write(byte[] b, int off, int len)
			throws IOException {
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
