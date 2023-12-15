package org.csci4180;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Chunker {
    private int minChunkSize; // m : window size
    private int avgChunkSize; // q : modulus
    private int maxChunkSize;
    private int d; // multiplier parameter

    public Chunker(int min, int avg, int max, int d) {
        this.minChunkSize = min;
        this.avgChunkSize = avg;
        this.maxChunkSize = max;
        this.d = d;
    }

    public List<Chunk> getChunks(byte[] fileBuffer) {
        List<Chunk> chunks = new ArrayList<>();
        if (fileBuffer.length < minChunkSize) {
            chunks.add(new Chunk(fileBuffer, 0, fileBuffer.length - 1));
            return chunks;
        }
        // Rabin fingerprint
        int anchorMask = avgChunkSize - 1;
        int rfp = 0;
        int lastAnchor = 0;

        int tmp_d = d;
        int tmp_m = minChunkSize - 1;
        tmp_d = tmp_d & anchorMask;
        int dm = 1;
        while (tmp_m > 0) {
            if ((tmp_m & 1)> 0) {
                dm = (tmp_d * dm) & anchorMask;
            }
            tmp_d = (tmp_d*tmp_d) & anchorMask;
            tmp_m = tmp_m >> 1;
        }

        int i = 0;
        int j = 0;
        while (i < fileBuffer.length) {
            while(j < fileBuffer.length && !(rfp == 0 && j - i >= minChunkSize) && j - i < maxChunkSize) {
                rfp = (rfp * d + fileBuffer[j]) & anchorMask;
                if (j - i >= minChunkSize) {
                    rfp = (rfp - dm * fileBuffer[j - minChunkSize]) & anchorMask;
                }
                j++;
            }

            if(rfp != 0 && j - i < maxChunkSize)
                break;
            chunks.add(new Chunk(fileBuffer, i, j - 1));
            rfp = 0;
            i = j;


            // Reach the EOF
//            if (i + minChunkSize >= fileBuffer.length) {
//                chunks.add(new Chunk(fileBuffer, lastAnchor, fileBuffer.length - 1));
//                break;
//            }
//
//            rfp = getRFP(fileBuffer, i, rfp);
//
//            // Reach maximum chunk size
//            if ((i + minChunkSize - lastAnchor == maxChunkSize) && (rfp != 0)) {
//                chunks.add(new Chunk(fileBuffer, lastAnchor, i + minChunkSize - 1));
//                lastAnchor = i + minChunkSize;
//                i = lastAnchor;
//                continue;
//            }
//
//            int anchor = rfp & anchorMask;
//            if (anchor == 0) {
//                int end = i + minChunkSize - 1;
//                chunks.add(new Chunk(fileBuffer, lastAnchor, end));
//                i = end + 1;
//                lastAnchor = i;
//                continue;
//            }
//            i++;
        }

        if(fileBuffer.length != 1048576 && i < fileBuffer.length) {
            chunks.add(new Chunk(fileBuffer, i, fileBuffer.length - 1));
        }
        return chunks;
    }

//    private int getRFP(byte[] fileBuffer, int curPos, int prev) {
//        int rfp = 0;
//        int tmp = 1;
//        if (prev == 0) {
//            for (int j = 1; j <= minChunkSize; j++) {
//                rfp += ((fileBuffer[curPos + j - 1] % avgChunkSize) * ((int) Math.pow(d, minChunkSize - j) % avgChunkSize)) % avgChunkSize; // \sum_{i=1}^m (t_i * d^{m-i}) mod q
//            }
//            rfp %= avgChunkSize;
//            for (int i = curPos + minChunkSize - 1; i >= curPos; i--) {
//                if (i != curPos + minChunkSize - 1)
//                    tmp = tmp * d % avgChunkSize;
//                rfp += (int) fileBuffer[i] % avgChunkSize * tmp;
//                rfp %= avgChunkSize;
//            }
//        }
//        else {
//            int x = ((d % avgChunkSize) * (prev % avgChunkSize)) % avgChunkSize;
//            int y = ((((int) Math.pow(d, minChunkSize)) % avgChunkSize) * (fileBuffer[curPos - 1] % avgChunkSize)) % avgChunkSize;
//            int z = fileBuffer[curPos - 1 + minChunkSize] % avgChunkSize;
//            rfp = (x - y + z) % avgChunkSize;
//        }
//        return rfp;
//    }

//    public int getRFP(byte[] f, int idx, int prevRFP) {
//        int m = minChunkSize;
//        int q = avgChunkSize;
//        int temp = 1;
//        int rt = 0;
//        if (prevRFP == 0) {
//            for (int i = idx + m - 1; i >= idx; i--) {
//                if (i != idx + m - 1) temp = temp * d % q;
//                rt += (int) f[i] % q * temp;
//                rt %= q;
//            }
//            return rt;
//        } else {
//            int dp = (((d % q) * (prevRFP % q)) % q);
//            int dt = (((f[idx] & 0xFF) % q) * (int) (Math.pow(d, m) % q)) % q;
//            int t = (f[idx + m - 1] & 0xFF % q);
//            rt = ((dp - dt + t) % q);
//        }
//        return rt;
//    }

    public static void main(String[] args) throws Exception {
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

        String file_to_upload = "/Users/nickwkt/Documents/UBL_doc.pdf";
//        File f = new File(FileRecipe.getPath(file_to_upload));
//        if(f.exists()) {
//            System.out.println("Error: File already uploaded.");
//            System.exit(0);
//        }
        byte[] fileBuffer = Files.readAllBytes(Paths.get(file_to_upload));
//        byte[] arr = new byte[]{2, 1, 4, 4, 3, 6, 4, 3, 2, 9, 6, 5, 8, 8, 1, 2};
        Chunker chunker = new Chunker(8, 32 ,64, 16);
        List<Chunk> chunks = new ArrayList<>();
        chunks = chunker.getChunks(fileBuffer);
//        System.out.println(getRFP(arr, 6, 10));
//        for (Chunk c : chunks) {
//            System.out.println(c);
//        }
        int total = 0;
        for (Chunk c : chunks) {
            total += c.getSize();
            System.out.println(c.getSize());
        }
//        System.out.println(total);
//        System.out.println(chunks.size());
    }
}
