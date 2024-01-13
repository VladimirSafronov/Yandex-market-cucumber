package helpers;

import static pages.YandexMarketMain.SEARCH_FIELD_XPATH;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Данный класс помогает программе корректно отработать в цикле при проходе на первую и последнюю
 * страницы
 */
public class ReferenceRefresher {

  private static WebDriver driver;
  private static WebDriverWait wait;

  public static void setDriver(WebDriver driver) {
    ReferenceRefresher.driver = driver;
    ReferenceRefresher.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  /**
   * переменнаая хранит количество попыток
   */
  private static final int RETRY_NUMBER = 5;

  /**
   * Метод помогает прогрузить товар на странице
   *
   * @param goalXpath xpath элемента до которого производиться скроллинг
   * @return получилось ли просколлить до целевого элемента
   */
  public static boolean retryMoveToElement(String goalXpath) {
    for (int i = 1; i <= RETRY_NUMBER; i++) {
      try {
        new Actions(driver)
            .moveToElement(driver.findElement(By.xpath(SEARCH_FIELD_XPATH)))
            .moveToElement(driver.findElement(By.xpath(goalXpath)))
            .perform();
        return true;
      } catch (StaleElementReferenceException ex) {
        System.out.println(ex.getMessage());
      }
    }
    return false;
  }

  /**
   * @param goalXpath xpath элемента по которому осуществляется клик мышью
   * @return получилось ли кликнуть по целевому элементу
   */
  public static boolean retryClickToElement(String goalXpath) {

    for (int i = 1; i <= RETRY_NUMBER; i++) {
      try {
        new Actions(driver)
            .moveToElement(driver.findElement(By.xpath(SEARCH_FIELD_XPATH)))
            .moveToElement(driver.findElement(By.xpath(goalXpath)))
            .perform();
        driver.findElement(By.xpath(goalXpath)).click();
        return true;
      } catch (StaleElementReferenceException ex) {
        System.out.println(ex.getMessage());
      } catch (NoSuchElementException ex) {
        System.out.println("In retryClickToElement: element not found. Xpath = " + goalXpath);
        return false;
      } catch (RuntimeException ex) {
        System.out.println("In retryClickToElement: error - " + ex.getClass().getName());
        throw ex;
      }
    }
    return false;
  }

  public static boolean retryWaitNumberOfElement(String elementXpath, int count) {
    try {
      wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(elementXpath), count));
      wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath(elementXpath), count));
      return true;
    } catch (TimeoutException ex) {
      System.out.println(ex.getMessage());
    }
    return false;
  }

  public static void waitNumberOfElement(String elementXpath, int count) {
    wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(elementXpath), count));
    wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath(elementXpath), count));
  }
}
