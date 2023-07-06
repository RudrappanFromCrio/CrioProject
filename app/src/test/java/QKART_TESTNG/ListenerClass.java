package QKART_TESTNG;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener{

    // public void onStart(ITestContext context){
        

    // }

    public void onTestStart(ITestResult result){
        takeScreenshot(QKART_Tests.driver, "TestStart", result.getName());
        System.out.println("Test Started :" + result.getName() + "Taking ScreenShot !");

    }

     public void onTestSuccess(ITestResult result){
        takeScreenshot(QKART_Tests.driver, "TestSuccess", result.getName());
        System.out.println("Test Succeed :" + result.getName() + "Taking ScreenShot !");

     }

    public void onTestFailure(ITestResult result){
        takeScreenshot(QKART_Tests.driver, "TestFailure", result.getName());
        System.out.println("Test Failed :" + result.getName() + "Taking ScreenShot !");

    }

    // public void onTestSkipped(ITestResult result){

    // }

    // public void onTestFailedButWithinSuccessPercentage(ITestResult result){

    // }

    public void onFinish(ITestContext context){
        takeScreenshot(QKART_Tests.driver, "TestFinish", context.getName());
        System.out.println("Test Finished :" + context.getName() + "Taking ScreenShot !");

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