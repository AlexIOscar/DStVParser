package DStVParserCore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DSTVComponentParser {
    File file;

    public DSTVComponentParser(File file) {
        this.file = file;
    }

    public Map<String, String> getElemMap() {
        Map<String, String> elemList = new HashMap<>();
        try (FileReader fr = new FileReader(file)) {
            BufferedReader reader = new BufferedReader(fr);

            String curKey = null;
            String line = reader.readLine();
            while (line != null) {
                if (checkIfMark(line)) {
                    curKey = line;
                    line = reader.readLine();
                    continue;
                }
                if (curKey == null) {
                    line = reader.readLine();
                    continue;
                }
                elemList.put(curKey, line);
                //получаем новую строку
                line = reader.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return elemList;
    }

    /**
     * Возвращает true если строка является меткой блока DStV-кода
     *
     * @param str любая строка-претендент
     * @return matching result
     */
    private boolean checkIfMark(String str) {
        //there are some more patterns in DStV, but they are too seldom and/or is not necessary yet
        return str.matches("^BO$|^SI$|^AK$|^IK$|^PU$|^KO$|^SC$|^UE$|^KA$|" +
                "^E[0-9]$|^B[0-9]$|^S[0-9]$|^A[0-9]$|^I[0-9]$|^P[0-9]$|^K[0-9]$");
    }
}
