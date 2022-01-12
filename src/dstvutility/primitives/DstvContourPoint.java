package dstvutility.primitives;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsercore.DstvComponentParser;

public class DstvContourPoint extends LocatedElem implements DstvElement {

    double radius;

    public DstvContourPoint(String flCode, double xCoord, double yCoord, double radius) {
        this.flCode = flCode;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.radius = radius;
    }

    public static DstvContourPoint createPoint(String DStVSign) throws DstvParseEx {
        String[] separated = DstvElement.getDataVector(DStVSign, DstvComponentParser.fineSplitter);

        //проверяем что первая лексема - валидный код фланца. Если нет, то будет получено исключение.
        DstvElement.validateFlange(separated[0]);

        //удаляем все кроме чисел и разделительной точки
        for (int i = 1; i < separated.length; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d])", "");
        }

        separated = DstvComponentParser.removeVoids(separated);

        double xCoord = Double.parseDouble(separated[1]);
        double yCoord = Double.parseDouble(separated[2]);
        double rad = Double.parseDouble(separated[3]);

        if (separated.length == 4) {
            return new DstvContourPoint(separated[0], xCoord, yCoord, rad);
        }

        double ang1;
        double blunting1;
        if (separated.length == 6) {
            ang1 = Double.parseDouble(separated[4]);
            blunting1 = Double.parseDouble(separated[5]);
            return new DstvSkewedPoint(separated[0], xCoord, yCoord, rad, ang1, blunting1, 0, 0);
        }

        if (separated.length == 8) {
            ang1 = Double.parseDouble(separated[4]);
            blunting1 = Double.parseDouble(separated[5]);
            double ang2 = Double.parseDouble(separated[6]);
            double blunting2 = Double.parseDouble(separated[7]);
            return new DstvSkewedPoint(separated[0], xCoord, yCoord, rad, ang1, blunting1, ang2, blunting2);
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
