package com.sync.api.application.operation;

public class StringExtensions  {

    private static final String[] UNIDADES = {
            "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove"
    };

    private static final String[] DEZENAS = {
            "", "dez", "vinte", "trinta", "quarenta", "cinquenta",
            "sessenta", "setenta", "oitenta", "noventa"
    };

    private static final String[] ESPECIAIS = {
            "dez", "onze", "doze", "treze", "quatorze", "quinze",
            "dezesseis", "dezessete", "dezoito", "dezenove"
    };

    private static final String[] CENTENAS = {
            "", "cem", "duzentos", "trezentos", "quatrocentos",
            "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"
    };

    private static final String[] GRUPOS = {
            "", "mil", "milhão", "bilhão", "trilhão"
    };

    public static String toExtenso(String numeroStr) {
        try {
            long numero = Long.parseLong(numeroStr);

            if (numero == 0) {
                return "zero";
            }

            String resultado = "";
            int grupoAtual = 0;

            while (numero > 0) {
                int tripla = (int) (numero % 1000);
                if (tripla > 0) {
                    String porExtenso = converteTripla(tripla);
                    resultado = porExtenso + (grupoAtual > 0 ? " " + GRUPOS[grupoAtual] : "") +
                            (resultado.isEmpty() ? "" : " ") + resultado;
                }
                numero /= 1000;
                grupoAtual++;
            }

            return resultado.trim();
        } catch (NumberFormatException e) {
            return "Entrada inválida. Insira apenas números.";
        }
    }

    private static String converteTripla(int numero) {
        int centenas = numero / 100;
        int resto = numero % 100;
        int dezenas = resto / 10;
        int unidades = resto % 10;

        String resultado = "";

        if (centenas > 0) {
            resultado += (centenas == 1 && resto > 0 ? "cento" : CENTENAS[centenas]);
        }

        if (resto > 0) {
            if (!resultado.isEmpty()) {
                resultado += " e ";
            }
            if (resto < 20 && resto > 9) {
                resultado += ESPECIAIS[resto - 10];
            } else {
                resultado += DEZENAS[dezenas];
                if (unidades > 0) {
                    if (!DEZENAS[dezenas].isEmpty()) {
                        resultado += " e ";
                    }
                    resultado += UNIDADES[unidades];
                }
            }
        }

        return resultado.trim();
    }
}
