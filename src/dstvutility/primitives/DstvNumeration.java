package dstvutility.primitives;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsecore.DstvComponentParser;

@SuppressWarnings("SpellCheckingInspection")
public class DstvNumeration extends LocatedElem implements DstvElement {

    final double angle;
    final double letterHeight;
    final String text;

    public DstvNumeration(String flCode, double xCoord, double yCoord, double angle, double letterHeight, String text) {
        super(flCode, xCoord, yCoord);
        this.angle = angle;
        this.letterHeight = letterHeight;
        this.text = text;
    }

    @SuppressWarnings("Duplicates")
    public static DstvNumeration createNumeration(String dstvLine) throws DstvParseEx {
        String[] separated = DstvElement.getDataVector(dstvLine, DstvComponentParser.POSITION_NUM_SPLITTER);
        //temporary flange-code in case of missing a signature in line
        String flCode = "x";

        if (DstvElement.validateFlange(separated[1])) {
            flCode = separated[1];
        }

        //удаляем все кроме чисел, разделительной точки и знака "hyphen" (играет роль минуса для числа)
        for (int i = 0; i < separated.length - 1; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d-]+)", "");
        }

        separated = DstvComponentParser.removeVoids(separated);

        if (separated.length < 5) {
            throw new DstvParseEx("Illegal data-vector length (SI) - too short");
        }

        double xCoord = Double.parseDouble(separated[0]);
        double yCoord = Double.parseDouble(separated[1]);
        double angle = Double.parseDouble(separated[2]);
        double lh = Double.parseDouble(separated[3]);

        return new DstvNumeration(flCode, xCoord, yCoord, angle, lh, separated[4]);
    }

    @Override
    public String toString() {
        return "DstvNumeration {"+
                " flCode='" + flCode + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                ", angle=" + angle +
                ", letterHeight=" + letterHeight +
                ", text='" + text + '\'' +
                '}';
    }
}
