package Vista;

import java.util.*;
import java.util.stream.Collectors;

public class Vista {
    private Scanner teclado;

    /**
     * Constructor de la vista, inicializamos el Scanner para leer entradas
     */
    public Vista() {
        teclado = new Scanner(System.in);
    }

    /**
     * Metodo que pide una palabra al usuario
     * @return La cadena a leer
     */
    public String preguntarPalabra() {
        System.out.println("Ingresa una palabra: ");
        return leerPalabra();
    }

    /**
     * Metodo que muestra el menú al usuario
     * @return Opcion seleccionada por el usuario
     */
    public int mostrarMenuTurno() {
        System.out.println("¿Que deseas hacer en esta ronda?");
        System.out.println("1. Adivinar la palabra \t 2. Pedir una pista \t 3. Rendirse");
        System.out.println("Nota: El sistema normaliza el texto. Ingresa tus intentos sin acentos y cambiando la 'ñ' por 'n'.");
        return leerNumeroEntero();
    }

    /**
     * Muestra el estado actual del juego
     * @param palabraBaja Palabra que se ubica despues de la adivinada
     * @param palabraAlta Palabra que se ubica antes de la adivinada
     * @param intentosRestantes Intentos que le quedan al jugador para adivinar
     */
    public void mostrarEstadoJuego(String palabraBaja, String palabraAlta, int intentos, int intentosRestantes, double[] distancias) {
        System.out.println("Estadisticas:");
        System.out.println("[" + palabraBaja.toUpperCase() + "] <-----?????-----> [" + palabraAlta.toUpperCase() + "] \t Intentos restantes: " + intentosRestantes + "/" + intentos);
        if (distancias != null) {
            System.out.println(" " + distancias[0] + "\t\t\t\t\t   " + distancias[1]);
        } else {
            System.out.println(" Abajo \t\t\t\t\t  Arriba");
        }



    }

    /**
     * Metodo que muestra el historial de intentos para las estadisticas
     * @param historial ArrayList de la palabra escrita en cada intento
     * @param letrasUsadas HashSet de las letras usadas
     */
    public void mostrarHistorialIntentos(ArrayList<String> historial, HashSet<Character> letrasUsadas) {
        System.out.println("Palabras ingresadas:");
        Iterator<String> iterador = historial.iterator();
        int contador = 1;
        // Si el historial de palabras esta vacio, se lo comunicamos al usuario
        if (historial.isEmpty()) {
            System.out.println("No se ha realizado ningún intento.");
        } else {
            // Si el historial de palabras contiene intentos, se escribiran uno por uno, en cada iteracion
            while (iterador.hasNext()) {
                System.out.println("Intento #" + contador + ": " + iterador.next());
                contador++;
            }
            // Usamos un stream para ordenar las letras y ordenarlas
            String letrasOrdenadas = letrasUsadas.stream()
                    .sorted(Character::compareTo)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            System.out.println("Letras usadas: " + letrasOrdenadas);
        }

        System.out.println("----Fin estadisticas----");
    }

    /**
     * Metodo para mostrar el resultado del intento despues de adivinar una palabra
     * @param resultado Numero entero que representa el resultadp
     * @param palabraAlta Nuestra palabra alta
     * @param palabraBaja Nuestra palabra baja
     */
    public void mostrarResultado(int resultado, String palabraAlta, String palabraBaja) {
        if (resultado == -1) {
            System.out.println("La palabra secreta esta por debajo de su intento");
        } else if (resultado == 1) {
            System.out.println("La palabra secreta está antes de su intento");
        } else if (resultado == 2) {
            System.out.println("La palabra introducida está fuera del rango actual. " +
                    "Introduce una palabra que se ubique *despues* de " + palabraBaja.toUpperCase());
        } else if (resultado == 3) {
            System.out.println("La palabra introducida está fuera del rango actual. " +
                    "Introduce una palabra que se ubique *antes* de " + palabraAlta.toUpperCase());
        }
    }

    /**
     * Metodo que imprime en pantalla en caso de que el jugador gane el juego
     * @param palabraSecreta
     */
    public void mostrarVictoria(String palabraSecreta) {
        System.out.println("Felicidades, Ganaste el juego! La palabra era: " + palabraSecreta);
    }

    /**
     * Metodo que imprime en pantalla la solicitud para que el usuario indique
     * el número de letras de la palabra a adivinar.
     */
    public void pedirDificultad() {
        System.out.println("Ingresa el numero de letras que quieres que posea la palabra secreta.");
    }

    /**
     * Imprime el mensaje de derrota cuando el usuario se queda sin intentos.
     * @param palabraSecreta La palabra que el usuario no logró adivinar.
     */
    public void mostrarDerrota(String palabraSecreta) {
        System.out.println("El jugador se quedó sin intentos. La palabra secreta era " + palabraSecreta);
    }

    /**
     * Muestra el mensaje de despedida y revela la palabra cuando el jugador decide rendirse.
     * @param palabraSecreta La palabra secreta de la ronda actual.
     */
    public void mostrarRendicion(String palabraSecreta){
        System.out.println("Te rendiste, la palabra era: " + palabraSecreta);
    }

    /**
     * Muestra el mensaje inicial al ejecutar el juego.
     */
    public void mostrarBienvenida() {
        System.out.println("Bienvenido al Betweenle!");
    }

