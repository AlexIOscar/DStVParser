package dstvutility.primitives;

public abstract class LocatedElem {
    protected String flCode;
    protected double xCoord;
    protected double yCoord;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocatedElem)) return false;

        LocatedElem that = (LocatedElem) o;

        if (Double.compare(that.xCoord, xCoord) != 0) return false;
        if (Double.compare(that.yCoord, yCoord) != 0) return false;
        return flCode.equals(that.flCode);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = flCode.hashCode();
        temp = Double.doubleToLongBits(xCoord);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yCoord);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
