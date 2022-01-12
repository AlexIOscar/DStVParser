package dstvutility.primitives.synthetic;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.primitives.DstvContourPoint;

import java.util.ArrayList;
import java.util.List;

public class Contour {

    List<DstvContourPoint> pointList;
    ContourType type;

    private Contour(List<DstvContourPoint> pointList, ContourType type) throws DstvParseEx {
        if (!pointList.get(0).equals(pointList.get(pointList.size() - 1))) {
            throw new DstvParseEx("Attempt to create open contour. Operation will abort");
        }
        this.pointList = pointList;
        this.type = type;
    }

    public static List<Contour> createContList(List<DstvContourPoint> pointList, ContourType type) throws DstvParseEx {
        List<Contour> outList = new ArrayList<>();
        DstvContourPoint first = pointList.get(0);
        int firstIndex = 0;
        int lastIndex = 0;

        for (int i = 1; i < pointList.size(); i++) {
            if (pointList.get(i).equals(first)) {
                lastIndex = i;
                outList.add(new Contour(pointList.subList(firstIndex, lastIndex), type));
                firstIndex = i++;
                first = pointList.get(firstIndex);
            }
        }

        return outList;
    }

    public enum ContourType {
        AK, IK
    }
}
