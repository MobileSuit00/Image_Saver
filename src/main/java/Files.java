import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.Cutter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Files {
    public String path;
    public String extension;
    public List<String> listOfTags;

    public Files(String path, String extention, List<String> listOfTags) {
        this.path = path;
        this.extension = extention;
        this.listOfTags = listOfTags;
    }

    public void downloadFile(String toFile) {
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        try {
            URL website = new URL(this.path);
            rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(toFile + this.extension);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (rbc != null) {
                try {
                    rbc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cuttFile(String toFile) {
        System.out.println("Start to find and cut watermark...");
        File file = new File(toFile + this.extension);
        Color[] colors = new Color[14];
        int countOfCurrentPixel = 0;
        int countOfAllPixel = 0;
        try {
            BufferedImage source = ImageIO.read(file);
            for (int x = source.getHeight() - 14; x < source.getHeight(); x++) {
                Color color = new Color(source.getRGB(source.getWidth() - 1, x));
                colors[countOfAllPixel] = color;
                countOfAllPixel++;
                if (color.getRed() >= 227 &&  color.getRed() <= 255 && color.getGreen() >= 172 &&
                        color.getGreen() <= 205 && color.getBlue() >= 0 && color.getBlue() <= 115) {
                    countOfCurrentPixel++;
                }
            }

            if (countOfCurrentPixel == 14) {
                System.out.println("Success!\n");
                File outputfile = new File(toFile + this.extension);
                ImageIO.write(source.getSubimage(0, 0, source.getWidth(), source.getHeight() - 14),
                        this.extension, outputfile);
            } else {
                System.out.println("Watermark not found!");
                for (Color color: colors) {
                    System.out.println(color);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findDir(List<String> tags) {
        System.out.println("Start to find current directory in files...");
        Cutter cutter = new Cutter();
        File dir = new File("E:\\Anime");
        File[] files = dir.listFiles();
        List<String> partsOfDirName;
        String currentDir = "";
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    partsOfDirName = cutter.dirNameDeterminant(file.getName());
                    if (tags.containsAll(partsOfDirName)) {
                        System.out.println("Success!\n");
                        dir = new File("E:\\Anime\\" + file.getName() + "\\Обои");
                        currentDir = dir.toString() + "\\" + (Objects.requireNonNull(dir.listFiles()).length + 1 + ".");
                        System.out.println("Dir name: " + currentDir);
                    }
                }
            }
        }

        if (currentDir.equals("")) {
            System.out.println("Directory not found\n");
            System.out.println("Start to find directory in excel file...");
            FileInputStream file;
            try {
                file = new FileInputStream(new File("E:\\Anime\\Other\\Подлежит сортировке\\Теги.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook (file);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIter = sheet.rowIterator();

                while (rowIter.hasNext()) {
                    XSSFRow row = (XSSFRow) rowIter.next();
                    XSSFCell cell = row.getCell(0);
                    if (tags.containsAll(cutter.dirNameDeterminant(cell.toString()))) {
                        dir = new File("E:\\Anime\\" + row.getCell(1) + "\\Обои");
                        currentDir = dir.toString() + "\\" + (Objects.requireNonNull(dir.listFiles()).length
                                + 1 + ".");
                        System.out.println("Dir name: " + currentDir);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (currentDir.equals("")) {
            System.out.println("Directory not found\n");
        } else {
            System.out.println("Success!\n");
        }

        return currentDir;
    }
}
