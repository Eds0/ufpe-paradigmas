package lf3.plp.functional3.util;

import lf3.plp.expressions1.util.Tipo;
import lf3.plp.functional1.util.TipoPolimorfico;

/**
 * Representa um tipo Property usado para property-based testing
 */
public class TipoProperty implements Tipo {

    /**
     * Subtipo - tipo de retorno da propriedade (sempre booleano)
     */
    private Tipo tipoRetorno;
    
    /**
     * Tipo do argumento da propriedade
     */
    private Tipo tipoArgumento;

    /**
     * Cria uma instância de TipoProperty com tipos específicos
     * 
     * @param tipoArgumento Tipo do argumento da propriedade
     */
    public TipoProperty(Tipo tipoArgumento) {
        this.tipoArgumento = tipoArgumento;
        // O tipo de retorno de uma propriedade é sempre booleano
        this.tipoRetorno = lf3.plp.expressions1.util.TipoPrimitivo.BOOLEANO;
    }

    /**
     * Cria uma instância de TipoProperty com tipo polimórfico
     */
    public TipoProperty() {
        this.tipoArgumento = new TipoPolimorfico();
        this.tipoRetorno = lf3.plp.expressions1.util.TipoPrimitivo.BOOLEANO;
    }

    /**
     * Retorna o tipo do argumento da propriedade
     * 
     * @return tipo do argumento da propriedade
     */
    public Tipo getTipoArgumento() {
        return tipoArgumento;
    }

    /**
     * Retorna o tipo de retorno da propriedade (sempre booleano)
     * 
     * @return tipo de retorno da propriedade
     */
    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    @Override
    public boolean eBooleano() {
        return false;
    }

    @Override
    public boolean eIgual(Tipo tipo) {
        if (tipo instanceof TipoProperty) {
            TipoProperty outraProperty = (TipoProperty) tipo;
            return outraProperty.tipoArgumento.eIgual(this.tipoArgumento) && 
                   outraProperty.tipoRetorno.eIgual(this.tipoRetorno);
        }
        return tipo.eIgual(this);
    }

    @Override
    public boolean eInteiro() {
        return false;
    }

    @Override
    public boolean eString() {
        return false;
    }

    @Override
    public boolean eValido() {
        return tipoArgumento.eValido() && tipoRetorno.eValido();
    }

    @Override
    public String getNome() {
        return "Property<" + tipoArgumento.getNome() + ">";
    }

    @Override
    public Tipo intersecao(Tipo outroTipo) {
        if (outroTipo instanceof TipoProperty) {
            TipoProperty outraProperty = (TipoProperty) outroTipo;
            Tipo tipoArgumentoIntersecao = this.tipoArgumento.intersecao(outraProperty.tipoArgumento);
            
            if (tipoArgumentoIntersecao != null) {
                return new TipoProperty(tipoArgumentoIntersecao);
            }
            return null;
        }
        return outroTipo.intersecao(this);
    }

    @Override
    public String toString() {
        return "Property<" + tipoArgumento.getNome() + ">";
    }
} 