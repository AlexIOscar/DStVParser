package dstvutility.parsercore;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.primitives.DstvContourPoint;
import dstvutility.primitives.DstvElement;
import dstvutility.primitives.DstvHole;

import java.io.*;
import java.util.*;

public class DstvComponentParser {
    File file;

    public DstvComponentParser(File file) {
        this.file = file;
    }

    public List<DstvElement> getElemList() {
        List<DstvElement> outList = new ArrayList<>();
        Map<String, List<String>> elemMap = getElemMap();
        List<String> holeList = elemMap.get("BO");
        if (holeList != null) {
            for (String holeSigh : holeList) {
                try {
                    outList.add(DstvHole.createHole(holeSigh));
                } catch (DstvParseEx DStV_parseEx) {
                    DStV_parseEx.printStackTrace();
                }
            }
        }

        List<String> outerPointList = elemMap.get("AK");

        if (outerPointList != null) {
            int startPoint = outList.size();
            for (String pointSigh : outerPointList) {
                try {
                    outList.add(DstvContourPoint.createPoint(pointSigh));
                } catch (DstvParseEx DStV_parseEx) {
                    DStV_parseEx.printStackTrace();
                    System.out.println(DStV_parseEx.getMessage());
                }
            }
            int endPoint = outList.size() - 1;

        }

        return outList;
    }

    /**
     * Получаем карту "тип элемента - набор data-line типа"
     *
     * @return
     */
    private Map<String, List<String>> getElemMap() {
        Map<String, List<String>> elemMap = new HashMap<>();
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
                        elemMap.put(curKey, new ArrayList<>());
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

    /**
     * Splitter for full carefully splitting - saving all lexemes
     */
    public static Splitter fineSplitter = str -> str.split("(?<=\\d)(?=[a-z])|(?<=[a-z])(?=\\d)| +");

    /**
     * Splitter for rough splitting - delete letter-sequence lexemes between two digits (without additional spaces)
     */
    public static Splitter roughSplitter = str -> str.split("(?<!\\s|\\D)[a-z]+(?!\\s+|\\D)|\\s+");

    /**
     * кривовато... возможно потеря перформанса
     *
     * @param toBeRefined String array to be refined from voids (empty strings)
     * @return Refined array
     */
    public static String[] removeVoids(String[] toBeRefined) {
        List<String> tempList = new ArrayList<>(Arrays.asList(toBeRefined));
        tempList.removeAll(Collections.singleton(""));
        toBeRefined = tempList.toArray(new String[]{});
        return toBeRefined;
    }
}
