package org.csci4180;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRecipe {
    public static final String DIR = "data/recipe/";
    List<FingerPrint> fpList;
    List<Integer> sizeList;

    public FileRecipe() {
        fpList = new ArrayList<>();
        sizeList = new ArrayList<>();
    }

    public FileRecipe(String path) {
        File file = new File(getInputPath(path));
        if(!file.exists()) {
            System.err.println("Error: File does not exist in Recipe");
            System.exit(1);
        };

        fpList = new ArrayList<>();
        sizeList = new ArrayList<>();
//        System.out.println(file.getAbsolutePath());
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);

            int size = in.readInt();
            fpList = new ArrayList<>(size);
            sizeList = new ArrayList<>(size);

//            System.out.println("SIZE " + size);

            for(int i = 0; i < size; i++) {
                FingerPrint fp = new FingerPrint();
                if (in.available() > 0) {
                    fp.readFrom(in);
                    fpList.add(fp);
                }
                int s = 0;
                if (in.available() > 0) {
                    s = in.readInt();
                }
                sizeList.add(s);
            }
//            System.out.println("fplist size " + fpList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Catched Exception in FileRecipe");
            fpList = new ArrayList<>();
            sizeList = new ArrayList<>();
        }

    }

    public void add(FingerPrint fp, int size) {
        fpList.add(fp);
        sizeList.add(size);
    }

    public List<FingerPrint> getFpList() {
        return fpList;
    }

    public void setFpList(List<FingerPrint> fpList) {
        this.fpList = fpList;
    }

    public List<Integer> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<Integer> sizeList) {
        this.sizeList = sizeList;
    }

    public void writeTo(String path) throws Exception {
        File file = new File(getInputPath(path));
        if(file.exists()) {
            boolean isDeleted = file.delete();
        }
        else {
            file.getParentFile().mkdirs();
        }

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        int size = fpList.size();
//        System.out.println(size);
        out.writeInt(size);
        for(int i = 0; i < size; i++) {
            fpList.get(i).writeTo(out);
            out.writeInt(sizeList.get(i));
        }
        out.flush();
    }

    public static String getInputPath(String path) {
        return DIR + hashString(path);
    }

    public static String hashString(String path) {
        String s = "0123456789ABCDEF";
        char[] ca = s.toCharArray();
        char[] hexChars = new char[path.length() * 2];

        for (int i = 0; i < path.length(); i++) {
            int v = path.charAt(i) & 0xFF;
            hexChars[i * 2] = ca[v >>> 4];
            hexChars[i * 2 + 1] = ca[v & 0x0F];
        }

        return new String(hexChars);
    }
}
