package stepdefs;

import static stash.Context.*;

import helpers.Assertions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import pages.YandexMarketAfterSearch;
import pages.YandexMarketCatalog;
import pages.YandexMarketSection;
import pages.YandexMarketMain;
import stash.TestContext;

/**
 * Класс с реализацией тестовых шагов
 */
public class YandexMarketSteps extends BaseSteps {

  @When("пользователь переходит на сайт {string}")
  public void goPage(String url) {
    chromeDriver.get(url);
  }

  @Then("тайтл верен {string}")
  public void checkTitle(String title) {
    String currentTitle = chromeDriver.getTitle();
    Assert.assertTrue("Тайтл " + title + " не содержится в заголовке",
        currentTitle.contains(title));
  }

  @When("пользователь переходит в {string}")
  public void goTo(String buttonName) {
    YandexMarketMain yandexMarketMain = new YandexMarketMain(chromeDriver, buttonName);
    yandexMarketMain.clickButton();
  }

  @When("наводит курсор на раздел {string} и переходит в раздел {string}")
  public void moveMainSection(String mainSectionName, String secondSectionName) {
    YandexMarketCatalog yandexMarketCatalog = new YandexMarketCatalog(chromeDriver,
        mainSectionName);
    yandexMarketCatalog.moveCursorToMainSection();
    yandexMarketCatalog.goSection(secondSectionName);
  }

  @When("пользователь задает параметр «Цена, Р» от {string} до {string} рублей")
  public void fillPriceField(String min, String max) {
    TestContext.getInstance().put(MIN_PRICE.name(), Integer.parseInt(min));
    TestContext.getInstance().put(MAX_PRICE.name(), Integer.parseInt(max));

    YandexMarketSection yandexMarketSection = new YandexMarketSection(chromeDriver);
    yandexMarketSection.fillField(yandexMarketSection.getFieldMinPrice(), min);
    yandexMarketSection.fillField(yandexMarketSection.getFieldMaxPrice(), max);
  }

  @When("выбирает производителя {string} и {string}")
  public void fillCheckbox(String company1, String company2) {
    TestContext.getInstance().put(CORRECT_COMPANY_NAMES.name(), Arrays.asList(company1, company2));

    YandexMarketSection yandexMarketSection = new YandexMarketSection(chromeDriver,
        (List<String>) TestContext.getInstance().get(CORRECT_COMPANY_NAMES.name()));
    yandexMarketSection.clickCheckbox(yandexMarketSection.getCompanies());
  }

  @Then("на первой странице отобразилось более {string} элементов товаров")
  public void checkElementsCount(String count) {
    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    yandexMarketAfterSearch.loadProducts();
    int currentProductCount = yandexMarketAfterSearch.getProducts().size();
    Assertions.assertTrue(currentProductCount > Integer.parseInt(count),
        "Количество товаров на первой странице " + currentProductCount);
  }

  @When("пользователь открывает все предложения")
  public void getAllProducts() {
    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    yandexMarketAfterSearch.loadAllProducts(yandexMarketAfterSearch.getNextButtonXpath());
    TestContext.getInstance().put(ALL_PRODUCTS.name(), yandexMarketAfterSearch.getProducts());
  }

  @Then("товар соответствуют фильтру")
  public void productsCorrespond(List<String> arg) {
    List<String> allProducts = (List<String>) TestContext.getInstance().get(ALL_PRODUCTS.name());
    int minPrice = (int) TestContext.getInstance().get(MIN_PRICE.name());
    int maxPrice = (int) TestContext.getInstance().get(MAX_PRICE.name());

    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    System.out.println("allProducts from context size: " + allProducts.size());
    System.out.println("minPrice from context : " + minPrice);
    System.out.println("maxPrice from context : " + maxPrice);

    boolean isCorrespond = yandexMarketAfterSearch.isProductsCorrespond(allProducts, minPrice,
        maxPrice, arg);
    Assertions.assertTrue(isCorrespond, "Список несоответствующих фильтру товаров: " +
        TestContext.getInstance().get(NOT_CORRESPOND_PRODUCTS.name()));
  }

  @When("пользователь возвращается на первую страницу с результатами поиска ноутбуков")
  public void returnToPage() {
    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    yandexMarketAfterSearch.rideToPage(yandexMarketAfterSearch.getPrevPageButtonXpath());
  }

  @When("пользователь запоминает первое наименование ноутбука")
  public void saveContext() {
    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    String firstProductData = yandexMarketAfterSearch.getFirstElementText(
        yandexMarketAfterSearch.getProductTitleXpath());
    TestContext.getInstance().put(FIRST_PRODUCT_ON_PAGE.name(), firstProductData);
  }

  @When("пользователь вводит в поисковую строку запомненное значение")
  public void fillFindField() {
    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    yandexMarketAfterSearch.fillSearchField(
        (String) TestContext.getInstance().get(FIRST_PRODUCT_ON_PAGE.name()));
  }

  @When("пользователь нажимает кнопку {string}")
  public void clickButton(String buttonName) {
    YandexMarketMain yandexMarketMain = new YandexMarketMain(chromeDriver, buttonName);
    yandexMarketMain.clickButton();
  }

  @Then("в результатах поиска, на первой странице, есть искомый товар")
  public void isProductExists() {
    YandexMarketAfterSearch yandexMarketAfterSearch = new YandexMarketAfterSearch(chromeDriver);
    yandexMarketAfterSearch.loadProducts();
    List<String> products = yandexMarketAfterSearch.getProducts();
    String expectedProduct = (String) TestContext.getInstance().get(
        FIRST_PRODUCT_ON_PAGE.name());
    Assertions.assertTrue(yandexMarketAfterSearch.isListContainsProduct(products, expectedProduct),
        "Список товаров на странице: " + products + " не содержит товара: " + expectedProduct);
  }
}
