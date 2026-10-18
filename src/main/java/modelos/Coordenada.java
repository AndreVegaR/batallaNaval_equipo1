package modelos;

/**
 * Modelo que representa una posición del tablero con fila y columna,
 * desacoplando así la forma de ubicar las casillas
 * @author Andre Andre
 */
public class Coordenada {

    private final int fila;
    private final int columna;

    /**
     *  Constructor
     * 
     * @param fila
     * @param columna 
     */
    public Coordenada(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
