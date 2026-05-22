package Vista;

import java.util.*;
import java.util.stream.Collectors;

public class Vista {
    private Scanner teclado;

    public Vista() {
        teclado = new Scanner(System.in);
    }

    public String preguntarPalabra() {
        System.out.println("Ingresa una palabra: ");
        return leerCadena();
    }

    public int mostrarMenuTurno() {
        System.out.println("¿Que deseas hacer en esta ronda?");
        System.out.println("1. Adivinar la palabra \t 2. Pedir una pista \t 3. Rendirse");
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

    public void mostrarHistorialIntentos(ArrayList<String> historial, HashSet<Character> letrasUsadas) {

        System.out.println("Palabras ingresadas:");
        Iterator<String> iterador = historial.iterator();
        int contador = 1;
        if (historial.isEmpty()) {
            System.out.println("No se ha realizado ningún intento.");
        } else {
            while (iterador.hasNext()) {
                System.out.println("Intento #" + contador + ": " + iterador.next());
                contador++;
            }
            String letrasOrdenadas = letrasUsadas.stream()
                    .sorted(Character::compareTo)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            System.out.println("Letras usadas: " + letrasOrdenadas);
        }

        System.out.println("----Fin estadisticas----");
    }

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

    public void mostrarVictoria(String palabraSecreta) {
        System.out.println("Felicidades, Ganaste el juego! La palabra era: " + palabraSecreta);
    }

    public void pedirDificultad() {
        System.out.println("Ingresa el numero de letras que quieres que posea la palabra secreta.");
    }

    public void mostrarDerrota(String palabraSecreta) {
        System.out.println("El jugador se quedó sin intentos. La palabra secreta era " + palabraSecreta);
    }

    public void mostrarRendicion(String palabraSecreta){
        System.out.println("Te rendiste, la palabra era: " + palabraSecreta);
    }


    public void mostrarBienvenida() {
        System.out.println("Bienvenido al Betweenle!");
    }

    public void mostrarAvisoTamaño(int tamañoPalabra) {
        System.out.println("La palabra debe de tener " + tamañoPalabra + " letras. Intentalo de nuevo");
    }

    /**
     * Funcion que muestra una cadena cuando acabemos
     * @param mensaje cadena a mostrar
     */
    public void mostrarCadena(String mensaje) {
        System.out.println(mensaje);
    }

    public int preguntarIdioma() {
        System.out.println("Selecciona el idioma:");
        System.out.println("1. Español \t 2. Ingles");
        return leerNumeroEntero();
    }

    public int preguntarDificultad() {
        System.out.println("Selecciona la dificultad.");
        System.out.println("1. Fácil (Las palabras tienen una longitud de 5 letras)");
        System.out.println("2. Intermedio (Las palabras tienen una longitud de 6 letras)");
        System.out.println("3. Dificil (Las palabras tienen una longitud de n letras)");
        return leerNumeroEntero();
    }

    public int preguntarIntentos() {
        System.out.println("Selecciona el número de intentos");
        System.out.println("1. 14 intentos \t 2. 12 intentos \t 3. 10 intentos");
        return leerNumeroEntero();
    }

    public int preguntarAgregarPalabra(String palabra) {
        System.out.println("La palabra " + palabra + " no esta en el diccionario, ¿desea agregarla?");
        System.out.println("1. Si");
        System.out.println("2. No");
        return leerNumeroEntero();
    }

    public String preguntarDefinicion() {
        System.out.println("Escriba la definicion de la palabra: ");
        return leerCadena();
    }

    public int preguntarOpcionPista() {
        System.out.println("¿Que deseas revelar?");
        System.out.println("1. Recorrer el 1% de la palabra de arriba");
        System.out.println("2. Recorrer el 1% de la palabra de abajo");
        System.out.println("3. Obtener la primera letra de la palabra.");

        return leerNumeroEntero();
    }


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

    public void mostrarPrimeraLetraSecreta(String palabraSecreta) {
        System.out.println("La primera letra de la palabra secreta empieza con " + palabraSecreta.toUpperCase().charAt(0));
    }

    public String leerCadena() {
        boolean cadenaValida = false;
        String cadena = "";

        while (!cadenaValida) {
            cadena = teclado.nextLine().trim().toLowerCase();

            // Esta expresión regular exige que la cadena tenga SOLO letras (de la 'a' a la 'z', más acentos y ñ)
            if (!cadena.matches("^[a-záéíóúüñ ]+$")) {
                System.out.println("Entrada inválida. La palabra ingresada solo debe contener letras. Inténtalo de nuevo.");
            } else {
                cadenaValida = true;
            }
        }

        return cadena;
    }

    public void pistaUsada() {
        System.out.println("Ya usaste una pista en este turno. ¡Intenta adivinar la palabra!");
    }



}
