package modelos;

/**
 * Modelo que representa un disparo hecho a una coordenada y su resultado xdd
 * @author aaron
 */
public class Disparo {

    private Coordenada casillaDisparada;
    private boolean impactoNavio;
    private Navio navioImpactado;

    /**
     * Construye un disparo que por defecto cae en agua
     * @param casillaDisparada coordenada a la que se dispara
     */
    public Disparo(Coordenada casillaDisparada) {
        this.casillaDisparada = casillaDisparada;
        this.impactoNavio = false;
        this.navioImpactado = null;
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
}
