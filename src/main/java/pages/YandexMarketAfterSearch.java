package pages;

import helpers.Assertions;
import helpers.ReferenceRefresher;
import helpers.exceptions.NoElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import stash.Context;
import stash.TestContext;

/**
 * PO страницы после применения фильтрации
 */
public class YandexMarketAfterSearch extends YandexMarketSection {

  /**
   * Переменная используется для нахождения цены продукта из текста всего WebElement-а (цена может
   * быть до миллиона)
   */
  private static final int PRICE_MAX_LENGTH = 8;
  /**
   * Символ рубля
   */
  private static final char REGEX_PRICE_TO = '₽';
  /**
   * Xpath одного товара
   */
  private final String productXpath = "//div[@data-known-size<='350']";
  /**
   * Xpath кнопки Вперёд
   */
  private final String nextButtonXpath = "//span[text()='Вперёд']";
  /**
   * Xpath пагинатора
   */
  private final String paginatorXpath = "//div[@data-zone-name='SearchPager']";
  /**
   * Xpath кнопки Назад
   */
  private final String prevPageButtonXpath = "//div[@data-auto='pagination-prev']";
  /**
   * Xpath заголовка товара
   */
  private final String productTitleXpath = "//h3[@data-auto='snippet-title-header']";
  /**
   * Список отфильтрованных товаров
   */
  private final List<String> products;

  public YandexMarketAfterSearch(WebDriver driver) {
    super(driver);
    this.products = new ArrayList<>();
  }

  /**
   * Метод загрузки отфильтрованного товара с одной страницы
   */
  public void loadProducts() {
    if (!ReferenceRefresher.retryMoveToElement(paginatorXpath)) {
      Assertions.fail("Не получилось прокрутить экран до пагинатора страницы");
    }
    //TODO:периодически не загружает первую страницу с товаром
    // org.openqa.selenium.StaleElementReferenceException: stale element reference: stale element not found

    List<String> productsTittle = driver.findElements(By.xpath(productXpath)).stream()
        .map(WebElement::getText)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());

    Assertions.assertTrue(!productsTittle.isEmpty(),
        "Во время загрузки на странице не оказалось товара");
    products.addAll(productsTittle);
    System.out.println("Products size = " + products.size());
  }

  /**
   * Метод загрузки отфильтрованного товара со всех страниц
   *
   * @param buttonXpath - кнопка через которую осуществляется переход на другую страницу
   */
  public void loadAllProducts(String buttonXpath) {
    while (isElementExists(buttonXpath)) {
      loadProducts();
      driver.findElement(By.xpath(buttonXpath)).click();
      waitNumberOfElement(preloaderXpath, preloaderCountWhenPageLoad);
    }
    loadProducts();
  }

  /**
   * Метод хождения по страницам
   *
   * @param buttonXpath - кнопка через которую осуществляется переход
   */
  public void rideToPage(String buttonXpath) {
    while (isElementExists(buttonXpath)) {
      driver.findElement(By.xpath(buttonXpath)).click();
      waitNumberOfElement(preloaderXpath, preloaderCountWhenPageLoad);
    }
  }

  /**
   * Метод проверяющий соответствие товара фильтру
   *
   * @param products            - список проверяемых товаров
   * @param minPrice            - цена от
   * @param maxPrice            - цена до
   * @param correctCompanyNames - список производителей
   * @return - соответствует ли список фильтру
   */
  public boolean isProductsCorrespond(List<String> products, int minPrice,
      int maxPrice, List<String> correctCompanyNames) {
    List<String> notCorrespondProducts = new ArrayList<>();

    for (String product : products) {
      String productCompany = getProductCompany(product);
      int productPrice = getProductPrice(product);
      if (!correctCompanyNames.contains(productCompany)) {
        notCorrespondProducts.add(productCompany);
      } else if (productPrice < minPrice || productPrice > maxPrice) {
        notCorrespondProducts.add("Цена продукта: " + productCompany + " - " + productPrice);
      }
    }

    if (!notCorrespondProducts.isEmpty()) {
      TestContext.getInstance().put(Context.NOT_CORRESPOND_PRODUCTS.name(), notCorrespondProducts);
      return false;
    }
    return true;
  }

  /**
   * Метод проверяет содержит ли полностью или частично один из элементов списка значение
   *
   * @param productList - список
   * @param product     - значение
   */
  public boolean isListContainsProduct(List<String> productList, String product) {
    for (String p : productList) {
      if (p.contains(product)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Метод находит название компании в заголовке продукта
   *
   * @param elementInfo - текст заголовка продукта
   * @return - название искомой компании (либо иной производитель)
   */
  private static String getProductCompany(String elementInfo) {
    elementInfo = elementInfo.toLowerCase();
    if (elementInfo.contains("lenovo")) {
      return "Lenovo";
    } else if (elementInfo.contains("hp") || (elementInfo.contains("нр"))
        || (elementInfo.contains("hр")) || (elementInfo.contains("нp"))) {
      return "HP";
    } else {
      return "Иной производитель: " + elementInfo;
    }
  }

  /**
   * Метод находит цену товара в тексте заголовка товара
   *
   * @param elementInfo - текст заголовка товара
   * @return - цена товара
   */
  private static int getProductPrice(String elementInfo) {
    int priceIndexTo = elementInfo.lastIndexOf(REGEX_PRICE_TO);
    String price = deleteAllSpaces(
        elementInfo.substring(priceIndexTo - PRICE_MAX_LENGTH, priceIndexTo));
    return Integer.parseInt(price);
  }

  /**
   * Метод удаления нечисловых символов (в том числе убирает нестандартный пробел - &thinsp;)
   *
   * @param str - текстовые данные, содержащие цену
   * @return строка, состоящая из числовыч символов
   */
  private static String deleteAllSpaces(String str) {
    StringBuilder newStr = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
      if (Character.isDigit(str.charAt(i))) {
        newStr.append(str.charAt(i));
      }
    }
    return newStr.toString();
  }

  /**
   * Метод проверяет имеется ли элемент на странице
   *
   * @param elementXpath - Xpath элемента
   */
  private boolean isElementExists(String elementXpath) {
    return driver.findElements(By.xpath(elementXpath)).size() > 0;
  }

  /**
   * Метод возвращает текстовое представление первого на странице искомого элемента
   *
   * @param elementXpath - Xpath искомого элемента
   */
  public String getFirstElementText(String elementXpath) {
    String data = driver.findElement(By.xpath(elementXpath)).getText();
    if (data.isEmpty()) {
      throw new NoElementException(
          "Не найдено ни одного элемента на странице по xPath: " + elementXpath);
    }
    return data;
  }

  /**
   * Геттер списка продуктов
   */
  public List<String> getProducts() {
    return products;
  }

  /**
   * Геттер Xpath кнопки Назад
   */
  public String getPrevPageButtonXpath() {
    return prevPageButtonXpath;
  }

  /**
   * Геттер Xpath кнопки Вперед
   */
  public String getNextButtonXpath() {
    return nextButtonXpath;
  }

  /**
   * Геттер Xpath заголовка продукта
   */
  public String getProductTitleXpath() {
    return productTitleXpath;
  }
}
