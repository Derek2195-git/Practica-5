package Controlador;


import Modelo.Betweenle;
import Modelo.Diccionario;
import Vista.Vista;

public class Controlador {
    private Diccionario diccionario;
    private Betweenle juego;
    private Vista vista;

    /**
     * Metodo estatico para correr el codigo
     * @param args Argumentos que necesita el main
     */
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        controlador.iniciarJuego();
    }

    /**
     * Constructor del controlador, aqui se inicializa el idioma, dificultad, y los intentos maximos
     * asi como el diccionario, la palabra secreta y la lógica
     */
    public Controlador() {
        vista = new Vista();

        int idiomaSeleccionada = vista.preguntarIdioma();
        boolean esIngles = (idiomaSeleccionada == 2);

        int dificultadSeleccionada = vista.preguntarDificultad();
        int longitud;
        if (dificultadSeleccionada == 1) longitud = 5;
        else if (dificultadSeleccionada == 2) longitud = 6;
        else if (dificultadSeleccionada == 3) {
            vista.pedirDificultad();
            longitud = vista.leerNumeroEntero();
        } else {

            vista.dificultadPorDefectoSeleccionada();
            longitud = 5;
        }

        int intentosSeleccionados = vista.preguntarIntentos();
        int intentos;
        if (intentosSeleccionados == 1) intentos = 14;
        else if (intentosSeleccionados == 2) intentos = 12;
        else intentos = 10;

        diccionario = new Diccionario(esIngles);
        String palabraElegida = diccionario.getPalabraAleatoria(longitud);
        juego = new Betweenle(palabraElegida, intentos);

    }

    /**
     * Metodo que inicia el juego
     */
    public void iniciarJuego() {
        vista.mostrarBienvenida();
        // Mientras el juego no se haya acabado de alguna forma
        while (!juego.juegoAcabado() && !juego.juegoGanado()) {

            String limiteInicial = "a".repeat(juego.getPalabraSecreta().length());
            String limiteFinal = "z".repeat(juego.getPalabraSecreta().length());

            // Empezamos a calcular las distancias cuando uno de los limites se vuelva un valor diferente a su forma inicial
            double[] distancias = null;
            if (!juego.getPalabraBaja().equals(limiteInicial) || !juego.getPalabraAlta().equals(limiteFinal)) {
                distancias = new double[]{
                        juego.calcularProximidadLimite(juego.getPalabraBaja(), diccionario),
                        juego.calcularProximidadLimite(juego.getPalabraAlta(), diccionario)
                };
            }

            vista.mostrarEstadoJuego(
                    juego.getPalabraBaja(),
                    juego.getPalabraAlta(),
                    juego.getIntentosTotales(),
                    juego.getIntentosRestantes(),
                    distancias
            );
            vista.mostrarHistorialIntentos(juego.getHistorialPalabras(), juego.getLetrasUsadas());
            int opcion = vista.mostrarMenuTurno();
            if (opcion == 1) {
                // Si el usuario ingresa la primera opcion, le pedimos que ingrese su intento y verificamos que sea del mismo tamaño que la secreta
                String intento = vista.preguntarPalabra();
                if (intento.length() != juego.getPalabraSecreta().length()) {
                    vista.mostrarAvisoTamaño(juego.getPalabraSecreta().length());
                    continue;
                }
                // Si en el diccionario no se encuentra la palabra, se pregunta al usuario si la agregar, dependiendo del resultado
                // se agrega o no
                if (!diccionario.esUnaPalabraValida(intento)) {
                    int opcionAgregarPalabra = vista.preguntarAgregarPalabra(intento);
                    if (opcionAgregarPalabra == 1) {
                        String definicion = vista.preguntarDefinicion();
                        diccionario.agregarPalabraArchivo(intento, definicion);
                    } else {
                        continue;
                    }
                }
                // Mostramos el resultado de adivinar la palabra
                int resultado = juego.adivinarPalabra(intento);
                vista.mostrarResultado(resultado, juego.getPalabraAlta(), juego.getPalabraBaja());
            } else if (opcion == 2) {
                // Si el jugador elige agarrar una pista, revisamos que no lo haya hecho anteriormente
                if (juego.isPistaUsada()) {
                    vista.pistaUsada();
                    continue;
                }

                // Revisamos si los limites actuales son iguales a los iniciales, cualquiera de los dos sigue el inicial
                // no te dejará usar la pista
                if (juego.getPalabraBaja().equalsIgnoreCase(limiteInicial) ||
                juego.getPalabraAlta().equalsIgnoreCase(limiteFinal)) {

                    vista.mostrarPistaNoPosible();
                } else {
                    // Le pedimos al usuario que vuelva a ingresar un numero y dependiendo del resultado, ejecutamos la pista correspondiente
                    int opcionPista = vista.preguntarOpcionPista();
                    // Recorrer limite de arriba
                    if (opcionPista == 1) {
                        String nuevaAlta = juego.recorrerLimites(diccionario, true);
                        if (nuevaAlta.isEmpty()) {
                            vista.mostrarLimiteAltoCercano();
                        } else {
                            juego.setPalabraAlta(nuevaAlta);
                            vista.mostrarNuevoLimiteAlto(nuevaAlta);
                            juego.setPistaUsada(true);
                        }
                        // Recorrer limite de abajo
                    } else if (opcionPista == 2){

                        String nuevaBaja = juego.recorrerLimites(diccionario, false);
                        if (nuevaBaja.isEmpty()) {
                            vista.mostrarLimiteBajoCercano();
                        } else {
                            juego.setPalabraBaja(nuevaBaja);
                            vista.mostrarNuevoLimiteBajo(nuevaBaja);
                            juego.setPistaUsada(true);
                        }
                        // Obtener la primera letra de la palabra
                    } else if (opcionPista == 3) {
                        vista.mostrarPrimeraLetraSecreta(juego.getPalabraSecreta());
                        juego.setPistaUsada(true);
                    }
                }
            // Si el jugador se rinde, acabamos el codigo puesto que ya no hay más que hacer
            } else if (opcion == 3) {
                vista.mostrarRendicion(juego.getPalabraSecreta());
                return;
            }
        }
        // Si el jugador gana o pierde el juego, el texto se actualiza de forma correspondiente
        if (juego.juegoGanado()) {
            vista.mostrarVictoria(juego.getPalabraSecreta());
        } else {
            vista.mostrarDerrota(juego.getPalabraSecreta());
        }
    }
}
