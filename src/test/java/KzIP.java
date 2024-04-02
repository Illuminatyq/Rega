import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class KzIP {

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
        $$(".v-list-item__title").findBy(text("Казахстан")).click();
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
        $("input[input-label='ИИН']").setValue("771469832022");
        $("input[input-label='ОКПО, иначе первые 8 цифр БИН']").setValue("12345678");
    }

    private void generateAndAttachFiles() throws IOException {
        // Создать экземпляр класса PdfGenerator
        PdfGenerator pdfGenerator = new PdfGenerator();

        // Сгенерировать PDF-файлы
        String filePath1 = pdfGenerator.generatePdfFile("file1.pdf");

        $$("input[type='file']").get(0).uploadFile(new File(filePath1)); // Загружает файл FilePath1 в первый input

    }
    private void submitForm() {
        // Согласие с условиями
        $("input[aria-checked='false']").click();
        $("span[class='button__text']").click();
    }
}
