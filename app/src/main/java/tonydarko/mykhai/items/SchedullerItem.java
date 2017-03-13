package tonydarko.mykhai.items;

public class SchedullerItem {

    private String fio;
    private String group;
    private String para;
    private String fioPrepod;
    private String type;
    private String date;

    public SchedullerItem(String fio, String group, String para, String type, String date,String fioPrepod) {
        this.fio = fio;
        this.group = group;
        this.para = para;
        this.type = type;
        this.date = date;
        this.fioPrepod = fioPrepod;

    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFioPrepod() {
        return fioPrepod;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public void setFioPrepod(String fioPrepod) {
        this.fioPrepod = fioPrepod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}