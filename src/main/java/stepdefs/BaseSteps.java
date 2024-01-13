package stepdefs;

import org.openqa.selenium.WebDriver;
import stash.Context;
import stash.TestContext;

/**
 * Базоваый step-класс
 */
public class BaseSteps {

  public TestContext testContext;

  public WebDriver chromeDriver;

  public BaseSteps() {
    this.testContext = TestContext.getInstance();
    this.chromeDriver = (WebDriver) this.testContext.get(Context.CHROME_DRIVER.toString());
  }
}
