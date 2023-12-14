package org.csci4180;

import java.security.MessageDigest;
import java.util.Arrays;

public class Chunk {
    private byte[] data;
    private int startOffset;
    private int endOffset;
    private byte[] fingerprint;

    public Chunk(byte[] fileData, int start, int end) {
        this.startOffset = start;
        this.endOffset = end;

        // Copy relevant byte range
        data = Arrays.copyOfRange(fileData, start, end + 1);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data, start, end - start + 1);
            fingerprint = md.digest();
        } catch (Exception e) {
            System.err.println("Cannot convert to fingerprint using SHA-1");
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public byte[] getFingerprint() {
        return fingerprint;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return Arrays.equals(fingerprint, chunk.fingerprint);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fingerprint);
    }
}
