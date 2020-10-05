package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Cutter {
    public String getFileExtension(String fileUrl) {
        File file = new File(fileUrl);
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else {
            return "";
        }
    }

    public List<String> tagsDeterminant(String fileUrl) {
        File file = new File(fileUrl);
        String fileName = file.getName();
        String[] tags;
        String nonSortTags = fileName.substring(fileName.indexOf('/') + 1, fileName.indexOf('.'));
        tags = nonSortTags.split("-");
        for (String tag : tags) {
            StringBuilder sb = new StringBuilder(tag.toLowerCase());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            System.out.println(tag);
        }

        return Arrays.asList(tags);
    }

    public List<String> dirNameDeterminant(String dirName) {
        return Arrays.asList(dirName.split(" "));
    }
}
