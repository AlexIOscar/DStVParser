package entrypoint;

import dstvutility.parsecore.DstvComponentParser;
import dstvutility.primitives.DstvElement;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {

        JFileChooser jfc = new JFileChooser();
        File file = new File("C:\\IntellijProj\\DStVParser\\00155_КО.01.15_КО.01.15.nc");


        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile().getAbsoluteFile();
        }

        if (file.exists()) {
            System.out.println("file " + file.getName() + " finded");
        }

        DstvComponentParser dcp = new DstvComponentParser(file);
        /*
        List<DstvElement> elSet = dcp.getElemList();
        System.out.println(elSet.size());
        for (DstvElement element : elSet) {
            System.out.println(element);
        }
        */

        String out = dcp.fixDstvFile();
        System.out.println(out);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("dstvOut.nc"))) {
            bw.write(out);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
