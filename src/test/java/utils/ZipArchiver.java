package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.*;

public class ZipArchiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipArchiver.class);

    public static void compress(String filename, String... args) throws IOException {
        FileOutputStream f = new FileOutputStream("src/test/resources/" + filename + ".zip");
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream zos = new ZipOutputStream(csum);
        BufferedOutputStream out = new BufferedOutputStream(zos);
        zos.setComment("file archiving...");

        for (String arg : args) {
            LOGGER.info("Writing file " + arg);
            BufferedReader in = new BufferedReader(new FileReader(arg));
            zos.putNextEntry(new ZipEntry(arg));
            int c;
            while ((c = in.read()) != -1)
                out.write(c);
            in.close();
            out.flush();
        }
        out.close();

    }

    public static void unzip(File source, File dest) throws IOException {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                LOGGER.info("File name: {}", name);
                FileOutputStream fout = new FileOutputStream(dest);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        }
    }
}

