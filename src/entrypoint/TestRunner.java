package entrypoint;

import dstvutility.parsecore.DstvComponentParser;
import dstvutility.primitives.DstvElement;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {

        JFileChooser jfc = new JFileChooser();
        File file = new File("TestNC.nc");


        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile().getAbsoluteFile();
        }

        if (file.exists()) {
            System.out.println("file " + file.getName() + " finded");
        }

        DstvComponentParser dcp = new DstvComponentParser(file);
        List<DstvElement> elSet = dcp.getElemList();
        System.out.println(elSet.size());
        for (DstvElement element : elSet) {
            System.out.println(element);
        }
    }
}
