package Primitives;

import Misc.DStVParseEx;

public class DStVHole implements DStVElement {
    String flCode;
    double xCoord;
    double yCoord;
    double diam;
    //0 if through
    double depth;
    //optional, for slots only
    double slotLen;
    double slotWidth;
    double slotAng;

    //hole
    public DStVHole(String flCode, double xCoord, double yCoord, double diam, double depth) {
        this.flCode = flCode;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
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

    //slot
    public DStVHole(String flCode, double xCoord, double yCoord, double diam, double depth,
                    double slotLen, double slotWidth, double slotAng) {
        this(flCode, xCoord, yCoord, diam, depth);
        this.slotLen = slotLen;
        this.slotWidth = slotWidth;
        this.slotAng = slotAng;
    }

    public static DStVHole createHole(String DStVSign) throws DStVParseEx {

        String flCode;
        String[] separated = DStVElement.getDataVector(DStVSign);

        flCode = separated[0];
        //проверяем что первая лексема - валидный код фланца. Если нет, то будет получено исключение.
        DStVElement.validateFlange(flCode);

        //удаляем все кроме чисел и разделительной точки
        for (int i = 1; i < separated.length; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d])", "");
        }

        double xCoord = Double.parseDouble(separated[1]);
        double yCoord = Double.parseDouble(separated[2]);
        double diam = Double.parseDouble(separated[3]);
        double depth = Double.parseDouble(separated[4]);

        if (separated.length == 5) {
            return new DStVHole(flCode, xCoord, yCoord, diam, depth);
        }

        if (separated.length == 8) {
            double slotLen = Double.parseDouble(separated[5]);
            double slotWidth = Double.parseDouble(separated[6]);
            double slotAng = Double.parseDouble(separated[7]);
            return new DStVHole(flCode, xCoord, yCoord, diam, depth, slotLen, slotWidth, slotAng);
        }
        throw new DStVParseEx("Illegal data vector format (BO)");
    }
}