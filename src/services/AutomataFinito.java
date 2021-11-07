package services;

import java.util.List;
import models.token.Token;

/**
 * Interfaz para la creación de un autómata finito
 *
 * @author Manu
 */
public interface AutomataFinito {

    /**
     * Estado de aceptación de error del autómata.
     */
    int ERROR = -1;

    /**
     * Método con el que se le da inicio al automata.
     */
    void iniciar();

    /**
     * Metodo para obtener la matriz de transiciones del automata.
     *
     * @return Matriz que contiene los diferentes estados del automata en valor
     * númerico.
     */
    int[][] getMatrizTransiciones();

    /**
     * Método para obtener los estados de aceptacion del automata.
     *
     * @return Arreglo que incluye los estados de aceptación del automata.
     */
    int[] getEstadosAceptacion();

    /**
     * Método para obtener el valor númerico que representa el caracter dentro
     * del alfabeto
     *
     * @param c El caracter del que se quiere saber su valor dentro del
     * automata.
     * @return El valor numérico que posee el caracter dentro del automata.
     */
    int getCaracterInt(char c);

    /**
     * Método para moverse dentro de la matriz de transiciones del autómata
     *
     * @param estadoActual El estado desde el cual se desea mover.
     * @param c El caracter con el que se desea mover de estado.
     * @return El valor del estado al que se puede mover el automata según el
     * caracter evaluado.
     */
    int getEstadoSiguiente(int estadoActual, char c);

    /**
     * Método para generar un tóken.
     *
     * @param texto El texto sobre el cual se desea generar el token.
     * @return Retorna un Token de acuerdo al texto evaluado.
     */
    Token getToken(char[] texto);

    /**
     * Método para obtener los tokens generados por el autómata.
     *
     * @param isValidToken Se debe indicar si se desean los tokens válidos
     * @return Si es true retorna los tokens válidos generados, de lo contrario
     * retorna los tokens inválidos.
     */
    List<Token> getTokens(boolean isValidToken);

    /**
     * Método para obtener los mensajes generados por el autómata durante el
     * proceso de validacion de tokens.
     *
     * @return La lista de mensajes generados por el autómata.
     */
    List<String> getMensajes();
}
