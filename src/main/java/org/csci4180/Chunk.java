package org.csci4180;

import java.security.MessageDigest;
import java.util.Arrays;

public class Chunk {
    private byte[] data;
    private int startOffset;
    private int endOffset;
    private FingerPrint fingerprint;

    public Chunk(byte[] fileData, int start, int end) {
        this.startOffset = start;
        this.endOffset = end;

        // Copy relevant byte range
        data = Arrays.copyOfRange(fileData, start, end + 1);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data, 0, data.length);
            byte[] checksumBytes = md.digest();
            fingerprint = new FingerPrint(checksumBytes);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot convert to fingerprint using SHA-1");
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public FingerPrint getFingerprint() {
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

    public int getSize() {
        return endOffset - startOffset + 1;
    }
}
