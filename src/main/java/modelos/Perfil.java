package modelos;

import enumeradores.Color;

/**
 * Modelo que representa el perfil de un jugador con su nombre y su color
 * @author Andre
 */
public class Perfil {

    private String nombre;
    private Color color;

    /**
     * Constructor
     * 
     * @param nombre
     * @param color 
     */
    public Perfil(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}