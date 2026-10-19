package controladores;

import enumeradores.EstadoNavio;
import modelos.Casilla;
import modelos.Coordenada;
import modelos.Disparo;
import modelos.Jugador;
import modelos.Navio;
import modelos.Partida;
import modelos.Tablero;

/**
 * Controlador que recibe las acciones de la vista y ordena a la partida y a sus
 * modelos qué hacer
 *
 * @author Andre
 */
public class JuegoController {

    private Partida partida;

    /**
     * Construye el controlador asociado a una partida
     *
     * @param partida que se va a controlar
     */
    public JuegoController(Partida partida) {
        this.partida = partida;
    }

    /**
     * Procesa el disparo del jugador en turno: valida, marca la casilla,
     * registra impactos, hunde navíos y decide si hay ganador o si cambia el
     * turno
     *
     * @param coordenada del tablero rival a la que se dispara
     */
    public void procesarDisparo(Coordenada coordenada) {
        // Valida que el jugador aún tenga tiempo y que la casilla se pueda disparar
        Jugador jugadorActual = partida.obtenerTurnoActual();
        if (!partida.validarTurnoJugador(jugadorActual)) {
            throw new IllegalStateException("Se agotó el tiempo de tu turno, el disparo no se registró");
        }
        validarCasillaSeleccionada(coordenada);

        // Marca la casilla disparada y registra el impacto si había un navío
        Jugador rival = partida.obtenerRival(jugadorActual);
        Casilla casilla = partida.marcarCasillaDisparada(coordenada);
        Disparo disparo = new Disparo(coordenada);

        if (casilla.contieneNavio()) {
            partida.marcarCasillaNavio(coordenada);
            Navio navio = casilla.getReferenciaNavio();
            disparo.setImpactoNavio(true);
            disparo.setNavioImpactado(navio);

            // Si el disparo hundió el navío, lo hunde por completo
            if (navio.estadoNavio() == EstadoNavio.HUNDIDO) {
                hundirNavio(rival, navio);
            }
        }

        // Registra el disparo y decide si hay ganador, si se conserva el turno o si pasa al rival
        partida.agregarDisparoTurno(disparo);
        Jugador sinNavios = partida.obtenerNaviosFaltante();
        if (sinNavios != null) {
            partida.darVictoriaJugador(jugadorActual);
        } else if (!disparo.isImpactoNavio()) {
            partida.cambiarJugador(rival);
        }
    }

    /**
     * Valida que la casilla pueda ser disparada
     *
     * @param coordenada casilla seleccionada por el jugador en turno
     */
    public void validarCasillaSeleccionada(Coordenada coordenada) {
        boolean valida = partida.validarCasillaSeleccionada(coordenada);
        if (!valida) {
            throw new IllegalArgumentException("Casilla no válida, está fuera del tablero o ya fue disparada");
        }
    }

    /**
     * Pasa el turno al rival, la vista lo usa cuando el jugador agota su tiempo
     */
    public void pasarTurno() {
        Jugador jugadorActual = partida.obtenerTurnoActual();
        Jugador rival = partida.obtenerRival(jugadorActual);
        partida.cambiarJugador(rival);
    }

    /**
     * Reinicia el tiempo del jugador en turno, la vista lo usa al arrancar la
     * partida
     */
    public void reiniciarContador() {
        partida.reiniciarContador();
    }

    /**
     * Hunde un navío: lo marca como destruido en el rival y marca como
     * disparadas las casillas que lo rodean
     *
     * @param rival jugador dueño del navío
     * @param navio navío que se hundió
     */
    private void hundirNavio(Jugador rival, Navio navio) {
        // Marca el navío como destruido
        rival.agregarNavioDestruido(navio);
        Tablero tableroRival = rival.getTablero();

        // Busca las casillas del navío y marca sus ocho vecinas
        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            for (int col = 0; col < Tablero.TAMANO; col++) {
                Casilla actual = tableroRival.obtenerCasilla(new Coordenada(fila, col));
                if (actual.getReferenciaNavio() != navio) {
                    continue;
                }
                for (int df = -1; df <= 1; df++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        Coordenada vecina = new Coordenada(fila + df, col + dc);
                        if (tableroRival.coordenadaValida(vecina)) {
                            tableroRival.obtenerCasilla(vecina).careceAdyacente();
                        }
                    }
                }
            }
        }
    }

}