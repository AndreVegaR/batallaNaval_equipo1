package vistas;

import controladores.JuegoController;
import enumeradores.Color;
import enumeradores.EstadoCasilla;
import enumeradores.EstadoNavio;
import enumeradores.TipoNavio;
import interfaces.IModelo;
import java.util.List;
import java.util.Scanner;
import modelos.Coordenada;
import modelos.Disparo;
import modelos.Jugador;
import modelos.Navio;
import modelos.Perfil;
import modelos.Turno;
import observador.IObservadorTurno;

/**
 * Vista donde se ejecutará el juego, gestiona el bucle de juego y se encarga de
 * imprimir en consola los menús, los tableros y los mensajes
 *
 * @author Andre
 */
public class PantallaBatalla implements IObservadorTurno {

    private static final String SEPARADOR = "==================================================";

    // Atributos
    private JuegoController controlador;
    private IModelo modelo;
    private Scanner scanner;
    private boolean ejecutando;
    private Jugador jugadorMostrado;

    /**
     * Construye la vista con todo lo que necesita para funcionar
     *
     * @param controlador controlador al que se le envían las acciones del
     * jugador
     * @param modelo modelo de solo lectura del que se consultan los datos
     */
    public PantallaBatalla(JuegoController controlador, IModelo modelo) {
        this.controlador = controlador;
        this.modelo = modelo;
        this.scanner = new Scanner(System.in);
        this.ejecutando = false;
    }

    /**
     * Arranca el ciclo principal del menú y lo mantiene mientras ejecutando sea
     * verdadero y el juego no haya terminado
     */
    public void iniciar() {
        // Muestra la bienvenida y espera al jugador antes de empezar
        ejecutando = true;
        imprimirBienvenida();
        leerLinea();

        // Prepara el primer turno para que el tiempo cuente desde que inicia el juego
        jugadorMostrado = modelo.obtenerTurnoActual();
        controlador.reiniciarContador();

        // Mantiene el ciclo del menú principal hasta que se salga o termine la partida
        while (ejecutando && !modelo.esFinDeJuego()) {
            mostrarMenuPrincipal();
            String opcion = leerLinea();

            // Si durante la lectura de línea se canceló la ejecución (ej. fin de flujo), salimos
            if (!ejecutando || modelo.esFinDeJuego()) {
                break;
            }

            switch (opcion) {
                case "1" ->
                    gestionarVerNavios();
                case "2" ->
                    gestionarVerMiTablero();
                case "3" ->
                    gestionarVerTableroRival();
                case "4" ->
                    gestionarDisparar();
                case "5" ->
                    gestionarSalir();
                default ->
                    imprimirError("Opción no válida");
            }
        }

        // Se despide cuando el ciclo termina
        imprimirDespedida();
    }

    /**
     * Consulta los datos del turno en curso y muestra el menú principal
     */
    private void mostrarMenuPrincipal() {
        // Obtiene los datos del turno en curso
        Jugador jugador = modelo.obtenerTurnoActual();
        int numeroTurno = modelo.obtenerCantidadTurnos();
        int segundosRestantes = modelo.obtenerSegundosRestantes();

        // Cuenta los disparos realizados en el turno
        Turno turno = jugador.getTurno();
        List<Disparo> disparos = turno.getDisparos();
        int disparosTurno = disparos.size();

        // Imprime el menú con esos datos
        imprimirMenuPrincipal(jugador, numeroTurno, segundosRestantes, disparosTurno);
    }

    /**
     * Muestra los navíos activos del jugador en turno
     */
    private void gestionarVerNavios() {
        List<Navio> navios = modelo.obtenerNaviosActivos();
        imprimirNavios(navios);
    }

    /**
     * Muestra el tablero propio del jugador en turno revelando sus navíos
     */
    private void gestionarVerMiTablero() {
        Jugador jugador = modelo.obtenerTurnoActual();
        EstadoCasilla[][] estados = hacerTablero(jugador);
        imprimirTablero("TU TABLERO", estados, true);
    }

    /**
     * Muestra el tablero del rival ocultando sus navíos y cuántos le quedan
     */
    private void gestionarVerTableroRival() {
        Jugador rival = modelo.obtenerRivalActual();
        EstadoCasilla[][] estados = hacerTablero(rival);
        imprimirTablero("TABLERO DEL RIVAL", estados, false);
        List<Navio> restantes = rival.getNaviosActivos();
        int cantidad = restantes.size();
        imprimirNaviosRestantes(cantidad);
    }

