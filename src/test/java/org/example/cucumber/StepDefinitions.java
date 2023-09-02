package org.example.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class StepDefinitions {
    private static WebDriver chromeDriver;
    public String betRate;

    @Given("User try to open site on default SportBook page")
    public void userOpenSiteOnDefaultPage() throws InterruptedException {
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        chromeDriver.get("https://en.greatodds.com/");
    }

    @And("User searches menu {string} on homepage")
    public void userSearchAProduct(String menuName) throws InterruptedException {
        chromeDriver.findElement(By.xpath("//*[@class = 'spb-logo spb-util-align-float-left']"));
        WebElement leftMenuElement = chromeDriver.findElement(By.xpath("//span[text() ='" + menuName + "']"));
        leftMenuElement.click();
    }

    @And("User select category {string}")
    public void userSearchCategory(String categoryName) {
        WebElement categoryElement = chromeDriver.findElement(By.xpath("//span[contains(text(), 'E-Sports - " + categoryName + "')]"));
        categoryElement.click();
    }

    @When("User choose today's fist available match")
    public void userChooseFirstAvailableMatch() {
        WebElement element = chromeDriver.findElement(By.xpath("//div[text()='today']"));
        if (element.isDisplayed()) {
            List<WebElement> todayMatchTable = chromeDriver.findElements(By.xpath("//*[@id=0]//*[name()='spb-coupons-grid-table']"));
            Optional<WebElement> firstTodaysMatch = todayMatchTable.stream()
                    .findFirst();
            firstTodaysMatch.stream().filter(firstMatch -> firstMatch.findElement(By.xpath("//div[starts-with(@class,\"spb-market-group\")]")).isDisplayed())
                    .findFirst()
                    .ifPresent(WebElement::click);
        }
    }

    @And("User take a bet")
    public void userTakeBet() {
        List<WebElement> matchBetsCollection = chromeDriver.findElements(By.xpath("//div/spb-market-coupon//spb-odds-content"));
        for (WebElement webElement : matchBetsCollection) {
            betRate = webElement.getText();
            webElement.click();
            break;
        }
    }

    @And("Check button {string} is enabled")
    public void buttonIsEnabledCheck(String buttonName) {
        Assert.assertTrue(chromeDriver.findElement(By.xpath("//*[contains(@class,'general-btn')]//*[text()='" + buttonName + "']")).isDisplayed());
        String buttonClassInformation = chromeDriver.findElement(By.xpath("//*[contains(@class,'general-btn')]//*[text()='" + buttonName + "']//parent::div")).getAttribute("class");
        Assert.assertFalse("Button " + buttonName + " is Disabled", buttonClassInformation.contains("disabled"));
    }

    @And("Get an Error message: {string}")
    public void errorMessage(String expectedErrorMessage) {
        if (chromeDriver.findElement(By.xpath("//*[@class='spb-alert-block__message']")).isDisplayed()) {
            String errorMessage = chromeDriver.findElement(By.xpath("//*[@class='spb-alert-block__message']")).getText();
            Assert.assertTrue("Displayed Error message:" + errorMessage + "", errorMessage.contains(expectedErrorMessage));
        }
    }

    @And("After clicking the button Place bet, a login window opens")
    public void loginWindowOpens() {
        if (chromeDriver.findElement(By.xpath("//*[contains(@class,'general-btn')]//*[text()='Place bet']")).isDisplayed()) {
            chromeDriver.findElement(By.xpath("//*[contains(@class,'general-btn')]//*[text()='Place bet']")).click();
            Assert.assertTrue("Login form window is not displayed", chromeDriver.findElement(By.xpath("//spb-login-form")).isDisplayed());
        }
    }

    @And("Click button {string}")
    public void clickButton(String buttonName) {
        chromeDriver.findElement(By.xpath(" //button[text()='" + buttonName + "']")).click();
    }

    @And("Booking Code is available")
    public void bookingCodeIsAvailable() {
        String bookingCode = chromeDriver.findElement(By.xpath("//span[text()='Your Booking Code is']//following-sibling::div/span")).getText();
        Assert.assertFalse("Booking code is empty", bookingCode.isEmpty());
    }

    @And("Bet is displayed on Bet Slip block")
    @Then("Bet is displayed on Bet Slip block and Correct")
    public void betSlipSelection() {
        if (chromeDriver.findElement(By.xpath("//spb-betslip-selections-singles//*[@class='ng-star-inserted']")).isDisplayed()) {
            String betRateDisplayed = chromeDriver.findElement(By.xpath("//spb-betslip-selections-singles//*[@class='ng-star-inserted']")).getText();
            Assert.assertTrue("Selected bet rate not equals displayed bet rate at Slip  section ", betRate.contains(betRateDisplayed));
        }
    }

    @Then("The user adds amount {string} of money to place a bet")
    public void userAddsAmountOfMoney(String amountOfMoney) {
        chromeDriver.findElement(By.xpath("//input[@type='number']")).sendKeys(amountOfMoney);
    }

    @After()
    public void closeBrowser() {
        chromeDriver.quit();
    }

}
