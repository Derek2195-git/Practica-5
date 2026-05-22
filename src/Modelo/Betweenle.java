package Modelo;



import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

public class Betweenle {

    private String palabraSecreta;
    private String palabraBaja;
    private String palabraAlta;
    private int intentosTotales;
    private int intentosRestantes;
    private ArrayList<String> historialPalabras;
    private HashSet<Character> letrasUsadas;
    private boolean pistaUsada = false;

    /**
     * Constructor de la lógica del Betweenle
     * @param palabraSecreta Palabra que se va a esconder
     * @param intentos Intentos totales antes de perder
     */
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

    /**
     * Metodo para quitar tildes de una palabra antes de adivinarla
     * @param texto Texto al que le vamos a quitar tildes
     * @return Nuevo texto libre de acentos
     */
    public String quitarTildes(String texto) {
        // Normalizamos usando el estandar de unicode NDF para separar los acentos
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Retornamos una palabra a la que se aplico una expresión regular la cual quita todos los acentos
        return texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Adivina una palabra dada por el usuario
     * @param palabra Palabra a adivinar
     * @return Diferentes resultados los cuales varían del retorno usado
     */
    public int adivinarPalabra(String palabra){
        palabra = quitarTildes(palabra);
        // Si la palabra es igual al limite bajo, nos regresamos
        if (palabra.compareToIgnoreCase(palabraBaja) <= 0) {
            return 2;
        }
        // Si la palabra es igual al limite alto, nos regresamos
        if (palabra.compareToIgnoreCase(palabraAlta) >= 0) return 3;

        historialPalabras.add(palabra);
        intentosRestantes--;

        // Si no se regreso, comparamos la palabra con la palabra secreta
        int comparacion = palabra.compareToIgnoreCase(palabraSecreta);

        // Dependiendo de la comparacion y distancia con el limite, regresamos
        // un valor diferente que indica la cercania con el limite
        // Posteriormente actualizamos las letras que usadas actualmente
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

    /**
     * Metodo que verifica si el juego acabo por turnos
     * @return Booleano que indica si el numero de intentos restantes llegó a cero
     */
    public boolean juegoAcabado() {
        return intentosRestantes <= 0;
    }

    /**
     * Metodo que verifica si el jugador ganó el juego
     * @return Booleando que indica si el jugador si la ultima palabra ingresada es igual a la palabra secreta
     */
    public boolean juegoGanado() {

        return !historialPalabras.isEmpty() &&
                historialPalabras.get(historialPalabras.size() - 1).equalsIgnoreCase(palabraSecreta);
    }

    /**
     * Getter de la palabra secreta
     * @return Palabra que el betweenle tiene como secreta
     */
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

    /**
     * Este metodo calcula la proximidad de cada limite, teniendo de parametros la palabra
     * y el diccionario donde se revisa
     * @param limite Limite a revisar
     * @param diccionario Diccionario con el que se revisaran las palabras
     * @return Porcentaje representado en decimales de la distancia de un limite
     */
    public double calcularProximidadLimite(String limite, Diccionario diccionario) {
        // Inicializamos los valores que vamos a ocupar, estos siendo la
        //  longitud, un ArrayList de palabras ordenadas y el total de esta clase de coleccion
        int longitud = palabraSecreta.length();
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);
        int totalPalabras = palabrasOrdenadas.size();

        // Si el ArrayList tiene 0 o 1 palabra, ambos limites se representarán como 0.01
        if (totalPalabras <= 1) return 0.01;

        // Calculamos el indice actual de la palabra secreta haciendo una busqueda binaria
        int indicePalabraS = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        // Calculamos el indice actual del limite dado por el usuario haciendo una busqueda binaria
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limite.toLowerCase());

        //  Si la palabra dada no existe en el arreglo, la busqueda binaria de la clase Collections
        // devuelve un indice negativo basado en la formula -(insertionPoint - 1).
        // Asi que aplicamos esta operacion de forma inversa para obtener el indice correcto
        if (indicePalabraS < 0) indicePalabraS = -(indicePalabraS + 1);
        if (indiceLimite < 0) indiceLimite = -(indiceLimite + 1);

        // Calculamos la distancia que hay entre los indices
        double distanciaIndices = Math.abs(indiceLimite - indicePalabraS);
        // Luego convertimos esta distancia en un porcentaje respecto al total de palabras del diccionario
        double distanciaPorcentaje = (distanciaIndices / totalPalabras) * 100.0;

        // Para evitar desbordamientos, si una palabra supera el 100% o el 0.001, automaticamente los volvemos estos valores
        if (distanciaPorcentaje < 0.001) return 0.001;
        if (distanciaPorcentaje > 100.0) return 100.0;

        // Por ultimo, redondeamos el porcentaje para que se usen dos cifras decimales
        return Math.round(distanciaPorcentaje * 100.0) / 100.0;
    }

