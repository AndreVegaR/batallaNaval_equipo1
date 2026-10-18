package vistas;

import controladores.JuegoController;
import java.util.Scanner;
import modelos.Jugador;

/**
 * Vista donde se ejecutará el juego
 * IDEA: Pongamos una clase impresora
 * @author Andre
 */
public class VistaTerminal {
    
    //Atributos
    private JuegoController controlador;
    private Scanner scanner;
    private boolean ejecutando;
    private Jugador jugadorMostrado;

    /**
     * Constructor
     * 
     * @param controlador
     */
    public VistaTerminal(JuegoController controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
        this.ejecutando = false;
    }
    
    
    
    /** Arranca el ciclo principal del menú y lo mantiene mientras ejecutando sea verdadero y el juego no haya terminado */
    public void iniciar() {
        // Muestra la bienvenida y espera al jugador antes de empezar
        ejecutando = true;
        imprimirBienvenida();
        leerLinea();


        // CONTADOR Y MODELO AQUÍ
        
        // Mantiene el ciclo del menú principal hasta que se salga o termine la partida
        while (ejecutando) {
            System.out.println("Bienvenido");
            String opcion = leerLinea();

            switch (opcion) {
                case "1" -> gestionarTemporal();
                case "2" -> gestionarTemporal();
                case "3" -> gestionarTemporal();
                case "4" -> gestionarTemporal();
                case "5" -> gestionarTemporal();
                default -> gestionarTemporal();
            }
        }
    }
    
    
    private void gestionarTemporal() {
        System.out.println("xd");
    }
    
    
    private void imprimirBienvenida() {
        System.out.println("Bienvenido a Batalla Naval");
    }
    
    /**
     * Lee una línea escrita por el jugador, y si ya 
     * no hay entrada detiene el ciclo del menú
     * @return texto leído sin espacios en los extremos, o 0 si ya no hay entrada
     */
    private String leerLinea() {
        if (!scanner.hasNextLine()) {
            ejecutando = false;
            return "0";
        }
        return scanner.nextLine().trim();
    }
}