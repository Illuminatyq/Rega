import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class ZvenoRegistrationTest {

    public ZvenoRegistrationTest() throws IOException {
    }

    @BeforeAll
    public static void setup() {
        SelenideConfig.configure();
    }

    @Test
    public void testRegistrationRussiaIP() throws IOException {
        open("/reg");
        fillPersonalData();
        fillCompanyData();
        generateAndAttachFiles();
        submitForm();
        // Проверка успешной регистрации
        // ...
    }

    private void fillPersonalData() {
        // Заполнение личных данных
        $("#input-24").setValue("Иванов");
        $("#input-27").setValue("Иван");
        $("#input-30").setValue("Иванович");
        $("#input-33").setValue("ivanov@example.com");
        $("#input-40").setValue("999-888-77-66");
        $("#input-43").setValue("Pass-word12@");
        $("#input-47").setValue("Pass-word12@");
        $("button[name='Далее']").click();
        $(".country-select").click();
        $$(".v-list-item__title").findBy(text("Россия")).click();
    }

    private void fillCompanyData() {
        // Заполнение данных компании
        $("#input-59").setValue("Моя компания");
        $("#input-62").setValue("МК");
        $(".ownership-type-select").click();
        $$(".v-list-item__title").findBy(text("ИП")).click();
        $(".company-type-select").click();
        $$(".v-list-item__title").findBy(text("Отправитель")).click();
        $("#list-76").sendKeys(Keys.ESCAPE);
        $("input[input-label='Код ТГНЛ']").setValue("4444");
        $("input[input-label='ИНН']").setValue("7716915428");
        $("input[input-label='ОКПО']").setValue("1234567890");
    }

    private void generateAndAttachFiles() throws IOException {
        // Сгенерировать PDF-файлы
        String filePath1 = generatePdfFile("file1.pdf");
        String filePath2 = generatePdfFile("file2.pdf");

        // Прикрепить файлы
        attachFileToDynamicInput(filePath1, filePath2);
    }

    private void attachFileToDynamicInput(String filePath1, String filePath2) {
        // Находим элементы input по их атрибутам accept и тексту метки "Прикрепить"
        ElementsCollection fileInputs = $$("input[type='file']");
        ElementsCollection labels = $$(".label-container__text");

        // Ищем элементы, которые соответствуют меткам "Свидетельство о гос.регистрации" и "Свидетельство о постановке на нал.учет"
        SelenideElement input131 = findInputByLabel(fileInputs, labels, "Свидетельство о гос.регистрации");
        SelenideElement input138 = findInputByLabel(fileInputs, labels, "Свидетельство о постановке на нал.учет");

        // Загружаем файлы
        input131.uploadFromClasspath(filePath1);
        input138.uploadFromClasspath(filePath2);
    }

    private SelenideElement findInputByLabel(ElementsCollection fileInputs, ElementsCollection labels, String labelText) {
        // Поиск метки с заданным текстом
        SelenideElement label = labels.find(text(labelText));
        // Находим родительский элемент метки (для него есть атрибут for, который ссылается на id input)
        String forAttribute = label.attr("for");
        // Находим input, который соответствует метке
        return fileInputs.find(attribute("id", forAttribute));
    }

    private void submitForm() {
        // Согласие с условиями
        $("input[aria-checked='false']").click();
        $("span[class='button__text']").click();
    }

    private String generatePdfFile(String fileName) throws IOException {
        // Метод для генерации PDF-файла
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("This is a test PDF file.");
            contentStream.endText();
        }

        String filePath = STR."src/main/resources/\{fileName}"; // Путь к файлу в директории ресурсов
        document.save(filePath);
        document.close();

        return filePath;
    }
}
