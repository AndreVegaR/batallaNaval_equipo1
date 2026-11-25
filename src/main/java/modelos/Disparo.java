package modelos;

/**
 * Modelo que representa un disparo hecho a una coordenada y su resultado xdd
 * @author aaron
 */
public class Disparo {

    private Coordenada casillaDisparada;
    private boolean impactoNavio;
    private Navio navioImpactado;
    private boolean esHabilidad;

    /**
     * Construye un disparo que por defecto cae en agua y no es de habilidad
     * @param casillaDisparada coordenada a la que se dispara
     */
    public Disparo(Coordenada casillaDisparada) {
        this.casillaDisparada = casillaDisparada;
        this.impactoNavio = false;
        this.navioImpactado = null;
        this.esHabilidad = false;
    }

    public Coordenada getCasillaDisparada() {
        return casillaDisparada;
    }

    public boolean isImpactoNavio() {
        return impactoNavio;
    }

    public void setImpactoNavio(boolean impactoNavio) {
        this.impactoNavio = impactoNavio;
    }

    public Navio getNavioImpactado() {
        return navioImpactado;
    }

    public void setNavioImpactado(Navio navioImpactado) {
        this.navioImpactado = navioImpactado;
    }

    /**
     * Indica si el disparo se hizo usando la habilidad
     * @return true si fue un disparo de habilidad
     */
    public boolean isEsHabilidad() {
        return esHabilidad;
    }

    public void setEsHabilidad(boolean esHabilidad) {
        this.esHabilidad = esHabilidad;
    }
}