    /**
     * Gestiona el submenú de disparo, pide coordenadas hasta que el turno
     * cambie, termine la partida o se regrese con 0
     */
    private void gestionarDisparar() {
        boolean disparando = true;

        while (disparando && ejecutando && !modelo.esFinDeJuego()) {
            // Muestra el tablero rival y pide la coordenada
            gestionarVerTableroRival();
            imprimirSolicitudCoordenada();
            String entrada = leerLinea();

            // Regresa al menú principal, cancela o procesa el disparo
            if (entrada.equals("0") || !ejecutando || modelo.esFinDeJuego()) {
                disparando = false;
            } else {
                disparando = procesarEntradaDisparo(entrada);
            }
        }
    }

    /**
     * Interpreta la entrada del jugador, envía el disparo al controlador y
     * atiende los errores de validación
     *
     * @param entrada texto escrito por el jugador con la coordenada
     * @return true si el jugador conserva el turno y puede volver a disparar
     */
    private boolean procesarEntradaDisparo(String entrada) {
        // Interpreta la coordenada escrita
        Coordenada coordenada = interpretarCoordenada(entrada);
        if (coordenada == null) {
            imprimirError("Formato no válido, escribe fila,columna con números del 1 al 10");
            return true;
        }

        // Envía el disparo al controlador y atiende los errores de validación
        Jugador jugadorInicial = modelo.obtenerTurnoActual();
        try {
            controlador.procesarDisparo(coordenada);
        } catch (IllegalArgumentException e) {
            imprimirError(e.getMessage());
            return true;
        } catch (IllegalStateException e) {
            imprimirError(e.getMessage());
            controlador.pasarTurno();
            return false;
        }

        // El jugador conserva el turno solo si sigue siendo el mismo y la partida no terminó
        Jugador jugadorActual = modelo.obtenerTurnoActual();
        boolean finDeJuego = modelo.esFinDeJuego();
        return jugadorActual == jugadorInicial && !finDeJuego && ejecutando;
    }

