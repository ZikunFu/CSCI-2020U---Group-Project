package sample;

import java.io.*;
import java.util.Scanner;

/**
 * this class is used for file management, CSV files specifically
 * including numerous methods for reading/writing files.
 */
public class fileManager {
    fileManager(){}

    /**
     * This method is used to create the default
     * user Profile csv file
     * @param file the target directory
     * @throws IOException
     */
    public void createProfile(File file) throws IOException {
        //make a directory if the given directory does not exisit
        if(!file.exists()){
            file.mkdirs();
        }
        FileWriter csvWriter = new FileWriter(file);
        csvWriter.append("username");
        csvWriter.append(",");
        csvWriter.append("password");
        csvWriter.append(",");
        csvWriter.append("hp");
        csvWriter.append(",");
        csvWriter.append("attack");
        csvWriter.append(",");
        csvWriter.append("defence");
        csvWriter.append(",");
        csvWriter.append("rank");
        csvWriter.append(",");
        csvWriter.append("item");
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
    }

    /**
     * This method is used to append data to the target CSV file
     * @param path the target location
     * @param data the String to write
     * @throws IOException
     */
    public void appendCSV(File path, String data) throws IOException {
        FileWriter csvWriter = new FileWriter(path,true);
        csvWriter.append(data);
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
    }

    /**
     * This method is used to find target string given the column
     * @param path the target file
     * @param target the target String
     * @param column the target column in file
     * @return true of false given the existence of target string
     * @throws IOException
     */
    public boolean matchCSV(File path, String target, int column) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String nextLine;
        boolean found = false;
        while ((nextLine = br.readLine())!=null){
            String[] col = nextLine.split(",");
            String str = col[column];
            if(str.equals(target)){
                found = true;
            }
        }
        br.close();
        return found;
    }

    /**
     * This method is used to find target string given the column
     * then returns the whole row back as string
     * @param path the target file
     * @param target the target String
     * @param column the target column in file
     * @return The String with the row containing target String
     * @throws IOException
     */
    public String searchCSV(File path, String target, int column) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String nextLine;
        String targetLine="";
        while ((nextLine = br.readLine())!=null){
            String[] col = nextLine.split(",");
            String str = col[column];
            if(str.equals(target)){
                targetLine=nextLine;
            }
        }
        return targetLine;
    }
}
