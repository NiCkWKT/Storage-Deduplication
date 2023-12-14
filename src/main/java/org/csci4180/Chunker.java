package org.csci4180;

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

        // Rabin fingerprint

        int anchorMask = avgChunkSize - 1;
        int rfp = 0;
        int lastAnchor = 0;

        int i = 0;
        while (i < fileBuffer.length) {
            // Reach the EOF
            if (i + minChunkSize >= fileBuffer.length) {
                chunks.add(new Chunk(fileBuffer, lastAnchor, fileBuffer.length - 1));
                break;
            }

            rfp = getRFP(fileBuffer, i, rfp);

            // Reach maximum chunk size
            if (i + minChunkSize - lastAnchor == maxChunkSize && rfp != 0) {
                chunks.add(new Chunk(fileBuffer, lastAnchor, i + minChunkSize - 1));
                lastAnchor = i + minChunkSize;
                i = lastAnchor;
                continue;
            }

            int anchor = rfp & anchorMask;
            if (anchor == 0) {
                int end = i + minChunkSize - 1;
                chunks.add(new Chunk(fileBuffer, lastAnchor, end));
                i = end + 1;
                lastAnchor = i;
                continue;
            }
            i++;
        }

        return chunks;
    }

    private int getRFP(byte[] fileBuffer, int curPos, int prev) {
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
}
