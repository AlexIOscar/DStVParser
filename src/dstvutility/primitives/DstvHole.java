package dstvutility.primitives;

import dstvutility.miscellaneous.DstvNativeFactory;
import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsecore.DstvComponentParser;

public class DstvHole extends LocatedElem implements DstvElement {
    final double diam;
    //0 if through
    final double depth;

    //hole
    public DstvHole(String flCode, double xCoord, double yCoord, double diam, double depth) {
        super(flCode, xCoord, yCoord);
        if (diam > 0) {
            this.diam = diam;
        } else {
            this.diam = 0;
        }

        if (depth > 0) {
            this.depth = depth;
        } else {
            this.depth = 0;
        }
    }

    @DstvNativeFactory
    public static DstvHole createHole(String DStVSign) throws DstvParseEx {
        String[] separated = DstvElement.getDataVector(DStVSign, DstvComponentParser.FINE_SPLITTER);
        separated[0] = separated[0].trim();

        //проверяем что первая лексема - валидный код фланца. Если нет, то будет получено исключение.
        if (!DstvElement.validateFlange(separated[0])) {
            throw new DstvParseEx("Illegal flange code signature in BO data line");
        }

        //удаляем все кроме чисел, разделительной точки и знака "hyphen" (играет роль минуса для числа)
        for (int i = 1; i < separated.length; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d-]+)", "");
        }

        separated = DstvComponentParser.removeVoids(separated);

        if (separated.length < 5) {
            throw new DstvParseEx("Illegal data vector format (BO): too short");
        }

        double xCoord = Double.parseDouble(separated[1]);
        double yCoord = Double.parseDouble(separated[2]);
        double diam = Double.parseDouble(separated[3]);
        double depth = Double.parseDouble(separated[4]);

        if (separated.length == 5) {
            return new DstvHole(separated[0], xCoord, yCoord, diam, depth);
        }

        if (separated.length == 8) {
            double slotLen = Double.parseDouble(separated[5]);
            double slotWidth = Double.parseDouble(separated[6]);
            double slotAng = Double.parseDouble(separated[7]);
            return new DstvSlot(separated[0], xCoord, yCoord, diam, depth, slotLen, slotWidth, slotAng);
        }
        throw new DstvParseEx("Illegal data vector format (BO): length not equals 5 or 8");
    }

    @Override
    public String toString() {
        return "DStVHole {" +
                "flCode='" + flCode + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                ", diam=" + diam +
                ", depth=" + depth +
                '}';
    }
}