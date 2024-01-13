package pages;

import helpers.ReferenceRefresher;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * PO подраздела
 */
public class YandexMarketSection extends YandexMarketMain {

  /**
   * Xpath поля Цена от
   */
  private final String fieldPriceMinXpath =
      "//input[starts-with(@id, 'range-filter-field-glprice') and contains(@id, 'min')]";
  /**
   * Xpath поля Цена до
   */
  private final String fieldPriceMaxXpath =
      "//input[starts-with(@id, 'range-filter-field-glprice') and contains(@id, 'max')]";
  /**
   * Xpath чекбокса Производитель (применяется с названием производителя)
   */
  private final String checkboxCompanyNameXpath = "//span[text()='%s']";
  /**
   * Web-элемент Цена от
   */
  private final WebElement fieldMinPrice;
  /**
   * Web-элемент Цена до
   */
  private final WebElement fieldMaxPrice;
  /**
   * Список производителей для фильтрации
   */
  private List<WebElement> companies;

  public YandexMarketSection(WebDriver driver, List<String> companiesData) {
    super(driver);
    this.companies = new ArrayList<>();

    wait.until(elementToBeClickable(By.xpath(fieldPriceMinXpath)));
    this.fieldMinPrice = driver.findElement(By.xpath(fieldPriceMinXpath));
    this.fieldMaxPrice = driver.findElement(By.xpath(fieldPriceMaxXpath));

    for (int i = 0; i < companiesData.size(); i++) {
      wait.until(elementToBeClickable(
          By.xpath(String.format(checkboxCompanyNameXpath, companiesData.get(i)))));
      companies.add(driver.findElement(
          By.xpath(String.format(checkboxCompanyNameXpath, companiesData.get(i)))));
    }
  }

  public YandexMarketSection(WebDriver driver) {
    super(driver);

    wait.until(elementToBeClickable(By.xpath(fieldPriceMinXpath)));
    this.fieldMinPrice = driver.findElement(By.xpath(fieldPriceMinXpath));
    this.fieldMaxPrice = driver.findElement(By.xpath(fieldPriceMaxXpath));
  }

  /**
   * Метод заполнения поля
   *
   * @param field - поле
   * @param data  - значение
   */
  public void fillField(WebElement field, String data) {
    field.click();
    field.sendKeys(data);
  }

  /**
   * Метод заполнения чекбокса
   *
   * @param webElements - список элементов для заполнения
   */
  public void clickCheckbox(List<WebElement> webElements) {
    for (WebElement element : webElements) {
      element.click();
    }
    ReferenceRefresher.waitNumberOfElement(preloaderXpath, preloaderCountWhenPageLoad);
  }

  /**
   * Геттер Цена от
   */
  public WebElement getFieldMinPrice() {
    return fieldMinPrice;
  }

  /**
   * Геттер Цена до
   */
  public WebElement getFieldMaxPrice() {
    return fieldMaxPrice;
  }

  /**
   * Геттер списка производителей
   */
  public List<WebElement> getCompanies() {
    return companies;
  }
}
