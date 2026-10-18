package modelos;

/**
 * Modelo que representa el turno de un jugador con su límite de tiempo y sus disparos
 * @author Andre
 */
public class Turno {

    private int segundosTranscurridos;
    private int segundosTotales;

    /** Construye un turno sin límite de tiempo configurado */
    public Turno() {
        this.segundosTranscurridos = 0;
        this.segundosTotales = 0;
    }

    /** Reinicia el contador de tiempo y limpia los disparos del turno */
    public void reiniciarContador() {
        
    }
    
    public int getSegundosTranscurridos() {
        return segundosTranscurridos;
    }

    public int getSegundosTotales() {
        return segundosTotales;
    }

    public void setSegundosTotales(int segundosTotales) {
        this.segundosTotales = segundosTotales;
    }
}
