package modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa el turno de un jugador con su límite de tiempo y sus disparos
 * @author Andre
 */
public class Turno {

    private int segundosTranscurridos;
    private int segundosTotales;

    // para tener trazabilidad de los disparos
    private long inicioMillis;
    private List<Disparo> disparos;

    /** Construye un turno sin límite de tiempo configurado */
    public Turno() {
        this.segundosTranscurridos = 0;
        this.segundosTotales = 0;
        this.inicioMillis = System.currentTimeMillis();
        this.disparos = new ArrayList<>();
    }

    /** Reinicia el contador de tiempo y limpia los disparos del turno */
    public void reiniciarContador() {
        segundosTranscurridos = 0;
        inicioMillis = System.currentTimeMillis();
        disparos.clear();
    }

    /**
     * Calcula los segundos que han pasado desde que inició el turno
     */
    public void actualizarTranscurridos() {
        long ahora = System.currentTimeMillis();
        long milisegundos = ahora - inicioMillis;
        segundosTranscurridos = (int) (milisegundos / 1000);
    }

    /**
     * Indica si el jugador ya consumió todo el tiempo de su turno,
     * un límite de cero significa sin límite
     * @return true si el tiempo del turno se agotó
     */
    public boolean tiempoAgotado() {
        actualizarTranscurridos();
        return segundosTotales > 0 && segundosTranscurridos >= segundosTotales;
    }

    /**
     * Calcula los segundos que le quedan al turno
     * @return segundos restantes, nunca menor a cero
     */
    public int obtenerSegundosRestantes() {
        actualizarTranscurridos();
        int restantes = segundosTotales - segundosTranscurridos;
        return Math.max(restantes, 0);
    }

    /**
     * Registra un disparo realizado durante este turno
     * @param disparo disparo que se guarda
     */
    public void agregarDisparo(Disparo disparo) {
        disparos.add(disparo);
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

    public List<Disparo> getDisparos() {
        return disparos;
    }
}
