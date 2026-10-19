package modelos;

import enumeradores.EstadoCasilla;
import enumeradores.EstadoNavio;

/**
 * Modelo que representa una casilla del tablero, puede tener un navío y puede
 * haber sido disparada
 *
 * @author Andre
 * @author Brian
 */
public class Casilla {

    private Coordenada coordenada;
    private Navio referenciaNavio;
    private boolean disparada;

    /**
     * Construye una casilla vacía y sin disparar
     *
     * @param coordenada posición de la casilla dentro del tablero
     */
    public Casilla(Coordenada coordenada) {
        this.coordenada = coordenada;
        this.referenciaNavio = null;
        this.disparada = false;
    }

    /**
     * Indica si la casilla está ocupada por un navío
     *
     * @return true si hay un navío en la casilla
     */
    public boolean contieneNavio() {
        return referenciaNavio != null;
    }

    /**
     * Registra un impacto en el navío que ocupa la casilla
     */
    public void impactarNavio() {
        if (referenciaNavio != null) {
            referenciaNavio.agregarImpacto();
        }
    }

    /**
     * Marca como disparada a la casilla vacía que está junto a un navío hundido
     */
    public void careceAdyacente() {
        if (!contieneNavio()) {
            disparada = true;
        }
    }

    /**
     * Calcula el estado de la casilla, retorna el enumerador en lugar del
     * boolean del diagrama porque IModel necesita distinguir agua, impacto y
     * hundimiento
     *
     * @return estado actual de la casilla
     */
    public EstadoCasilla estadoCasilla() {
        // Una casilla sin disparar solo puede estar vacía u ocupada
        if (!disparada) {
            return contieneNavio() ? EstadoCasilla.NAVIO : EstadoCasilla.VACIA;
        }

        // Una casilla disparada sin navío es agua
        if (!contieneNavio()) {
            return EstadoCasilla.AGUA;
        }

        // Una casilla disparada con navío es impacto o hundimiento según el estado del navío
        EstadoNavio estadoNavio = referenciaNavio.estadoNavio();
        if (estadoNavio == EstadoNavio.HUNDIDO) {
            return EstadoCasilla.HUNDIDO;
        }
        return EstadoCasilla.IMPACTO;
    }

    public Navio getReferenciaNavio() {
        return referenciaNavio;
    }

    public void setReferenciaNavio(Navio referenciaNavio) {
        this.referenciaNavio = referenciaNavio;
    }

    public boolean isDisparada() {
        return disparada;
    }

    public void setDisparada(boolean disparada) {
        this.disparada = disparada;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

}
