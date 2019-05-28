package club.spreadme.basic.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public abstract class FileUtil {

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    public static String getFileDirectory(String path){
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);

        return "";
    }

    public static File createFile(String path, boolean isFile) throws IOException {
        path = path.replaceAll("\\*", "/");
        File file = new File(path);
        if(!file.exists()){
            if(isFile){
                String directoryPath = path.substring(0, path.lastIndexOf(FileSystems.getDefault().getSeparator()));
                File directoryFile = new File(directoryPath);
                if(!directoryFile.exists()){
                    directoryFile.mkdirs();
                }
                file.createNewFile();
                return file;
            }
            else{
                file.mkdirs();
                return file;
            }
        }
        return file;
    }
}
