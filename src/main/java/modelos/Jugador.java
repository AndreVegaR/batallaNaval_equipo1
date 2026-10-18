package modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa a una instancia de perfil dentro de
 * una partida en específico
 * @author Andre
 */
public class Jugador {

    private Perfil perfil;
    private List<Navio> naviosActivos;
    private List<Navio> naviosDestruidos;
    private Tablero tablero;
    private Turno turno;

    /**
     * Constructor
     * 
     * @param perfil del jugador
     */
    public Jugador(Perfil perfil) {
        this.perfil = perfil;
        this.naviosActivos = new ArrayList<>();
        this.naviosDestruidos = new ArrayList<>();
        this.tablero = new Tablero();
        this.turno = new Turno();
    }

    /**
     * Mueve un navío de la lista de activos a la lista de destruidos
     * @param navio navío que acaba de hundirse
     */
    public void agregarNavioDestruido(Navio navio) {
        naviosActivos.remove(navio);
        if (!naviosDestruidos.contains(navio)) {
            naviosDestruidos.add(navio);
        }
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public List<Navio> getNaviosActivos() {
        return naviosActivos;
    }

    public List<Navio> getNaviosDestruidos() {
        return naviosDestruidos;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Turno getTurno() {
        return turno;
    }
}