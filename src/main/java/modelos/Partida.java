package modelos;

import enumeradores.EstadoCasilla;
import enumeradores.EstadoPartida;
import interfaces.IModelo;
import java.util.ArrayList;
import java.util.List;
import observador.IObservableTurno;
import observador.IObservadorTurno;

/**
 * Modelo que gestiona la partida, el jugador en turno, los disparos y los observadores suscritos
 * @author Andre
 */
public class Partida implements IModelo, IObservableTurno{

    private EstadoPartida estado;
    private List<IObservadorTurno> observadores;
    private Jugador ganador;
    private int limiteSegundosTurno;
    private Jugador turnoJugador;
    private Jugador jugador1; 
    private Jugador jugador2; 
    private int cantidadTurnos; 
    private Disparo ultimoDisparo; 

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

        // Deja la partida lista para que inicie el primer jugador
        this.estado = EstadoPartida.EN_CURSO;
        this.observadores = new ArrayList<>();
        this.ganador = null;
        this.ultimoDisparo = null;
        this.turnoJugador = jugador1;
        this.cantidadTurnos = 1;

        // Configura el límite de tiempo en el turno de cada jugador
        Turno turno1 = jugador1.getTurno();
        Turno turno2 = jugador2.getTurno();
        turno1.setSegundosTotales(limiteSegundosTurno);
        turno2.setSegundosTotales(limiteSegundosTurno);
    }
    
    /**
     * Declara ganador a un jugador, termina la partida y avisa a los observadores
     * @param jugadorGanador jugador que ganó la partida
     */
    public void darVictoriaJugador(Jugador jugadorGanador) {
        ganador = jugadorGanador;
        estado = EstadoPartida.FINALIZADA;
        notificarObservadores();
    }
    
    /**
     * Pasa el turno a otro jugador, avisa a los observadores y después reinicia el contador de tiempo
     * @param jugador jugador que recibe el turno
     */
    public void cambiarJugador(Jugador jugador) {
        turnoJugador = jugador;
        cantidadTurnos++;
        notificarObservadores();
        reiniciarContador();
    }
    
    /**
     * Valida que sea el turno del jugador, que la partida siga en curso y que no se agote su tiempo
     * @param jugador jugador que quiere realizar una acción
     * @return true si el jugador puede actuar
     */
    public boolean validarTurnoJugador(Jugador jugador) {
        if (estado != EstadoPartida.EN_CURSO || jugador != turnoJugador) {
            return false;
        }
        Turno turno = jugador.getTurno();
        return !turno.tiempoAgotado();
    }
    
    /**
     * Valida que la casilla esté dentro del tablero del rival y que no haya sido disparada antes
     * @param coordenada casilla que el jugador en turno quiere disparar
     * @return true si la casilla se puede disparar
     */
    public boolean validarCasillaSeleccionada(Coordenada coordenada) {
        // Obtiene el tablero del rival, que es el que recibe los disparos
        Jugador rival = obtenerRival(turnoJugador);
        Tablero tablero = rival.getTablero();

        // Descarta las coordenadas fuera del tablero
        if (!tablero.coordenadaValida(coordenada)) {
            return false;
        }

        // Descarta las casillas que ya fueron disparadas
        Casilla casilla = tablero.obtenerCasilla(coordenada);
        return !casilla.isDisparada();
    }
    
    /**
     * Registra un disparo en el turno actual, lo guarda como el último y avisa a los observadores
     * @param disparo disparo realizado por el jugador en turno
     * @return el mismo disparo registrado
     */
    public Disparo agregarDisparoTurno(Disparo disparo) {
        Turno turno = turnoJugador.getTurno();
        turno.agregarDisparo(disparo);
        ultimoDisparo = disparo;
        notificarObservadores();
        return disparo;
    }
    
    /**
     * Marca como disparada una casilla del tablero del rival
     * @param coordenada casilla que se dispara
     * @return casilla del rival que quedó marcada
     */
    public Casilla marcarCasillaDisparada(Coordenada coordenada) {
        Jugador rival = obtenerRival(turnoJugador);
        Tablero tablero = rival.getTablero();
        Casilla casilla = tablero.obtenerCasilla(coordenada);
        casilla.setDisparada(true);
        return casilla;
    }
    
    /**
     * Registra el impacto en el navío que ocupa una casilla del tablero del rival, si lo hay
     * @param coordenada casilla que fue disparada
     * @return casilla del rival revisada
     */
    public Casilla marcarCasillaNavio(Coordenada coordenada) {
        // Obtiene la casilla del tablero del rival
        Jugador rival = obtenerRival(turnoJugador);
        Tablero tablero = rival.getTablero();
        Casilla casilla = tablero.obtenerCasilla(coordenada);

        // Solo se impacta el navío cuando la casilla lo contiene
        if (casilla.contieneNavio()) {
            casilla.impactarNavio();
        }
        return casilla;
    }
    
    /** Reinicia el contador de tiempo del jugador que tiene el turno */
    public void reiniciarContador() {
        Turno turno = turnoJugador.getTurno();
        turno.reiniciarContador();
    }
    
    /**
     * Busca al jugador que se quedó sin navíos activos, es decir al que perdió
     * @return jugador sin navíos o null si ambos todavía tienen navíos
     */
    public Jugador obtenerNaviosFaltante() {
        // Revisa si el primer jugador perdió toda su flota
        List<Navio> activos1 = jugador1.getNaviosActivos();
        if (activos1.isEmpty()) {
            return jugador1;
        }

        // Revisa si el segundo jugador perdió toda su flota
        List<Navio> activos2 = jugador2.getNaviosActivos();
        if (activos2.isEmpty()) {
            return jugador2;
        }

        // Ambos jugadores todavía tienen navíos
        return null;
    }
    
    /**
     * Obtiene al rival de un jugador
     * @param jugador jugador del que se busca el rival
     * @return el otro jugador de la partida
     */
    public Jugador obtenerRival(Jugador jugador) {
        return jugador == jugador1 ? jugador2 : jugador1;
    }
    
    @Override
    public int obtenerCantidadTurnos() {
        return cantidadTurnos;
    }

    @Override
    public EstadoCasilla obtenerEstadoCasilla(Jugador jugador, Coordenada coordenada) {
        Tablero tablero = jugador.getTablero();
        Casilla casilla = tablero.obtenerCasilla(coordenada);
        return casilla.estadoCasilla();
    }
    
    @Override
    public Disparo obtenerUltimoDisparo() {
        return ultimoDisparo;
    }

    @Override
    public Jugador obtenerTurnoActual() {
        return turnoJugador;
    }

    @Override
    public boolean esFinDeJuego() {
        return estado == EstadoPartida.FINALIZADA;
    }

    @Override
    public Jugador obtenerGanador() {
        return ganador;
    }

    @Override
    public List<Navio> obtenerNaviosActivos() {
        return turnoJugador.getNaviosActivos();
    }

    @Override
    public Jugador obtenerRivalActual() {
        return obtenerRival(turnoJugador);
    }

    @Override
    public int obtenerTamanoTablero() {
        return Tablero.TAMANO;
    }

    @Override
    public int obtenerSegundosRestantes() {
        Turno turno = turnoJugador.getTurno();
        return turno.obtenerSegundosRestantes();
    }

    @Override
    public void agregarObservador(IObservadorTurno observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(IObservadorTurno observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        List<IObservadorTurno> copia = new ArrayList<>(observadores);
        for (IObservadorTurno observador : copia) {
            observador.actualizar(this);
        }
    }

    
    
}