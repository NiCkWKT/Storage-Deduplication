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

    public static void main(String[] args) throws Exception{
        // Check the number of arguments
        if (args.length < 2) {
            System.out.println("Invalid number of arguments");
            System.out.println("Usage: java MyDedup upload <min_chunk> <avg_chunk> <max_chunk> <d> <file_to_upload>");
            System.out.println("Or: java MyDedup download <file_to_download> <local_file_name>");
            return;
        }

        // Get the first argument
        String operation = args[0];

        // Switch on the operation
        switch (operation) {
            case "upload":
                // Check the number of arguments for upload
                if (args.length != 6) {
                    System.out.println("Invalid number of arguments for upload");
                    System.out.println("Usage: java MyDedup upload <min_chunk> <avg_chunk> <max_chunk> <d> <file_to_upload>");
                    return;
                }
                // Parse and validate the arguments for upload
                try {
                    int min_chunk = Integer.parseInt(args[1]);
                    int avg_chunk = Integer.parseInt(args[2]);
                    int max_chunk = Integer.parseInt(args[3]);
                    int d = Integer.parseInt(args[4]);
                    String file_to_upload = args[5];
                    // Call the upload method
//                    upload(min_chunk, avg_chunk, max_chunk, d, file_to_upload);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid integer argument: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid argument: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IO error: " + e.getMessage());
                }
                break;
            case "download":
                // Check the number of arguments for download
                if (args.length != 3) {
                    System.out.println("Invalid number of arguments for download");
                    System.out.println("Usage: java MyDedup download <file_to_download> <local_file_name>");
                    return;
                }
                // Get and validate the arguments for download
                String file_to_download = args[1];
                String local_file_name = args[2];
                // Call the download method
                try {
//                    download(file_to_download, local_file_name);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid argument: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IO error: " + e.getMessage());
                }
                break;
            default:
                // Invalid operation
                System.out.println("Invalid operation: " + operation);
                System.out.println("Usage: java MyDedup upload <min_chunk> <avg_chunk> <max_chunk> <d> <file_to_upload>");
                System.out.println("Or: java MyDedup download <file_to_download> <local_file_name>");
        }
    }
}