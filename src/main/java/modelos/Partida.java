package modelos;

/**
 * Modelo que gestiona la partida, el jugador en turno, los disparos y los observadores suscritos
 * @author Andre
 */
public class Partida {

    private Jugador ganador;
    private int limiteSegundosTurno;
    private Jugador turnoJugador;
    private Jugador jugador1;
    private Jugador jugador2;
    private int cantidadTurnos;

    /**
     * Constructor
     * @param jugador1 primer jugador, es quien inicia
     * @param jugador2 segundo jugador
     * @param limiteSegundosTurno segundos que dura cada turno
     */
    public Partida(Jugador jugador1, Jugador jugador2, int limiteSegundosTurno) {
        // Guarda a los jugadores y el límite de tiempo
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.limiteSegundosTurno = limiteSegundosTurno;
        
        // Configura el límite de tiempo en el turno de cada jugador
        Turno turno1 = jugador1.getTurno();
        Turno turno2 = jugador2.getTurno();
        turno1.setSegundosTotales(limiteSegundosTurno);
        turno2.setSegundosTotales(limiteSegundosTurno);
    }
}