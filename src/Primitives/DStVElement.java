package Primitives;

import Misc.DStVParseEx;

public interface DStVElement {

    static String[] getDataVector(String DStVSign) throws DStVParseEx {

        //проверяем что линия стартует с двух пробелов
        if (!DStVSign.matches("^  \\S.*")) {
            throw new DStVParseEx("Illegal start sequence in data line (must starts with \"  \")");
        }

        DStVSign = DStVSign.trim();
        return DStVSign.split("(?<!\\s)[a-z](?!\\s+)|\\s+");
    }

    public static void validateFlange(String flDependLine) throws DStVParseEx {
        if (!flDependLine.matches("[ovuh]")) {
            throw new DStVParseEx("Illegal flange code signature in BO data line");
        }
    }

}
