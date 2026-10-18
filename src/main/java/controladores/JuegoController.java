package controladores;

import modelos.Coordenada;
import modelos.Jugador;
import modelos.Partida;

/**
 * Controlador que recibe las acciones de la vista y 
 * ordena a la partida y a sus modelos qué hacer
 * @author Andre
 */
public class JuegoController {
    private Partida partida;
    Jugador jugador;

    /**
     * Construye el controlador asociado a una partida
     * @param partida que se va a controlar
     */
    public JuegoController(Partida partida) {
        this.partida = partida;
    }
    

    /**
     * Procesa el disparo del jugador en turno
     * FALTA ALGO PARA DISPAROS XD
     * @param coordenada del tablero rival a la que se dispara
     */
    public void procesarDisparo(Coordenada coordenada) {
        System.out.println("Disparo realizado en la coordenada: " + coordenada.getFila() + ", " + coordenada.getColumna());
    }
}