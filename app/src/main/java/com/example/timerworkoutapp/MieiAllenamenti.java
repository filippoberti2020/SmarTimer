package com.example.timerworkoutapp;

import android.content.Context;

import java.util.ArrayList;

public class MieiAllenamenti {
String titleAllenamento,dateAllenamento,descAllenamento;

    public MieiAllenamenti() {
    }

    public MieiAllenamenti(String titleAllenamento, String dateAllenamento, String descAllenamento) {
        this.titleAllenamento = titleAllenamento;
        this.dateAllenamento = dateAllenamento;
        this.descAllenamento = descAllenamento;
    }

    public String getTitleAllenamento() {
        return titleAllenamento;
    }

    public void setTitleAllenamento(String titleAllenamento) {
        this.titleAllenamento = titleAllenamento;
    }

    public String getDateAllenamento() {
        return dateAllenamento;
    }

    public void setDateAllenamento(String dateAllenamento) {
        this.dateAllenamento = dateAllenamento;
    }

    public String getDescAllenamento() {
        return descAllenamento;
    }

    public void setDescAllenamento(String descAllenamento) {
        this.descAllenamento = descAllenamento;
    }
}
