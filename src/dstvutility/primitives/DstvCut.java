package dstvutility.primitives;

import dstvutility.miscellaneous.DstvNativeFactory;
import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsecore.DstvComponentParser;

@SuppressWarnings("SpellCheckingInspection")
public class DstvCut implements DstvElement {
    private final double spPointX;
    private final double spPointY;
    private final double spPointZ;
    private final double normVecX;
    private final double normVecY;
    private final double normVecZ;

    public DstvCut(double spPointX, double spPointY, double spPointZ, double normVecX, double normVecY, double normVecZ) {
        this.spPointX = spPointX;
        this.spPointY = spPointY;
        this.spPointZ = spPointZ;
        this.normVecX = normVecX;
        this.normVecY = normVecY;
        this.normVecZ = normVecZ;
    }

    @DstvNativeFactory
    @SuppressWarnings("Duplicates")
    public static DstvCut createCut (String dataLine) throws DstvParseEx {
        String[] separated = DstvElement.getDataVector(dataLine, DstvComponentParser.FINE_SPLITTER);

        //удаляем все кроме чисел, разделительной точки и знака "hyphen" (играет роль минуса для числа)
        //согласно стандарту, в этом типе код-линий и так не допускается никаких символов, не относящихся к числам,
        // так что здесь это "на всякий случай"
        for (int i = 0; i < separated.length; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d-]+)", "");
        }

        separated = DstvComponentParser.removeVoids(separated);

        if (separated.length < 6) {
            throw new DstvParseEx("Illegal data vector format (SC): too short");
        }

        double ptX = Double.parseDouble(separated[0]);
        double ptY = Double.parseDouble(separated[1]);
        double ptZ = Double.parseDouble(separated[2]);
        double vecX = Double.parseDouble(separated[3]);
        double vecY = Double.parseDouble(separated[4]);
        double vecZ = Double.parseDouble(separated[5]);
        return new DstvCut(ptX, ptY, ptZ, vecX, vecY, vecZ);
    }

    @Override
    public String toString() {
        return "DstvCut{" +
                "spPointX=" + spPointX +
                ", spPointY=" + spPointY +
                ", spPointZ=" + spPointZ +
                ", normVecX=" + normVecX +
                ", normVecY=" + normVecY +
                ", normVecZ=" + normVecZ +
                '}';
    }
}
