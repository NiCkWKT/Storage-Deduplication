package org.csci4180;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChunkMetadata implements Serializable {
    private int containerId;
    private int offset;

    @Override
    public String toString() {
        return "ChunkMetadata{" +
                ", containerId=" + containerId +
                ", offset=" + offset +
                '}';
    }

    public ChunkMetadata(int containerId, int offset) {
        this.containerId = containerId;
        this.offset = offset;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void readFrom(ObjectInputStream in) throws IOException {
        containerId = in.readInt();
        offset = in.readInt();
    }

    public void writeTo(ObjectOutputStream out) throws IOException {
        out.writeInt(containerId);
        out.writeInt(offset);
    }

}
