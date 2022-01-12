package dstvutility.primitives;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsercore.DstvComponentParser;
import dstvutility.parsercore.Splitter;

public interface DstvElement {

    static String[] getDataVector(String DStVSign, Splitter splitter) throws DstvParseEx {

        //проверяем что линия не является комментарием
        if (DStVSign.matches("^\\*\\*.*")){
            throw new DstvParseEx("Attempt to get data from quote-line detected");
        }

        //проверяем что линия стартует с двух пробелов
        if (!DStVSign.matches("^  [^ ].*")) {
            throw new DstvParseEx("Illegal start sequence in data line (must starts with \"  \")");
        }

        DStVSign = DStVSign.trim();
        return splitter.split(DStVSign);
    }



    static void validateFlange(String flDependMark) throws DstvParseEx {
        if (!flDependMark.matches("[ovuh]")) {
            throw new DstvParseEx("Illegal flange code signature in BO data line");
        }
    }
}
