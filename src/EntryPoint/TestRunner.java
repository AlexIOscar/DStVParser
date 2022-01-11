package EntryPoint;

import DStVParserCore.DSTVComponentParser;
import Primitives.DStVElement;
import Primitives.DStVHole;

import java.io.File;
import java.util.Set;

public class TestRunner {
    public static void main(String[] args) {

        File file = new File("TestNC.nc");
        if (file.exists()) {
            System.out.println("file finded");
        }
        DSTVComponentParser dcp = new DSTVComponentParser(file);
        Set<DStVElement> elSet = dcp.getElemSet();
        System.out.println(elSet.size());
        for (DStVElement hole : elSet) {
            System.out.println(hole);
        }
    }
}
