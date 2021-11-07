package controllers;

import archives.ManejoArchivo;
import java.util.*;
import javax.swing.*;
import models.token.Token;
import services.AutomataFinito;
import services.GeneralAFD;
import user_interface.*;
import user_interface.reports.*;

/**
 * Esta clase se encarga de manejar a la ventana principal del programa.
 * 
 * @author Manu
 */
public class ManejadorVentanaPrincipal {

    private VentanaPrincipal frame;
    private AutomataFinito validador;
    private List<Token> tokensValidos;
    private List<Token> tokensInvalidos;

    /**
     * Contructor del manejador para ventana principal.
     * 
     * @param frame El frame que contiene a la ventana principal
     */
    public ManejadorVentanaPrincipal(VentanaPrincipal frame) {
        this.frame = frame;
        tokensValidos = new ArrayList<>();
        tokensInvalidos = new ArrayList<>();
    }

    /**
     * Método para validar el texto que se encuentra en el editor de texto del
     * frame.
     */

    public void validarTexto() {
        String textEditor = frame.getjTextPaneEditor().getText();
        if (textEditor != null && !textEditor.isBlank()) {
            validador = new GeneralAFD(textEditor.toCharArray());
            tokensValidos = validador.getTokens(true);
            tokensInvalidos = validador.getTokens(false);
            new VentanaValidarTexto(validador.getMensajes());
        } else {
            JOptionPane.showMessageDialog(null,
                    "No hay contenido para validar, escriba algo primero en el area de texto", "Area de texto vacía",
                    JOptionPane.WARNING_MESSAGE);
            frame.getjTextPaneEditor().setText("");
        }

    }

    /**
     * Método para invocar a la ventana para realizar busquedas dentro del area de
     * texto.
     */

    public void mostrarBuscador() {
        JTextPane editorArea = frame.getjTextPaneEditor();
        if (editorArea.getText() != null && !editorArea.getText().isBlank()) {
            new VentanaBuscar(frame.getjTextPaneEditor());
        } else {
            JOptionPane.showMessageDialog(null,
                    "No hay contenido para buscar, escriba algo primero en el area de texto", "Area de texto vacía",
                    JOptionPane.WARNING_MESSAGE);
            frame.getjTextPaneEditor().setText("");
        }

    }

    /**
     * Método para invocar la ventana que contiene los reportes
     * 
     * @param tipo Se debe indicar el tipo de reporte que se quiere ver.
     */

    public void mostrarReportes(TipoReporte tipo) {

        List<Token> listadoTokens = switch (tipo) {
            case TOKENS -> tokensValidos;
            case RECUENTO -> tokensValidos;
            case ERRORES -> tokensInvalidos;
        };
        if (listadoTokens.isEmpty()) {
            String mensaje = switch (tipo) {
                case TOKENS, RECUENTO -> "válidos";
                case ERRORES -> "inválidos";
            };
            JOptionPane.showMessageDialog(null, "No hay tokens " + mensaje + " para mostrar en el reporte",
                    "No hay tokens", JOptionPane.WARNING_MESSAGE);
        } else {
            if ((tipo == TipoReporte.TOKENS || tipo == TipoReporte.RECUENTO) && !tokensInvalidos.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No se puede mostrar el reporte de tokens cuando el texto contiene errores",
                        "No se puede mostrar reporte", JOptionPane.WARNING_MESSAGE);
            } else {
                new VentanaReportes(listadoTokens, tipo);
            }

        }

    }

    /**
     * Método para guardar archivos dentro del programa en formato txt.
     */
    public void guardarArchivo() {
        String texto = frame.getjTextPaneEditor().getText();
        if (texto != null && !texto.isBlank()) {
            ManejoArchivo.guardarArchivoTxt(ManejoArchivo.getFileChooserPath(), texto);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No hay contenido para guardar, escriba algo primero en el area de texto", "Area de texto vacía",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Método para cargar un documento de texto al editor del programa.
     */

    public void cargarArchivo() {
        List<String> lineas = ManejoArchivo.getLinesTextFile(ManejoArchivo.getFileChooserPath());
        JTextPane panelEditor = frame.getjTextPaneEditor();
        panelEditor.setText("");
        if (!lineas.isEmpty()) {
            String salto;
            for (int i = 0; i < lineas.size(); i++) {
                salto = (i + 1 == lineas.size()) ? "" : "\n";
                panelEditor.setText(panelEditor.getText() + lineas.get(i) + salto);
            }
        }
    }

}
