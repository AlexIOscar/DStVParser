package entrypoint;

import dstvutility.parsecore.DstvComponentParser;
import dstvutility.primitives.DstvElement;

import javax.swing.*;
import java.io.*;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {

        JFileChooser jfc = new JFileChooser();
        File file = new File("C:\\IntellijProj\\DStVParser\\00155_КО.01.15_КО.01.15.nc");


        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile().getAbsoluteFile();
        }

        if (file.exists()) {
            System.out.println("file " + file.getName() + " found");
        }

        DstvComponentParser dcp = new DstvComponentParser(file);
        /*
        List<DstvElement> elSet = dcp.getElemList();
        System.out.println(elSet.size());
        for (DstvElement element : elSet) {
            System.out.println(element);
        }
        */


        /*
        String out = dcp.fixDstvFile();
        System.out.println(out);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("dstvOut.nc"))) {
            bw.write(out);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
         */



        File folder = new File("C:\\Users\\Aleksey\\Desktop\\dstv\\2022 02 01 ProNC");
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        int outIndex = 0;
        for (File f : files) {
            outIndex++;
            DstvComponentParser parser = new DstvComponentParser(f);
            String fixed = parser.fixDstvFile();
            String outFileAddr = f.getAbsolutePath().replaceAll("ProNC", "ProNC_Repaired");

            try (BufferedWriter bw =new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(outFileAddr), "CP1251"))) {
                bw.write(fixed);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("File #" + outIndex + ": " + f + " repaired");
        }
    }
}
