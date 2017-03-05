package tonydarko.mykhai.Utils;

import java.util.ArrayList;

import tonydarko.mykhai.Items.ExtraBallItem;

public class Cache {
    public static ArrayList<ExtraBallItem> data = new ArrayList<>();
    public static String[][] newTableFinal;

    public static ArrayList<ExtraBallItem> getData() {
        return data;
    }

    public static void setData(ArrayList<ExtraBallItem> data) {
        System.out.println(data);
        Cache.data = data;
    }

    public static String[][] getNewTableFinal() {
        return newTableFinal;
    }

    public static void setNewTableFinal(String[][] newTableFinal) {
        System.out.println(newTableFinal);
        Cache.newTableFinal = newTableFinal;
    }
}
