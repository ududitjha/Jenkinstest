package in.nkn.pojos;

import java.io.File;
import java.io.InputStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Manish Kumar
 */
public class NKNFile {

    private String fileName;
    private long fileSizeInKb;
    private String fileType;
    private String fileContainerName;
    private String fileMD5;
    private File file;

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public long getFileSizeInKb() {
        return fileSizeInKb;
    }

    public void setFileSizeInKb(long fileSizeInKb) {
        this.fileSizeInKb = fileSizeInKb;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileContainerName() {
        return fileContainerName;
    }

    public void setFileContainerName(String fileContainerName) {
        this.fileContainerName = fileContainerName;
    }
}
