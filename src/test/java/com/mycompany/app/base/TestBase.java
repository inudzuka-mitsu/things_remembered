package com.mycompany.app.base;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.qameta.allure.Allure;

public class TestBase {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    public Properties props;

    private static final Map<String, Browser.NewContextOptions> DEVICE_MAP = new HashMap<>();

    static {
        DEVICE_MAP.put("iPhone 13 Pro Max", new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1")
                .setViewportSize(428, 926)
                .setDeviceScaleFactor(3)
                .setIsMobile(true)
                .setHasTouch(true));
        DEVICE_MAP.put("Samsung Galaxy A52", new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (Linux; Android 12; SM-A525F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36")
                .setViewportSize(412, 915)
                .setDeviceScaleFactor(3)
                .setIsMobile(true)
                .setHasTouch(true));
    }

    @RegisterExtension
    public final TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            System.out.println(">>> ❌ Test Failed. Capturing screenshot...");
            try {
                if (page != null) {
                    byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                    Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));
                }
            } catch (Exception e) {
                System.out.println(">>> Failed to take screenshot: " + e.getMessage());
            } finally {
                closeBrowser();
            }
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            closeBrowser();
        }

        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            closeBrowser();
        }
    };

    @BeforeEach
    public void setup() throws IOException {
        String env = System.getProperty("env", "stg");
        props = new Properties();
        if (env == null) {
            env = "stg";
        }

        FileInputStream ip = new FileInputStream("src/test/resources/config-" + env.toLowerCase() + ".properties");
        props.load(ip);

        System.out.println(">>> Starting test on: " + env.toUpperCase());

        playwright = Playwright.create();

        String headlessVal = getProperty("headless");
        boolean isHeadless = (headlessVal != null) && Boolean.parseBoolean(headlessVal);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setArgs(java.util.List.of("--disable-blink-features=AutomationControlled"));

        String browserName = getProperty("browser");
        if (browserName == null || browserName.trim().isEmpty()) {
            browserName = "chromium";
        }

        System.out.println(">>> Launching browser: " + browserName);

        switch (browserName.toLowerCase()) {
            case "edge":
                launchOptions.setChannel("msedge");
                browser = playwright.chromium().launch(launchOptions);
                break;
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            case "chromium":
            default:
                browser = playwright.chromium().launch(launchOptions);
                break;
        }

        String deviceName = getProperty("device.name");
        Browser.NewContextOptions options;

        if (deviceName != null && !deviceName.isEmpty()) {
            System.out.println(">>> Emulating Mobile Device: " + deviceName);

            options = DEVICE_MAP.get(deviceName);

            if (options == null) {
                throw new RuntimeException("Device '" + deviceName + "' not defined in TestBase.DEVICE_MAP. Please add it.");
            }
        } else {
            options = new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        }

        context = browser.newContext(options);
        page = context.newPage();
    }

    private void closeBrowser() {
        if (context != null) {
            context.close();
            context = null;
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    public String getProperty(String key) {
        String systemProp = System.getProperty(key);
        return (systemProp != null) ? systemProp : props.getProperty(key);
    }

    public boolean isMobile() {
        String deviceName = getProperty("device.name");
        return deviceName != null && !deviceName.trim().isEmpty();
    }
}
