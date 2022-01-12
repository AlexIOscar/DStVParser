package dstvutility.primitives;

public class DstvSlot extends DstvHole {

    //optional, for slots only
    double slotLen;
    double slotWidth;
    double slotAng;

    public DstvSlot(String flCode, double xCoord, double yCoord, double diam, double depth,
                    double slotLen, double slotWidth, double slotAng) {
        super(flCode, xCoord, yCoord, diam, depth);
        this.slotLen = slotLen;
        this.slotWidth = slotWidth;
        this.slotAng = slotAng;
    }

    @Override
    public String toString() {
        return "DStVSlot {" +
                "flCode='" + flCode + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                ", slotLen=" + slotLen +
                ", slotWidth=" + slotWidth +
                ", slotAng=" + slotAng +
                ", diam=" + diam +
                ", depth=" + depth +
                '}';
    }
}
