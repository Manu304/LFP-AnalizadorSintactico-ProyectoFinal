package services;

import java.util.*;

import models.token.TipoToken;
import models.token.Token;
import util.ValidadorChar;

/**
 * 
 * Clase que se encarga de validar tokens dentro de un texto dado.
 * 
 * @author Manu
 */

public class GeneralAFD implements AutomataFinito {

    private List<Token> tokens;
    private List<String> mensajes;
    private int posicion, estadoActual, fila, columna;
    private char[] textoTotal;
    private int[][] matrizTransicion;
    private int[] estadosAceptacion;
    /**
     * Valor que tiene la letra dentro de la matriz de transiciones del automata.
     */
    public static final int LETRA = 0;
    /**
     * Valor que tiene un dígito dentro de la matriz de transiciones del automata.
     */
    public static final int DIGITO = 1;
    /**
     * Valor que tiene un punto dentro de la matriz de transiciones del automata.
     */
    public static final int PUNTO = 2;
    /**
     * Valor que tiene un signo de puntuación dentro de la matriz de transiciones
     * del automata.
     */
    public static final int SIG_PUNTUACION = 3;
    /**
     * Valor que tiene un signo de operación dentro de la matriz de transiciones del
     * automata.
     */
    public static final int SIG_OPERACION = 4;
    /**
     * Valor que tiene un signo de agrupación dentro de la matriz de transiciones
     * del automata.
     */
    public static final int SIG_AGRUPACION = 5;

    /**
     * Constructor del autómata finito para la aplicación.
     * 
     * @param texto
     */

    public GeneralAFD(char[] texto) {
        this.textoTotal = texto;
        iniciar();
    }

    @Override
    public void iniciar() {
        matrizTransicion = getMatrizTransiciones();
        estadosAceptacion = getEstadosAceptacion();
        posicion = 0;
        fila = 1;
        columna = 1;
        tokens = new ArrayList<>();
        mensajes = new ArrayList<>();
        while (posicion < textoTotal.length) {
            if (!Character.isWhitespace(textoTotal[posicion])) {
                tokens.add(getToken(textoTotal));
            } else {
                if (textoTotal[posicion] == '\n') {
                    fila++;
                    columna = 1;
                } else {
                    columna++;
                }
                posicion++;
            }
        }
        mensajes.add("\nFINALIZADO: El texto ha sido procesado con éxito");
    }

    @Override
    public int[][] getMatrizTransiciones() {
        return new int[][] { { 1, 2, 5, 5, 6, 7 }, { 1, 1, ERROR, ERROR, ERROR, ERROR },
                { ERROR, 2, 3, ERROR, ERROR, ERROR }, { ERROR, 4, ERROR, ERROR, ERROR, ERROR },
                { ERROR, 4, ERROR, ERROR, ERROR, ERROR }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 } };
    }

    @Override
    public int[] getEstadosAceptacion() {
        return new int[] { 1, 2, 4, 5, 6, 7 };
    }

    @Override
    public int getCaracterInt(char c) {
        if (ValidadorChar.isLetter(c)) {
            return LETRA;
        }
        if (Character.isDigit(c)) {
            return DIGITO;
        }
        if (c == '.') {
            return PUNTO;
        }
        if (ValidadorChar.isPuntuacionSymbol(c)) {
            return SIG_PUNTUACION;
        }
        if (ValidadorChar.isOperatorSymbol(c)) {
            return SIG_OPERACION;
        }
        if (ValidadorChar.isAgrupationSymbol(c)) {
            return SIG_AGRUPACION;
        }
        return ERROR;
    }

    @Override
    public int getEstadoSiguiente(int estadoActual, char c) {
        int siguiente = ERROR;
        int caracterInt = getCaracterInt(c);
        String mensajeTransicion = "ERROR en la fila " + fila + " y columna " + columna + " a causa de '" + c + "'";
        if (caracterInt != ERROR) {
            siguiente = matrizTransicion[estadoActual][caracterInt];
            if (siguiente != ERROR) {
                mensajeTransicion = "Pase del estado s" + estadoActual + " al estado s" + siguiente + " con '" + c
                        + "'";
            }
        }
        mensajes.add(mensajeTransicion);
        return siguiente;
    }

    @Override
    public Token getToken(char[] texto) {
        estadoActual = 0;
        String textToken = "";
        char tmp;

        while (posicion < texto.length && !Character.isWhitespace(tmp = texto[posicion]) && estadoActual != ERROR
                && estadoActual < 5) {
            int estadoTemporal = getEstadoSiguiente(estadoActual, tmp);
            textToken += tmp;
            estadoActual = estadoTemporal;
            posicion++;
            columna++;

        }
        return crearToken(textToken);
    }

    @Override
    public List<Token> getTokens(boolean isValidToken) {
        List<Token> validos = new ArrayList<>();
        for (Token e : tokens) {
            boolean condicion = e.getTipo() != TipoToken.ERROR;
            if (isValidToken ? condicion : !condicion) {
                validos.add(e);
            }
        }
        return validos;
    }

    @Override
    public List<String> getMensajes() {
        return mensajes;
    }

    private Token crearToken(String textoToken) {
        TipoToken tipoToken;
        if (estadoActual == estadosAceptacion[0]) {
            tipoToken = TipoToken.IDENTIFICADOR;
        } else if (estadoActual == estadosAceptacion[1]) {
            tipoToken = TipoToken.NUMERO_ENTERO;
        } else if (estadoActual == estadosAceptacion[2]) {
            tipoToken = TipoToken.NUMERO_DECIMAL;
        } else if (estadoActual == estadosAceptacion[3]) {
            tipoToken = TipoToken.SIG_PUNTUACION;
        } else if (estadoActual == estadosAceptacion[4]) {
            tipoToken = TipoToken.SIG_OPERADOR;
        } else if (estadoActual == estadosAceptacion[5]) {
            tipoToken = TipoToken.SIG_AGRUPACION;
        } else {
            tipoToken = TipoToken.ERROR;
        }
        mensajes.add("Token " + tipoToken + " generado\n");
        return new Token(textoToken, tipoToken, fila, columna);
    }

}
