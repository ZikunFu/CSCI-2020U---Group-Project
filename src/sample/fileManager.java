package sample;

import java.io.*;
import java.util.Scanner;

/**
 * this class is used to realize the functions during connection
 * including "read path", "read file" "write file" "find file" "make file list"
 */
public class fileManager {
    fileManager(){}

    /**
     * this method is used to save the content in the file
     * and used to transfer From Clint to Server by reading the path
     * also used to "view" the file inside
     * the data is the content about the file
     * @param path the string that includes the file path
     * @return the file's content
     * @throws FileNotFoundException
     */
    public String readPath(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner fileReader = new Scanner(file);
        StringBuilder data = new StringBuilder();
        while (fileReader.hasNextLine()) {
            data.append(fileReader.nextLine());
        }
        fileReader.close();
        return data.toString();
    }

    /**
     * same as the last method, however
     * this time is used for the content "download" to client
     * and the parameter is the file instead of the path
     * @param file the file about to transfer
     * @return the content of the file
     * @throws FileNotFoundException
     */
    public String readFile(File file) throws FileNotFoundException {
        Scanner fileReader = new Scanner(file);
        StringBuilder data = new StringBuilder();
        while (fileReader.hasNextLine()) {
            data.append(fileReader.nextLine());
        }
        fileReader.close();
        return data.toString();
    }

    /**
     * this method is used to store a file
     * @param path the destination path
     * @param data the content about to deliver
     * @throws IOException
     */
    public void writeFile(String path,String data) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.append(data);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * this method is used to find the file that is targeted
     * used for-loop in files
     * when the name is matched, then return the file
     * @param fileName the target file's name
     * @param dir directory of files
     * @return file we want
     */
    public File findFile(String fileName, File dir){
        for (File file : dir.listFiles()) {
            if(file.getName().equals(fileName)){ return file; }
            else if(file.isDirectory()){
                for(File file1 : file.listFiles()){
                    if(file1.getName().equals(fileName)){ return file1; }
                }
            }
        }
        return null;
    }

    /**
     * this method is used to return a list of files given a directory
     * @param dir directory of files
     * @return the list of the files
     */
    public String getFileList(File dir){
        String temp="";
        for (File file : dir.listFiles()) {
            if(file.isFile()){ temp+=file.getName()+" "; }
        }

        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                temp+="@"+file.getName()+" ";
                for(File subFile : file.listFiles()){
                    temp+=subFile.getName()+" ";
                }
            }
        }
        return temp;
    }

    public void createProfile(File file) throws IOException {
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

    //data will be <apple banana candy>
    public void appendCSV(File path, String data) throws IOException {
        FileWriter csvWriter = new FileWriter(path,true);
        csvWriter.append(data);
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();

    }

    //search for target String in specific column of CSV
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
