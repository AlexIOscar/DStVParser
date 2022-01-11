package Primitives;

import Misc.DStVParseEx;

public interface DStVElement {

    static String[] getDataVector(String DStVSign) throws DStVParseEx {

        if (DStVSign.matches("^\\*\\*.*")){
            throw new DStVParseEx("Attempt to get data from quote-line detected");
        }

        //проверяем что линия стартует с двух пробелов
        if (!DStVSign.matches("^  [^ ].*")) {
            throw new DStVParseEx("Illegal start sequence in data line (must starts with \"  \")");
        }

        DStVSign = DStVSign.trim();
        return DStVSign.split("(?<!\\s|\\D)[a-z]+(?!\\s+|\\D)|\\s+");
    }

    public static void validateFlange(String flDependLine) throws DStVParseEx {
        if (!flDependLine.matches("[ovuh]")) {
            throw new DStVParseEx("Illegal flange code signature in BO data line");
        }
    }
}
