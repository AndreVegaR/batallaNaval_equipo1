package enumeradores;

/**
 * Enumerador que define los tipos de navío que existen en el juego
 * @author
 */
public enum TipoNavio {

    // Características del enumerador, formato (casillas, cantidad por jugador)
    PORTAAVIONES(4, 2),
    CRUCERO(3, 3),
    SUBMARINO(2, 4),
    BARCO(1, 3);

    private final int numCasillas;
    private final int cantidadInicial;

    /**
     * Se crea con los datos ingresados
     * @param numCasillas casillas seguidas que ocupa el navío
     * @param cantidadInicial navíos de este tipo que recibe cada jugador al iniciar
     */
    TipoNavio(int numCasillas, int cantidadInicial) {
        this.numCasillas = numCasillas;
        this.cantidadInicial = cantidadInicial;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

    public int getCantidadInicial() {
        return cantidadInicial;
    }
}