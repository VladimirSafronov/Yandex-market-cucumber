package pages;

import helpers.exceptions.NoElementActivateException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * PO главной страницы Яндекс Маркет
 */
public class YandexMarketMain {

  /**
   * Xpath кнопки (Каталог или Найти)
   */
  private final String buttonXpath = "//span[contains(text(), '%s')]";
  /**
   * Xpath поля поиска
   */
  public static final String SEARCH_FIELD_XPATH = "//input[@type='text' and @name='text']";
  /**
   * Кнопка (Каталог или Найти)
   */
  private WebElement button;
  /**
   * Xpath прелоадера
   */
  protected final String preloaderXpath = "//div[@data-auto='preloader']";
  /**
   * Количество прелоадеров во время загрузки страницы
   */
  protected final int preloaderCountWhenPageLoad = 3;
  protected WebDriver driver;
  protected WebDriverWait wait;

  public YandexMarketMain(WebDriver driver, String buttonName) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    wait.until(elementToBeClickable(By.xpath(String.format(buttonXpath, buttonName))));
    this.button = driver.findElement(By.xpath(String.format(buttonXpath, buttonName)));
  }

  public YandexMarketMain(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  /**
   * Метод клика по кнопке
   */
  public void clickButton() {
    if (button == null) {
      throw new NoElementActivateException(
          "Элемент объекта не был проинициализирован при создании " + buttonXpath);
    }
    button.click();
  }

  /**
   * Метод заполнения поискового поля
   *
   * @param value - заполняемое значение
   */
  public void fillSearchField(String value) {
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SEARCH_FIELD_XPATH)));
    driver.findElement(By.xpath(SEARCH_FIELD_XPATH)).click();
    driver.findElement(By.xpath(SEARCH_FIELD_XPATH)).sendKeys(value);
  }

  /**
   * Метод ожидания действия (ждет необходимого количества элементов, с последующим уменьшением)
   *
   * @param elementXpath - Xpath элемента
   * @param count        - количество элементов
   */
  protected void waitNumberOfElement(String elementXpath, int count) {
    wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(elementXpath), count));
    wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath(elementXpath), count));
  }
}
