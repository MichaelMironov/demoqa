package com.herokuapp;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.DownloadOptions;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ZipArchiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Configuration.downloadsFolder;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesTests {

    private static final ClassLoader CLASS_LOADER = FilesTests.class.getClassLoader();
    private static final Logger log = LoggerFactory.getLogger(FilesTests.class);

    @BeforeAll
    static void setup() {
        Configuration.headless = true;
        downloadsFolder = "src/test/resources";
    }

    @Test
    void jacksonTest() throws IOException {

        byte[] json = Files.readAllBytes(Paths.get("src/test/resources/JSON-file-with-multiple-records.json"));

        ObjectMapper objectMapper = new ObjectMapper();

        Users users = objectMapper.readValue(json, Users.class);
        Users.User actualFirstUser = users.getUsers().get(0);

        Users.User expectedFirstUser = new Users.User();

        expectedFirstUser.setUserId(1);
        expectedFirstUser.setFirstName("Krish");
        expectedFirstUser.setLastName("Lee");
        expectedFirstUser.setPhoneNumber("123456");
        expectedFirstUser.setEmailAddress("krish.lee@learningcontainer.com");

        log.info("\nexpected user: {}\nactual user: {}", expectedFirstUser, actualFirstUser);

        assertThat(expectedFirstUser).isEqualTo(actualFirstUser);
    }

    @Test
    @Disabled
    @DisplayName("Проверка скачанного txt архива")
    void downloadZipTxtTest() throws Exception {

        open("http://the-internet.herokuapp.com/download");
        File zipArchive = $(byText("sample-zip-file.zip")).download(withExtension("zip"));

        assertThat(zipArchive.getName()).contains("sample-zip-file");
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
    @DisplayName("Проверка pdf архива")
    void resourceZipPDFTest() throws IOException {

        String expected = "Congratulations, your computer is equipped with a PDF (Portable Document Format)";

        try (InputStream zipArchive = CLASS_LOADER.getResourceAsStream("sample.pdf.zip");
             ZipInputStream zipInputStream = new ZipInputStream(Optional.ofNullable(zipArchive)
                     .orElseThrow(() -> new IOException("can't read file")))) {
            ZipEntry entry;
            if ((entry = zipInputStream.getNextEntry()) != null) {
                assertThat(entry.getName()).contains("sample.pdf");
                log.info("file: {} exist in archive", entry.getName().replace("__MACOSX/._", ""));
                PDF pdf = new PDF(zipInputStream);
                assertThat(pdf.text).contains(expected);
                log.info("PDF contains expected text: {}", expected);
            }
        }
    }

    @Test
    @DisplayName("Проверка excel архива")
    void resourceZipXLSTest() throws IOException {

        String expected = "Test Data Sample";

        try (InputStream zipArchive = CLASS_LOADER.getResourceAsStream("Excel.xlsx.zip");
             ZipInputStream zipInputStream = new ZipInputStream(Optional.ofNullable(zipArchive)
                     .orElseThrow(() -> new IOException("can't read file")))) {

            ZipEntry entry;
            if ((entry = zipInputStream.getNextEntry()) != null) {
                assertThat(entry.getName()).contains("Excel.xlsx");
                log.info("file: {} exist in archive", entry.getName());
                XLS content = new XLS(zipInputStream);
                assertThat(content.excel.getSheetAt(0)
                        .getRow(0).getCell(0)
                        .getStringCellValue()).contains(expected);
                log.info("PDF contains expected text: {}", expected);
            }
        }
    }


    @Test
    @Disabled
    @DisplayName("Проверка скачанного csv файла")
    void downloadedCSVTest() throws IOException {

        String expected = "Macrotrends Data Download";

        open("http://the-internet.herokuapp.com/download");
        File csv = $(byText("csv.csv")).download(DownloadOptions.using(FOLDER)
                .withFilter(withExtension("csv"))
                .withTimeout(5000)
                .withIncrementTimeout(ofMillis(100)));

        assertThat(csv).hasName("csv.csv");
        assertThat(csv).content().contains(expected);

        log.info("CSV file contains expected text: {}", expected);

        FileUtils.deleteDirectory(csv.getParentFile());
    }


    @ParameterizedTest(name = "Проверка пользователя: {0} {1} {2}")
    @CsvFileSource(resources = "/customers.csv")
    void resourceArchiverCSVTest(String firstname, String lastname, String postcode) throws IOException {
        String filename = "customers.csv";
        File csvSource = new File("src/test/resources/" + filename);
        File zip = new File(csvSource + ".zip");

        ZipArchiver.compress(filename, csvSource.getAbsolutePath());

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry entry;
            if ((entry = zipInputStream.getNextEntry()) != null) {
                assertThat(entry.getName()).contains(filename);
                log.info("file: {} exist in archive", entry.getName());
                ZipArchiver.unzip(zip, csvSource);
                assertThat(csvSource).content()
                        .containsIgnoringNewLines(firstname, lastname, postcode);
                log.info("file contains expected value: {}", String.join(" ", firstname, lastname, postcode));
            }
        } catch (Exception e) {
            throw new ZipException("unzip failed");
        } finally {
            FileUtils.delete(zip);
        }
    }
}
