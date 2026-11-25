package controladores;

import enumeradores.EstadoNavio;
import modelos.Casilla;
import modelos.Coordenada;
import modelos.Disparo;
import modelos.Jugador;
import modelos.Navio;
import modelos.Partida;

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

        // Si la casilla tiene un navío
        if (casilla.contieneNavio()) {
            partida.marcarCasillaNavio(coordenada);
            Navio navio = casilla.getReferenciaNavio();
            disparo.setImpactoNavio(true);
            disparo.setNavioImpactado(navio);
            reiniciarContador();

            //  de habilidad
            // Si el impacto hundió el navío, el rival lo pierde de su flota activa
            if (navio.estadoNavio() == EstadoNavio.HUNDIDO) {
                rival.agregarNavioDestruido(navio);
            }
        }

        // ahora se usa el metodo privado porque disparo y habilidad comparten lógica
        partida.agregarDisparoTurno(disparo);
        resolverResultadoDisparo(disparo, jugadorActual, rival);

    }

    /**
     * Procesa el uso de la habilidad del jugador en turno: valida el tiempo, que
     * la habilidad esté disponible y que la casilla se pueda disparar, la usa y
     * decide si hay ganador o si cambia el turno. Si el navío se hunde el jugador
     * conserva el turno, si cae en agua el turno pasa al rival
     *
     * @param coordenada del tablero rival sobre la que se usa la habilidad
     */
    public void usarHabilidad(Coordenada coordenada) {
        // Valida que el jugador aún tenga tiempo, que tenga la habilidad y que la casilla se pueda disparar
        Jugador jugadorActual = partida.obtenerTurnoActual();
        if (!partida.validarTurnoJugador(jugadorActual)) {
            throw new IllegalStateException("Se agotó el tiempo de tu turno, la habilidad no se usó");
        }
        if (!partida.esHabilidadDisponible()) {
            throw new IllegalArgumentException("No tienes la habilidad disponible, consigue 3 aciertos seguidos en un turno");
        }
        validarCasillaSeleccionada(coordenada);

        // Usa la habilidad y decide si hay ganador, si se conserva el turno o si pasa al rival
        Jugador rival = partida.obtenerRival(jugadorActual);
        Disparo disparo = partida.usarHabilidad(coordenada);
        if (disparo.isImpactoNavio()) {
            reiniciarContador();
        }
        resolverResultadoDisparo(disparo, jugadorActual, rival);
    }

    /**
     * Decide qué sigue después de un disparo: gana el jugador si el rival se
     * quedó sin navíos, conserva el turno si impactó o pasa el turno si falló
     *
     * @param disparo disparo que se acaba de registrar
     * @param jugadorActual jugador que disparó
     * @param rival rival del jugador que disparó
     */
    private void resolverResultadoDisparo(Disparo disparo, Jugador jugadorActual, Jugador rival) {
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
}