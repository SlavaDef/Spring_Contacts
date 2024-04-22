package ua.kiev.prog.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UniversalReader {

    private static final String CONTAKTS_URL = "https://iportal.com.ua/wp-content/uploads/Contacts.xml";
    private static final String CONTAKTS_URL2 = "https://senior-pomidor.com.ua/Contacts25.xml";

    // метод читає з сервака json/xml фаіл і записує данні у корінь проєкта створюючи новий json/xml файл
    public static void universalParser(){
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URL url = new URL(CONTAKTS_URL); // тут треба json чи xml на серваці
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();

                File file = new File("contacts444.xml"); // новий файл куди записуємо or contacts333.json
                outputStream = new FileOutputStream(file);

                int bytesRead = -1; // -1 це кінець файлу зчитуємо дані по 1мб
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            }

        } catch (IOException e) {
            System.out.println("Internet connection error" + e.toString());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                System.out.println("Error closing input stream" + e.toString());
            }

        }
    }

    public static void main(String[] args) {
        universalParser();
    }
}
