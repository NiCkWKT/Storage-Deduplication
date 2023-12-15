package org.csci4180;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Container {
    private List<Chunk> chunks;
    private int containerId;
    private int offset;
    public static final String dest = "data/containers/";

    public int getContainerID() {
        return containerId;
    }

    public int getOffset() {
        return offset;
    }

    public Container(int containerID) {
        this.chunks = new ArrayList<>();
        this.containerId = containerID;
        this.offset = 0;
    }

    public Container(int containerID, int offset) {
        this.chunks = new ArrayList<>();
        this.containerId = containerID;
        this.offset = offset;
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
        offset += chunk.getSize();
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void saveChunks() throws Exception {
        File directory = new File(dest);
        String filename = "container-" + containerId;

        if (!directory.exists()) {
            boolean isCreated = directory.mkdir();
            if (!isCreated) {
                System.err.println("Failed to create the directory: " + dest);
                System.exit(1);
            }
        }

        File file = new File(directory, filename);
        FileOutputStream out = new FileOutputStream(file);
        for(Chunk chunk : chunks) {
            out.write(chunk.getData());
        }
        out.flush();

        containerId++;
        chunks = new ArrayList<>();
        offset = 0;
    }

    public byte[] readFrom(int size) throws Exception {
        String file = dest + "container-" + containerId;
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(offset);
        byte[] data = new byte[size];
        raf.read(data, 0, size);
        return data;
    }
}
