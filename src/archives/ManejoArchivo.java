package archives;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Clase que se encarga de manejar archivos dentro de la aplicación.
 *
 * @author Manu
 */
public class ManejoArchivo {

    /**
     * Método para solicitarle al usuario una ruta en el ordenador
     *
     * @return Retorna la ruta que seleccionó el usuario a través de un
     * JFileChooser, en caso de no hacerlo retorna nulo.
     */
    public static String getFileChooserPath() {
        JFileChooser fileChooser = new JFileChooser("Seleccione la ruta");
        fileChooser.setApproveButtonText("Ok");
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile().getPath();
    }

    /**
     * Metodo para leer archivos en forma de texto
     *
     * @param path Se requiere indicar la ruta del archivo que se quiere leer
     * @return Retorna una lista de Strings con el contenido del archivo
     */
    public static List<String> getLinesTextFile(String path) {
        List<String> response = new ArrayList<>();

        if (path != null && !path.isBlank() && path.endsWith(".txt")) {
            Charset utf8 = Charset.forName("UTF-8");
            try (
                     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), utf8));) {
                String line = null;
                while ((line = in.readLine()) != null) {
                    response.add(line);
                }
            } catch (IOException e) {
                System.out.println(e);
                response = new ArrayList<>();
            }
        } else {
            JOptionPane.showMessageDialog(null, "El archivo no existe o no es válido", "Archivo inválido o inexistente", JOptionPane.ERROR_MESSAGE);
        }

        return response;
    }

    /**
     * Método para guardar un archivo de texto. Se debe de asegurar que la
     * carpeta tenga permisos de escritura.
     *
     * @param path Se requiere indicar la ruta en la que se guardará el archivo.
     * @param texto El texto que se incluirá dentro del archivo
     */
    public static void guardarArchivoTxt(String path, String texto) {
        if (path != null && !path.isBlank()) {
            File archivo = new File(path + ".txt");
            try ( PrintWriter buffer = new PrintWriter(new FileWriter(archivo, false))) {

                buffer.print(texto);
                JOptionPane.showMessageDialog(null, "¡Se ha guardado el archivo!", "Guardado", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("El archivo se ha creado con éxito!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "No se ha podido guardar el archivo", "Error al Guardar", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No ha seleccionado una ruta válida", "Ruta inválida o inexistente", JOptionPane.ERROR_MESSAGE);
        }

    }

}