    /**
     * Este metodo recorre un limite un 1% en una direccion, dependiendo de lo que el usuario quiera
     * @param diccionario Diccionario donde estan todas las palabras
     * @param limiteAltoBuscado Booleano que indica si se va a buscar el limite alto, en caso contrario se indica el limite bajo
     * @return Palabra que se ubica un 1% por encima o por debajo del limite pedido por el usuario
     */
    public String recorrerLimites(Diccionario diccionario, boolean limiteAltoBuscado) {
        // Inicializamos los valores que vamos a ocupar, siendo la longitud de la palabra secreta y
        // un ArrayList con todas las palabras que tengan el mismo tamaño que la secreta ordenadas
        int longitud = palabraSecreta.length();
        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);
        String palabraLimite = limiteAltoBuscado ? palabraAlta : palabraBaja;

        // Si el limite buscado tiene una proximidad de más de 1%, no se realizará esta pista
        if (calcularProximidadLimite(palabraLimite, diccionario) <= 1.00) {
            return "";
        }

        // Calculamos el indice actual de la palabra secreta haciendo una busqueda binaria
        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        // Calculamos el indice del limite secreta haciendo una busqueda binaria
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, palabraLimite.toLowerCase());

        // De igual forma que en el calculo de proximidad, comprobamos si los indices son negativos
        // e invertimos la formula en caso de que lo sean para obtener los puntos de insercion correctos
        if (indiceSecreto < 0) indiceSecreto = -(indiceSecreto + 1);
        if (indiceLimite < 0) indiceLimite = -(indiceLimite + 1);

        // Calculamos cuantas palabras hay de distancia entre el limite y la palabra secreta
        int distancia = Math.abs(indiceLimite - indiceSecreto);

        // Calculamos cuantas palabras equivaldrian al 1% total del diccionario
        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        // Si el 1% calculado en pasos es mayor o igual a la distancia que nos separa de la palabra secreta,
        // reajustamos los pasos para que se detenga exactamente un casillero *antes* de la solución

        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        // Usando los valores obtenidos, empezamos a buscar una nueva palabra la cual represente a
        // un salto de 1%
        int nuevoIndice;



        if (limiteAltoBuscado) {
            // Si estamos buscando el limite alto, restamos posiciones en el arreglo para *bajar* en la direccion de la palabra secreta
            // Usamos Math.max para que el nuevo limite nunca sea menor o igual al indice de la palabra secreta
            nuevoIndice = indiceLimite - pasos;
            nuevoIndice = Math.max(indiceSecreto + 1, nuevoIndice);
        } else {
            // Si buscamos el limite bajo, aumentamos las posiciones en el arreglo para *subir* en la direccion de la palabra secreta
            nuevoIndice = indiceLimite + pasos;
            // Usamos Math.min para que el nuevo limite no sea mayor o igual al indice de la palabra secreta
            nuevoIndice = Math.min(indiceSecreto - 1, nuevoIndice);
        }

        // Finalmente, retornamos la nueva palabra que se calculo en el if anterior
        return palabrasOrdenadas.get(nuevoIndice);


    }

    /**
     * Revisa que se haya usado una pista
     * @return booleano que indica si la pista se usó
     */
    public boolean isPistaUsada() {
        return pistaUsada;
    }

    /**
     * Setter del atributo pistaUsada
     * @param pistaUsada Nuevo valor que se le va a poner a pistaUsada
     */
    public void setPistaUsada(boolean pistaUsada) {
        this.pistaUsada = pistaUsada;
    }

    /**
     * Metodo que actualiza el HashSet de letras usadas
     */
    public void actualizarLetrasUsadas() {
        // Limpiamos el ArrayList de letras usadas
        letrasUsadas.clear();
        // Añadimos los caracteres que contiene la palabra baja en nuestro ArrayList
        for (char letra : palabraBaja.toCharArray()) {
            letrasUsadas.add(letra);
        }
        // Añadimos los caracteres que se contienen en la palabra Baja en nuestro ArrayList
        for (char letra : palabraAlta.toCharArray()) {
            letrasUsadas.add(letra);
        }

    }

    public int getIntentosTotales() {
        return intentosTotales;
    }
}