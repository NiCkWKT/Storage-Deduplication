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
}
