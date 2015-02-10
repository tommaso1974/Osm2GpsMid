/**
 * This file is part of OSM2GpsMid
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * Copyright (C) 2007 Harald Mueller Copyright (C) 2008 Kai Krueger
 */
package osmToGpsMid;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import osm2gpsmid.StaticVariable;

import osmToGpsMid.area.Area;
import osmToGpsMid.area.SeaGenerator;
import osmToGpsMid.area.SeaGenerator2;
import osmToGpsMid.model.Damage;
import osmToGpsMid.model.Relation;
import osmToGpsMid.model.RouteAccessRestriction;
import osmToGpsMid.model.TollRule;
import osmToGpsMid.model.TravelMode;
import osmToGpsMid.model.TravelModes;

/**
 * This is the main class of Osm2GpsMid. It triggers all the steps necessary to
 * create a GpsMid JAR file ready for downloading to the mobile phone.
 */
public class BundleGpsMid implements Runnable {

    static Calendar startTime;

   // static boolean compressed = true;
   // static Configuration config;
//    static String dontCompress[] = null;
    //private static volatile boolean createSuccessfully;
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
//        String dataModel = System.getProperty("sun.arch.data.model");
//        System.out.println("Available memory: " + maxMem + "MB (" + dataModel + " bit system)");
//        String warning = null;
//        if ((maxMem < 800 && dataModel.equals("32")) || (maxMem < 1500 && dataModel.equals("64"))) {
//            warning = "Heap space might be not set or set too low! (available memory of " + dataModel + " bit system is " + maxMem + "MB)\r\n"
//                    + "   Use command line options to avoid out-of-memory errors during map making.\r\n"
//                    + "   On 32 bit systems start Osm2GpsMid e.g. with:\r\n"
//                    + "     java -Xmx1024M -jar Osm2GpsMid-xxxx.jar\r\n"
//                    + "     to increase the heap space to 1024 MB\r\n"
//                    + "   On 64 bit systems use e.g.:\r\n"
//                    + "     java -Xmx4096M -XX:+UseCompressedOops -jar Osm2GpsMid-xxxx.jar\r\n"
//                    + "     for 4096 MB heap space and an option to reduce memory requirements\r\n";
//        }
//
//        BundleGpsMid bgm = new BundleGpsMid();
//        GuiConfigWizard gcw = null;
//
//        Configuration c;
//        if (args.length == 0 || (args.length == 1 && args[0].startsWith("--properties="))) {
//            if (warning != null) {
//                JFrame frame = new JFrame("Alert");
//                JOptionPane.showMessageDialog(frame,
//                        warning,
//                        "Osm2GpsMid",
//                        JOptionPane.WARNING_MESSAGE);
//
//            }
//            gcw = new GuiConfigWizard();
//            c = gcw.startWizard(args);
//        } else {
//            if (warning != null) {
//                System.out.println("WARNING:");
//                System.out.println(warning);
//            }
//            c = new Configuration(args);
//        }
//        /**
//         * Decouple the computational thread from the GUI thread to make the GUI
//         * more smooth Not sure if this is actually necessary, but it shouldn't
//         * harm either.
//         */
//        if (c.getDontCompress().equals("*")) {
//            compressed = false;
//        } else {
//            dontCompress = c.getDontCompress().split("[;,]");
//            if (dontCompress[0].equals("")) {
//                dontCompress = null;
//            }
//        }
//
//        config = c;
//        Thread t = new Thread(bgm);
//        createSuccessfully = false;
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            // Nothing to do
//        }
//        if (gcw != null) {
//            if (createSuccessfully) {
//                JOptionPane.showMessageDialog(gcw, "A GpsMid midlet was successfully created and can now be copied to your phone.");
//            } else {
//                JOptionPane.showMessageDialog(gcw, "A fatal error occured during processing. Please have a look at the output logs.");
//            }
//            gcw.reenableClose();
//        }
//    }
    private static void expand(Configuration c, String tmpDir) throws ZipException, IOException {

        System.out.println("Preparing " + c.getOsmBaseDir() + c.getJarFileName());
        File myfile = new File(c.getOsmBaseDir() + c.getJarFileName());

        if (!myfile.exists()) {
            myfile.createNewFile();
        }

        FileInputStream fis = new FileInputStream(myfile);
        //FileInputStream fis = new FileInputStream(new File(c.getJarFile());
        InputStream appStream = fis;
        // InputStream appStream = new InputStreamnew File(c.getJarFile());
//        if (appStream == null) {
//            System.out.println("ERROR: Couldn't find the jar file for " + c.getJarFileName());
//            System.out.println("Check the app parameter in the properties file for misspellings");
//            System.exit(1);
//        }
        File file = new File(c.getTempBaseDir() + "/" + c.getJarFileName());
        writeFile(appStream, file.getAbsolutePath());

        ZipFile zf = new ZipFile(file.getCanonicalFile());
        for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();) {
            ZipEntry ze = e.nextElement();
            if (ze.isDirectory()) {
//				System.out.println("dir  " + ze.getName());
            } else {
//				System.out.println("file " + ze.getName());
                InputStream stream = zf.getInputStream(ze);
                writeFile(stream, tmpDir + "/" + ze.getName());
            }
        }
    }

    /**
     * Rename the Copying files to .txt suffix for easy access on all OS's
     *
     */
    private static void renameCopying(Configuration c) {
        String tmpDir = c.getTempDir();
        File copying = new File(tmpDir + "/COPYING");
        File copying2 = new File(tmpDir + "/COPYING.txt");
        File copyingosm = new File(tmpDir + "/COPYING-OSM");
        File copyingosm2 = new File(tmpDir + "/COPYING-OSM.txt");
        File copyingmaps = new File(tmpDir + "/COPYING-MAPS");
        File copyingmaps2 = new File(tmpDir + "/COPYING-MAPS.txt");
        copying.renameTo(copying2);
        copyingosm.renameTo(copyingosm2);
        copyingmaps.renameTo(copyingmaps2);
    }

    /**
     * Rewrite or remove the Manifest file to change the bundle name to reflect
     * the one specified in the properties file.
     *
     * @param c
     */
    private static void rewriteManifestFile(Configuration c, boolean rename) {
        StaticVariable staticVariable = StaticVariable.getGetInstance();
        String tmpDir = c.getTempDir();
        try {
            File manifest = new File(tmpDir + "/META-INF/MANIFEST.MF");
            File manifest2 = new File(tmpDir + "/META-INF/MANIFEST.tmp");
            FileWriter fw = null;

            if (rename) {
                BufferedReader fr = new BufferedReader(new FileReader(manifest));
                fw = new FileWriter(manifest2);
                String line;
                Pattern p1 = Pattern.compile("MIDlet-(\\d):\\s(.*),(.*),(.*)");
                while (true) {
                    line = fr.readLine();
                    if (line == null) {
                        break;
                    }

                    Matcher m1 = p1.matcher(line);
                    if (m1.matches()) {
                        fw.write("MIDlet-" + m1.group(1) + ": " + c.getMidletName()
                                + "," + m1.group(3) + "," + m1.group(4) + "\n");
                    } else if (line.startsWith("MIDlet-Name: ")) {
                        fw.write("MIDlet-Name: " + c.getMidletName() + "\n");
                    } else {
                        fw.write(line + "\n");
                    }
                    if (line.startsWith("MicroEdition-Profile: ")) {
                        if (staticVariable.getConfig().getAddToManifest().length() != 0) {
                            fw.write(staticVariable.getConfig().getAddToManifest() + "\n");
                        }
                    }
                }
                fr.close();
            }
            manifest.delete();
            if (rename) {
                fw.close();
                manifest2.renameTo(manifest);
            }

        } catch (IOException ioe) {
            System.out.println("Something went wrong rewriting the manifest file");
            return;
        }

    }

    private static void writeJADfile(Configuration c, long jarLength) throws IOException {
        String tmpDir = c.getTempDir();
        File manifest = new File(tmpDir + "/META-INF/MANIFEST.MF");
        BufferedReader fr = new BufferedReader(new FileReader(manifest));
        File jad = new File(c.getMidletFileName() + ".jad");
        FileWriter fw = new FileWriter(jad);

        /**
         * Copy over the information from the manifest file, to the jad file.
         * This way we use the information generated by the build process of
         * GpsMid, to duplicate as little data as possible.
         */
        try {
            String line = fr.readLine();;
            while (true) {
                if (line == null) {
                    break;
                }
                String nextline = fr.readLine();
                if (nextline != null && nextline.substring(0, 1).equals(" ")) {
                    line += nextline.substring(1);
                    continue;
                }

                if (line.startsWith("MIDlet") || line.startsWith("MicroEdition") || (StaticVariable.getGetInstance().getConfig().getAddToManifest().length() != 0 && line.startsWith(StaticVariable.getGetInstance().getConfig().getAddToManifest()))) {
                    fw.write(line + "\n");
                }
                line = nextline;
            }
        } catch (IOException ioe) {
            //This will probably be the end of the file
        }
        /**
         * Add some additional fields to the jad file, that aren't present in
         * the manifest file.
         */
        fw.write("MIDlet-Jar-Size: " + jarLength + "\n");
        fw.write("MIDlet-Jar-URL: " + c.getMidletFileName() + ".jar\n");
        fw.close();
        fr.close();
    }

    private static void pack(Configuration c) throws ZipException, IOException {
        File n = null;
        if (StaticVariable.getGetInstance().getConfig().getMapName().equals("")) {
            n = new File(c.getMidletFileName() + (StaticVariable.getGetInstance().getConfig().sourceIsApk ? ".apk" : ".jar"));
            rewriteManifestFile(c, true);
        } else {
            n = new File(c.getMapFileName());
            rewriteManifestFile(c, false);
            renameCopying(c);
        }
        BufferedOutputStream fo = new BufferedOutputStream(new FileOutputStream(n));
        ZipOutputStream zf = new ZipOutputStream(fo);
        zf.setLevel(9);
        if (StaticVariable.getGetInstance().isCompressed() == false) {
            zf.setMethod(ZipOutputStream.STORED);
        }
        File src = new File(c.getTempDir());
        if (src.isDirectory() == false) {
            throw new Error("TempDir is not a directory");
        }
        packDir(zf, src, "");
        String bundleName = n.getAbsolutePath();
        String jarSigner = StaticVariable.getGetInstance().getConfig().getJarsignerPath();

        // resolve Windows environment variables case-insensitive
        java.util.Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            jarSigner = jarSigner.replaceAll("(?i)%" + envName + "%", Matcher.quoteReplacement(env.get(envName)));
        }

        zf.close();

        if (StaticVariable.getGetInstance().getConfig().sourceIsApk && StaticVariable.getGetInstance().getConfig().signApk) {
            Process signer = null;
            // FIXME add "-storepass" handling with a password field on GUI

            String command[] = {jarSigner,
                "-verbose",
                "-verbose",
                "-verbose",
                "-digestalg",
                "SHA1",
                "-sigalg",
                "MD5withRSA",
                bundleName,
                "gpsmid"};
            String passString = StaticVariable.getGetInstance().getConfig().getSignApkPassword();
            if (!passString.equals("")) {
                command[2] = "-storepass";
                command[3] = passString;
            }
            try {
                String jarsignerOutputLine = null;
                System.out.println("Signing with external program " + command[0] + " (set jarsignerPath=<jarsigner-path-or-commandname> in .properties to change)");
                ProcessBuilder pBuilder = new ProcessBuilder(command);
                pBuilder.redirectErrorStream(true);
                signer = pBuilder.start();
                //signer = Runtime.getRuntime().exec(command);
                // Runtime.getRuntime().exec(jarSigner + " -verbose -digestalg SHA1 -sigalg MD5withRSA " + bundleName + " gpsmid");

                //DataInputStream jarsignerOutputDataStream = new InputStream(signer.getInputStream());
                BufferedReader jarsignerOutput = new BufferedReader(new InputStreamReader(signer.getInputStream()));
                // if jarsigner asks for a password, this makes it stop
                // asking and show the query/error output
                signer.getOutputStream().flush();
                signer.getOutputStream().close();
                while ((jarsignerOutputLine = jarsignerOutput.readLine()) != null) {
                    System.out.println(jarsignerOutputLine);
                }
            } catch (IOException ioe) {
                System.out.println("Error: IO exception " + ioe);
                showSigningMessage(bundleName);
            }
            if (signer != null) {
                try {
                    signer.waitFor();
                    int exitStatus = signer.exitValue();
                    if (exitStatus != 0) {
                        System.out.println("ERROR: jarsigner exited with exit status " + exitStatus + ", signing failed");
                        showSigningMessage(bundleName);
                    }
                } catch (InterruptedException ie) {
                    System.out.println("Error: interrupted execution " + ie);
                    showSigningMessage(bundleName);
                }
            }
        }
        // System.out.println("Bundlename: " + bundleName + " jarSigner: " + jarSigner);
