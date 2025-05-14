package lf3.plp.functional3.expression;

import lf3.plp.expressions1.util.Tipo;
import lf3.plp.expressions1.util.TipoPrimitivo;
import lf3.plp.expressions2.expression.Expressao;
import lf3.plp.expressions2.expression.Valor;
import lf3.plp.expressions2.expression.ValorBooleano;
import lf3.plp.expressions2.expression.ValorInteiro;
import lf3.plp.expressions2.memory.AmbienteCompilacao;
import lf3.plp.expressions2.memory.AmbienteExecucao;
import lf3.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf3.plp.expressions2.memory.VariavelNaoDeclaradaException;

/**
 * Expressão que configura a quantidade de testes a serem executados para property-based testing
 */
public class ExpTestConfig implements Expressao {

    /**
     * Expressão que deve avaliar para um valor inteiro representando a quantidade de testes
     */
    private Expressao quantidade;

    /**
     * Construtor
     * @param quantidade Expressão que representa a quantidade de testes
     */
    public ExpTestConfig(Expressao quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public Valor avaliar(AmbienteExecucao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Valor valorQuantidade = quantidade.avaliar(ambiente);
        
        if (!(valorQuantidade instanceof ValorInteiro)) {
            throw new RuntimeException("A quantidade de testes deve ser um valor inteiro");
        }
        
        int valor = ((ValorInteiro) valorQuantidade).valor();
        
        if (valor <= 0) {
            throw new RuntimeException("A quantidade de testes deve ser maior que zero");
        }
        
        // Define a quantidade de testes para as expressões property
        ExpProperty.setQuantidadeTestes(valor);
        
        // Retorna true para indicar que a configuração foi bem-sucedida
        return new ValorBooleano(true);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        return quantidade.checaTipo(ambiente) && quantidade.getTipo(ambiente).eInteiro();
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        // O tipo de retorno é sempre booleano
        return TipoPrimitivo.BOOLEANO;
    }
    
    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        // TestConfig não pode ser reduzida
        return this;
    }
    
    @Override
    public ExpTestConfig clone() {
        return new ExpTestConfig(quantidade.clone());
    }
} 