package Modelo;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Betweenle {

    private String palabraSecreta;
    private String palabraBaja;
    private String palabraAlta;
    private int intentosTotales;
    private int intentosRestantes;
    private ArrayList<String> historialPalabras;
    private HashSet<Character> letrasUsadas;
    private boolean pistaUsada = false;

    public Betweenle(String palabraSecreta, int intentos) {
        this.palabraSecreta = palabraSecreta;
        String limiteAlto = "z".repeat(palabraSecreta.length());
        String limiteBajo = "a".repeat(palabraSecreta.length());
        palabraBaja = limiteBajo;
        palabraAlta = limiteAlto;
        intentosRestantes = intentos;
        intentosTotales = intentos;
        historialPalabras = new ArrayList<String>();
        letrasUsadas = new HashSet<>();
        actualizarLetrasUsadas();
    }



    public int adivinarPalabra(String palabra){
        pistaUsada = false;
        if (palabra.compareToIgnoreCase(palabraBaja) <= 0) {
            return 2;
        }

        if (palabra.compareToIgnoreCase(palabraAlta) >= 0) return 3;

        historialPalabras.add(palabra);
        intentosRestantes--;

        int comparacion = palabra.compareToIgnoreCase(palabraSecreta);

        if (comparacion == 0) {
            return 0;
        } else if (comparacion < 0) {
            if (palabra.compareToIgnoreCase(palabraBaja) > 0) {
                palabraBaja = palabra;
                actualizarLetrasUsadas();
            }
            return -1;
        } else {
            if (palabra.compareToIgnoreCase(palabraAlta) < 0) {
                palabraAlta = palabra;
                actualizarLetrasUsadas();
            }
            return 1;
        }
    }


    public boolean juegoAcabado() {
        return intentosRestantes <= 0;
    }

    public boolean juegoGanado() {

        return !historialPalabras.isEmpty() &&
                historialPalabras.get(historialPalabras.size() - 1).equalsIgnoreCase(palabraSecreta);
    }

    public String getPalabraSecreta() {
        return palabraSecreta;
    }

    public String getPalabraBaja() {
        return palabraBaja;
    }

    public String getPalabraAlta() {
        return palabraAlta;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public ArrayList<String> getHistorialPalabras() {
        return historialPalabras;
    }

    public HashSet<Character> getLetrasUsadas() {
        return letrasUsadas;
    }

    public void setPalabraAlta(String palabraAlta) {
        this.palabraAlta = palabraAlta;
        actualizarLetrasUsadas();
    }

    public void setPalabraBaja(String palabraBaja) {
        this.palabraBaja = palabraBaja;
        actualizarLetrasUsadas();
    }

    public double calcularProximidadLimite(String limite, Diccionario diccionario) {
        int longitud = palabraSecreta.length();
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);
        int totalPalabras = palabrasOrdenadas.size();

        if (totalPalabras <= 1) return 0.01;

        int indicePalabraS = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limite.toLowerCase());

        if (indicePalabraS < 0) indicePalabraS = -(indicePalabraS + 1);
        if (indiceLimite < 0) indiceLimite = -(indiceLimite + 1);

        double distanciaIndices = Math.abs(indiceLimite - indicePalabraS);
        double distanciaPorcentaje = (distanciaIndices / totalPalabras) * 100.0;

        if (distanciaPorcentaje < 0.001) return 0.001;
        if (distanciaPorcentaje > 100.0) return 100.0;

        return Math.round(distanciaPorcentaje * 100.0) / 100.0;
    }

    public String recorrerLimites(Diccionario diccionario, boolean limiteAltoBuscado) {
        int longitud = palabraSecreta.length();
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);
        String palabraLimite = limiteAltoBuscado ? palabraAlta : palabraBaja;

        if (calcularProximidadLimite(palabraLimite, diccionario) <= 1.00) {
            return "la distancia es cercana a 1";
        }

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, palabraLimite.toLowerCase());

        if (indiceSecreto < 0) indiceSecreto = -(indiceSecreto + 1);
        if (indiceLimite < 0) indiceLimite = -(indiceLimite + 1);

        int distancia = Math.abs(indiceLimite - indiceSecreto);

        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));
        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        // 1. Declaras la variable vacía
        int nuevoIndice;

        // 2. Calculas el movimiento en la dirección correcta según el límite
        if (limiteAltoBuscado) {
            nuevoIndice = indiceLimite - pasos; // El de arriba RESTA para bajar hacia la secreta
            nuevoIndice = Math.max(indiceSecreto + 1, nuevoIndice);
        } else {
            nuevoIndice = indiceLimite + pasos; // El de abajo SUMA para subir hacia la secreta
            nuevoIndice = Math.min(indiceSecreto - 1, nuevoIndice);
        }

        return palabrasOrdenadas.get(nuevoIndice);


    }

    public boolean isPistaUsada() {
        return pistaUsada;
    }

    public void setPistaUsada(boolean pistaUsada) {
        this.pistaUsada = pistaUsada;
    }

    public void actualizarLetrasUsadas() {
        letrasUsadas.clear();
        for (char letra : palabraBaja.toCharArray()) {
            letrasUsadas.add(letra);
        }
        for (char letra : palabraAlta.toCharArray()) {
            letrasUsadas.add(letra);
        }

    }

    public int getIntentosTotales() {
        return intentosTotales;
    }
}



