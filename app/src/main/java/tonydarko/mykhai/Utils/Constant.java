package tonydarko.mykhai.Utils;


import java.util.Map;

public class Constant {

    public static String token;
    public static  Map<String, String> common;

    public static Map<String, String> getCommon() {
        return common;
    }

    public static void setCommon(Map<String, String> common) {
        System.out.println("Common" + common);
        Constant.common = common;
    }

    final static String url = "http://my.khai.edu/my/login";
    static String MyLogin = "martishkov_a";
    static String MyPassword = "ant641448";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";

    public static String getUrl() {
        return url;
    }

    public static void setMyLogin(String myLogin) {
        MyLogin = myLogin;
    }

    public static void setMyPassword(String myPassword) {
        MyPassword = myPassword;
    }

    public static String getMyLogin() {
        return MyLogin;
    }

    public static String getMyPassword() {
        return MyPassword;
    }

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Constant.token = token;
    }
}
