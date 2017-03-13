package tonydarko.mykhai.items;

public class BallStudentItem {
    //[1, Авіоніка, зал, 100, A, Зал., 2016/2017, 1],
    // [2, Пристрої формування та генерування сигналів в засобах зв'язку, 3, 70, D, Екз, 2016/2017, 1],

    private String subject;
    private String otcenka;
    private String ball;
    private String ESTC;

    public BallStudentItem(String subject, String otcenka, String ball, String ESTC, String formContrl) {
        this.subject = subject;
        this.otcenka = otcenka;
        this.ball = ball;
        this.ESTC = ESTC;
        this.formContrl = formContrl;
    }

    private String formContrl;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOtcenka() {
        return otcenka;
    }

    public void setOtcenka(String otcenka) {
        this.otcenka = otcenka;
    }

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }

    public String getESTC() {
        return ESTC;
    }

    public void setESTC(String ESTC) {
        this.ESTC = ESTC;
    }

    public String getFormContrl() {
        return formContrl;
    }

    public void setFormContrl(String formContrl) {
        this.formContrl = formContrl;
    }
}
