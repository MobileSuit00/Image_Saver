import utils.Cutter;
import java.util.List;
import java.util.Scanner;

public class ImageMain {
    public static void main(String[] args) {
        Cutter fileNameCutter = new Cutter();
        Scanner scanner = new Scanner(System.in);
        String imageUrl = scanner.nextLine();
        List<String> tagsOfUrl = fileNameCutter.tagsDeterminant(imageUrl);
        Files imageSaver = new Files(imageUrl, fileNameCutter.getFileExtension(imageUrl), tagsOfUrl);
        String dir = imageSaver.findDir(tagsOfUrl);
        imageSaver.downloadFile(dir);
        imageSaver.cuttFile(dir);
    }
}
