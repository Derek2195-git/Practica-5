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
    private boolean pistaUsadaEnElTurno = false;

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
        pistaUsadaEnElTurno = false;
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

    public String procesarIntento(String intento, Diccionario diccionario) {
        String palabraLimpia = intento.trim().toLowerCase();
        if (palabraLimpia.length() != palabraSecreta.length()) {
            return "longitud";
        }

        if (diccionario.esUnaPalabraValida(palabraLimpia)){
            return "noExiste";
        }

        if (palabraLimpia.compareToIgnoreCase(palabraBaja) <= 0 ||
                palabraLimpia.compareToIgnoreCase(palabraAlta) >= 0) return "fueraDeRango";

        int resultado = adivinarPalabra(palabraLimpia);

        if (resultado == 0) return "correcto";
        if (resultado == -1) return "menor";
        return "mayor";

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

    public String recorrerLimiteAlto(Diccionario diccionario) {
        int longitud = palabraSecreta.length();
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);

        if (calcularProximidadLimite(palabraAlta, diccionario) <= 1.00) {
            return "distanciaMuyCercana";
        }

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, palabraAlta.toLowerCase());

        if (indiceSecreto < 0) indiceSecreto = -(indiceSecreto + 1);
        if (indiceLimite < 0) indiceLimite = -(indiceLimite + 1);

        int distancia = indiceLimite - indiceSecreto;
        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        int nuevoIndice = indiceLimite - pasos;
        nuevoIndice = Math.max(indiceSecreto + 1, nuevoIndice);
        return palabrasOrdenadas.get(nuevoIndice);
    }

    public String recorrerLimiteBajo(Diccionario diccionario) {
        int longitud = palabraSecreta.length();
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);

        if (calcularProximidadLimite(palabraBaja, diccionario) <= 1.00) {
            return "distanciaMuyCercana";
        }

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, palabraBaja.toLowerCase());

        if (indiceSecreto < 0) indiceSecreto = -(indiceSecreto + 1);
        if (indiceLimite < 0) indiceLimite = -(indiceLimite + 1);

        int distancia = indiceSecreto - indiceLimite;
        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        int nuevoIndice = indiceLimite + pasos;
        nuevoIndice = Math.min(indiceSecreto - 1, nuevoIndice);
        return palabrasOrdenadas.get(nuevoIndice);
    }

    public boolean isPistaUsadaEnElTurno() {
        return pistaUsadaEnElTurno;
    }

    public void setPistaUsadaEnElTurno(boolean pistaUsadaEnElTurno) {
        this.pistaUsadaEnElTurno = pistaUsadaEnElTurno;
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



