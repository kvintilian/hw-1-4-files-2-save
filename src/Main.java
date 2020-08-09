import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        String savePath = "/Users/andrejzemlacev/Desktop/Games/savegames/";

        List<GameProgress> gameProgresses = Arrays.asList(
                new GameProgress(100, 23, 3, 23.5),
                new GameProgress(80, 10, 10, 53.5),
                new GameProgress(95, 3, 4, 10)
        );

        List<String> savedFiles = new ArrayList<>();
        for (int i = 0; i < gameProgresses.size(); i++) {
            String datPath = savePath + "save" + i + ".dat";
            if (saveGame(datPath, gameProgresses.get(i)))
                savedFiles.add(datPath);
        }

        zipFiles(savePath + "zip.zip", savedFiles);
        deleteFiles(savedFiles);
    }

    public static Boolean saveGame(String path, GameProgress gp) {
        try (FileOutputStream stream = new FileOutputStream(path);
             ObjectOutputStream outputStream = new ObjectOutputStream(stream)) {
            outputStream.writeObject(gp);
            System.out.println("Сохранение записано: " + path);
            return true;
        } catch (Exception ex) {
            System.out.println("Ошибка записи сохранения: " + ex.getMessage());
            return false;
        }
    }

    public static void zipFiles(String path, List<String> savedFiles) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path))) {
            System.out.println("Создание архива: " + path);
            for (String sf : savedFiles) {
                try (FileInputStream fileInputStream = new FileInputStream(sf)) {
                    ZipEntry entry = new ZipEntry(new File(sf).getName());
                    zipOutputStream.putNextEntry(entry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                    System.out.println("Файл " + sf + " добавлен в архив!");
                } catch (IOException ex) {
                    System.out.println("Ошибка сжатия файлов сохранения: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("Ошибка сжатия файлов сохранения: " + ex.getMessage());
        }
    }

    private static void deleteFiles(List<String> savedFiles) {
        for (String sf : savedFiles) {
            File fileToDel = new File(sf);
            if (fileToDel.delete()) {
                System.out.println("Файл " + sf + " удален!");
            } else {
                System.out.println("Файл " + sf + " не был удален!");
            }
        }
    }
}
