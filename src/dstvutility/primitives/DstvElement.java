package dstvutility.primitives;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsecore.Splitter;

public interface DstvElement {

    static String[] getDataVector(String DStVSign, Splitter splitter) throws DstvParseEx {

        //проверяем что линия не является комментарием
        if (DStVSign.matches("^\\*\\*.*")) {
            throw new DstvParseEx("Attempt to get data from quote-line detected");
        }

        //проверяем что линия стартует с двух пробелов
        if (!DStVSign.matches("^  .*")) {
            throw new DstvParseEx("Illegal start sequence in data line (must starts with \"  \")");
        }

       //DStVSign = DStVSign.trim();
        return splitter.split(DStVSign);
    }

    static boolean validateFlange(String flDependMark) {
        return flDependMark.matches("[ovuh]");
    }

    static <T> T getElement(String dataLine){
        return null;
    }
}
