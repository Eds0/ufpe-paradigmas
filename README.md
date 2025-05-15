# PLP - Property-based Testing Nativo

**Alunos:**  
- Dayvison Oliveira Silva  
- Francisco Edson Gomes de Morais Júnior

---
A gramática foi estendida com as expressões [`ExpProperty`](https://github.com/usuario/repositorio/blob/main/caminho/ExpProperty.java) e [`ExpTestConfig`](https://github.com/usuario/repositorio/blob/main/caminho/ExpTestConfig.java), conforme mostrado abaixo:

## 🎯 BNF baseado na linguagem funcional 3 com suporte a Property-Based Testing

```bnf
Programa ::= Expressao

Expressao ::= Valor
            | ExpUnaria
            | ExpBinaria
            | ExpDeclaracao
            | Id
            | Aplicacao
            | IfThenElse
            | ExpProperty        # Nova expressão
            | ExpTestConfig      # Nova expressão

Valor ::= ValorConcreto | ValorAbstrato

ValorAbstrato ::= ValorFuncao

ValorConcreto ::= ValorInteiro 
                | ValorBooleano 
                | ValorString 
                | ValorLista

ValorFuncao ::= "fn" Id Id "." Expressao

ExpUnaria ::= "-" Expressao 
            | "not" Expressao 
            | "length" Expressao
            | head(Expressao) 
            | tail(Expressao)
            | ExpCompreensaoLista

ExpCompreensaoLista ::= Expressao Gerador | Expressao Gerador Filtro

Gerador ::= "for" Id "in" Expressao
          | "for" Id "in" Expressao [","] Gerador

Filtro ::= "if" Expressao

ExpBinaria ::= Expressao "+" Expressao
             | Expressao "-" Expressao
             | Expressao "*" Expressao
             | Expressao ">" Expressao
             | Expressao "<" Expressao
             | Expressao "and" Expressao
             | Expressao "or" Expressao
             | Expressao "==" Expressao
             | Expressao "++" Expressao
             | Expressao ".." Expressao
             | Expressao ":" Expressao
             | Expressao "^^" Expressao

ExpDeclaracao ::= "let" DeclaracaoFuncional "in" Expressao

DeclaracaoFuncional ::= DecVariavel
                      | DecFuncao
                      | DecComposta

DecVariavel ::= "var" Id "=" Expressao

DecFuncao ::= "fun" ListId "=" Expressao

DecComposta ::= DeclaracaoFuncional "," DeclaracaoFuncional

ListId ::= Id | Id "," ListId

Aplicacao ::= Expressao "(" ListExp ")"

ListExp ::= Expressao | Expressao "," ListExp

# Novas produções para Property-Based Testing

ExpProperty ::= "property" "(" Expressao "," TipoTeste ")"

ExpTestConfig ::= "testConfig" "(" Expressao ")"

TipoTeste ::= "int"
            | "boolean"
            | "list"
            | "string"
