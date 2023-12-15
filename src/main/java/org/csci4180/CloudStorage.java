package org.csci4180;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CloudStorage {

    public CloudStorage() {

    }

    public void upload(List<Chunk> chunks, String dest, int length) throws Exception {
        FingerprintIndex fpi = new FingerprintIndex();

        FileRecipe recipe = new FileRecipe();
        Container container = new Container(fpi.getNumOfContainers());
        fpi.setNumOfChunks(fpi.getNumOfChunks() + chunks.size());
        for (Chunk chunk : chunks) {
            FingerPrint fp = chunk.getFingerprint();
            int size = chunk.getSize();
            recipe.add(fp, size);
            if (!fpi.isContained(fp)) {
                if (container.getOffset() + size > 1048576) {
                    container.saveChunks();
                }
                fpi.updateChunkMetadata(fp, container.getContainerID(), container.getOffset());
                container.addChunk(chunk);
                fpi.setNumOfUniqueChunks(fpi.getNumOfUniqueChunks() + 1);
                fpi.setUniqueByteSum(fpi.getUniqueByteSum() + size);
            }
        }

        if (container.getOffset() > 0) {
            fpi.setNumOfContainers(container.getContainerID() + 1);
            container.saveChunks();
        }
        else {
            fpi.setNumOfContainers(container.getContainerID());
        }
        fpi.setNumOfFiles(fpi.getNumOfFiles() + 1);
        fpi.setByteSum(fpi.getByteSum() + length);
        fpi.writeTo();
        fpi.printReport();
//        System.out.println(recipe.getFpList().size());
        recipe.writeTo(dest);
    }

    public void download(String path, String localFileName) throws Exception {
        File file = new File(FileRecipe.getInputPath(path));
        if (!file.exists()) {
            System.err.println("Error: File does not exist");
            System.exit(1);
        }
        FingerprintIndex fpi = new FingerprintIndex();
        File outFile = new File(localFileName);
        if (outFile.exists()) {
            outFile.delete();
        }
        file.createNewFile();
        FileRecipe fr = new FileRecipe(path);

        FileOutputStream fos = new FileOutputStream(outFile);

//        System.out.println(fr.getFpList().size());

        for (int i = 0; i < fr.getFpList().size() ; i++) {
            ChunkMetadata metadata = fpi.getChunkMetadata(fr.getFpList().get(i));
            Container container = new Container(metadata.getContainerId(), metadata.getOffset());
            byte[] data = container.readFrom(fr.getSizeList().get(i));
//            System.out.println(data.length);
            fos.write(data);
        }
        fos.flush();
    }


    public static void writeObjects(List<ChunkMetadata> list, String fileName) throws IOException {
        // Create a FileOutputStream object with the file name
        FileOutputStream fos = new FileOutputStream(fileName);
        // Create an ObjectOutputStream object with the FileOutputStream object
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        // Write the size of the list to the file

        // Loop through the list and write each object to the file
        for (ChunkMetadata cm : list) {
            oos.writeObject(cm);
        }
        oos.writeInt(list.size());
        // Close the ObjectOutputStream and the FileOutputStream objects
        oos.close();
        fos.close();
    }

    public static List<ChunkMetadata> readObjects(String fileName) throws IOException, ClassNotFoundException {
        // Create a FileInputStream object with the file name
        FileInputStream fis = new FileInputStream(fileName);
        // Create an ObjectInputStream object with the FileInputStream object
        ObjectInputStream ois = new ObjectInputStream(fis);
        // Read the size of the list from the file
        int size = ois.readInt();
        // Create an ArrayList object to store the objects
        List<ChunkMetadata> list = new ArrayList<>();
        // Loop through the file and read each object
        for (int i = 0; i < size; i++) {
            // Cast the object to the ChunkMetadata type
            ChunkMetadata cm = (ChunkMetadata) ois.readObject();
            // Add the object to the list
            list.add(cm);
        }
        // Close the ObjectInputStream and the FileInputStream objects
        ois.close();
        fis.close();
        // Return the list
        return list;
    }
}
