package tonydarko.mykhai.Utils;


import java.util.Map;

public class Constant {

    private static String token;
    private static Map<String, String> common;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    private final static String url = "http://my.khai.edu/my/login";
    private static String MyLogin = "";
    private static String MyPassword = "";
    private static String info = "";
    private static String danger = "";
    private static Boolean noOrYes;

    public static Boolean getNotAuthWithoutInternet() {
        return notAuthWithoutInternet;
    }

    public static void setNotAuthWithoutInternet(Boolean notAuthWithoutInternet) {
        Constant.notAuthWithoutInternet = notAuthWithoutInternet;
    }

    private static Boolean notAuthWithoutInternet;

    public static Boolean getNoOrYes() {
        return noOrYes;
    }

    public static void setNoOrYes(Boolean noOrYes) {
        Constant.noOrYes = noOrYes;
    }

    public static String getInfo() {
        return info;
    }

    public static void setInfo(String info) {
        Constant.info = info;
    }

    public static String getDanger() {
        return danger;
    }

    public static void setDanger(String danger) {
        Constant.danger = danger;
    }

    public static Map<String, String> getCommon() {
        return common;
    }

    public static void setCommon(Map<String, String> common) {
        System.out.println("Common" + common);
        Constant.common = common;
    }


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
