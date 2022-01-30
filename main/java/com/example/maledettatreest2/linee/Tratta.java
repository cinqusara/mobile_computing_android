package com.example.maledettatreest2.linee;

public class Tratta {
    private String nome;
    private String did;

    public Tratta(String nome, String did) {
        this.nome = nome;
        this.did = did;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    @Override
    public String toString() {
        return "Tratta{" +
                "nome='" + nome + '\'' +
                ", did='" + did + '\'' +
                '}';
    }
}