    /**
     * Notifica al usuario que la palabra ingresada no tiene la longitud correcta.
     * @param tamañoPalabra La cantidad exacta de letras que debe tener la palabra.
     */
    public void mostrarAvisoTamaño(int tamañoPalabra) {
        System.out.println("La palabra debe de tener " + tamañoPalabra + " letras. Intentalo de nuevo");
    }

    /**
     * Funcion que muestra una cadena de texto arbitraria en pantalla.
     * @param mensaje La cadena a mostrar.
     */
    public void mostrarCadena(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Despliega el menú para seleccionar el idioma del diccionario a usar.
     * @return El número entero de la opción seleccionada.
     */
    public int preguntarIdioma() {
        System.out.println("Selecciona el idioma:");
        System.out.println("1. Español \t 2. Ingles");
        return leerNumeroEntero();
    }

    /**
     * Muestra las opciones de dificultad inicial (tamaño de las palabras).
     * @return El número de la opción seleccionada por el jugador.
     */
    public int preguntarDificultad() {
        System.out.println("Selecciona la dificultad.");
        System.out.println("1. Fácil (Las palabras tienen una longitud de 5 letras)");
        System.out.println("2. Intermedio (Las palabras tienen una longitud de 6 letras)");
        System.out.println("3. Dificil (Las palabras tienen una longitud de n letras)");
        return leerNumeroEntero();
    }

    /**
     * Muestra el menú para elegir la cantidad de intentos permitidos en el juego.
     * @return La opción elegida.
     */
    public int preguntarIntentos() {
        System.out.println("Selecciona el número de intentos");
        System.out.println("1. 14 intentos \t 2. 12 intentos \t 3. 10 intentos");
        return leerNumeroEntero();
    }

    /**
     * Pregunta al usuario si desea añadir una nueva palabra al diccionario.
     * @param palabra La palabra que no fue encontrada.
     * @return La respuesta elegida (1 para Sí, 2 para No).
     */
    public int preguntarAgregarPalabra(String palabra) {
        System.out.println("La palabra " + palabra + " no esta en el diccionario, ¿desea agregarla?");
        System.out.println("1. Si");
        System.out.println("2. No");
        return leerNumeroEntero();
    }

    /**
     * Solicita la definición para una palabra que va a ser añadida al diccionario.
     * @return La cadena de texto con la definición ingresada.
     */
    public String preguntarDefinicion() {
        System.out.println("Escriba la definicion de la palabra: ");
        return leerCadena();
    }

    /**
     * Despliega las diferentes opciones de pistas que el jugador puede elegir.
     * @return La opción de pista elegida.
     */
    public int preguntarOpcionPista() {
        System.out.println("¿Que deseas revelar?");
        System.out.println("1. Recorrer el 1% de la palabra de arriba");
        System.out.println("2. Recorrer el 1% de la palabra de abajo");
        System.out.println("3. Obtener la primera letra de la palabra.");
        return leerNumeroEntero();
    }

    /**
     * Este metodo lee y valida de forma segura un numero entero dado por el usuario
     * evitando que el programa caiga si el usuario ingresa un texto por accidente
     * @return Un numero entero ingresado y validado
     */
    public int leerNumeroEntero(){
        boolean numeroIngresado = false;
        int numeroLeido = 0;
        while (!numeroIngresado) {
            try {
                int numero = teclado.nextInt();
                teclado.nextLine();
                numeroIngresado = true;
                numeroLeido = numero;
            } catch (InputMismatchException e) {
                teclado.nextLine();
                System.out.println("Entrada inválida, escribe un número.");
            }
        }
        return numeroLeido;
    }

    /**
     * Este metodo muestra como pista la primera letra de la palabra secreta
     * @param palabraSecreta Palabra que el jugador debe adivinar
     */
    public void mostrarPrimeraLetraSecreta(String palabraSecreta) {
        System.out.println("La primera letra de la palabra secreta empieza con " + palabraSecreta.toUpperCase().charAt(0));
    }

    /**
     * Lee y valida una cadena de texto, garantizando que solo contenga caracteres alfabeticos
     * @return La palabra introducida, pasada a minusculas y sin espacios a los lados
     */
    public String leerPalabra() {
        boolean cadenaValida = false;
        String cadena = "";

        while (!cadenaValida) {
            cadena = teclado.nextLine().trim().toLowerCase();

            if (!cadena.matches("^[a-záéíóúüñ ]+$")) {
                System.out.println("Entrada inválida. La palabra ingresada solo debe contener letras. Inténtalo de nuevo.");
            } else {
                cadenaValida = true;
            }
        }

        return cadena;
    }

    /**
     * Lee y valida una cadena, asegurando que no este vacia y no contenga
     * un separador usado para el sistema de archivos
     * @return La definición ingresada por el usuario
     */
    public String leerCadena() {
        boolean definicionValida = false;
        String definicion = "";

        while (!definicionValida) {
            definicion = teclado.nextLine().trim();

            if (definicion.isEmpty()) {
                System.out.println("Entrada invalida. La definicion no puede estar vacia, intentalo de nuevo.");
            } else if (definicion.contains(":")) {
                System.out.println("Entrada invalida. La definicion no puede contener el caracter, ':'. Intentalo de nuevo");
            } else {
                definicionValida = true;
            }
        }
        return definicion;
    }

    /**
     * Metodo que informa al jugador que ha usado su unica pista en la partida.
     */
    public void pistaUsada() {
        System.out.println("Ya usaste tu pista. ¡Intenta adivinar la palabra!");
    }



}
