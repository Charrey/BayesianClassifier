package main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Administrator on 27-11-2015.
 */
public final class Builder {

    public static String giantstring = "";

    public static void main(String[] args) {
        File folder = new File("./input");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (i%10==0) {
                System.out.println(i+"/"+listOfFiles.length);
            }

            if (listOfFiles[i].isFile()) {
                //System.out.println("File " + listOfFiles[i].getName());
                loadIndividualFile(listOfFiles[i]);
            }
        }


        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("raw.xml", true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(giantstring);
        out.flush();
        out.close();




        System.out.println("Done.");
    }



    private static void loadIndividualFile(File file) {

        try {
            Files.deleteIfExists(Paths.get("raw.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

         BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Can't happen
        }

        String line = null;
        String total = "";
        try {

            while ((line = br.readLine()) != null) {

                if (line.length()>0) {
                    if (line.startsWith("Subject: ")) {
                        giantstring+=line+" ";
                    } else {
                        giantstring+=line+"\n";
                    }
                }
            }
        br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }





