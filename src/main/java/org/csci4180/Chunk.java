package org.csci4180;

import java.util.Arrays;

public class Chunk {
    private byte[] data;
    private int startOffset;
    private int endOffset;

    public Chunk(byte[] fileData, int start, int end) {
        this.startOffset = start;
        this.endOffset = end;

        // Copy relevant byte range
        data = Arrays.copyOfRange(fileData, start, end + 1);
    }

    public byte[] getData() {
        return data;
    }

    public int getStartOffset() {
        return startOffset;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public int getEndOffset() {
        return endOffset;
    }

    @Override
    public boolean equals(Object o) {
        Chunk other = (Chunk) o;
        return Arrays.equals(this.data, other.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