/*
         if (StaticVariable.getGetInstance().getConfig().getMapName().equals("") && !StaticVariable.getGetInstance().getConfig().sourceIsApk) {
         writeJADfile(c, n.length());
         }*/
        Calendar endTime = Calendar.getInstance();
        System.out.println(n.getName() + " created successfully with " + (n.length() / 1024 / 1024) + " MiB in "
                + getDuration(endTime.getTimeInMillis() - startTime.getTimeInMillis()));
    }

    public static void showSigningMessage(String bundleName) {
        System.out.println("Error: Wasn't able to sign " + bundleName);
        System.out.println("You may need to set up jarsigner path and/or settings for signing, see the GpsMid Wiki Properties page under \"Signing an apk\"");
        System.out.println("You may also need to install the java development environment");
    }

    private static String getDuration(long duration) {
        final int millisPerSecond = 1000;
        final int millisPerMinute = 1000 * 60;
        final int millisPerHour = 1000 * 60 * 60;
        final int millisPerDay = 1000 * 60 * 60 * 24;
        int days = (int) (duration / millisPerDay);
        int hours = (int) (duration % millisPerDay / millisPerHour);
        int minutes = (int) (duration % millisPerHour / millisPerMinute);
        int seconds = (int) (duration % millisPerMinute / millisPerSecond);
        return String.format("%d %02d:%02d:%02d", days, hours, minutes, seconds);
    }

    private static void packDir(ZipOutputStream os, File d, String path) throws IOException {
        File[] files = d.listFiles();
        if (StaticVariable.getGetInstance().getConfig().sourceIsApk) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()
                        && files[i].getName().equals("META-INF")
                        && path.length() == 0) {
                    // put META-INF first, not sure if it matters but is customary
                    File tmp = files[0];
                    files[0] = files[i];
                    files[i] = tmp;
                }
            }
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                if (path.length() > 0) {
                    packDir(os, files[i], path + "/" + files[i].getName());
                } else {
                    packDir(os, files[i], files[i].getName());
                }
            } else {
//				System.out.println();
                ZipEntry ze = null;
                if (path.length() > 0) {
                    ze = new ZipEntry(path + "/" + files[i].getName());
                } else {
                    ze = new ZipEntry(files[i].getName());
                }
                int ch;
                int count = 0;
                boolean storethis = false;

                if (StaticVariable.getGetInstance().isCompressed() && StaticVariable.getGetInstance().getDontCompress() != null) {
                    for (String extension : StaticVariable.getGetInstance().getDontCompress()) {
                        if (files[i].getName().toLowerCase().endsWith(extension)) {
                            ze.setMethod(ZipOutputStream.STORED);
                            storethis = true;
                        }
                    }
                }

                //byte buffer to read in larger chunks
                byte[] bb = new byte[4096];
                FileInputStream stream = new FileInputStream(files[i]);
                if ((!StaticVariable.getGetInstance().isCompressed()) || storethis) {
                    CRC32 crc = new CRC32();
                    count = 0;
                    while ((ch = stream.read(bb)) != -1) {
                        crc.update(bb, 0, ch);
                    }
                    ze.setCrc(crc.getValue());
                    ze.setSize(files[i].length());
                }
//				ze.
                os.putNextEntry(ze);
                count = 0;
                stream.close();
                stream = new FileInputStream(files[i]);
                while ((ch = stream.read(bb)) != -1) {
                    os.write(bb, 0, ch);
                    count += ch;
                }
                stream.close();
//				System.out.println("Wrote " + path + "/" + files[i].getName() + " byte:" + count);

            }
        }

    }

    /**
     * @param stream
     * @param string
     */
    private static void writeFile(InputStream stream, String name) {
        File f = new File(name);
        try {
            if (!f.canWrite()) {
                createPath(f.getParentFile());
            }
            FileOutputStream fo = new FileOutputStream(name);
            int ch;
            int count = 0;
            byte[] bb = new byte[4096];
            while ((ch = stream.read(bb)) != -1) {
                fo.write(bb, 0, ch);
                count += ch;
            }
            fo.close();
//			System.out.println("Wrote " + name + " byte:" + count);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Failed to write " + name + " err:" + e.getMessage());
        }
    }

    /**
     * Ensures that the path denoted with <code>f</code> will exist on the
     * file-system.
     *
     * @param f File whose directory must exist
     */
    private static void createPath(File f) {
        if (!f.canWrite()) {
            createPath(f.getParentFile());
        }
        f.mkdir();
    }

    /**
     * remove a directory and all its subdirectories and files
     *
     * @param path
     * @return
     */
    static private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    static private void validateConfig(Configuration config) {
        if (config == null) {
            System.out.println("ERROR: can't find config, exiting");
            System.exit(1);
        }
        if ((config.enableEditingSupport) && (!(config.getAppParam().contains("editing")) && !(config.getAppParam().contains("full-connected")))) {
            System.out.println("ERROR: You are creating a map with editing support, but use a app version that does not support editing\n"
                    + "     please fix your .properties file (full-connected app versions have editing support)");
            System.exit(1);
        }
        if ((config.getPlanetName() == null || config.getPlanetName().equals(""))) {
            System.out.println("ERROR: You haven't specified a planet file\n"
                    + "     please fix your .properties file");
            System.exit(1);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        StaticVariable staticVariable = StaticVariable.getGetInstance();
        InputStream fr;
        try {
            validateConfig(staticVariable.getConfig());
            System.out.println(staticVariable.getConfig().toString());

            // the legend must be parsed after the configuration to apply parameters to the travel modes specified in useRouting
            //tommaso; qui andiamo a definire le tipologie di mezzi che si vogliono utilizzare per poter essettuare il percorso
            //macchina, moto, a piedi
            TravelModes.stringToTravelModes(staticVariable.getConfig().useRouting);

            //tommaso; qui abbiamo memorizzato tutte le informazioni sulle legend da inserire eventulamente nella mappa
            // nel nostro caso nn mi interessa, almeno per il momento, ma da tenere a mente quando andremo a raffinare 
            //le varie logiche
            staticVariable.getConfig().parseLegend();

            // Maybe some of these should be configurable in the future.
            SeaGenerator.setOptions(staticVariable.getConfig(), false, false, false, true, 100);

            startTime = Calendar.getInstance();

            TravelMode tm = null;
            if (Configuration.attrToBoolean(staticVariable.getConfig().useRouting) >= 0) {
                for (int i = 0; i < TravelModes.travelModeCount; i++) {
                    tm = TravelModes.travelModes[i];
                    System.out.println("Route and toll rules in " + staticVariable.getConfig().getStyleFileName()
                            + " for " + tm.getName() + ":");
                    if ((tm.travelModeFlags & TravelMode.AGAINST_ALL_ONEWAYS) > 0) {
                        System.out.println(" Going against all accessible oneways is allowed");
                    }
                    if ((tm.travelModeFlags & TravelMode.BICYLE_OPPOSITE_EXCEPTIONS) > 0) {
                        System.out.println(" Opposite direction exceptions for bicycles get applied");
                    }
                    int routeAccessRestrictionCount = 0;
                    if (TravelModes.getTravelMode(i).getRouteAccessRestrictions().size() > 0) {
                        for (RouteAccessRestriction r : tm.getRouteAccessRestrictions()) {
                            routeAccessRestrictionCount++;
                            System.out.println(" " + r.toString());
                        }
                    }
                    if (routeAccessRestrictionCount == 0) {
                        System.out.println("Warning: No access restrictions in "
                                + staticVariable.getConfig().getStyleFileName() + " for " + tm.getName());
                    }
                    //toll = pedaggio
                    int tollRuleCount = 0;
                    if (TravelModes.getTravelMode(i).getTollRules().size() > 0) {
                        for (TollRule r : tm.getTollRules()) {
                            tollRuleCount++;
                            System.out.println(" " + r.toString());
                        }
                    }
                    if (tollRuleCount == 0) {
                        System.out.println("Warning: No toll rules in "
                                + staticVariable.getConfig().getStyleFileName() + " for " + tm.getName());
                    }
                }
                System.out.println("");
            }

            if (LegendParser.getDamages().isEmpty()) {
                System.out.println("No damage markers in " + staticVariable.getConfig().getStyleFileName());
            } else {
                System.out.println("Rules specified in " + staticVariable.getConfig().getStyleFileName() + " for marking damages:");
                for (Damage damage : LegendParser.getDamages()) {
                    System.out.println(" Ways/Areas with key " + damage.key + "=" + damage.values);
                }
            }
            String tmpDir = staticVariable.getConfig().getTempDir();
            System.out.println("Unpacking application to " + tmpDir);
            // expand(staticVariable.getConfig(), tmpDir);
            File target = new File(tmpDir);
            createPath(target);
            //se stiamo creando un file apk allora dobbiamo inserire ulteriori elementi
            if (staticVariable.getConfig().sourceIsApk) {
                // create /assets
                File targetAssets = new File(tmpDir + "/assets");
                createPath(targetAssets);
                // unsign the APK by deleting files from META-INF (e.g. from debug signing) to be able to sign the APK with the private key
                File f = new File(tmpDir + "/META-INF/MANIFEST.MF");
                f.delete();
                f = new File(tmpDir + "/META-INF/CERT.SF");
                f.delete();
                f = new File(tmpDir + "/META-INF/CERT.RSA");
                f.delete();
            }

            //Tommaso, metodo che permette di parsare gli oggetti OSM, volendo po
            // quando scriviamo la riga
            //Nodes 1008000/6822, Ways 0/0, Relations 0/0/0
            // vuol dire che degli n nodi fino adessi processati solamente 6822 sono presenti 
            //al'interno dell'area di interesse
            OsmParser parser = staticVariable.getConfig().getPlanetParser();

            SeaGenerator2 sg2 = new SeaGenerator2();
            long startTime = System.currentTimeMillis();
            long time;
            SeaGenerator2.setOptions(staticVariable.getConfig(), true, true, true, true, 100);
            if (staticVariable.getConfig().getGenerateSea()) {
                System.out.println("Starting SeaGenerator");
                sg2.generateSea(parser);
                time = (System.currentTimeMillis() - startTime);
                System.out.println("SeaGenerator run");
                System.out.println("  Time taken: " + time / 1000 + " seconds");
            }

            System.out.println("Starting relation handling");
            startTime = System.currentTimeMillis();
            
            //todo tommaso, non sembra che oltre a questo set sull'oggetto Area venga 
            //fatto nulla di particolare
            Area.setParser(parser);
            
            //questo metodo permette di escludere dalla relation tutti quegli elementi
            //che non soddisfano i criteri di routing, per esempio se la relation 
            //fa rifermento al routing da parte di un autobus oppure se risulta essere una build etc etc
            Relations test = new Relations(parser, staticVariable.getConfig());
            
            System.out.println("Relations processed");
            time = (System.currentTimeMillis() - startTime);
            System.out.println("  Time taken: " + time / 1000 + " seconds");

            /**
             * Display some stats about the type of relations we currently
             * aren't handling to see which ones would be particularly useful to
             * deal with eventually
             */
            Hashtable<String, Integer> relTypes = new Hashtable<>();
            for (Relation r : parser.getRelations()) {
                String type = r.getAttribute("type");
                if (type == null) {
                    type = "unknown";
                }
                Integer count = relTypes.get(type);
                if (count != null) {
                    count = new Integer(count.intValue() + 1);
                } else {
                    count = new Integer(1);
                }
                relTypes.put(type, count);
            }
            System.out.println("Types of relations present but ignored: ");
            for (Entry<String, Integer> e : relTypes.entrySet()) {
                System.out.println("   " + e.getKey() + ": " + e.getValue());

            }
            relTypes = null;
            System.out.println("Splitting long ways");
            int numWays = parser.getWays().size();
            startTime = System.currentTimeMillis();
            //todo Tommaso, metodo che mi permette di effettuare lo split di vie estremamente lungo
            new SplitLongWays(parser);
            
            time = (System.currentTimeMillis() - startTime);
            System.out.println("Splitting long ways increased ways from "
                    + numWays + " to " + parser.getWays().size());
            System.out.println("  Time taken: " + time / 1000 + " seconds");
            OxParser.printMemoryUsage(1);

            RouteData rd = null;
            if (Configuration.attrToBoolean(staticVariable.getConfig().useRouting) >= 0) {
                rd = new RouteData(parser, target.getCanonicalPath());
                System.out.println("Remembering " + parser.trafficSignalCount + " traffic signal nodes");
                //Questo metodo colleziona tutti i nodi che risultano essere dei semafori
                rd.rememberDelayingNodes();
            }

            System.out.println("Removing unused nodes");
            //Todo Tommaso Questo metodo permette di ripulire i nodi/way e relation
            // Way vengono esclusi tutti quei elementi che non risultano essere vie vere e proprio 
            new CleanUpData(parser, staticVariable.getConfig());

            //TODO TOMMASO ...... adesso abbiamo solamente i dati che ci interessano per poter effettuare
            //tutti i calcoli del caso, effettuiamo tutte le operazioni di routing sui dati
            if (Configuration.attrToBoolean(staticVariable.getConfig().useRouting) >= 0) {
                System.out.println("Creating route data");
                System.out.println("===================");
                rd.create(staticVariable.getConfig());
                //Questo non fa nulla
                rd.optimise();
                OsmParser.printMemoryUsage(1);
            }
            CreateGpsMidData cd = new CreateGpsMidData(parser, target.getCanonicalPath());
            //				rd.write(target.getCanonicalPath());
            //				cd.setRouteData(rd);
            cd.setConfiguration(staticVariable.getConfig());

            new CalcNearBy(parser);
            cd.exportMapToMid();
            //Drop parser to conserve Memory
            parser = null;
            cd = null;
            rd = null;

            if (!staticVariable.getConfig().getCellOperator().equalsIgnoreCase("false")) {
                CellDB cellDB = new CellDB();
                cellDB.parseCellDB();
            }

            pack(staticVariable.getConfig());

            //Cleanup after us again. The .jar and .jad file are in the main directory,
            //so these won't get deleted
            if (staticVariable.getConfig().cleanupTmpDirAfterUse()) {
                File tmpBaseDir = new File(staticVariable.getConfig().getTempBaseDir());
                System.out.println("Cleaning up temporary directory " + tmpBaseDir);
                deleteDirectory(tmpBaseDir);
            }

            staticVariable.setCreateSuccessfully(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
