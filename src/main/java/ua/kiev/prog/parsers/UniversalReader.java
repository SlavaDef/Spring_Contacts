package ua.kiev.prog.parsers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class UniversalReader {

    public static final String CONTAKTS_URL = "https://iportal.com.ua/wp-content/uploads/Contacts.xml";
    public static final String CONTAKTS_URL2 = "https://senior-pomidor.com.ua/Contacts25.xml";

    List<String> red = new ArrayList<>();

    // метод читає з сервака json/xml фаіл і записує данні у корінь проєкта створюючи новий json/xml файл
    public static void universalParser() {
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


    // метод зчитає з сайту xml файл і запише все у String чи StringBuilder
    public String readerXml() throws Exception {
        String res = null;
        URL url = new URL(CONTAKTS_URL2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(conn.getInputStream()))) {
            // StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                red.add(line);
                res += line;
                //   System.out.println(line);
                //  result.append(line);
            }
            //  res = result.toString();
        }

        return res;
        //   File file = new File(Url);
        //  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //  return dbFactory.newDocumentBuilder().parse(file);
    }
}

