package ru.netology.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ApiAuthHelper {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"");

    private ApiAuthHelper() {
    }

    public static String loginAndGetToken(String baseUrl, DataHelper.AuthInfo authInfo, DataHelper.VerificationCode verificationCode) {
        var client = HttpClient.newHttpClient();

        post(client, baseUrl + "/api/auth", String.format(
                "{\"login\":\"%s\",\"password\":\"%s\"}",
                authInfo.login(), authInfo.password()
        ));

        var response = post(client, baseUrl + "/api/auth/verification", String.format(
                "{\"login\":\"%s\",\"code\":\"%s\"}",
                authInfo.login(), verificationCode.code()
        ));

        var matcher = TOKEN_PATTERN.matcher(response.body());
        if (!matcher.find()) {
            throw new IllegalStateException("Token not found in response: " + response.body());
        }
        return matcher.group(1);
    }

    private static HttpResponse<String> post(HttpClient client, String url, String body) {
        var request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Unexpected status " + response.statusCode() + " for " + url + ": " + response.body());
            }
            return response;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request failed for " + url, e);
        }
    }
}