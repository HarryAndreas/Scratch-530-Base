package org.rs2.net.msg;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Message
 * @author 'Mystic Flow
 */
public class Message {
	
	/**
	 * PacketType
	 * @author 'Mystic Flow
	 */
	public enum PacketType {
		STANDARD(0),
		VAR_BYTE(1),
		VAR_SHORT(2);
		
		private final int size;
		
		private PacketType(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}
		
	}

	private final int opcode;
	
	private final PacketType type;
	
	private final ChannelBuffer buffer;
	
	private final int length;

	public Message(int opcode, PacketType type, ChannelBuffer buffer) {
		this.opcode = opcode;
		this.type = type;
		this.buffer = buffer;
		this.length = buffer.readableBytes();
	}
	
	public boolean isRaw() {
		return opcode == -1;
	}
	
	public int getOpcode() {
		return opcode;
	}
	
	public PacketType getType() {
		return type;
	}
	
	public byte readByte() {
		return buffer.readByte();
	}
	
	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	public void read(byte[] b) {
		buffer.readBytes(b);
	}
	
	/**
	 * Reads a short.
	 * @return A short.
	 */
	public short readShort() {
		return buffer.readShort();
	}
	
	/**
	 * Reads an unsigned short.
	 * @return An unsigned short.
	 */
	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}
	
	/**
	 * Reads an integer.
	 * @return An integer.
	 */
	public int readInt() {
		return buffer.readInt();
	}
	
	/**
	 * Reads a long.
	 * @return A long.
	 */
	public long readLong() {
		return buffer.readLong();
	}

	/**
	 * Reads a type C byte.
	 * @return A type C byte.
	 */
	public byte readByteC() {
		return (byte) (-readByte());
	}
	
	/**
	 * reads a type S byte.
	 * @return A type S byte.
	 */
	public byte readByteS() {
		return (byte) (128 - readByte());
	}
	
	/**
	 * Reads a little-endian type A short.
	 * @return A little-endian type A short.
	 */
	public short readLEShortA() {
		int i = (buffer.readByte() - 128 & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a little-endian short.
	 * @return A little-endian short.
	 */
	public short readLEShort() {
		int i = (buffer.readByte() & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}
	
	/**
	 * Reads a V1 integer.
	 * @return A V1 integer.
	 */
	public int readInt1() {
		byte b1 = buffer.readByte();
		byte b2 = buffer.readByte();
		byte b3 = buffer.readByte();
		byte b4 = buffer.readByte();
		return ((b3 << 24) & 0xFF) | ((b4 << 16) & 0xFF) | ((b1 << 8) & 0xFF) | (b2 & 0xFF);
	}
	
	/**
	 * Reads a V2 integer.
	 * @return A V2 integer.
	 */
	public int readInt2() {
		int b1 = buffer.readByte() & 0xFF;
		int b2 = buffer.readByte() & 0xFF;
		int b3 = buffer.readByte() & 0xFF;
		int b4 = buffer.readByte() & 0xFF;
		return ((b2 << 24) & 0xFF) | ((b1 << 16) & 0xFF) | ((b4 << 8) & 0xFF) | (b3 & 0xFF);
	}
	
	/**
	 * reads a 3-byte integer.
	 * @return The 3-byte integer.
	 */
	public int readTriByte() {
		return ((buffer.readByte() << 16) & 0xFF) | ((buffer.readByte() << 8) & 0xFF) | (buffer.readByte() & 0xFF);
	}

	/**
	 * Reads a type A byte.
	 * @return A type A byte.
	 */
	public byte readByteA() {
		return (byte) (readByte() - 128);
	}
	
	
	/**
	 * Reads a RuneScape string.
	 * @return The string.
	 */
	public String readRS2String() {
		StringBuilder sb = new StringBuilder();
		byte b;
		while(buffer.readable() && (b = buffer.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}

	/**
	 * Reads a type A short.
	 * @return A type A short.
	 */
	public short readShortA() {
		int i = ((buffer.readByte() & 0xFF) << 8) | (buffer.readByte() - 128 & 0xFF);
		if(i > 32767)
			i -= 0x10000;
		return (short) i;
	}
	
	/**
	 * Reads a series of bytes in reverse.
	 * @param is The tarread byte array.
	 * @param offset The offset.
	 * @param length The length.
	 */
	public void readReverse(byte[] is, int offset, int length) {
		for(int i = (offset + length - 1); i >= offset; i--) {
			is[i] = buffer.readByte();
		}
	}
	
	/**
	 * Reads a series of type A bytes in reverse.
	 * @param is The tarread byte array.
	 * @param offset The offset.
	 * @param length The length.
	 */
	public void readReverseA(byte[] is, int offset, int length) {
		for(int i = (offset + length - 1); i >= offset; i--) {
			is[i] = readByteA();
		}
	}
	
	/**
	 * Reads a series of bytes.
	 * @param is The tarread byte array.
	 * @param offset The offset.
	 * @param length The length.
	 */
	public void read(byte[] is, int offset, int length) {
		for(int i = 0; i < length; i++) {
			is[offset + i] = buffer.readByte();
		}
	}
	
	/**
	 * reads a smart.
	 * @return The smart.
	 */
	public int readSmart() {
		int peek = buffer.getByte(buffer.readerIndex());
		if(peek < 128) {
			return (readByte() & 0xFF);
		} else {
			return (readShort() & 0xFFFF) - 32768;
		}
	}
	
	public int remaining() {
		return buffer.readableBytes();
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public int getLength() {
		return length;
	}

	public void readBytes(byte[] textBuffer, int length) {
		buffer.readBytes(textBuffer, 0, length);
	}

	public String readJagString() {
		readByte();
		return readRS2String();
	}

	public int readLEInt() {
		return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + (readUnsignedByte() << 24);
	}

}
