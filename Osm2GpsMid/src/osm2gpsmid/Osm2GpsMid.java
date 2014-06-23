/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package osm2gpsmid;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import osmToGpsMid.BundleGpsMid;
import osmToGpsMid.Configuration;
import osmToGpsMid.GuiConfigWizard;

/**
 *
 * @author nexse
 */
public class Osm2GpsMid {

    /**
     * @param args
     */
    public static void main(String[] args) {

        //TODO TOMMASO........MAIN
        StaticVariable staticVariable = StaticVariable.getGetInstance();
        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        String dataModel = System.getProperty("sun.arch.data.model");
        System.out.println("Available memory: " + maxMem + "MB (" + dataModel + " bit system)");
        String warning = null;
        if ((maxMem < 800 && dataModel.equals("32")) || (maxMem < 1500 && dataModel.equals("64"))) {
            warning = "Heap space might be not set or set too low! (available memory of " + dataModel + " bit system is " + maxMem + "MB)\r\n"
                    + "   Use command line options to avoid out-of-memory errors during map making.\r\n"
                    + "   On 32 bit systems start Osm2GpsMid e.g. with:\r\n"
                    + "     java -Xmx1024M -jar Osm2GpsMid-xxxx.jar\r\n"
                    + "     to increase the heap space to 1024 MB\r\n"
                    + "   On 64 bit systems use e.g.:\r\n"
                    + "     java -Xmx4096M -XX:+UseCompressedOops -jar Osm2GpsMid-xxxx.jar\r\n"
                    + "     for 4096 MB heap space and an option to reduce memory requirements\r\n";
        }

        BundleGpsMid bgm = new BundleGpsMid();
        GuiConfigWizard gcw = null;

        Configuration c;
        if (args.length == 0 || (args.length == 1 && args[0].startsWith("--properties="))) {
            if (warning != null) {
                JFrame frame = new JFrame("Alert");
                JOptionPane.showMessageDialog(frame, warning, "Osm2GpsMid", JOptionPane.WARNING_MESSAGE);
            }
            gcw = new GuiConfigWizard();
            c = gcw.startWizard(args);
        } else {
            if (warning != null) {
                System.out.println("WARNING:");
                System.out.println(warning);
            }
            c = new Configuration(args);
        }
        /**
         * Decouple the computational thread from the GUI thread to make the GUI
         * more smooth Not sure if this is actually necessary, but it shouldn't
         * harm either.
         */
        if (c.getDontCompress().equals("*")) {
            staticVariable.setCompressed(false);
        } else {
            staticVariable.setDontCompress(c.getDontCompress().split("[;,]"));
            if (staticVariable.getDontCompress()[0].equals("")) {
                staticVariable.setDontCompress(null);
            }
        }

        staticVariable.setConfig(c);
        Thread t = new Thread(bgm);
        staticVariable.setCreateSuccessfully(false);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            // Nothing to do
        }
        if (gcw != null) {
            if (staticVariable.isCreateSuccessfully()) {
                JOptionPane.showMessageDialog(gcw, "A GpsMid midlet was successfully created and can now be copied to your phone.");
            } else {
                JOptionPane.showMessageDialog(gcw, "A fatal error occured during processing. Please have a look at the output logs.");
            }
            gcw.reenableClose();
        }
    }
}
