/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.batallanaval_equipo1;

import controladores.JuegoController;
import enumeradores.Color;
import enumeradores.TipoNavio;
import java.util.Random;
import modelos.Casilla;
import modelos.Coordenada;
import modelos.Jugador;
import modelos.Navio;
import modelos.Partida;
import modelos.Perfil;
import modelos.Tablero;
import vistas.VistaTerminal;

/**
 *
 * @author Andre
 */
public class BatallaNaval_equipo1 {
    private static final int LIMITE_SEGUNDOS_TURNO = 30;

    public static void main(String[] args) {
        // Crea los perfiles y los jugadores
        Perfil perfil1 = new Perfil("Jugador 1", Color.ROJO);
        Perfil perfil2 = new Perfil("Jugador 2", Color.AZUL);
        Jugador jugador1 = new Jugador(perfil1);
        Jugador jugador2 = new Jugador(perfil2);

        // Despliega la flota de ambos jugadores directamente
        Random random = new Random();
        desplegarFlota(jugador1, random);
        desplegarFlota(jugador2, random);

        // Crea instancias para inyectarlas
        Partida partida = new Partida(jugador1, jugador2, LIMITE_SEGUNDOS_TURNO);
        JuegoController controlador = new JuegoController(partida);
        VistaTerminal vista = new VistaTerminal(controlador, partida);

        // Arranca el juego
        partida.agregarObservador(vista);
        vista.iniciar();
    }

    /**
     * Coloca directamente todos los navíos en el tablero del jugador validando límites y adyacencias
     */
    private static void desplegarFlota(Jugador jugador, Random random) {
        Tablero tablero = jugador.getTablero();

        for (TipoNavio tipo : TipoNavio.values()) {
            for (int i = 0; i < tipo.getCantidadInicial(); i++) {
                Navio navio = new Navio(tipo);
                boolean colocado = false;

                while (!colocado) {
                    boolean horizontal = random.nextBoolean();
                    int filaInicio = random.nextInt(Tablero.TAMANO);
                    int colInicio = random.nextInt(Tablero.TAMANO);
                    int numCasillas = navio.getNumCasillas();

                    boolean esValido = true;

                    // Validar límites y que no toque a ningún otro navío (3x3)
                    for (int k = 0; k < numCasillas; k++) {
                        int f = filaInicio + (horizontal ? 0 : k);
                        int c = colInicio + (horizontal ? k : 0);

                        Coordenada coord = new Coordenada(f, c);

                        // Si se sale del tablero
                        if (!tablero.coordenadaValida(coord)) {
                            esValido = false;
                            break;
                        }

                        // Revisar las 8 casillas vecinas y la central
                        for (int df = -1; df <= 1; df++) {
                            for (int dc = -1; dc <= 1; dc++) {
                                Coordenada vecina = new Coordenada(f + df, c + dc);
                                if (tablero.coordenadaValida(vecina)) {
                                    if (tablero.obtenerCasilla(vecina).contieneNavio()) {
                                        esValido = false;
                                        break;
                                    }
                                }
                            }
                            if (!esValido) break;
                        }
                        if (!esValido) break;
                    }

                    // Si es válido, asignar el navío directamente a la casilla
                    if (esValido) {
                        for (int k = 0; k < numCasillas; k++) {
                            int f = filaInicio + (horizontal ? 0 : k);
                            int c = colInicio + (horizontal ? k : 0);
                            Casilla casilla = tablero.obtenerCasilla(new Coordenada(f, c));
                            casilla.setReferenciaNavio(navio);
                        }

                        // Agregar navío a los activos del jugador
                        jugador.getNaviosActivos().add(navio);
                        colocado = true;
                    }
                }
            }
        }
    }
}
