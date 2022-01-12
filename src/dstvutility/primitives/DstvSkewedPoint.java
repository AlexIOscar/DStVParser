package dstvutility.primitives;

public class DstvSkewedPoint extends DstvContourPoint {
    double ang1;
    double blunting1;
    double ang2;
    double blunting2;

    public DstvSkewedPoint(String flCode, double xCoord, double yCoord,
                           double radius, double ang1, double blunting1, double ang2, double blunting2) {
        super(flCode, xCoord, yCoord, radius);
        this.ang1 = ang1;
        this.ang2 = ang2;

        if (blunting1 >= 0) {
            this.blunting1 = blunting1;
        } else {
            this.blunting1 = 0;
        }

        if (blunting2 >= 0) {
            this.blunting2 = blunting2;
        } else {
            this.blunting2 = 0;
        }
    }

    @Override
    public String toString() {
        return "DStVSkewedPoint{" +
                "ang1=" + ang1 +
                ", blunting1=" + blunting1 +
                ", ang2=" + ang2 +
                ", blunting2=" + blunting2 +
                ", radius=" + radius +
                ", flCode='" + flCode + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }
}
