package dstvutility.parsecore;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.primitives.DstvContourPoint;
import dstvutility.primitives.DstvElement;
import dstvutility.primitives.DstvHole;
import dstvutility.primitives.DstvNumeration;
import dstvutility.primitives.synthetic.Contour;

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

        List<String> outerPointSignList = elemMap.get("AK");

        if (outerPointSignList != null) {
            int startPoint = outList.size();
            for (String pointSigh : outerPointSignList) {
                try {
                    outList.add(DstvContourPoint.createPoint(pointSigh));
                } catch (DstvParseEx DStV_parseEx) {
                    DStV_parseEx.printStackTrace();
                }
            }

            int endPoint = outList.size() - 1;
            List<DstvContourPoint> contoursPoints = new ArrayList<>();
            for (int i = startPoint; i <= endPoint; i++) {
                contoursPoints.add((DstvContourPoint) outList.get(i));
            }

            try {
                outList.addAll(Contour.createContList(contoursPoints, Contour.ContourType.AK));
            } catch (DstvParseEx dstvParseEx) {
                dstvParseEx.printStackTrace();
            }
        }

        List<String> numerationSignList = elemMap.get("SI");
        if (numerationSignList != null) {
            for (String numSigh : numerationSignList) {
                try {
                    outList.add(DstvNumeration.createNumeration(numSigh));
                } catch (DstvParseEx DStV_parseEx) {
                    DStV_parseEx.printStackTrace();
                }
            }
        }
        return outList;
    }

    /**
     * Получаем карту "тип элемента - список data-line типа"
     *
     * @return
     */
    private Map<String, List<String>> getElemMap() {
        Map<String, List<String>> elemMap = new HashMap<>();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "CP1251")) {
            BufferedReader reader = new BufferedReader(isr);

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
            reader.close();
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

    /*
    Список сплиттеров - экземпляров интерфейса, реализующих различные способы разделения входящей строки на лексемы,
    и возвращающих их в виде массива строк
     */

    /**
     * Splitter for full carefully splitting - saving all lexemes
     */
    public static Splitter fineSplitter = str -> str.split("(?<=\\d)(?=[a-z])|(?<=[a-z])(?=\\d)|(?<=[\\d\\w.]) +");

    /**
     * Splitter for rough splitting - delete letter-sequence lexemes between two digits (without additional spaces)
     */
    public static Splitter roughSplitter = str -> str.split("(?<!\\s|\\D)[a-z]+(?!\\s+|\\D)|\\s+");

    public static Splitter positionNumSplitter = str -> {
        List<String> outList = new ArrayList<>();
        outList.add(str.substring(0, 2));
        outList.add(str.substring(2, 3));
        outList.add(str.substring(3, 15));
        outList.add(str.substring(15, 25));
        outList.add(str.substring(25, 31));
        outList.add(str.substring(31, 35));
        outList.add(str.substring(35, 36));
        outList.add(str.substring(36));
        String[] outStr = new String[0];
        return outList.toArray(outStr);
    };

    /**
     * Вытаскивает подстроки, представляющие числа, с фиксированным количеством знаков целого и десятичного
     * представления от точки. Прототип, не доработан
     */
    private static Splitter dotSplitter = str -> {
        int intDigits = 4;
        int n = 3;
        int[] dotPoss = new int[n];
        for (int i = 0; i < n; i++) {
            int prevPos = 0;
            if (i > 0) {
                prevPos = dotPoss[i - 1];
            }
            dotPoss[i] = str.indexOf(".", prevPos);
        }
        String[] outArr = new String[n];
        for (int i = 0; i < n; i++) {
            outArr[i] = str.substring(dotPoss[i] - intDigits, dotPoss[i] + 2);
        }
        return outArr;
    };

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
