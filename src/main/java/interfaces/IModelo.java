package interfaces;

import enumeradores.EstadoCasilla;
import modelos.Coordenada;
import modelos.Disparo;
import modelos.Jugador;
import modelos.Navio;
import java.util.List;

/**
 * Interfaz de modelo que expone a las vistas los datos de la partida en modo lectura
 * @author aaron
 */
public interface IModelo {

    /**
     * Obtiene el estado de una casilla dentro del tablero de un jugador
     * @param jugador dueño del tablero que se consulta
     * @param coordenada posición de la casilla dentro del tablero
     * @return estado actual de la casilla
     */
    EstadoCasilla obtenerEstadoCasilla(Jugador jugador, Coordenada coordenada);

    /**
     * Obtiene el último disparo realizado en la partida
     * @return último disparo o null si todavía no hay ninguno
     */
    Disparo obtenerUltimoDisparo();

    /**
     * Obtiene al jugador que tiene el turno en este momento
     * @return jugador en turno
     */
    Jugador obtenerTurnoActual();

    /**
     * Indica si la partida ya terminó
     * @return true si hay un ganador
     */
    boolean esFinDeJuego();

    /**
     * Obtiene al ganador de la partida
     * @return jugador ganador o null si la partida sigue en curso
     */
    Jugador obtenerGanador();

    /**
     * Obtiene los navíos que aún siguen a flote del jugador en turno
     * @return lista de navíos activos del jugador en turno
     */
    List<Navio> obtenerNaviosActivos();

    /**
     * CLAUDE
     * Obtiene al rival del jugador que tiene el turno, la vista lo necesita para mostrar su tablero
     * @return jugador rival del jugador en turno
     */
    Jugador obtenerRivalActual();

    /**
     * CLAUDE
     * Obtiene el tamaño del tablero, que es el mismo número de filas y de columnas
     * @return cantidad de filas y columnas del tablero
     */
    int obtenerTamanoTablero();

    /**
     * CLAUDE
     * Obtiene el número de turno en el que va la partida
     * @return cantidad de turnos transcurridos incluyendo el actual
     */
    int obtenerCantidadTurnos();

    /**
     * CLAUDE
     * Obtiene los segundos que le quedan al jugador en turno
     * @return segundos restantes del turno actual
     */
    int obtenerSegundosRestantes();
}