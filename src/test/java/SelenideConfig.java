import com.codeborne.selenide.Configuration;

public class SelenideConfig {

    public static void configure() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://demo.zveno.io";
    }
}