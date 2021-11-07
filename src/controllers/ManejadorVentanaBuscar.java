package controllers;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import user_interface.VentanaBuscar;

/**
 * 
 * Clase que se encarga de manejar la ventana de busqueda de la app.
 *
 * @author Manu
 */
public class ManejadorVentanaBuscar {

    private VentanaBuscar frame;
    private int coincidenciaTotal;
    private int coincidenciaActual;

    /**
     * Constructor del manejador de la ventana buscar
     * 
     * @param frame El frame de la ventana buscar.
     */
    public ManejadorVentanaBuscar(VentanaBuscar frame) {
        this.frame = frame;
        coincidenciaActual = 0;
    }

    /**
     * Método para activar o desactivar los botones que se encargan de iniciar la
     * busqueda.
     */
    public void activarBotones() {
        boolean activar = false;
        if (frame.getTextBuscar().getText() != null && !frame.getTextBuscar().getText().isBlank()) {
            activar = true;
        }
        frame.getBotonSiguiente().setEnabled(activar);
        frame.getBotonAnterior().setEnabled(activar);
    }

    /**
     * Método que se encarga de buscar una palabra dentro del area de texto (Hace un
     * llamado al método del mismo nombre)
     * 
     * @param buscarSiguiente Se le debe indicar si se desea indicar una siguiente
     *                        coincidencia con true, de lo contrario debe de mandar
     *                        false.
     */
    public void buscarPalabra(boolean buscarSiguiente) {
        buscarPalabra(frame.getAreaTexto(), frame.getTextBuscar().getText(), frame.getjCheckBox1().isSelected(),
                buscarSiguiente);
    }

    /**
     * Método que se encarga de buscar una palabra dentro del area de texto.
     * 
     * @param componente      El componente de texto sobre el cual se realizará la
     *                        busqueda.
     * @param cadena          La cadena que se quiere buscar dentro del area de
     *                        texto.
     * @param caseSensitive   Se debe indicar si la busqueda será sensible a las
     *                        mayúsculas con true, de lo contrario se debe indicar
     *                        false.
     * @param buscarSiguiente Se debe indicar si se realizará una busqueda de la
     *                        siguiente coincidencia con true, de lo contrario se
     *                        debe indicar false.
     */

    public void buscarPalabra(JTextComponent componente, String cadena, boolean caseSensitive,
            boolean buscarSiguiente) {
        String textoArea;
        int posicion = componente.getCaretPosition(), textoAreaTamanio = 0;
        int encontrado;
        try {
            textoAreaTamanio = componente.getDocument().getLength();
            textoArea = componente.getDocument().getText(0, textoAreaTamanio);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            textoArea = "";
        }

        if (!caseSensitive) {
            textoArea = textoArea.toLowerCase();
            cadena = cadena.toLowerCase();
        }

        int ultimoResultado = buscarSiguiente ? textoArea.lastIndexOf(cadena) : textoArea.indexOf(cadena);
        boolean condicionBuscar = buscarSiguiente ? posicion >= textoAreaTamanio : false;

        if (condicionBuscar || (posicion - cadena.length()) == ultimoResultado) {
            posicion = buscarSiguiente ? 0 : textoAreaTamanio + 2;
        }
        if (buscarSiguiente) {
            encontrado = textoArea.indexOf(cadena, posicion - 1);
        } else {
            encontrado = textoArea.lastIndexOf(cadena, posicion - cadena.length() - 1);
        }

        if (encontrado > -1) {
            componente.setSelectionStart(encontrado);
            componente.setSelectionEnd(encontrado + cadena.length());
            componente.getCaret().setSelectionVisible(true);
            cambiarLabel(textoArea, cadena, caseSensitive);
        } else {
            JOptionPane.showMessageDialog(null, "No se han encontrado coincidencias", "No encontrado",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.getLabelBusqueda().setText("");
        }
    }

    /**
     * Método indicar a través del label del frame la cantidad de coincidencias
     * encontradas en el texto y la coincidencia actual.
     * 
     * @param textoTotal    El texto sobre el cual se realizará la búsqueda.
     * @param cadena        La cadena que se desea buscar.
     * @param caseSensitive Se debe indicar si el sistema es sensible a las
     *                      mayúsculas con true, de lo contrario se debe indicar
     *                      false.
     */

    public void cambiarLabel(String textoTotal, String cadena, boolean caseSensitive) {
        if (!caseSensitive) {
            cadena = cadena.toLowerCase();
            textoTotal = textoTotal.toLowerCase();
            System.out.println("no es sensible a mayusculas");
        }
        int posicion = 0, contador = 1;
        while (posicion > -1 && (posicion + 1) <= textoTotal.length() && posicion != textoTotal.lastIndexOf(cadena)) {
            posicion = textoTotal.indexOf(cadena, posicion + 1);
            contador++;
        }
        coincidenciaTotal = contador;
        coincidenciaActual++;
        if (coincidenciaActual > coincidenciaTotal) {
            coincidenciaActual = 1;
        }
        frame.getLabelBusqueda().setText("Coincidencia " + coincidenciaActual + " de " + coincidenciaTotal);
    }

}
