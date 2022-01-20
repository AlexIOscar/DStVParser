package dstvutility.parsecore;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.primitives.*;
import dstvutility.primitives.synthetic.Contour;

import java.io.*;
import java.util.*;

public class DstvComponentParser {
    File file;

    public DstvComponentParser(File file) {
        this.file = file;
    }

    public List<DstvElement> getElemList() {

        List<DstvElement> outputList = new ArrayList<>();
        Map<String, List<List<String>>> elemMap = getElemMap();

        //holes & slots
        List<List<String>> holeBlocks = elemMap.get("BO");
        if (holeBlocks != null) {
            for (List<String> holeList : holeBlocks) {
                for (String holeNote : holeList) {
                    try {
                        outputList.add(DstvHole.createHole(holeNote));
                    } catch (DstvParseEx DStV_parseEx) {
                        DStV_parseEx.printStackTrace();
                    }
                }
            }
        }

        //TODO добавлять либо точки, либо контуры. Возможно, добавляя контур, нужно удалять вошедшие в него точки
        //outer contours
        List<List<String>> outerBorders = elemMap.get(Contour.ContourType.AK.signature);
        addContoursByType(outputList, outerBorders, Contour.ContourType.AK);

        //inner contours
        List<List<String>> innerBorders = elemMap.get(Contour.ContourType.IK.signature);
        addContoursByType(outputList, innerBorders, Contour.ContourType.IK);

        //powder contours
        List<List<String>> powderPointNotes = elemMap.get(Contour.ContourType.PU.signature);
        addContoursByType(outputList, powderPointNotes, Contour.ContourType.PU);

        //punch contours
        List<List<String>> punchPointNotes = elemMap.get(Contour.ContourType.KO.signature);
        addContoursByType(outputList, punchPointNotes, Contour.ContourType.KO);

        //numeration
        List<List<String>> numerationBlocks = elemMap.get("SI");
        if (numerationBlocks != null) {
            for (List<String> numList : numerationBlocks) {
                for (String numNote : numList) {
                    try {
                        outputList.add(DstvNumeration.createNumeration(numNote));
                    } catch (DstvParseEx DStV_parseEx) {
                        DStV_parseEx.printStackTrace();
                    }
                }
            }
        }

        //bends
        List<List<String>> bendBlocks = elemMap.get("KA");
        if (bendBlocks != null) {
            for (List<String> bendList : bendBlocks) {
                for (String bendNote : bendList) {
                    try {
                        outputList.add(DstvBend.createBend(bendNote));
                    } catch (DstvParseEx dstvParseEx) {
                        dstvParseEx.printStackTrace();
                    }
                }
            }
        }
        return outputList;
    }

    private void addContoursByType(List<DstvElement> outElemList,
                                   List<List<String>> notesBlockList,
                                   Contour.ContourType type) {

        if (notesBlockList == null) return;

        for (List<String> noteList : notesBlockList) {
            List<DstvContourPoint> localList = new ArrayList<>();
            for (String pointNote : noteList) {
                try {
                    localList.add(DstvContourPoint.createPoint(pointNote));
                } catch (DstvParseEx DStV_parseEx) {
                    DStV_parseEx.printStackTrace();
                }
            }

            try {
                outElemList.addAll(Contour.createSeveralContours(localList, type));
            } catch (DstvParseEx dstvParseEx) {
                dstvParseEx.printStackTrace();
            }
        }
    }

    /**
     * Получаем карту "тип элемента - список списков data-line для этого типа"
     *
     * @return
     */
    private Map<String, List<List<String>>> getElemMap() {
        Map<String, List<List<String>>> elemMap = new HashMap<>();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(file), "CP1251"))) {

            String curKey = null;
            String line = reader.readLine();
            while (line != null) {
                //if has END-mark.
                // Maybe to be refactored for multi-peace file processing
                if (line.equals("EN")) break;

                //if has quote-sign
                if (line.matches("^\\*\\*.*")) {
                    line = reader.readLine();
                    continue;
                }

                if (checkCodeLine(line)) {
                    curKey = line;

                    if (!checkIfMark(line)) {
                        System.out.println("Warning: unregistered DStV code-line detected: " + line);
                    }

                    if (!elemMap.containsKey(curKey)) {
                        elemMap.put(curKey, new ArrayList<>());
                    }
                    elemMap.get(curKey).add(new ArrayList<>());

                    line = reader.readLine();
                    continue;
                }

                if (curKey == null) {
                    line = reader.readLine();
                    continue;
                }

                List<List<String>> outerList = elemMap.get(curKey);
                outerList.get(outerList.size() - 1).add(line);
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
        return str.matches("^BO$|^SI$|^AK$|^IK$|^PU$|^KO$|^SC$|^UE$|^KA$|^EN$|^ST$|" +
                "^E[0-9]$|^B[0-9]$|^S[0-9]$|^A[0-9]$|^I[0-9]$|^P[0-9]$|^K[0-9]$");
    }

    /**
     * Возвращает true, если строка начинается с любой комбинации заглавных латинских букв или десятичных цифр в
     * количестве ровно двух.
     *
     * @param str любая строка-претендент
     * @return matching result
     */
    private boolean checkCodeLine(String str) {
        return str.matches("^[A-Z0-9]{2}$");
    }

    /*
    Список сплиттеров - экземпляров интерфейса, реализующих различные способы разделения входящей строки на лексемы,
    и возвращающих их в виде массива строк
     */

    /**
     * Splitter for full carefully splitting - saving all lexemes
     */
    public static final Splitter FINE_SPLITTER = str -> str.split("(?<=\\d)(?=[a-z])|(?<=[a-z])(?=\\d)|(?<=[\\d\\w.]) +");

    /**
     * Splitter for rough splitting - delete letter-sequence lexemes between two digits (without additional spaces)
     */
    public static final Splitter ROUGH_SPLITTER = str -> str.split("(?<!\\s|\\D)[a-z]+(?!\\s+|\\D)|\\s+");

    public static final Splitter POSITION_NUM_SPLITTER = str -> {
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
    public static final Splitter DOT_SPLITTER = str -> {
        int intDigits = 4;
        int n = 3;
        int[] dotPoss = new int[n];
        for (int i = 0; i < n; i++) {
            int prevPos = 0;
            if (i > 0) {
                prevPos = dotPoss[i - 1];
            }
            dotPoss[i] = str.indexOf(".", prevPos + 1);
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
