package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;
import static org.junit.Assert.assertFalse;
import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.annotations.Test;

public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(description = "verify registration happens correctly", priority = 1, groups = {"sanity"})
    @Parameters({"TC1_User", "TC1_Password"})
    public void TestCase01(String TC1_User, String TC1_Password) throws InterruptedException {
        Boolean status;
        // logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
        // takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

        // logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
        // ? "PASS" : "FAIL");
        // takeScreenshot(driver, "EndTestCase", "TestCase1");
    }

    @Test(description = "verify re-registring an existing user fails", priority = 2, groups = {"sanity"})
    @Parameters({"TC2_User", "TC2_Password"})
    public void TestCase02(String TC2_User, String TC2_Password) throws InterruptedException {
        Boolean status;
        // logStatus("Start Testcase", "Test Case 2: Verify User Registration with an existing
        // username ", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed to register new user");
        // logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        // if (!status) {
        // logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" :
        // "FAIL");


        // }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);
        assertFalse("Successfully registered using existing registered username", status);
        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        // logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" :
        // "PASS");

    }

    @Test(description = "verify function search text box", priority = 3, groups = {"sanity"})
    @Parameters({"TC3_PrductName1", "TC3_PrductName2"})
    public void TestCase03(String TC3_PrductName1, String TC3_PrductName2) throws InterruptedException {
        // logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct("YONEX");
        assertTrue(status, "Unable to search for given product");
        // if (!status) {
        // logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
        // return false;
        // }

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        Boolean results1 = searchResults.size() == 0;

        assertFalse("results are not found", results1);

        // Verify the search results are available
        // assertEquals(searchResults, searchResults.size() == 0, "There are results for the given
        // search string 1");
        // if (searchResults.size() == 0) {
        // logStatus("TestCase 3", "Test Case Failure. There were no results for the given search
        // string", "FAIL");
        // return false;
        // }

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();

            assertEquals(true, elementText.toUpperCase().contains("YONEX"),
                    "Test Results contains un-expected values");
            // if (!elementText.toUpperCase().contains("YONEX")) {
            // logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values:
            // " + elementText, // "FAIL");
            // return false;
            // }
        }

        // logStatus("Step Success", "Successfully validated the search results ", "PASS");

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        assertFalse("Able to search for given product", status);

        // if (status) {
        // logStatus("TestCase 3", "Test Case Failure. Invalid keyword returned results", "FAIL");
        // return false;
        // }

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        Boolean results2 = searchResults.size() == 0;
        assertTrue(results2, "results are found");

    }

    @Test(description = "verify size chart and contents inside size chart", priority = 4, groups = {"regression"})
    @Parameters({"TC4_PrductName"})
    public void TestCase04(String TC4_PrductName) throws InterruptedException {
        // logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);


            if (result.verifySizeChartExists()) {
                // logStatus("Step Success", "Successfully validated presence of Size Chart Link",
                // "PASS");

                status = result.verifySizeChartExists();
                System.out.println("failed to validate presence of Size Chart Link" + status);
                assertTrue(status, "failed to validate presence of Size Chart Link");
                // assertFalse("Successfully valiidated presence of Size Chart Link", status);


                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                assertTrue(status, "failed to validate presence of drop down");
                System.out.println("failed to validate presence of drop down" + status);
                // assertFalse("Successfully valiidated presence of drop down", status);
                // logStatus("Step Success", "Validated presence of drop down", status ? "PASS" :
                // "FAIL");

                // Open the size chart
                // Thread.sleep(2000);
                status = result.openSizechart();

                assertTrue(status, "failed to open size chart");
                // assertFalse("Successfully opened size chart", status);

                if (status) {

                    status = result.validateSizeChartContents(expectedTableHeaders,
                            expectedTableBody, driver);
                    assertTrue(status, "failed to validate contents of Size Chart Link");
                    // assertFalse("succesfully validated contents of Size Chart Link", status);


                    // Close the size chart modal
                    status = result.closeSizeChart(driver);
                    assertTrue(status, "Closing size chart failed");
                }
            }
        }


    }


    @Test(description = "verify new user can add multiple products inside cart", priority = 5, groups = {"sanity"})
    @Parameters({"TC5_PrductName1", "TC5_PrductName2"})
    public void TestCase05(String TC5_PrductName1, String TC5_PrductName2) throws InterruptedException {
        Boolean status;
        // logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Test Case Failure. unable to register");
        // if (!status) {
        // logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        // }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "User Perform Login Failed");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" :
        // "FAIL");
        // }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("YONEX");
        assertTrue(status, "Unable to search for given product");
        // homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.addProductToCart("YONEX Smash Badminton Racquet");
        assertTrue(status, "Unable to add product to cart 1");


        status = homePage.searchForProduct("Tan");
        assertTrue(status, "Unable to search for given product");
        // homePage.addProductToCart("Tan Leatherette Weekender Duffle");
        status = homePage.addProductToCart("Tan Leatherette Weekender Duffle");
        assertTrue(status, "Unable to add product to cart 2");

        // Click on the checkout button
        // homePage.clickCheckout();
        status = homePage.clickCheckout();
        assertTrue(status, "unable to find the checkout url");

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        // checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        status = checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        assertTrue(status, "unable to add new address");
        // checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        status = checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        // assertEquals(status, "Addr line 1 addr Line 2 addr line 3", "unable to select address");
        assertTrue(status, "unable to select address");
        // Place the order
        checkoutPage.placeOrder();


        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");
        assertTrue(status, "failed to redirect");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();

        // logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" :
        // "FAIL");

    }



    @Test(description = "verify contents of the cart can be edited", priority = 6, groups = {"regression"})
    @Parameters({"TC6_PrductName1", "TC6_PrductName2"})
    public void TestCase06(String TC6_PrductName1, String TC6_PrductName2) throws InterruptedException {
        Boolean status;
        // logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Test Case Failure. unable to register");

        // if (!status) {
        // logStatus("Step Failure", "User Perform Register Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ?
        // "PASS"
        // : "FAIL");
        // return false;
        // }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "User Perform Login Failed");

        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ?
        // "PASS"
        // : "FAIL");
        // return false;
        // }

        homePage.navigateToHome();
        status = homePage.searchForProduct("Xtend");
        assertTrue(status, "Unable to search for given product");
        // homePage.addProductToCart("Xtend Smart Watch");
        status = homePage.addProductToCart("Xtend Smart Watch");
        assertTrue(status, "Unable to add product to cart 1");

        status = homePage.searchForProduct("Yarine");
        assertTrue(status, "Unable to search for given product");
        // homePage.addProductToCart("Yarine Floor Lamp");
        status = homePage.addProductToCart("Yarine Floor Lamp");
        assertTrue(status, "Unable to add product to cart 2");

        // update watch quantity to 2
        // homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);
        status = homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);
        assertTrue(status, "falied to change product quantity");

        // update table lamp quantity to 0
        // homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);
        status = homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);
        // assertTrue(status, "falied to change product quantity");

        // update watch quantity again to 1
        // homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);
        status = homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);
        assertTrue(status, "falied to change product quantity");

        // homePage.clickCheckout();

        status = homePage.clickCheckout();
        assertTrue(status, "failed to click");

        Checkout checkoutPage = new Checkout(driver);
        // checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        status = checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        assertTrue(status, "unable to add new address");
        // checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        status = checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        assertTrue(status, "unable to select address");

        checkoutPage.placeOrder();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(
                    ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());

        }

        status = driver.getCurrentUrl().endsWith("/thanks");
        assertTrue(status, "failed to redirect");

        homePage.navigateToHome();
        homePage.PerformLogout();

        // logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ?
        // "PASS"
        // : "FAIL");
        // return status;
    }


    @Test(description = "verify contents made to the cart are saved againstthe user's login details", priority = 7, groups = {"regression"})
    @Parameters({"TC7_PrductName1", "TC7_PrductName2"})
    public void TestCase07(String TC7_PrductName1, String TC7_PrductName2) throws InterruptedException {
        Boolean status = false;
        List<String> expectedResult =
                Arrays.asList("Stylecon 9 Seater RHS Sofa Set ", "Xtend Smart Watch");

        //logStatus("Start TestCase",
               // "Test Case 7: Verify that cart contents are persisted after logout", "DONE");

        Register registration = new Register(driver);
        Login login = new Login(driver);
        Home homePage = new Home(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Test Case Failure. unable to register");

        
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     logStatus("End TestCase",
        //             "Test Case 7:  Verify that cart contents are persited after logout: ",
        //             status ? "PASS" : "FAIL");
        //     return false;
        // }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "failed to login");

        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     logStatus("End TestCase",
        //             "Test Case 7:  Verify that cart contents are persited after logout: ",
        //             status ? "PASS" : "FAIL");
        //     return false;
        // }

        homePage.navigateToHome();

        status = homePage.searchForProduct("Stylecon");
        assertTrue(status, "unable to search for given string");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");

        status = homePage.searchForProduct("Xtend");
        assertTrue(status, "unable to search for given string");
        homePage.addProductToCart("Xtend Smart Watch");

        status=homePage.PerformLogout();
        assertTrue(status, "Failed to perform logout");

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed to perform login");

        status = homePage.verifyCartContents(expectedResult);
        assertTrue(status, "failed to verify cart contents");

        // logStatus("End TestCase",
        //         "Test Case 7: Verify that cart contents are persisted after logout: ",
        //         status ? "PASS" : "FAIL");

        homePage.PerformLogout();
        
    }



    @Test(description = "verify insufficient balance is thrown when wallet balance is not enough", priority = 8, groups = {"sanity"})
    @Parameters({"TC8_PrductName1", "TC8_Qty"})
    public void TestCase08(String TC8_PrductName1, String TC8_Qty) throws InterruptedException {
        Boolean status;
        int TC8_Qty_int = Integer.parseInt(TC8_Qty);
        // logStatus("Start TestCase",
        //         "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is not enough",
        //         "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Test Case Failure. unable to register");
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
        //     logStatus("End TestCase",
        //             "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
        //             status ? "PASS" : "FAIL");
        //     return false;
        // }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "failed to login");
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     logStatus("End TestCase",
        //             "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
        //             status ? "PASS" : "FAIL");
        //     return false;
        // }

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("Stylecon");
        assertTrue(status, "unable to search for given string");

        status = homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");
        assertTrue(status, "failed to add to cart");

        status = homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", 10);
        assertTrue(status, "failed to change product quantity");

        status = homePage.clickCheckout();
        assertTrue(status, "failed to checkout");


        Checkout checkoutPage = new Checkout(driver);
        status = checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        assertTrue(status, "failed to add new address");
        status = checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        assertTrue(status, "failed to select address");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();
        assertTrue(status, "failed to verify insufficient balance");

        // logStatus("End TestCase",
        //         "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
        //         status ? "PASS" : "FAIL");        
    }

    @Test(dependsOnMethods = {"TestCase10"}, description = "verify product added to cart is available when new tab is added", priority = 10, groups = {"regression"})
    public void TestCase09() throws InterruptedException {
        Boolean status = false;

        // logStatus("Start TestCase",
        //         "Test Case 9: Verify that product added to cart is available when a new tab is opened",
        //         "DONE");
        // takeScreenshot(driver, "StartTestCase", "TestCase09");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        assertTrue(status, "failed to regiter new user");
        // if (!status) {
        //     logStatus("TestCase 9",
        //             "Test Case Failure. Verify that product added to cart is available when a new tab is opened",
        //             "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase09");
        // }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "failed to login");
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase9");
        //     logStatus("End TestCase",
        //             "Test Case 9:   Verify that product added to cart is available when a new tab is opened",
        //             status ? "PASS" : "FAIL");
        // }

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        assertTrue(status, "failed to search for given text");
        status = homePage.addProductToCart("YONEX Smash Badminton Racquet");
        assertTrue(status, "failed to add product to cart");

        String currentURL = driver.getCurrentUrl();
        assertEquals(currentURL, "https://crio-qkart-frontend-qa.vercel.app/", "failed to get current url");

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        WebDriver newWindow = driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);
        assertTrue(status, "failed to verify cart contents");

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        // logStatus("End TestCase",
        // "Test Case 9: Verify that product added to cart is available when a new tab is opened",
        // status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "EndTestCase", "TestCase09");

       
    }


    @Test(description = "verify privacy policy and terms of service links working fine", priority = 9, groups = {"regression"})
    public void TestCase10() throws InterruptedException {
        Boolean status = false;

        // logStatus("Start TestCase",
        //         "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        //         "DONE");
        // takeScreenshot(driver, "StartTestCase", "TestCase10");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "failed to register using new user");
        // if (!status) {
        //     logStatus("TestCase 10",
        //             "Test Case Failure.  Verify that the Privacy Policy, About Us are displayed correctly ",
        //             "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase10");
        // }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "failed to login");
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase10");
        //     logStatus("End TestCase",
        //             "Test Case 10:    Verify that the Privacy Policy, About Us are displayed correctly ",
        //             status ? "PASS" : "FAIL");
        // }

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);
        assertTrue(status, "failed to get current url");

        // if (!status) {
        //     logStatus("Step Failure", "Verifying parent page url didn't change on privacy policy link click failed", status ? "PASS" : "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase10");
        //     logStatus("End TestCase",
        //             "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        //             status ? "PASS" : "FAIL");
        // }

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        assertTrue(status, "failed to get privacy policy url");
        // if (!status) {
        //     logStatus("Step Failure", "Verifying new tab opened has Privacy Policy page heading failed", status ? "PASS" : "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase10");
        //     logStatus("End TestCase",
        //             "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        //             status ? "PASS" : "FAIL");
        // }

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        assertTrue(status, "failed to get Terms of Service url");
        // if (!status) {
        //     logStatus("Step Failure", "Verifying new tab opened has Terms Of Service page heading failed", status ? "PASS" : "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase10");
        //     logStatus("End TestCase",
        //             "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        //             status ? "PASS" : "FAIL");
        // }

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        // logStatus("End TestCase",
        // "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        // "PASS");
        // takeScreenshot(driver, "EndTestCase", "TestCase10");

    }

    @Test(description = "verify contact us dialogue box works fine", priority = 11, groups = {"regression"})
    @Parameters({"TC11_ContactUsUserName", "TC11_ContactUsEmail", "TC11_ContactUsMessage"})
    public void TestCase11(String TC11_ContactUsUserName, String TC11_ContactUsEmail, String TC11_ContactUsMessage) throws InterruptedException {
        Boolean status=false;
        // logStatus("Start TestCase",
        //         "Test Case 11: Verify that contact us option is working correctly ",
        //         "DONE");
        // takeScreenshot(driver, "StartTestCase", "TestCase11");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        WebElement contactUsClick = driver.findElement(By.xpath("//*[text()='Contact us']"));
        contactUsClick.click();


       

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");
        status = name.isDisplayed();
        assertTrue(status, "name is visible");
       
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys("criouser@gmail.com");
        status = email.isDisplayed();
        assertTrue(status, "email is visible");
        
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys("Testing the contact us page");
        status = message.isDisplayed();
        assertTrue(status, "message is visible");

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        status = wait.until(ExpectedConditions.invisibilityOf(contactUs));

        assertTrue(status, "contact us is visible");
        

        // logStatus("End TestCase",
        //         "Test Case 11: Verify that contact us option is working correctly ",
        //         "PASS");

        // takeScreenshot(driver, "EndTestCase", "TestCase11");

        // return true;
    }


    @Test(description = "ensure addvertisement links on the qkart page are clickable", priority = 12, groups = {"sanity"})
    @Parameters({"TC12_ProductName"})
    public void TestCase12(String TC12_ProductName) throws InterruptedException {
        Boolean status = false;
        // logStatus("Start TestCase",
        //         "Test Case 12: Ensure that the links on the QKART advertisement are clickable",
        //         "DONE");
        // takeScreenshot(driver, "StartTestCase", "TestCase12");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "failed to register using new user");
        // if (!status) {
        //     logStatus("TestCase 12",
        //             "Test Case Failure. Ensure that the links on the QKART advertisement are clickable",
        //             "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase12");
        // }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "failed to login");
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     takeScreenshot(driver, "Failure", "TestCase 12");
        //     logStatus("End TestCase",
        //             "Test Case 12:  Ensure that the links on the QKART advertisement are clickable",
        //             status ? "PASS" : "FAIL");
        // }

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
        assertTrue(status, "failed to search for given string");
        status = homePage.addProductToCart("YONEX Smash Badminton Racquet");
        assertTrue(status, "failed to add product to cart");
        status = homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        assertTrue(status, "failed to change product quantity");
        status = homePage.clickCheckout();
        assertTrue(status, "failed to check out");

        Checkout checkoutPage = new Checkout(driver);
        status = checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
        assertTrue(status, "failed to add address");
        status = checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        assertTrue(status, "failed to select address");
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        assertTrue(status, "unable to verify addvertisements size");
        //logStatus("Step ", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        assertTrue(status, "Verify that Advertisement 1 is clickable");
        //logStatus("Step ", "Verify that Advertisement 1 is clickable ", status ? "PASS" : "FAIL");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        assertTrue(status, "Verify that Advertisement 1 is clickable");

        // logStatus("Step ", "Verify that Advertisement 2 is clickable ", status ? "PASS" : "FAIL");

        // logStatus("End TestCase",
        //         "Test Case 12:  Ensure that the links on the QKART advertisement are clickable",
        //         status ? "PASS" : "FAIL");
        // return status;
    }


    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s",
                String.valueOf(java.time.LocalDateTime.now()), type, message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType,
                    description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

