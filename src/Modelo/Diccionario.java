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

    public Diccionario(boolean diccionarioIngles) {
        this.diccionarioIngles = diccionarioIngles;
        diccionarioPalabras = new HashMap<>();
        rutaArchivo = diccionarioIngles
                ? "src/recursos/palabras_english.txt"
                : "src/recursos/palabras_espanol.txt";

        cargarDiccionario(rutaArchivo);
    }

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

    public String getPalabraAleatoria(int longitud) {
        ArrayList<String> palabras = new ArrayList<>(diccionarioPalabras.keySet());
        ArrayList<String> palabrasFiltradas = palabras.stream()
                .filter(n -> n.length() == longitud)
                .collect(Collectors.toCollection(ArrayList::new));
        Random rnd = new Random();

        return palabrasFiltradas.get(rnd.nextInt(palabrasFiltradas.size()));
    }

    public boolean esUnaPalabraValida(String palabra) {
        return diccionarioPalabras.containsKey(palabra.toLowerCase());
    }

    public void agregarPalabra(String palabra) {
        diccionarioPalabras.put(palabra, palabra.length());
    }

    public void agregarPalabraArchivo(String palabra, String definicion) {
        String palabraAgregada = palabra.toLowerCase();
        HashSet<String> palabrasUnicas = new HashSet<>();
        agregarPalabra(palabra);

        if (rutaArchivo != null) {
            ArrayList<String> lineasArchivo = new ArrayList<>();
            try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = lector.readLine()) != null) {
                    String lineaArreglada = linea.toLowerCase().trim();
                    if (!lineaArreglada.isEmpty()) {
                        String palabraBase = lineaArreglada.contains(":")
                                ? lineaArreglada.split(":")[0].trim()
                                : lineaArreglada;
                        if (!palabrasUnicas.contains(palabraBase)) {
                            palabrasUnicas.add(palabraBase);
                            lineasArchivo.add(lineaArreglada);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error de entrada y salida: " + e.getMessage());
            }

            if (!palabrasUnicas.contains(palabraAgregada)) {
                lineasArchivo.add(palabraAgregada);
            }

            lineasArchivo.sort((linea1, linea2) -> {
                String p1 = linea1.contains(":") ? linea1.split(":")[0].trim() : linea1;
                String p2 = linea2.contains(":") ? linea2.split(":")[0].trim() : linea2;
                return p1.compareTo(p2);
            });

            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, StandardCharsets.UTF_8))) {
                for (int i = 0; i < lineasArchivo.size(); i++) {
                    if (lineasArchivo.get(i).equalsIgnoreCase(palabraAgregada)) {
                        escritor.write(palabraAgregada + ":" + definicion);
                    } else {
                        escritor.write(lineasArchivo.get(i));
                    }
                    if (i < lineasArchivo.size() - 1) {
                        escritor.newLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error de entrada y salida: " + e.getMessage());
            }
        }
    }

    public ArrayList<String> obtenerPalabrasOrdenadas(int longitudPalabra) {
        return diccionarioPalabras.entrySet().stream()
                .filter(n -> n.getValue() == longitudPalabra)
                .map(HashMap.Entry::getKey)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String quitarTildes(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}