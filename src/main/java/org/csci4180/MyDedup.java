package org.csci4180;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class MyDedup {
    public static boolean isPowerOfTwo(int n) {
        // Check if the number is positive
        if (n <= 0) {
            return false;
        }
        // Perform bitwise and on the number and the number minus 1
        int result = n & (n - 1);
        // Check if the result is zero
        if (result == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void checkChunkSizeParameters(int min_chunk, int avg_chunk, int max_chunk) throws IllegalArgumentException {
        // Loop through the chunk size parameters
        int[] params = {min_chunk, avg_chunk, max_chunk};
        for (int param : params) {
            // Check if the parameter is a power of 2
            if (!isPowerOfTwo(param)) {
                // Throw an error with a message
                throw new IllegalArgumentException("Chunk size parameter " + param + " is not a power of 2");
            }
        }
    }
    public static void main(String[] args) throws Exception{
        // Check the number of arguments
        if (args.length < 2) {
            System.err.println("Invalid number of arguments");
            System.err.println("Usage: java MyDedup upload <min_chunk> <avg_chunk> <max_chunk> <d> <file_to_upload>");
            System.err.println("Or: java MyDedup download <file_to_download> <local_file_name>");
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
                    checkChunkSizeParameters(min_chunk, avg_chunk, max_chunk);
                    String file_to_upload = args[5];
                    File f = new File(FileRecipe.getPath(file_to_upload));
                    if(f.exists()) {
                        System.out.println("Error: File already uploaded.");
                        System.exit(0);
                    }
                    byte[] fileBuffer = Files.readAllBytes(Paths.get(file_to_upload));
                    Chunker chunker = new Chunker(min_chunk, avg_chunk, max_chunk, d);
                    CloudStorage cloud = new CloudStorage();
                    cloud.upload(chunker.getChunks(fileBuffer), file_to_upload, fileBuffer.length);

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
                CloudStorage cloud = new CloudStorage();
                try {
                    cloud.download(file_to_download, local_file_name);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid argument: " + e.getMessage());
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