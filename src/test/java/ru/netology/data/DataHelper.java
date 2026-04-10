package ru.netology.data;

public final class DataHelper {
    private DataHelper() {
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static CardInfo getFirstCard() {
        return new CardInfo("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0001");
    }

    public static CardInfo getSecondCard() {
        return new CardInfo("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0002");
    }

    public static final class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String login() {
            return login;
        }

        public String password() {
            return password;
        }
    }

    public static final class VerificationCode {
        private final String code;

        public VerificationCode(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

    public static final class CardInfo {
        private final String id;
        private final String number;

        public CardInfo(String id, String number) {
            this.id = id;
            this.number = number;
        }

        public String id() {
            return id;
        }

        public String number() {
            return number;
        }
    }
}
