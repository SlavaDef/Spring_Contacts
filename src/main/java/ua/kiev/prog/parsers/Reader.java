package ua.kiev.prog.parsers;

import ua.kiev.prog.models.Group;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ua.kiev.prog.constants.Constants.CONTAKTS_URL2;

public class Reader {

    Group group2 = new Group();


    List<String> red = new ArrayList<>();
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
