package QKART_SANITY_LOGIN.Module1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // Wait for Logout to Complete
            Thread.sleep(3000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            WebElement searchBox= this.driver.findElement(By.name("search"));
            searchBox.clear();
            searchBox.sendKeys(product);
            // Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {};
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver
                    .findElements(By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']"));

            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            WebElement noProductsFoundElement =
                    driver.findElement(By.xpath("//h4[text()=' No products found ']"));
            if (noProductsFoundElement.isDisplayed()) {
                String expectedText = " No products found ";
                String actualText = noProductsFoundElement.getText();
                if (actualText.equals(expectedText)) {
                    status = true;
                }
            }
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding to the
             * matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            //// div[@class='MuiGrid-root MuiGrid-container MuiGrid-spacing-xs-2
            //// css-1msksyp']/div/div
            List<WebElement> productNameWebElements = driver.findElements(
                    By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 css-yg30e6']"));
            List<WebElement> addToCartButtonElements =
                    driver.findElements(By.xpath("//button[text()='Add to cart']"));

            boolean productName1 = false;

            for (int i = 0; i < productNameWebElements.size(); i++) {
                WebElement productNameWebElement = productNameWebElements.get(i);
                String actualProductName = productNameWebElement.getText();
                if (actualProductName.equals(productName)) {
                    WebElement addToCartButtonElement = addToCartButtonElements.get(i);
                    addToCartButtonElement.click();
                    productName1 = true;
                }

            }
            if (!productName1) {
                System.out.println("Unable to find the given product");
            }


            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            WebElement checkoutButtonElement =
                    driver.findElement(By.xpath("//button[text()='Checkout']"));
            checkoutButtonElement.click();
            return true;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName

            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)
            List<WebElement> cartItemElements = driver.findElements(By.xpath(
                    "//div[@class='cart MuiBox-root css-0']//div[@class='MuiBox-root css-0']"));
            for (WebElement cartItemElement : cartItemElements) {
                String actualProductName = cartItemElement
                        .findElement(By.xpath("//div[@class='MuiBox-root css-1gjj37g']/div[1]"))
                        .getText();
                if (actualProductName.equals(productName)) {
                    String quantityString = cartItemElement
                            .findElement(By.xpath(".//div[@data-testid='item-qty']")).getText();
                    int actualQuantity = Integer.parseInt(quantityString);
                    while (true) {
                        if (actualQuantity < quantity) {
                            WebElement plusIcon = cartItemElement.findElement(
                                    By.cssSelector("svg[data-testid='AddOutlinedIcon']"));
                            plusIcon.click();
                            //driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                            
                        } else if (actualQuantity > quantity) {
                            WebElement minusIcon = cartItemElement.findElement(
                                    By.cssSelector("svg[data-testid='RemoveOutlinedIcon']"));
                            minusIcon.click();
                            //driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                            
                        }
                        Thread.sleep(1000);
                      
                

                        if (quantity == 0) {
                            break;
                        }
                        String quantityString1 = cartItemElement
                                .findElement(By.xpath(".//div[@data-testid='item-qty']")).getText();
                        int actualQuantity1 = Integer.parseInt(quantityString1);
                        if (actualQuantity1 == quantity) {
                            break;
                        }

                    }
                }
            }


            return true;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    
    }
    


    

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6

            // Get all the cart items as an array of webelements

            // Iterate through expectedCartContents and check if item with matching product
            // name is present in the cart
            try{
            List<WebElement> cartItemElements = driver.findElements(By.xpath(
                "//div[@class='cart MuiBox-root css-0']//div[@class='MuiBox-root css-0']"));
        
                  
                    if(cartItemElements.equals(expectedCartContents))
                    return true;
                    
            } catch(Exception e) {
                    System.out.println("Exception while verifying cart contents: " + e.getMessage());
                    return false;
                }
            return true;
                 
                    
            }
        }
    
                    
            

         

    
        
