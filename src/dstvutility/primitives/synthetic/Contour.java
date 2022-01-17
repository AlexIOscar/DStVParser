package dstvutility.primitives.synthetic;

import dstvutility.miscellaneous.DstvParseEx;
import dstvutility.primitives.DstvContourPoint;
import dstvutility.primitives.DstvElement;

import java.util.ArrayList;
import java.util.List;

public class Contour implements DstvElement {

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
            if (pointList.get(i).getFlCode().equals("x")) {
                if (i == 0) {
                    throw new DstvParseEx("First point of AK/IK block haven't flange mark, processing aborted");
                }
                if (i == firstIndex) {
                    System.out.println("Warning: first point of contour haven't flange mark, mark will be taken " +
                            "from previous contour");
                }
                pointList.get(i).setFlCode(pointList.get(i - 1).getFlCode());
            }

            if (pointList.get(i).equals(first)) {
                lastIndex = i;
                outList.add(new Contour(pointList.subList(firstIndex, lastIndex + 1), type));
                firstIndex = ++i;
                //if pointer out of list bound:
                if (firstIndex == pointList.size()) {
                    break;
                }
                first = pointList.get(firstIndex);
            }
        }
        return outList;
    }

    @Override
    public String toString() {
        return "Contour{" +
                "pointListSize=" + pointList.size() +
                ", type=" + type +
                '}';
    }

    public enum ContourType {
        AK, IK
    }
}
