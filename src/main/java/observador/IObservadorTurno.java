package observador;

import interfaces.IModelo;

/**
 * Contrato de una clase que se suscribe a otra
 * @author aaron
 */
public interface IObservadorTurno {

    /**
     * Se actualiza según observa
     * @param model modelo con el estado más reciente de la partida
     */
    void actualizar(IModelo model);
}