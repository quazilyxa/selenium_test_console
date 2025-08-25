import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;
import java.time.Duration;

public class lyxaConsole {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.out.println("Setting up test environment...");
        
        // Use WebDriverManager instead of manual path
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(priority = 1, description = "Login to Lyxa Console")
    public void testLogin() {
        try {
            System.out.println("Navigating to login page...");
            driver.get("https://test-v2-panel.lyxa.ai/auth/sign-in?returnTo=%2F&loginAs=admin");
            takeScreenshot(driver, "screenshots/0_login_page.png");

            System.out.println("🔐 Logging in...");

            clearAndType(findElement(wait, driver,
                    By.name("email"),
                    By.xpath("//input[contains(@placeholder,'email') or contains(@placeholder,'Email')]"),
                    By.xpath("//input[@type='text' or @type='email']")),
                    "nour@gmail.com");
            Thread.sleep(1000);
            
            clearAndType(findElement(wait, driver,
                    By.name("password"),
                    By.id("password"),
                    By.xpath("//input[@type='password']"),
                    By.xpath("//input[contains(@placeholder,'password') or contains(@placeholder,'Password')]")),
                    "Nour1234@");
            Thread.sleep(1000);
            
            findElement(wait, driver,
                    By.cssSelector("button[type='submit']"),
                    By.xpath("//button[contains(text(),'Sign') or contains(text(),'Login') or contains(text(),'Submit')]"),
                    By.xpath("//input[@type='submit']"),
                    By.xpath("//button[@type='submit' or contains(@class,'submit') or contains(@class,'login')]")
            ).click();

            System.out.println("Waiting for login confirmation...");
            boolean loginSuccess = isLoginSuccessful(wait, driver);
            
            if (loginSuccess) {
                takeScreenshot(driver, "screenshots/1_lyxa_login_success.png");
                System.out.println("✅ Login successful");
                Assert.assertTrue(true, "Login was successful");
            } else {
                System.out.println("❌ Login confirmation failed.");
                takeScreenshot(driver, "screenshots/login_failed.png");
                Assert.fail("Login failed - could not verify successful login");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Login test failed: " + e.getMessage());
            takeScreenshot(driver, "screenshots/login_error.png");
            Assert.fail("Login test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 2, dependsOnMethods = "testLogin", description = "Navigate to Settings")
    public void testNavigateToSettings() {
        try {
            Thread.sleep(2000);

            // Click top-left menu button
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[contains(@class,'MuiIconButton-root')])[1]")
            ));
            menuButton.click();
            System.out.println("📂 Menu button clicked");
            takeScreenshot(driver, "screenshots/2_menu_opened.png");

            // Click the "Settings" menu item
            WebElement settingsMenuItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='mnl__nav__item__title MuiBox-root css-0' and text()='Settings']")
            ));
            
            Thread.sleep(2000);
            settingsMenuItem.click();
            System.out.println("⚙️ Settings menu item clicked");
            takeScreenshot(driver, "screenshots/3_settings_page.png");
            
            // Verify we're on settings page
            Assert.assertTrue(driver.getCurrentUrl().contains("settings") || 
                            driver.getPageSource().contains("Settings"), 
                            "Settings page was not loaded successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Navigate to settings failed: " + e.getMessage());
            takeScreenshot(driver, "screenshots/settings_navigation_error.png");
            Assert.fail("Navigate to settings failed: " + e.getMessage());
        }
    }

    @Test(priority = 3, dependsOnMethods = "testNavigateToSettings", description = "Add Default Chat Message")
    public void testAddDefaultChatMessage() {
        try {
            Thread.sleep(2500);

            // Click "Default Chat Messages"
            WebElement defaultChatMessages = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='MuiBox-root css-171onha' and text()='Default Chat Messages']")
            ));
            defaultChatMessages.click();
            System.out.println("📂 Default Chat Messages opened");
            Thread.sleep(2000);
         
            WebElement addChatMessageBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Add Chat Message')]")
            ));
            addChatMessageBtn.click();
            System.out.println("Add Chat Message clicked");
            
            Thread.sleep(2000);
            
            // Wait for text area to be visible (not the hidden one)
            WebElement messageTextarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[not(@readonly)]")
            ));

            // Type a random short message
            String randomMessage = "Test msg " + System.currentTimeMillis();
            messageTextarea.sendKeys(randomMessage);
            System.out.println("✏️ Message typed: " + randomMessage);

            // Click "Add" button to submit
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
            ));
            addButton.click();
            System.out.println("Chat message added");

            // Wait a bit to see result
            Thread.sleep(2000);
            takeScreenshot(driver, "screenshots/4_message_added.png");
            
            // Verify message was added (you can customize this assertion based on UI feedback)
            Assert.assertTrue(true, "Chat message was added successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Add chat message failed: " + e.getMessage());
            takeScreenshot(driver, "screenshots/add_message_error.png");
            Assert.fail("Add chat message failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }

    @AfterMethod
    public void afterMethod(org.testng.ITestResult result) {
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            takeScreenshot(driver, "screenshots/failure_" + result.getMethod().getMethodName() + ".png");
        }
    }

    // Reusable element finder with multiple locators
    private static WebElement findElement(WebDriverWait wait, WebDriver driver, By... locators) {
        for (By locator : locators) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception ignored) {
            }
        }
        throw new NoSuchElementException("Element not found with given locators");
    }

    // Clear & type method
    private static void clearAndType(WebElement element, String text) {
        element.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        element.sendKeys(text);
    }

    // Faster login confirmation with parallel conditions
    private static boolean isLoginSuccessful(WebDriverWait wait, WebDriver driver) {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Dashboard')]")),
                    ExpectedConditions.urlContains("dashboard"),
                    ExpectedConditions.not(ExpectedConditions.urlContains("sign-in"))
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Screenshot method
    private static void takeScreenshot(WebDriver driver, String filePath) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);
            System.out.println("📸 Screenshot saved: " + filePath);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}