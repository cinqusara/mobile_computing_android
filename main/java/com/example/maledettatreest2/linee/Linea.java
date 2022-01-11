package com.example.maledettatreest2.linee;

public class Linea {
    private Tratta tratta1;
    private Tratta tratta2;

    public Linea(Tratta tratta1, Tratta tratta2) {
        this.tratta1 = tratta1;
        this.tratta2 = tratta2;
    }

    public Tratta getTratta1() {
        return tratta1;
    }

    public void setTratta1(Tratta tratta1) {
        this.tratta1 = tratta1;
    }

    public Tratta getTratta2() {
        return tratta2;
    }

    public void setTratta2(Tratta tratta2) {
        this.tratta2 = tratta2;
    }

    public String getNomeLinea(){
        return tratta1.getNome() + " - " + tratta2.getNome();
    }

    @Override
    public String toString() {
        return "Linea{" +
                "tratta1=" + tratta1 +
                ", tratta2=" + tratta2 +
                '}';
    }
}
