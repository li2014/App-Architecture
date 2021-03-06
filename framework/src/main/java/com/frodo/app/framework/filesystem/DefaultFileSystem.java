package com.frodo.app.framework.filesystem;

import com.frodo.app.framework.controller.AbstractChildSystem;
import com.frodo.app.framework.controller.IController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 默认java 文件操作
 * Created by frodo on 2015/7/4.
 */
public class DefaultFileSystem extends AbstractChildSystem implements FileSystem {

    private String rootDir;
    private String filePath;

    public DefaultFileSystem(IController controller) {
        super(controller);
        this.rootDir = controller.getMicroContext().getRootDirName();
        this.filePath = controller.getMicroContext().getFilesDirName();

        canUsed();
    }

    @Override
    public boolean canUsed() {
        File file = new File(filePath);
        return file.exists() ? true : file.mkdirs();
    }

    @Override
    public String getRootDir() {
        return this.rootDir;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public File createFile(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            return file;
        } else {
            boolean success = false;
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (success) {
                return file;
            }
        }

        return null;
    }

    @Override
    public File createDirectory(String dirName) {
        File file = new File(dirName);
        if (file.exists()) {
            return file;
        } else {
            final boolean success = file.mkdirs();
            if (success) {
                return file;
            }
        }
        return null;
    }

    /**
     * Writes a file to Disk.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform this operation using another thread.
     *
     * @param file The file to write to Disk.
     */
    @Override
    public void writeToFile(File file, String fileContent, boolean append) {
        try {
            FileWriter writer = new FileWriter(file, append);
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a content from a file.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param file The file to read from.
     * @return A string with the content of the file.
     */
    @Override
    public String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            String stringLine;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine).append("\n");
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileContentBuilder.toString();
    }

    /**
     * Returns a boolean indicating whether this file can be found on the underlying file system.
     *
     * @param file The file to check existence.
     * @return true if this file exists, false otherwise.
     */
    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    /**
     * Warning: Deletes the content of a directory.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param directory The directory which its content will be deleted.
     */
    @Override
    public void clearDirectory(File directory) {
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }
}
