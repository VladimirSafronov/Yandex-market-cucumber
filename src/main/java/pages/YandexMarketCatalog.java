package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * PO страницы с разделами Яндекс Маркета
 */
public class YandexMarketCatalog extends YandexMarketMain {

  /**
   * Xpath основных разделов (применяется в сочетании с названием раздела)
   */
  private final String mainSectionXpath = "//span[text()='%s']";
  /**
   * Xpath уточняющих разделов (применяется в сочетании с названием раздела)
   */
  private final String secondSectionXpath = "//a[text()='%s']";
  /**
   * Web-элемент основного раздела
   */
  private WebElement mainSection;
  /**
   * Web-элемент уточняющего раздела
   */
  private WebElement secondSection;

  public YandexMarketCatalog(WebDriver driver, String mainSectionName) {
    super(driver);

    wait.until(elementToBeClickable(By.xpath(String.format(mainSectionXpath, mainSectionName))));
    this.mainSection = driver.findElement(
        By.xpath(String.format(mainSectionXpath, mainSectionName)));
  }

  /**
   * Метод наведения курсора на основной раздел
   */
  public void moveCursorToMainSection() {
    new Actions(driver)
        .moveToElement(mainSection)
        .perform();
  }

  /**
   * Метод входит в подраздел
   *
   * @param secondSectionName - Xpath подраздела
   */
  public void goSection(String secondSectionName) {
    wait.until(
        elementToBeClickable(By.xpath(String.format(secondSectionXpath, secondSectionName))));
    this.secondSection = driver.findElement(
        By.xpath(String.format(secondSectionXpath, secondSectionName)));
    secondSection.click();
  }
}
