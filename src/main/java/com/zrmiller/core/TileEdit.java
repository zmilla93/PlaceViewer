package com.zrmiller.core;

import java.nio.ByteBuffer;

public class TileEdit implements Comparable<TileEdit> {

    public final int timestamp;
    public final short color;
    public final short x;
    public final short y;

    public static final int BYTE_COUNT = 10;

    public TileEdit(int timestamp, short color, short x, short y) {
        this.timestamp = timestamp;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public TileEdit(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        timestamp = buffer.getInt();
        color = buffer.getShort();
        x = buffer.getShort();
        y = buffer.getShort();
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(timestamp);
        buffer.putShort(color);
        buffer.putShort(x);
        buffer.putShort(y);
        return buffer.array();
    }

    @Override
    public String toString() {
        return timestamp + "," + color + "," + x + "," + y;
    }

    @Override
    public int compareTo(TileEdit o) {
        return timestamp - o.timestamp;
    }
}
