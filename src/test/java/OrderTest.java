import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldTestSuccessOrderIfCorrectFilling() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Жанна Лиман");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79876543210");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldTestWarnIfIncorrectTel() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Жанна Лим");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+798765");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorMessage.getText().trim());
    }

    @Test
    void shouldTestWarnIfNoName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79876543210");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Поле обязательно для заполнения", errorMessage.getText().trim());
    }

    @Test
    void shouldTestWarnIfIncorrectName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Jeanny");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79876543210");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorMessage.getText().trim());
    }

    @Test
    void shouldTestWarnIfNoTel() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Жанна Лим");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Поле обязательно для заполнения", errorMessage.getText().trim());
    }

 @Test
void shouldTestChangeColorOfCheckBoxIfInvalid() {
    this.driver.get("http://localhost:9999");
    List<WebElement> elements = this.driver.findElements(By.className("input__control"));
    elements.get(0).sendKeys("\u041c\u0430\u0440\u0438\u044f \u0413\u0440\u0435\u0431\u0435\u043d\u044c\u043a\u043e\u0432\u0430");
    elements.get(1).sendKeys("+79876543210");
    this.driver.findElement(By.className("button")).click();
    WebElement checkboxText = this.driver.findElement(By.className("checkbox__text"));
    Assertions.assertNotNull(checkboxText, "Элемент checkbox__text не найден");
    String color = checkboxText.getCssValue("color");
    System.out.println("Полученный цвет: " + color); 
    Assertions.assertEquals("rgba(255, 92, 92, 1)", color, "Цвет текста не совпадает с ожидаемым");
}

}
