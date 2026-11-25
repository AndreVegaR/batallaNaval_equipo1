package modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa el tablero de un jugador como una matriz cuadrada de casillas
 * @author Andre
 */
public class Tablero {

    public static final int TAMANO = 10;

    private List<List<Casilla>> matriz;

    /** Construye un tablero vacío de 10x10 casillas */
    public Tablero() {
        // Inicializa la matriz vacía
        matriz = new ArrayList<>();

        // Crea cada renglón con sus casillas
        for (int fila = 0; fila < TAMANO; fila++) {
            List<Casilla> renglon = new ArrayList<>();
            for (int columna = 0; columna < TAMANO; columna++) {
                Coordenada coordenada = new Coordenada(fila, columna);
                renglon.add(new Casilla(coordenada));
            }
            matriz.add(renglon);
        }
    }

    /**
     * Obtiene la casilla que está en una coordenada, la coordenada debe ser válida
     * @param coordenada posición de la casilla buscada
     * @return casilla ubicada en la coordenada
     */
    public Casilla obtenerCasilla(Coordenada coordenada) {
        List<Casilla> renglon = matriz.get(coordenada.getFila());
        return renglon.get(coordenada.getColumna());
    }

    /**
     * Obtiene todas las casillas que ocupa un navío
     * @param navio navío buscado
     * @return casillas del tablero que referencian a ese navío
     */
    public List<Casilla> obtenerCasillasDeNavio(Navio navio) {
        List<Casilla> casillas = new ArrayList<>();
        for (List<Casilla> renglon : matriz) {
            for (Casilla casilla : renglon) {
                if (casilla.getReferenciaNavio() == navio) {
                    casillas.add(casilla);
                }
            }
        }
        return casillas;
    }

    /**
     * Indica si una coordenada cae dentro de los límites del tablero
     * @param coordenada posición a revisar
     * @return true si la fila y la columna están dentro del tablero
     */
    public boolean coordenadaValida(Coordenada coordenada) {
        int fila = coordenada.getFila();
        int columna = coordenada.getColumna();
        return fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO;
    }
}