    /**
     * Convierte el texto fila,columna escrito con números del 1 al 10 en una
     * coordenada que inicia en cero
     *
     * @param entrada texto escrito por el jugador, separado por coma o por
     * espacio
     * @return coordenada interpretada o null si el formato no es válido
     */
    private Coordenada interpretarCoordenada(String entrada) {
        // Separa la entrada por comas o espacios y revisa que sean dos datos
        String[] partes = entrada.split("[,\\s]+");
        if (partes.length != 2) {
            return null;
        }

        // Convierte los números de 1 en adelante a posiciones internas desde cero
        try {
            int fila = Integer.parseInt(partes[0]) - 1;
            int columna = Integer.parseInt(partes[1]) - 1;
            return new Coordenada(fila, columna);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Pide confirmación al jugador y detiene el ciclo del menú si responde que
     * sí
     */
    private void gestionarSalir() {
        boolean confirmado = confirmar("¿Seguro que deseas salir de la partida?");
        if (confirmado) {
            ejecutando = false;
        }
    }

    /**
     * Construye una matriz con el estado de cada casilla del tablero de un
     * jugador
     *
     * @param dueno jugador dueño del tablero que se consulta
     * @return matriz de estados indexada como estados[fila][columna]
     */
    private EstadoCasilla[][] hacerTablero(Jugador dueno) {
        // Crea la matriz con el tamaño del tablero
        int tamano = modelo.obtenerTamanoTablero();
        EstadoCasilla[][] estados = new EstadoCasilla[tamano][tamano];

        // Consulta al modelo el estado de cada casilla
        for (int fila = 0; fila < tamano; fila++) {
            for (int columna = 0; columna < tamano; columna++) {
                Coordenada coordenada = new Coordenada(fila, columna);
                estados[fila][columna] = modelo.obtenerEstadoCasilla(dueno, coordenada);
            }
        }
        return estados;
    }

    /**
     * Hace una pregunta que solo se responde con s o n y repite la pregunta
     * hasta recibir una respuesta válida
     *
     * @param pregunta texto de la pregunta
     * @return true si el jugador respondió s
     */
    private boolean confirmar(String pregunta) {
        String respuesta = "";
        while (ejecutando && !respuesta.equals("s") && !respuesta.equals("n")) {
            imprimirConfirmacion(pregunta);
            respuesta = leerLinea().toLowerCase();
        }
        return respuesta.equals("s");
    }

    /**
     * Lee una línea escrita por el jugador, y si ya no hay entrada detiene el
     * ciclo del menú
     *
     * @return texto leído sin espacios en los extremos, o 0 si ya no hay
     * entrada
     */
    private String leerLinea() {
        if (!scanner.hasNextLine()) {
            ejecutando = false;
            return "0";
        }
        return scanner.nextLine().trim();
    }

    // IMPLEMENTACIÓN IOBSERVADOR
    @Override
    public void actualizar(IModelo model) {
        // Anuncia al ganador si la partida terminó y detiene el ciclo del menú
        if (model.esFinDeJuego()) {
            Jugador ganador = model.obtenerGanador();
            int turnos = model.obtenerCantidadTurnos();
            imprimirVictoria(ganador, turnos);
            ejecutando = false;
            return;
        }

        // Anuncia el cambio de turno cuando el jugador en turno es distinto al que se mostraba
        Jugador jugadorActual = model.obtenerTurnoActual();
        if (jugadorActual != jugadorMostrado) {
            jugadorMostrado = jugadorActual;
            int numeroTurno = model.obtenerCantidadTurnos();
            Turno turno = jugadorActual.getTurno();
            int segundosTotales = turno.getSegundosTotales();
            imprimirCambioTurno(jugadorActual, numeroTurno, segundosTotales);
            imprimirPausa();
            leerLinea();
        }
    }

    // ==================== MÉTODOS DE IMPRESIÓN ====================
    /**
     * Imprime la pantalla de bienvenida
     */
    private void imprimirBienvenida() {
        // Imprime el título del juego
        System.out.println();
        System.out.println(SEPARADOR);
        System.out.println("            BIENVENIDO A BATALLA NAVAL");
        System.out.println(SEPARADOR);
        System.out.print("Presiona ENTER para comenzar...");
    }

    /**
     * Imprime el menú principal con los datos del turno en curso
     *
     * @param jugador que tiene el turno
     * @param numeroTurno en el que va la partida
     * @param segundosRestantes que le quedan al jugador en turno
     * @param disparosTurno realizados en el turno actual
     */
    private void imprimirMenuPrincipal(Jugador jugador, int numeroTurno, int segundosRestantes, int disparosTurno) {
        // Obtiene los datos del perfil del jugador
        Perfil perfil = jugador.getPerfil();
        String nombre = perfil.getNombre();
        Color color = perfil.getColor();

        // Imprime el encabezado del turno
        System.out.println();
        System.out.println(SEPARADOR);
        System.out.println("TURNO " + numeroTurno + " | " + nombre + " [" + color + "]");
        System.out.println("Tiempo restante: " + segundosRestantes + " s | Disparos en este turno: " + disparosTurno);
        System.out.println(SEPARADOR);

        // Imprime las opciones del menú
        System.out.println("1. Ver mis navíos");
        System.out.println("2. Ver mi tablero");
        System.out.println("3. Ver tablero del rival");
        System.out.println("4. Disparar");
        System.out.println("5. Salir");
        System.out.print("Elige una opción: ");
    }

    /**
     * Imprime la lista de navíos con su tamaño, impactos y estado
     *
     * @param navios que se van a mostrar
     */
    private void imprimirNavios(List<Navio> navios) {
        // Imprime el encabezado con la cantidad de navíos
        System.out.println();
        System.out.println("--- TUS NAVÍOS ACTIVOS (" + navios.size() + ") ---");

        // Imprime los navíos
        for (int i = 0; i < navios.size(); i++) {
            Navio navio = navios.get(i);
            TipoNavio tipo = navio.getTipo();
            int casillas = navio.getNumCasillas();
            int impactos = navio.getImpactos();
            EstadoNavio estado = navio.estadoNavio();
            String formato = "%2d. %-13s %d casilla(s) | impactos %d/%d | %s";
            System.out.println(String.format(formato, i + 1, tipo, casillas, impactos, casillas, estado));
        }
    }

    /**
     * Imprime un tablero como una cuadrícula con números de fila y de columna y
     * especifica los símbolos empleados
     *
     * @param titulo que se muestra sobre el tablero
     * @param estados de cada casilla
     * @param mostrarNavios para revelar los navíos sin disparar u ocultarlos
     */
    private void imprimirTablero(String titulo, EstadoCasilla[][] estados, boolean mostrarNavios) {
        // Imprime el título y los números de columna
        System.out.println();
        System.out.println("--- " + titulo + " ---");
        StringBuilder encabezado = new StringBuilder("    ");
        for (int columna = 0; columna < estados.length; columna++) {
            encabezado.append(String.format("%3d", columna + 1));
        }
        System.out.println(encabezado);

        // Imprime cada fila con su número y los símbolos de sus casillas
        for (int fila = 0; fila < estados.length; fila++) {
            StringBuilder renglon = new StringBuilder(String.format("%3d ", fila + 1));
            for (int columna = 0; columna < estados[fila].length; columna++) {
                String simbolo = obtenerSimbolo(estados[fila][columna], mostrarNavios);
                renglon.append("  ").append(simbolo);
            }
            System.out.println(renglon);
        }

        // Imprime el significado de los símbolos
        String leyenda = "Leyenda: . sin disparar | o agua | X impacto | # hundido";
        System.out.println(mostrarNavios ? leyenda + " | N navío" : leyenda);
    }

    /**
     * Pone un símbolo String según el estado de la casilla
     *
     * @param estado de la casilla
     * @param mostrarNavios si se revelan los navíos sin disparar
     * @return símbolo String
     */
    private String obtenerSimbolo(EstadoCasilla estado, boolean mostrarNavios) {
        return switch (estado) {
            case AGUA ->
                "o";
            case IMPACTO ->
                "X";
            case HUNDIDO ->
                "#";
            case NAVIO ->
                mostrarNavios ? "N" : ".";
            default ->
                ".";
        };
    }

    /**
     * Imprime cuántos navíos le quedan al rival
     *
     * @param cantidad navíos rivales que siguen a flote
     */
    private void imprimirNaviosRestantes(int cantidad) {
        System.out.println("Navíos del rival que siguen a flote: " + cantidad);
    }

    /**
     * Imprime la petición de una coordenada para disparar
     */
    private void imprimirSolicitudCoordenada() {
        System.out.print("Coordenada como fila,columna (ejemplo 3,5) o 0 para regresar: ");
    }

    /**
     * Imprime el aviso de cambio de turno
     *
     * @param jugador jugador que recibe el turno
     * @param numeroTurno número del nuevo turno
     * @param segundosTotales segundos que dura el turno
     */
    private void imprimirCambioTurno(Jugador jugador, int numeroTurno, int segundosTotales) {
        Perfil perfil = jugador.getPerfil();
        String nombre = perfil.getNombre();
        Color color = perfil.getColor();

        System.out.println();
        System.out.println(SEPARADOR);
        System.out.println("CAMBIO DE TURNO");
        System.out.println("Turno #" + numeroTurno + ": " + nombre + " [" + color + "]");
        System.out.println("Tienes " + segundosTotales + " segundos para jugar tu turno");
        System.out.println(SEPARADOR);
    }

    /**
     * Imprime la petición de presionar ENTER para continuar
     */
    private void imprimirPausa() {
        System.out.print("Presiona ENTER para continuar...");
    }

    /**
     * Imprime el anuncio del ganador de la partida
     *
     * @param ganador jugador que ganó
     * @param turnos cantidad de turnos que duró la partida
     */
    private void imprimirVictoria(Jugador ganador, int turnos) {
        Perfil perfil = ganador.getPerfil();
        String nombre = perfil.getNombre();
        Color color = perfil.getColor();

        System.out.println();
        System.out.println(SEPARADOR);
        System.out.println("¡FIN DE LA PARTIDA!");
        System.out.println("Ganador: " + nombre + " [" + color + "]");
        System.out.println("La partida duró " + turnos + " turnos");
        System.out.println(SEPARADOR);
    }

    /**
     * Imprime una pregunta de confirmación que se responde con s o n
     *
     * @param pregunta a hacer
     */
    private void imprimirConfirmacion(String pregunta) {
        System.out.print(pregunta + " (s/n): ");
    }

    /**
     * Imprime un mensaje de error
     *
     * @param mensaje del error que se muestra
     */
    private void imprimirError(String mensaje) {
        System.out.println("¡" + mensaje + "!");
    }

    /**
     * Imprime la despedida del juego
     */
    private void imprimirDespedida() {
        System.out.println();
        System.out.println("Hasta la próxima");
    }
}
