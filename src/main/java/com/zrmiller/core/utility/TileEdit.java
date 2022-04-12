package com.zrmiller.core.utility;

import java.nio.ByteBuffer;

public class TileEdit implements Comparable<TileEdit> {

    public final int timestamp;
    public final short color;
    public final short x;
    public final short y;
    public final short x2;
    public final short y2;
    private final boolean isRect;

    public static final int META_COUNT = 6;
    public static final int BYTE_COUNT = 10;
    public static final int BYTE_INCREASE = 6;
    public static final int BYTE_COUNT_INCREASED = BYTE_COUNT + BYTE_INCREASE;

    // Number of times compareTo is called when sorting 2017 dataset. Used for progress reporting
    private static final int MAX_SORT_COUNT = 349769937;
    public static int sortCount = 0;

    public TileEdit(int timestamp, short color, short x, short y) {
        this.timestamp = timestamp;
        this.color = color;
        this.x = x;
        this.y = y;
        this.x2 = 0;
        this.y2 = 0;
        isRect = false;
    }

    public TileEdit(int timestamp, short color, short x, short y, short x2, short y2) {
        this.timestamp = timestamp;
        this.color = color;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        isRect = true;
    }

    public TileEdit(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (bytes.length == BYTE_COUNT) {
            timestamp = buffer.getInt();
            color = buffer.getShort();
            x = buffer.getShort();
            y = buffer.getShort();
            x2 = 0;
            y2 = 0;
            isRect = false;
        } else {
            assert bytes.length == BYTE_COUNT_INCREASED;
            timestamp = buffer.getInt();
            buffer.getShort();  // Intentionally ignored, will always be -1
            x = buffer.getShort();
            y = buffer.getShort();
            x2 = buffer.getShort();
            y2 = buffer.getShort();
            color = buffer.getShort();
            isRect = true;
        }
    }

    public byte[] toByteArray() {
        ByteBuffer buffer;
        if (isRect) {
            buffer = ByteBuffer.allocate(TileEdit.BYTE_COUNT_INCREASED);
            buffer.putInt(timestamp);
            buffer.putShort((short) -1);
            buffer.putShort(x);
            buffer.putShort(y);
            buffer.putShort(x2);
            buffer.putShort(y2);
            buffer.putShort(color);
            buffer = ByteBuffer.allocate(BYTE_COUNT_INCREASED);
        } else {
            buffer = ByteBuffer.allocate(TileEdit.BYTE_COUNT);
            buffer.putInt(timestamp);
            buffer.putShort(color);
            buffer.putShort(x);
            buffer.putShort(y);
            buffer = ByteBuffer.allocate(BYTE_COUNT);
        }
        return buffer.array();
    }

    public static int getSortProgress() {
        return (int) Math.ceil(sortCount / (float) MAX_SORT_COUNT * 100);
    }

    @Override
    public String toString() {
        if (isRect) {
            return timestamp + "," + color + "," + x + "," + y + "," + x2 + ", " + y2 + ", ";
        } else {
            return timestamp + "," + color + "," + x + "," + y;
        }
    }

    @Override
    public int compareTo(TileEdit o) {
        sortCount++;
        return timestamp - o.timestamp;
    }
}
