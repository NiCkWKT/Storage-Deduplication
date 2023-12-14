package org.csci4180;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class MyDedup {

    static int minChunkSize = 4;
    static int avgChunkSize = 13;
    static int maxChunkSize;

    private static int generateMask(int avgChunkSize) {
        int mask_bits = (int) (Math.log(avgChunkSize) / Math.log(2));
        return (1 << mask_bits) - 1;
    }


    static int d = 10; // multiplier parameter
    private static int getRFP(int[] fileBuffer, int curPos, int prev) {
        int rfp = 0;
        if (prev == 0) {
            for (int j = 1; j <= minChunkSize; j++) {
                rfp += ((fileBuffer[curPos + j - 1] % avgChunkSize) * ((int) Math.pow(d, minChunkSize - j) % avgChunkSize)) % avgChunkSize; // \sum_{i=1}^m (t_i * d^{m-i}) mod q
            }
            rfp %= avgChunkSize;
        }
        else {
            int x = ((d % avgChunkSize) * (prev % avgChunkSize)) % avgChunkSize;
            int y = ((((int) Math.pow(d, minChunkSize)) % avgChunkSize) * (fileBuffer[curPos - 1] % avgChunkSize)) % avgChunkSize;
            int z = fileBuffer[curPos - 1 + minChunkSize] % avgChunkSize;
            rfp = (x - y + z) % avgChunkSize;
        }
        return rfp;
    }

    public static void main(String[] args) {
//        String data = "Sample data to be saved in the file.";
//        String directoryPath = "data";
//        String fileName = "output.txt"; // Specify the file path here
//        File directory = new File(directoryPath);
//
//        if (!directory.exists()) {
//            boolean isCreated = directory.mkdir();
//            if (!isCreated) {
//                System.err.println("Failed to create the directory: " + directoryPath);
//                return;
//            }
//        }
//
//        File file = new File(directoryPath, fileName);
//
//        try (FileWriter fileWriter = new FileWriter(file)) {
//            fileWriter.write(data);
//            System.out.println("Data has been successfully saved to the file: " + file.getPath());
//        } catch (IOException e) {
//            System.err.println("An error occurred while saving the data: " + e.getMessage());
//        }
//        System.out.println(generateMask(256));
        byte[] arr = new byte[]{2, 1, 4, 4, 3, 6, 4, 3, 2, 9, 6, 5, 8, 8, 1, 2};
        Chunker chunker = new Chunker(4, 13, 5, 10);
        List<Chunk> chunks = new ArrayList<>();
        chunks = chunker.getChunks(arr);
//        System.out.println(getRFP(arr, 6, 10));
        for (Chunk c : chunks) {
            System.out.println(c);
        }
    }
}