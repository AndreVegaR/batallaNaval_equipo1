package modelos;

import enumeradores.TipoNavio;
import enumeradores.EstadoNavio;

/**
 * Modelo que representa un navío con su tipo, su tamaño y los impactos que ha recibido
 * @author Andre
 */
public class Navio {

    private TipoNavio tipo;
    private int numCasillas;
    private int impactos;

    /**
     * Construye un navío sin impactos, su tamaño depende del tipo
     * @param tipo tipo de navío que se crea
     */
    public Navio(TipoNavio tipo) {
        this.tipo = tipo;
        this.numCasillas = tipo.getNumCasillas();
        this.impactos = 0;
    }



    /**
     * Calcula el estado del navío según los impactos que ha recibido
     * @return INTACTO sin impactos, HUNDIDO con todas sus casillas impactadas y AVERIADO en otro caso
     */
    public EstadoNavio estadoNavio() {
        // Sin impactos el navío sigue completo
        if (impactos == 0) {
            return EstadoNavio.INTACTO;
        }

        // Con todas sus casillas impactadas el navío se hunde
        if (impactos >= numCasillas) {
            return EstadoNavio.HUNDIDO;
        }

        // Con impactos parciales el navío está averiado
        return EstadoNavio.AVERIADO;
    }



    /** Suma un impacto al navío sin superar su tamaño */
    public void agregarImpacto() {
        if (impactos < numCasillas) {
            impactos++;
        }
    }

    public TipoNavio getTipo() {
        return tipo;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

    public int getImpactos() {
        return impactos;
    }
}
