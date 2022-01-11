package DStVParserCore;

import Misc.DStVParseEx;
import Primitives.DStVElement;
import Primitives.DStVHole;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DSTVComponentParser {
    File file;

    public DSTVComponentParser(File file) {
        this.file = file;
    }

    public Set<DStVElement> getElemSet() {
        Set<DStVElement> outSet = new HashSet<>();
        Map<String, Set<String>> elemMap = getElemMap();
        Set<String> holeSet = elemMap.get("BO");
        if (holeSet != null) {
            for (String holeSigh : holeSet) {
                try {
                    outSet.add(DStVHole.createHole(holeSigh));
                } catch (DStVParseEx DStV_parseEx) {
                    DStV_parseEx.printStackTrace();
                    System.out.println(DStV_parseEx.getMessage());
                }
            }
        }

        return outSet;
    }

    /**
     * Получаем карту "тип элемента - набор data-line типа"
     *
     * @return
     */
    private Map<String, Set<String>> getElemMap() {
        Map<String, Set<String>> elemMap = new HashMap<>();
        try (FileReader fr = new FileReader(file)) {
            BufferedReader reader = new BufferedReader(fr);

            String curKey = null;
            String line = reader.readLine();
            while (line != null) {
                //if has END-mark
                if (line.equals("EN")) break;

                //if has quote-sign
                if (line.matches("^\\*\\*.*")) {
                    line = reader.readLine();
                    continue;
                }

                if (checkIfMark(line)) {
                    curKey = line;

                    if (!elemMap.containsKey(curKey)) {
                        elemMap.put(curKey, new HashSet<>());
                    }

                    line = reader.readLine();
                    continue;
                }

                if (curKey == null) {
                    line = reader.readLine();
                    continue;
                }

                elemMap.get(curKey).add(line);
                //получаем новую строку
                line = reader.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return elemMap;
    }

    /**
     * Возвращает true если строка является меткой блока DStV-кода
     *
     * @param str любая строка-претендент
     * @return matching result
     */
    private boolean checkIfMark(String str) {
        //there are some more patterns in DStV, but they are too seldom and/or is not necessary yet
        return str.matches("^BO$|^SI$|^AK$|^IK$|^PU$|^KO$|^SC$|^UE$|^KA$|^EN$|" +
                "^E[0-9]$|^B[0-9]$|^S[0-9]$|^A[0-9]$|^I[0-9]$|^P[0-9]$|^K[0-9]$");
    }
}
