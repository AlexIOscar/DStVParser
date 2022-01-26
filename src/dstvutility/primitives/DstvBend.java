package dstvutility.primitives;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.parsecore.DstvComponentParser;

@SuppressWarnings("SpellCheckingInspection")
public class DstvBend implements DstvElement {
    public final double originX;
    public final double originY;
    public final double finishX;
    public final double finishY;
    public final double bendingAngle;
    public final double bendingRadius;

    public DstvBend(double originX, double originY, double finishX, double finishY, double bendingAngle, double bendingRadius) {
        this.originX = originX;
        this.originY = originY;
        this.finishX = finishX;
        this.finishY = finishY;
        this.bendingAngle = bendingAngle;
        this.bendingRadius = bendingRadius;
    }

    @SuppressWarnings("Duplicates")
    public static DstvBend createBend(String bendDataLine) throws DstvParseEx {
        String[] separated = DstvElement.getDataVector(bendDataLine, DstvComponentParser.FINE_SPLITTER);

        //удаляем все кроме чисел, разделительной точки и знака "hyphen" (играет роль минуса для числа)
        //согласно стандарту, в этом типе код-линий и так не допускается никаких символов, не относящихся к числам,
        // так что здесь это "на всякий случай"
        for (int i = 0; i < separated.length; i++) {
            separated[i] = separated[i].replaceAll("([^.\\d-]+)", "");
        }

        separated = DstvComponentParser.removeVoids(separated);

        if (separated.length < 6) {
            throw new DstvParseEx("Illegal data vector format (KA): too short");
        }

        double orX = Double.parseDouble(separated[0]);
        double orY = Double.parseDouble(separated[1]);
        double finX = Double.parseDouble(separated[2]);
        double finY = Double.parseDouble(separated[3]);
        double ang = Double.parseDouble(separated[4]);
        double rad = Double.parseDouble(separated[5]);
        return new DstvBend(orX, orY, finX, finY, ang, rad);
    }

    @Override
    public String toString() {
        return "DstvBend{" +
                "originX=" + originX +
                ", originY=" + originY +
                ", finishX=" + finishX +
                ", finishY=" + finishY +
                ", bendingAngle=" + bendingAngle +
                ", bendingRadius=" + bendingRadius +
                '}';
    }
}
