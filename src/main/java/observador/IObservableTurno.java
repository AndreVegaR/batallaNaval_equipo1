package observador;

/**
 * Contrato que administra una clase a quien se suscribe
 * @author aaron
 */
public interface IObservableTurno {

    /**
     * Agrega un observador
     * @param observador observador que recibirá las notificaciones
     */
    void agregarObservador(IObservadorTurno observador);

    /**
     * Quita a un observador
     * @param observador observador que dejará de recibir notificaciones
     */
    void eliminarObservador(IObservadorTurno observador);

    /** Avisa a todos los observadores suscritos que hubo un cambio */
    void notificarObservadores();
}
