package org.csci4180;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FingerprintIndex {

    Map<FingerPrint, ChunkMetadata> indexes;
    private int numOfFiles;
    private int numOfChunks;
    private int numOfUniqueChunks;
    private int byteSum;
    private int uniqueByteSum;
    private int numOfContainers;
    private double dedupRatio;

    @SuppressWarnings("unchecked")
    public FingerprintIndex() throws Exception {
        File file = new File("mydedup.index");
        indexes = new HashMap<>();

        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);
            this.numOfFiles = in.readInt();
            this.numOfChunks = in.readInt();
            this.numOfUniqueChunks = in.readInt();
            this.byteSum = in.readInt();
            this.uniqueByteSum = in.readInt();
            this.numOfContainers = in.readInt();
            this.indexes = (Map<FingerPrint, ChunkMetadata>) in.readObject();
        }
        else {
            this.numOfFiles = 0;
            this.numOfChunks = 0;
            this.numOfUniqueChunks = 0;
            this.byteSum = 0;
            this.uniqueByteSum = 0;
            this.numOfContainers = 0;
        }
    }

    public void printReport() {
        dedupRatio = (double) byteSum / (double) uniqueByteSum;
        System.out.println("Report Output:");
        System.out.println("Total number of files that have been stored: " + numOfFiles);
        System.out.println("Total number of pre-deduplicated chunks in storage: " + numOfChunks);
        System.out.println("Total number of unique chunks in storage: " + numOfUniqueChunks);
        System.out.println("Total number of bytes of pre-deduplicated chunks in storage: " + byteSum);
        System.out.println("Total number of bytes of unique chunks in storage: " + uniqueByteSum);
        System.out.println("Total number of containers in storage: " + numOfContainers);
        System.out.format("Duplication ratio: %.2f\n", dedupRatio);
    }

    public int getNumOfFiles() {
        return numOfFiles;
    }

    public int getNumOfChunks() {
        return numOfChunks;
    }

    public int getNumOfUniqueChunks() {
        return numOfUniqueChunks;
    }

    public int getByteSum() {
        return byteSum;
    }

    public int getUniqueByteSum() {
        return uniqueByteSum;
    }

    public int getNumOfContainers() {
        return numOfContainers;
    }

    public void setNumOfFiles(int numOfFiles) {
        this.numOfFiles = numOfFiles;
    }

    public void setNumOfChunks(int numOfChunks) {
        this.numOfChunks = numOfChunks;
    }

    public void setNumOfUniqueChunks(int numOfUniqueChunks) {
        this.numOfUniqueChunks = numOfUniqueChunks;
    }

    public void setByteSum(int byteSum) {
        this.byteSum = byteSum;
    }

    public void setUniqueByteSum(int uniqueByteSum) {
        this.uniqueByteSum = uniqueByteSum;
    }

    public void setNumOfContainers(int numOfContainers) {
        this.numOfContainers = numOfContainers;
    }

    public void writeTo() throws Exception {
        File file = new File("mydedup.index");

        if (file.exists()) {
            boolean isDeleted = file.delete();
        }

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeInt(numOfFiles);
        out.writeInt(numOfChunks);
        out.writeInt(numOfUniqueChunks);
        out.writeInt(byteSum);
        out.writeInt(uniqueByteSum);
        out.writeInt(numOfContainers);
        out.writeObject(indexes);
        out.flush();
    }

    public ChunkMetadata getChunkMetadata(FingerPrint fp) {
        return indexes.get(fp);
    }

    public void updateChunkMetadata(FingerPrint fp, int containerId, int offset) {
        indexes.put(fp, new ChunkMetadata(containerId, offset));
    }

    public boolean isContained(FingerPrint fp) {
        return indexes.containsKey(fp);
    }

}
