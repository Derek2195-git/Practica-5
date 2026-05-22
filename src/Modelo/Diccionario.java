package Modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Diccionario {
    private HashMap<String, Integer> diccionarioPalabras;
    private boolean diccionarioIngles;
    private String rutaArchivo;

    /**
     * Constructor del diccionario
     * @param diccionarioIngles Boolean que indica si vamos a usar el diccionario en ingles
     */
    public Diccionario(boolean diccionarioIngles) {
        this.diccionarioIngles = diccionarioIngles;
        diccionarioPalabras = new HashMap<>();
        rutaArchivo = diccionarioIngles
                ? "src/recursos/palabras_ingles.txt"
                : "src/recursos/palabras_espanol.txt";

        cargarDiccionario(rutaArchivo);

    }

    /**
     * Metodo que carga el archivo de texto del diccionario y lo añade al hashMap de diccionario
     * @param rutaArchivo Ruta del archivo a usar
     */
    public void cargarDiccionario(String rutaArchivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String lineaLimpia = linea.trim().toLowerCase();
                lineaLimpia = quitarTildes(lineaLimpia);
                String palabra = lineaLimpia.contains(":")
                        ? lineaLimpia.split(":")[0].trim()
                        : lineaLimpia;

                if (!palabra.isEmpty()) {
                    agregarPalabra(palabra);
                }
            }
        } catch (IOException e) {
            System.out.println("Error de entrada y salida: " + e.getMessage());
        }
    }

    /**
     * Metodo que nos retorna una palabra aleatoria del diccionario
     * @param longitud Tamaño de las palabras a seleccionar
     * @return Palabra aleatoria con la longitud deseada
     */
    public String getPalabraAleatoria(int longitud) {
        ArrayList<String> palabras = new ArrayList<>(diccionarioPalabras.keySet());
        ArrayList<String> palabrasFiltradas = palabras.stream()
                .filter(n -> n.length() == longitud)
                .collect(Collectors.toCollection(ArrayList::new));
        Random rnd = new Random();

        return palabrasFiltradas.get(rnd.nextInt(palabrasFiltradas.size()));
    }

    /**
     * Metodo que comprueba si una palabra es valida
     * @param palabra Palabra a comprobar
     * @return Booleano que indica si la palabra es valida o no
     */
    public boolean esUnaPalabraValida(String palabra) {
        return diccionarioPalabras.containsKey(palabra.toLowerCase());
    }

    /**
     * Metodo que agrega una palabra al HashMap de diccionario
     * @param palabra
     */
    public void agregarPalabra(String palabra) {
            diccionarioPalabras.put(palabra, palabra.length());
        }

    /**
     * Metodo que agrega una palabra al diccionario
     * @param palabra Palabra a agregar
     * @param definicion Definicion de la palabra
     */
    public void agregarPalabraArchivo(String palabra, String definicion) {
        // Convertimos la palabra dada como parametro a minusculas
        String palabraAgregada = palabra.toLowerCase();
        // Creamos otro hashMap con las palabras unicas y agregamos la palabra a nuestro diccionario principal
        HashSet<String> palabrasUnicas = new HashSet<>();
        agregarPalabra(palabra);

        // Si la ruta del archivo es correcta
        if (rutaArchivo != null) {
            // Obtenemos un ArrayList con todas las lineas del diccionario
            ArrayList<String> lineasArchivo = new ArrayList<>();
            try (
                    BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo, StandardCharsets.UTF_8))
                ){
                // Definimos una linea y mientras la linea leida no sea un valor nulo
                String linea;
                while ((linea = lector.readLine()) != null) {
                    // Arreglamos la linea linea volviendola en minuscula y recortando los espacios antes y despues de la palabra
                    String lineaArreglada = linea.toLowerCase().trim();
                    // Si la linea arreglada no esta vacia
                    if (!lineaArreglada.isEmpty()) {
                        // Creamos otra cadena la cual esta pensada para extraer el contenido de la linea, sin la definición de este
                        String palabraBase = lineaArreglada.contains(":")
                                ? lineaArreglada.split(":")[0].trim()
                                : lineaArreglada;
                        // Si el HashSet creado al principio no contiene esta palabra, la añadimos a este
                        if (!palabrasUnicas.contains(palabraBase)) {
                            palabrasUnicas.add(palabraBase);
                            // asimimo, añadimos al arraylist la linea ya arreglada
                            lineasArchivo.add(lineaArreglada);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error de entrada y salida: " + e.getMessage());
            }

            // Si el hashSet no contiene la palabra que vamos a agregar, lo añadimos

            if (!palabrasUnicas.contains(palabraAgregada))
                lineasArchivo.add(palabraAgregada);
            // Posteriormente, procedemos a ordenar inmediatamente el archivo de texto para que quede bonito
            lineasArchivo.sort((linea1, linea2) -> {
                String p1 = linea1.contains(":") ? linea1.split(":")[0].trim() : linea1;
                String p2 = linea2.contains(":") ? linea2.split(":")[0].trim() : linea2;
                return p1.compareTo(p2);
            });

            try (
                    BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, StandardCharsets.UTF_8))
                    ){
                for (int i = 0; i < lineasArchivo.size(); i++) {
                    // Si en el arraylist de lineas, el indice actual equivale a la palabra que vamos a agregar,
                    // vamos a escribir la nueva palabra en el archivo de texto
                    if (lineasArchivo.get(i).equalsIgnoreCase(palabraAgregada)) {
                        escritor.write(palabraAgregada + ":" + definicion);
                    } else {
                        // En todos los demas casos, volvemos a escribir las lineas correspondientes
                        escritor.write(lineasArchivo.get(i));
                    }
                    // Si estamos en por acabar de escribir el diccionario, escribimos una nueva linea para la posterioridad
                    if (i < lineasArchivo.size() - 1) {
                        escritor.newLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error de entrada y salida: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo que retorna un ArrayList de palabras de cierto tamaño ordenados
     * @param longitudPalabra Tamaño que se filtrará para obtener palabras de cierto tamaño
     * @return ArrayList de palabras ordenadas
     */
    public ArrayList<String> obtenerPalabrasOrdenadas(int longitudPalabra) {
        return diccionarioPalabras.entrySet().stream()
                .filter(n -> n.getValue() == longitudPalabra)
                .map(HashMap.Entry::getKey)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Metodo para quitar las tildes de una cadena
     * @param texto Palabra la cual debería contener tildes
     * @return Nueva cadena sin tildes
     */
    public String quitarTildes(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);

        return texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
