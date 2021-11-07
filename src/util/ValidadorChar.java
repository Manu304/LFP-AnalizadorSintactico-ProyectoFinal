package util;

/**
 * Clase que contiene métodos para la validación de chars.
 * 
 * @author Manu
 */
public class ValidadorChar {

    /**
     * Conjunto de simbolos operacionales válidos para el autómata.
     */
    public static final char[] OPERATOR_SYMBOLS = { '+', '-', '/', '*', '%' };
    /**
     * Conjunto de simbolos de agrupación válidos para el autómata.
     */
    public static final char[] AGRUPATION_SYMBOLS = { '(', ')', '[', ']', '{', '}' };
    /**
     * Conjunto de simbolos de puntuación válidos para el autómata.
     */
    public static final char[] PUNTUACION_SYMBOLS = { '.', ',', ':', ';' };

    /**
     * Método que determina si un caracter es una letra.
     * 
     * @param c Caracter a evaluar
     * @return Retorna true si es el caracter es una letra del alfabeto (sin
     *         importar mayúsculas o minúsculas), de lo contrario retorna false.
     */
    public static boolean isLetter(char c) {
        c = Character.toLowerCase(c);
        if (c >= 'a' && c <= 'z') {
            return true;
        }
        return false;
    }

    /**
     * Método que determina si un caracter es un simbolo operador.
     * 
     * @param c Caracter a evaluar
     * @return Retorna true si es un simbolo operador, de lo contrario retorna
     *         false.
     */

    public static boolean isOperatorSymbol(char c) {
        for (int i = 0; i < OPERATOR_SYMBOLS.length; i++) {
            if (c == OPERATOR_SYMBOLS[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que determina si un caracter es un simbolo de agrupación.
     * 
     * @param c Caracter a evaluar
     * @return Retorna true si es un simbolo de agrupación, de lo contrario retorna
     *         false.
     */

    public static boolean isAgrupationSymbol(char c) {
        for (int i = 0; i < AGRUPATION_SYMBOLS.length; i++) {
            if (c == AGRUPATION_SYMBOLS[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que determina si un caracter es un simbolo de puntuación.
     * 
     * @param c Caracter a evaluar
     * @return Retorna true si es un simbolo de puntuación, de lo contrario retorna
     *         false.
     */

    public static boolean isPuntuacionSymbol(char c) {
        for (int i = 0; i < PUNTUACION_SYMBOLS.length; i++) {
            if (c == PUNTUACION_SYMBOLS[i]) {
                return true;
            }
        }
        return false;
    }
}
