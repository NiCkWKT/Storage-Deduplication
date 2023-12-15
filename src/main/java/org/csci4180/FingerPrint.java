package org.csci4180;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class FingerPrint implements Serializable {
    private byte[] checksumBytes;

    public FingerPrint(byte[] checksumBytes) {
        this.checksumBytes = checksumBytes;
    }

    public byte[] getChecksumBytes() {
        return checksumBytes;
    }

    public FingerPrint() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FingerPrint that = (FingerPrint) o;
        return Arrays.equals(checksumBytes, that.checksumBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(checksumBytes);
    }

    public void readFrom(ObjectInputStream in) throws IOException {
        checksumBytes = new byte[20];
        in.readFully(checksumBytes);
    }

    public void writeTo(ObjectOutputStream out) throws IOException {
        out.write(checksumBytes);
    }

}
