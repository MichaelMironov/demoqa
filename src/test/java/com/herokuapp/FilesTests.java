package com.herokuapp;

import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ZipArchiver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class FilesTests {

    private static final ClassLoader CLASS_LOADER = FilesTests.class.getClassLoader();
    private static final Logger log = LoggerFactory.getLogger(FilesTests.class);

    @BeforeAll
    static void setup() {
        Configuration.headless = true;
        Configuration.downloadsFolder = "src/test/resources";
    }

    @Test
    void downloadZipTxtTest() throws Exception {
        open("http://the-internet.herokuapp.com/download");
        File zipArchive = $(byText("sample-zip-file.zip")).download(withExtension("zip"));

        assertThat(zipArchive.getName().contains("sample-zip-file")).isTrue();
        log.info("{} downloaded successfully", zipArchive.getName());

        File unzipFile = new File("src/test/resources/" + zipArchive.getName());

        String expected = "I'd love to see the end results.";

        try {
            ZipArchiver.unzip(zipArchive, unzipFile);
            assertThat(unzipFile).content().contains(expected);
            log.info("file contains expected text: {}", expected);
        } catch (Exception e) {
            throw new ZipException("unzip failed");
        } finally {
            FileUtils.deleteDirectory(zipArchive.getParentFile());
            FileUtils.delete(unzipFile);
        }
    }

    @Test
    void resourceZipPDFTest() throws IOException {
        try (InputStream zipArchive = CLASS_LOADER.getResourceAsStream("sample.pdf.zip");
             ZipInputStream zipInputStream = new ZipInputStream(Optional.ofNullable(zipArchive)
                     .orElseThrow(() -> new IOException("can't read file")))) {

            ZipEntry entry;
            if ((entry = zipInputStream.getNextEntry()) != null) {
                assertThat(entry.getName()).contains("sample.pdf");
                log.info("file: {} exist in archive", entry.getName().replace("__MACOSX/._", ""));
            }
        }
    }

    @Test
    void resourceZipXLSVTest() throws IOException {

        String excelName = "Excel.xlsx";

        try (InputStream zipArchive = CLASS_LOADER.getResourceAsStream(excelName + ".zip");
             ZipInputStream zipInputStream = new ZipInputStream(Optional.ofNullable(zipArchive)
                     .orElseThrow(() -> new IOException("can't read file")))) {

            ZipEntry entry;
            if ((entry = zipInputStream.getNextEntry()) != null) {
                assertThat(entry.getName()).contains(excelName);
                log.info("file: {} exist in archive", excelName);
            }

            ZipArchiver.unzip(new File("src/test/resources/" + excelName + ".zip"),
                    new File("src/test/resources/" + excelName));


            XLS content = new XLS(new File("src/test/resources/" + excelName));
            assertThat(content.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue())
                    .isEqualTo("Test Data Sample");

            FileUtils.delete(new File("src/test/resources/" + excelName));
        }


    }

}
