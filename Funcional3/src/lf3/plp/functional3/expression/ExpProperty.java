package lf3.plp.functional3.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lf3.plp.expressions1.util.Tipo;
import lf3.plp.expressions1.util.TipoPrimitivo;
import lf3.plp.expressions2.expression.Expressao;
import lf3.plp.expressions2.expression.Id;
import lf3.plp.expressions2.expression.Valor;
import lf3.plp.expressions2.expression.ValorBooleano;
import lf3.plp.expressions2.expression.ValorInteiro;
import lf3.plp.expressions2.memory.AmbienteCompilacao;
import lf3.plp.expressions2.memory.AmbienteExecucao;
import lf3.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf3.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf3.plp.functional1.util.TipoFuncao;
import lf3.plp.functional2.expression.ValorFuncao;
import lf3.plp.functional3.util.TipoLista;
import lf3.plp.functional3.util.TipoProperty;

/**
 * Expressão que representa um teste baseado em propriedades (property-based testing)
 */
public class ExpProperty implements Expressao {
    
    private static int QUANTIDADE_TESTES_PADRAO = 100;
    private static int quantidadeTestes = QUANTIDADE_TESTES_PADRAO;

    /**
     * Expressão que deve ser uma função que retorna booleano
     */
    private Expressao propriedade;
    
    /**
     * Tipo dos valores a serem gerados para teste
     */
    private Tipo tipoEntrada;

    /**
     * Construtor
     * @param propriedade Função que será testada (deve retornar booleano)
     * @param tipoEntrada Tipo dos valores de entrada a serem gerados
     */
    public ExpProperty(Expressao propriedade, Tipo tipoEntrada) {
        this.propriedade = propriedade;
        this.tipoEntrada = tipoEntrada;
    }

    /**
     * Define globalmente a quantidade de testes a serem executados por propriedade
     * @param quantidade Nova quantidade de testes
     */
    public static void setQuantidadeTestes(int quantidade) {
        if(quantidade > 0) {
            quantidadeTestes = quantidade;
        }
    }

    /**
     * Retorna a quantidade de testes configurada
     * @return Quantidade de testes
     */
    public static int getQuantidadeTestes() {
        return quantidadeTestes;
    }

    /**
     * Reseta a quantidade de testes para o valor padrão
     */
    public static void resetQuantidadeTestes() {
        quantidadeTestes = QUANTIDADE_TESTES_PADRAO;
    }

    @Override
    public Valor avaliar(AmbienteExecucao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        // Avalia a propriedade para obter a função a ser testada
        Valor valorPropriedade = this.propriedade.avaliar(ambiente);
        
        if (!(valorPropriedade instanceof ValorFuncao)) {
            throw new RuntimeException("A propriedade deve ser uma função que retorna um booleano");
        }
        
        ValorFuncao funcao = (ValorFuncao) valorPropriedade;
        
        // Cria uma lista para armazenar os resultados dos testes
        List<String> resultados = new ArrayList<>();
        boolean todosPassaram = true;
        int numeroDeTestes = quantidadeTestes;
        
        // Executa os testes com valores gerados aleatoriamente
        for (int i = 0; i < numeroDeTestes; i++) {
            // Gera um valor de entrada com base no tipo de entrada
            Valor entrada = gerarValorAleatorio(tipoEntrada);
            
            // Exibe a entrada gerada para o teste atual
            System.out.println("Teste " + (i + 1) + ": Entrada gerada = " + entrada);
            
            // Cria uma lista de parâmetros para chamar a função
            List<Expressao> parametros = new ArrayList<>();
            parametros.add(entrada);
            
            // Cria um ambiente temporário para a aplicação da função
            AmbienteExecucao ambienteTemp = ambiente.clone();
            
            // Simula a aplicação da função manualmente
            // Associa os parâmetros aos IDs da função
            List<Id> listaIds = funcao.getListaId();
            if (listaIds.size() != parametros.size()) {
                throw new RuntimeException("Número incorreto de parâmetros para a propriedade");
            }
            
            for (int j = 0; j < listaIds.size(); j++) {
                Id id = listaIds.get(j);
                Expressao expr = parametros.get(j);
                Valor valorParametro = expr.avaliar(ambiente);
                ambienteTemp.map(id, valorParametro);
            }
            
            // Avalia a expressão da função no ambiente com os parâmetros mapeados
            Valor resultado = funcao.getExp().avaliar(ambienteTemp);
            
            if (!(resultado instanceof ValorBooleano)) {
                throw new RuntimeException("A propriedade deve retornar um valor booleano");
            }
            
            ValorBooleano resultadoBooleano = (ValorBooleano) resultado;
            
            // Verifica se o teste passou
            if (!resultadoBooleano.valor()) {
                todosPassaram = false;
                resultados.add("Falha no teste " + (i + 1) + " com entrada: " + entrada);
                // Interrompe os testes após encontrar a primeira falha
                break;
            }
        }
        
        if (todosPassaram) {
            System.out.println("Todos os " + numeroDeTestes + " testes passaram para a propriedade!");
            return new ValorBooleano(true);
        } else {
            for (String resultado : resultados) {
                System.out.println(resultado);
            }
            return new ValorBooleano(false);
        }
    }

    /**
     * Gera um valor aleatório com base no tipo especificado
     * @param tipo Tipo do valor a ser gerado
     * @return Valor gerado aleatoriamente
     */
    private Valor gerarValorAleatorio(Tipo tipo) {
        Random random = new Random();
        
        if (tipo.eInteiro()) {
            // Gera um valor inteiro aleatório entre -100 e 100
            return new ValorInteiro(random.nextInt(201) - 100);
        } else if (tipo.eBooleano()) {
            // Gera um valor booleano aleatório
            return new ValorBooleano(random.nextBoolean());
        } else if (tipo instanceof TipoLista) {
            // Gera uma lista aleatória
            TipoLista tipoLista = (TipoLista) tipo;
            Tipo subTipo = tipoLista.getSubTipo();
            
            // Tamanho aleatório da lista entre 0 e 10
            int tamanho = random.nextInt(11);
            
            ValorLista lista = ValorLista.getInstancia(null, null);
            
            // Se a lista não for vazia, preenche com valores aleatórios
            if (tamanho > 0) {
                for (int i = 0; i < tamanho; i++) {
                    lista.cons(gerarValorAleatorio(subTipo));
                }
                return lista.inverter();
            }
            
            return lista;
        }
        
        // Para outros tipos, retorna um valor padrão (não implementado)
        throw new RuntimeException("Geração de valores aleatórios para o tipo " + tipo + " não implementada");
    }

    @Override
    public boolean checaTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        // Verifica se a propriedade é uma função
        if (!propriedade.checaTipo(ambiente)) {
            return false;
        }
        
        Tipo tipoPropriedade = propriedade.getTipo(ambiente);
        
        // Verifica se a propriedade tem um tipo compatível com uma função que retorna booleano
        if (tipoPropriedade instanceof TipoFuncao) {
            TipoFuncao tipoFuncao = (TipoFuncao) tipoPropriedade;
            
            // Verifica se o tipo de retorno da função é booleano
            return tipoFuncao.getImagem().eBooleano();
        }
        
        return false;
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        // O tipo de uma expressão Property é booleano, pois retorna se todos os testes passaram
        return TipoPrimitivo.BOOLEANO;
    }
    
    @Override
    public Expressao reduzir(AmbienteExecucao ambiente) {
        // Property não pode ser reduzida
        return this;
    }
    
    @Override
    public ExpProperty clone() {
        return new ExpProperty(this.propriedade, this.tipoEntrada);
    }
} 