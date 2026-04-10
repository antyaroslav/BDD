package ru.netology.page;

public abstract class BasePage {
    protected String byTestId(String value) {
        return String.format("[data-test-id='%s']", value);
    }

    protected String byTestIdAndTag(String value, String tag) {
        return byTestId(value) + " " + tag;
    }
}
