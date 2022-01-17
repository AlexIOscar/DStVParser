package dstvutility.primitives;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsecore.DstvComponentParser;

public class DstvContourPoint extends LocatedElem implements DstvElement {

    double radius;

    public DstvContourPoint(String flCode, double xCoord, double yCoord, double radius) {
        super(flCode, xCoord, yCoord);
        this.radius = radius;
    }

    @SuppressWarnings("Duplicates")
    public static DstvContourPoint createPoint(String DStVSign) throws DstvParseEx {
        String[] separated = DstvElement.getDataVector(DStVSign, DstvComponentParser.fineSplitter);

        //temporary flange-code in case of missing a signature in line
        String flCode = "x";
        separated[0] = separated[0].trim();

        //проверяем что первая лексема - валидный код фланца. Если нет, то будет оставлен временный код
        if (DstvElement.validateFlange(separated[0])) {
            flCode = separated[0];
        }

        //удаляем все кроме чисел и разделительной точки
        for (int i = 0; i < separated.length; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d-]+)", "");
        }

        separated = DstvComponentParser.removeVoids(separated);

        double xCoord = Double.parseDouble(separated[0]);
        double yCoord = Double.parseDouble(separated[1]);
        double rad = Double.parseDouble(separated[2]);


        if (separated.length == 3) {
            return new DstvContourPoint(flCode, xCoord, yCoord, rad);
        }

        double ang1;
        double blunting1;
        if (separated.length == 5) {
            ang1 = Double.parseDouble(separated[3]);
            blunting1 = Double.parseDouble(separated[4]);
            if (ang1 == 0 && blunting1 == 0) {
                return new DstvContourPoint(flCode, xCoord, yCoord, rad);
            }
            return new DstvSkewedPoint(flCode, xCoord, yCoord, rad, ang1, blunting1, 0, 0);
        }

        if (separated.length == 7) {
            ang1 = Double.parseDouble(separated[3]);
            blunting1 = Double.parseDouble(separated[4]);
            double ang2 = Double.parseDouble(separated[5]);
            double blunting2 = Double.parseDouble(separated[6]);
            if (ang1 == 0 && blunting1 == 0 && ang2 == 0 && blunting2 == 0) {
                return new DstvContourPoint(flCode, xCoord, yCoord, rad);
            }
            return new DstvSkewedPoint(flCode, xCoord, yCoord, rad, ang1, blunting1, ang2, blunting2);
        }

        throw new DstvParseEx("Illegal data vector format (AK/IK)");
    }

    @Override
    public String toString() {
        return "DStVContourPoint{" +
                "radius=" + radius +
                ", flCode='" + flCode + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }
}
