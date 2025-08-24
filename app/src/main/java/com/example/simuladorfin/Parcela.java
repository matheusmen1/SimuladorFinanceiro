package com.example.simuladorfin;

public class Parcela {
    private int num;
    private double valor;
    private double juros;
    private double amort;
    private double saldo;

    public Parcela(int num, double valor, double juros, double amort, double saldo) {
        this.num = num;
        this.valor = valor;
        this.juros = juros;
        this.amort = amort;
        this.saldo = saldo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public double getAmort() {
        return amort;
    }

    public void setAmort(double amort) {
        this.amort = amort;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return
       String.format(" %2d | %10.2f | %10.2f | %10.2f | %10.2f |",
               num,valor,juros,amort,saldo);
    }
}
