package entrypoint;

import dstvutility.parsercore.DstvComponentParser;
import dstvutility.primitives.DstvElement;

import java.io.File;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {

        File file = new File("TestNC.nc");
        if (file.exists()) {
            System.out.println("file finded");
        }
        DstvComponentParser dcp = new DstvComponentParser(file);
        List<DstvElement> elSet = dcp.getElemList();
        System.out.println(elSet.size());
        for (DstvElement element : elSet) {
            System.out.println(element);
        }
    }
}
