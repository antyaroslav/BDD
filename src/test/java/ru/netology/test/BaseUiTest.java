package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;

abstract class BaseUiTest {
    private static Process appProcess;
    private static boolean appStartedByTests;

    @BeforeAll
    static void setUpAll() throws Exception {
        Configuration.baseUrl = System.getProperty("selenide.baseUrl", "http://localhost:9999");
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 15000;
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "true"));

        var options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        Configuration.browserCapabilities = options;

        if (!isPortOpen("localhost", 9999)) {
            startApplication();
            appStartedByTests = true;
        }

        waitUntilAvailable(Duration.ofSeconds(60));
    }

    @AfterAll
    static void tearDownAll() {
        if (appStartedByTests && appProcess != null) {
            appProcess.destroy();
        }
    }

    private static void startApplication() throws IOException {
        var jar = new File("artifacts/app-ibank-build-for-testers.jar");
        var logFile = new File("build/app-under-test.log");
        logFile.getParentFile().mkdirs();
        appProcess = new ProcessBuilder("java", "-jar", jar.getAbsolutePath())
                .directory(new File("."))
                .redirectErrorStream(true)
                .redirectOutput(ProcessBuilder.Redirect.appendTo(logFile))
                .start();
    }

    private static void waitUntilAvailable(Duration timeout) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < deadline) {
            if (isPortOpen("localhost", 9999)) {
                return;
            }
            Thread.sleep(500);
        }
        throw new IllegalStateException("Application did not start on port 9999");
    }

    private static boolean isPortOpen(String host, int port) {
        try (var socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 500);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}