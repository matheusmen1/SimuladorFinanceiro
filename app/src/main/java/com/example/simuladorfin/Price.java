package com.example.simuladorfin;

public class Price {
    public static double calcParcela(double valor, double juros, int prazo){
        return valor*(juros/100/(1-Math.pow(1+juros/100,prazo*-1)));
    }
}
