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
    public static final int GUION_BAJO = 2;
    /**
     * Valor que tiene un signo de puntuación dentro de la matriz de transiciones
     * del automata.
     */

    public static final int GUION_MEDIO = 3;
    public static final int COMILLAS = 4;
    public static final int DIAGONAL = 5;
    public static final int SIG_PUNTUACION = 6;
    /**
     * Valor que tiene un signo de operación dentro de la matriz de transiciones del
     * automata.
     */
    public static final int SIG_OPERACION = 7;
    /**
     * Valor que tiene un signo de agrupación dentro de la matriz de transiciones
     * del automata.
     */
    public static final int SIG_AGRUPACION = 8;

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
        return new int[][] { { 1, 2, 1, 10, 3, 6, 9, 10, 11 }, { 1, 1, 1, 1, ERROR, ERROR, ERROR, ERROR, ERROR },
                { ERROR, 2, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }, { 4, 4, 4, 4, 4, 4, 4, 4, 4 },
                { 4, 4, 4, 4, 5, 4, 4, 4, 4 }, { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                { ERROR, ERROR, ERROR, ERROR, ERROR, 7, ERROR, ERROR, ERROR }, { 8, 8, 8, 8, 8, 8, 8, 8, 8 },
                { 8, 8, 8, 8, 8, 8, 8, 8, 8 }, { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }, };
    }

    @Override
    public int[] getEstadosAceptacion() {
        return new int[] { 1, 2, 5, 8, 9, 10, 11 };
    }

    @Override
    public int getCaracterInt(char c) {
        if (ValidadorChar.isLetter(c)) {
            return LETRA;
        }
        if (Character.isDigit(c)) {
            return DIGITO;
        }
        if (c == '_') {
            return GUION_BAJO;
        }
        if (c == '-') {
            return GUION_MEDIO;
        }
        if (c == '"') {
            return COMILLAS;
        }
        if (c == '/') {
            return DIAGONAL;
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
        if (c != '\n' && Character.isWhitespace(c)) {
            return DIGITO;
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
        boolean stop = false;

        while (posicion < texto.length && estadoActual != ERROR && estadoActual < 9 && !stop) {
            tmp = texto[posicion];
            int estadoTemporal = getEstadoSiguiente(estadoActual, tmp);
            if (Character.isWhitespace(tmp) && (estadoTemporal != 8 || estadoActual != 4)) {
                stop = true;
            } else {
                textToken += tmp;
                estadoActual = estadoTemporal;
                posicion++;
                columna++;
            }

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
            tipoToken = TipoToken.LITERAL;
        } else if (estadoActual == estadosAceptacion[3]) {
            tipoToken = TipoToken.COMENTARIO;
        } else if (estadoActual == estadosAceptacion[4]) {
            tipoToken = TipoToken.SIG_PUNTUACION;
        } else if (estadoActual == estadosAceptacion[5]) {
            tipoToken = TipoToken.SIG_OPERADOR;
        } else if (estadoActual == estadosAceptacion[6]) {
            tipoToken = TipoToken.SIG_AGRUPACION;
        } else {
            tipoToken = TipoToken.ERROR;
        }
        mensajes.add("Token " + tipoToken + " generado\n");
        return new Token(textoToken, tipoToken, fila, columna);
    }

}
