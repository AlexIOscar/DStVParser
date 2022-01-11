package Primitives;

public class DStVContourPoint extends LocatedElem implements DStVElement {
    double radius;
    double ang1;
    double blunting1;
    double ang2;
    double blunting2;


    public DStVContourPoint(String flCode, double xCoord, double yCoord,
                            double radius, double ang1, double blunting1, double ang2, double blunting2) {
        this.flCode = flCode;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.radius = radius;
        this.ang1 = ang1;

        if (blunting1 >= 0) {
            this.blunting1 = blunting1;
        } else {
            this.blunting1 = 0;
        }

        this.ang2 = ang2;

        if (blunting2 >= 0) {
            this.blunting2 = blunting2;
        } else {
            this.blunting2 = 0;
        }
    }


}
