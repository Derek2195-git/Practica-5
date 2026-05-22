package Controlador;


import Modelo.Betweenle;
import Modelo.Diccionario;
import Vista.Vista;

public class Controlador {
    private Diccionario diccionario;
    private Betweenle juego;
    private Vista vista;

    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        controlador.iniciarJuego();
    }

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
            vista.mostrarCadena("Por defecto se tomará la dificultad fácil.");
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

    public void iniciarJuego() {
        vista.mostrarBienvenida();

        while (!juego.juegoAcabado() && !juego.juegoGanado()) {

            String limiteInicial = "a".repeat(juego.getPalabraSecreta().length());
            String limiteFinal = "z".repeat(juego.getPalabraSecreta().length());

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
                String intento = vista.preguntarPalabra();
                if (intento.length() != juego.getPalabraSecreta().length()) {
                    vista.mostrarAvisoTamaño(juego.getPalabraSecreta().length());
                    continue;
                }
                if (!diccionario.esUnaPalabraValida(intento)) {
                    int opcionAgregarPalabra = vista.preguntarAgregarPalabra(intento);
                    if (opcionAgregarPalabra == 1) {
                        String definicion = vista.preguntarDefinicion();
                        diccionario.agregarPalabraArchivo(intento, definicion);
                    } else {
                        continue;
                    }
                }
                int resultado = juego.adivinarPalabra(intento);
                vista.mostrarResultado(resultado, juego.getPalabraAlta(), juego.getPalabraBaja());
            } else if (opcion == 2) {
                if (juego.isPistaUsada()) {
                    vista.pistaUsada();
                    continue;
                }

                String limiteInicialPista = "a".repeat(juego.getPalabraSecreta().length());
                String limiteFinalPista = "z".repeat(juego.getPalabraSecreta().length());

                if (juego.getPalabraBaja().equalsIgnoreCase(limiteInicialPista) ||
                juego.getPalabraAlta().equalsIgnoreCase(limiteFinalPista)) {
                    vista.mostrarCadena("No se puede dar una pista aún, los límites siguen siendo los iniciales.");
                } else {
                    int opcionPista = vista.preguntarOpcionPista();
                    if (opcionPista == 1) {
                        String nuevaAlta = juego.recorrerLimites(diccionario, true);
                        if (nuevaAlta.isEmpty()) {
                            vista.mostrarCadena("El límite de arriba ya está muy cerca de la palabra secreta.");
                        } else {
                            juego.setPalabraAlta(nuevaAlta);
                            vista.mostrarCadena("El nuevo límite de arriba es: " + nuevaAlta);
                            juego.setPistaUsada(true);
                        }
                    } else if (opcionPista == 2){
                        String nuevaBaja = juego.recorrerLimites(diccionario, false);
                        if (nuevaBaja.isEmpty()) {
                            vista.mostrarCadena("El límite de abajo ya está muy cerca de la palabra secreta.");
                        } else {
                            juego.setPalabraBaja(nuevaBaja);
                            vista.mostrarCadena("El nuevo límite de abajo es: " + nuevaBaja);
                            juego.setPistaUsada(true);
                        }
                    } else if (opcionPista == 3) {
                        vista.mostrarPrimeraLetraSecreta(juego.getPalabraSecreta());
                        juego.setPistaUsada(true);
                    }
                }

            } else if (opcion == 3) {
                vista.mostrarRendicion(juego.getPalabraSecreta());
                return;
            }
        }

        if (juego.juegoGanado()) {
            vista.mostrarVictoria(juego.getPalabraSecreta());
        } else {
            vista.mostrarDerrota(juego.getPalabraSecreta());
        }
    }
}
