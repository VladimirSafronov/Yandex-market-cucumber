package hooks;


import helpers.ReferenceRefresher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import stash.Context;
import stash.TestContext;

/**
 * Класс задает методы, которые могут выполняться в различных точках цикла выполнения Cucumber.
 * Обычно они используются для настройки и демонтажа среды до и после каждого сценария
 */
public class Hooks {

  /**
   * Инициализация и настройка драйвера
   * value указывает, для каких тегов выполнить метод
   */
  @Before(value = "@ui")
  public void initWebDriver() {
    System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
    ChromeDriver chromeDriver = new ChromeDriver();
    TestContext testContext = TestContext.getInstance();
    chromeDriver.manage().window().maximize();
    chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    testContext.put(String.valueOf(Context.CHROME_DRIVER),chromeDriver);

    ReferenceRefresher.setDriver((WebDriver) testContext.get(String.valueOf(Context.CHROME_DRIVER)));
  }

  @After(value = "@ui")
  public void quiteWebDriver() {
    ChromeDriver chromeDriver = (ChromeDriver) TestContext.getInstance().get(String.valueOf(Context.CHROME_DRIVER));
    chromeDriver.quit();
  }
}
