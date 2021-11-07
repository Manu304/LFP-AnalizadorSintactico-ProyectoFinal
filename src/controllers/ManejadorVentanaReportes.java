package controllers;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.token.TipoToken;
import models.token.Token;
import user_interface.reports.TipoReporte;
import user_interface.reports.VentanaReportes;

/**
 * 
 * Esta clase se encarga de manejar la ventana de reportes del programa.
 *
 * @author Manu
 */
public class ManejadorVentanaReportes {

    private VentanaReportes frame;
    private List<Token> tokens;
    private boolean isValidToken;
    private TipoReporte tipo;

    /**
     * Constructor del manejador para ventanas de reportes
     * 
     * @param frame Se debe indicar el frame que contiene la ventana de reportes.
     */
    public ManejadorVentanaReportes(VentanaReportes frame) {
        this.frame = frame;
        this.tokens = frame.getTokens();
        this.isValidToken = tokens.get(0).getTipo() != TipoToken.ERROR;
        this.tipo = frame.getTipoReporte();
    }

    /**
     * Método para cambiar el label que contiene el titulo del reporte según el tipo
     * de reporte que se solicita.
     */

    public void setTituloLabel() {
        String text = switch (tipo) {
            case TOKENS -> "Reporte de Tokens";
            case RECUENTO -> "Recuento de Tokens";
            case ERRORES -> "Reporte de Errores";
        };
        frame.getLabelTitulo().setText(text);
    }

    /**
     * Método para actualizar la tabla con los contenidos recien validados por el
     * programa.
     */
    public void actualizarTabla() {
        if (tipo == TipoReporte.RECUENTO) {
            tokens = getRecuentoTokens();
        }
        frame.getTablaReports().setModel(new DefaultTableModel(getDatosTabla(), getColumnNames()) {
            boolean[] canEdit = new boolean[] { false, false, false, false, false };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }

    /**
     * Método para obtener los datos de las filas de la tabla de los reportes
     * 
     * @return La matriz con los datos que llenaran la tabla.
     */
    private Object[][] getDatosTabla() {
        int columnas = getCantColumns();
        int filas = tokens.size();
        Object[][] datos = new Object[filas][columnas];
        for (int i = 0; i < filas; i++) {
            switch (tipo) {
                case TOKENS -> datos[i] = new Object[] { tokens.get(i).getFila(), tokens.get(i).getColumna(),
                        tokens.get(i).getCadena(), tokens.get(i).getTipo() };
                case ERRORES -> datos[i] = new Object[] { tokens.get(i).getFila(), tokens.get(i).getColumna(),
                        tokens.get(i).getCadena() };
                case RECUENTO -> datos[i] = new Object[] { tokens.get(i).getCadena(), tokens.get(i).getTipo(),
                        tokens.get(i).getCantidad() };
            }
        }
        return datos;
    }

    /**
     * Método para darle nombre a las columnas que contendrá la tabla.
     * 
     * @return Un arreglo con los nombres de las columnas de la tabla.
     */

    private String[] getColumnNames() {
        String nameFila = "Fila", nameColum = "Columna", nameTipo = "Tipo de Token", nameCantidad = "Cantidad";
        String nameLexema = isValidToken ? "Lexema" : "Cadena de error";
        if (tipo == TipoReporte.TOKENS) {
            return new String[] { nameFila, nameColum, nameLexema, nameTipo };
        }
        if (tipo == TipoReporte.ERRORES) {
            return new String[] { nameFila, nameColum, nameLexema };
        }
        return new String[] { nameLexema, nameTipo, nameCantidad };
    }

    /**
     * Método que se encarga de decidir la cantidad de columnas que tendrá la tabla
     * que mostrará los reportes dependiendo del tipo de reporte que se solicita.
     * 
     * @return
     */

    private int getCantColumns() {
        if (tipo == TipoReporte.TOKENS) {
            return 4;
        }
        return 3;
    }

    /**
     * Método para hacer el recuento de tokens generados por la aplicación.
     * 
     * @return Retorna una lista con los tokens validados y la cantidad que aparecen
     *         dentro del area de texto.
     */

    private List<Token> getRecuentoTokens() {
        List<Token> recuento = new ArrayList<>();
        for (Token e : tokens) {
            if (recuento.contains(e)) {
                recuento.get(recuento.indexOf(e)).setCantidad();
            } else {
                recuento.add(e);
            }
        }
        return recuento;
    }

}
