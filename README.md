# PLP - Property-based Testing Nativo

**Alunos:**  
- Dayvison Oliveira Silva  
- Francisco Edson Gomes de Morais Júnior

---

## 🎯 BNF baseado na linguagem imperativa II com suporte a Property-Based Testing

```bnf
Programa ::= Comando

Comando ::= Atribuicao
          | ComandoDeclaracao
          | While
          | IfThenElse
          | IO
          | Comando ";" Comando
          | Skip
          | ChamadaProcedimento
          | Property

Skip ::= /* palavra reservada 'skip' */

Atribuicao ::= Id ":=" Expressao

Expressao ::= Valor 
            | ExpUnaria 
            | ExpBinaria 
            | Id

Valor ::= ValorConcreto

ValorConcreto ::= ValorInteiro 
                | ValorBooleano 
                | ValorString

ExpUnaria ::= "-" Expressao 
            | "not" Expressao 
            | "length" Expressao

ExpBinaria ::= Expressao "+" Expressao
             | Expressao "-" Expressao
             | Expressao "*" Expressao
             | Expressao "/" Expressao
             | Expressao "and" Expressao
             | Expressao "or" Expressao
             | Expressao "==" Expressao
             | Expressao "++" Expressao

ComandoDeclaracao ::= "{" Declaracao ";" Comando "}"

Declaracao ::= DeclaracaoVariavel 
             | DeclaracaoProcedimento 
             | DeclaracaoComposta

DeclaracaoVariavel ::= "var" Id "=" Expressao 

DeclaracaoComposta ::= Declaracao "," Declaracao

DeclaracaoProcedimento ::= "proc" Id "(" [ ListaDeclaracaoParametro ] ")" "{" Comando "}"

ListaDeclaracaoParametro ::= Tipo Id 
                           | Tipo Id "," ListaDeclaracaoParametro

Tipo ::= "string" | "int" | "boolean"

While ::= "while" Expressao "do" Comando

IfThenElse ::= "if" Expressao "then" Comando "else" Comando

IO ::= "write" "(" Expressao ")" 
     | "read" "(" Id ")"

ChamadaProcedimento ::= "call" Id "(" [ ListaExpressao ] ")" 

ListaExpressao ::= Expressao 
                 | Expressao "," ListaExpressao

-- ⬇️ Extensão para Property-Based Testing ⬇️

Property ::= "property" Id "(" ListaParametroTeste ")" "{" Comando "}"

ListaParametroTeste ::= ParametroTeste 
                      | ParametroTeste "," ListaParametroTeste

ParametroTeste ::= Tipo Id "from" Gerador

Gerador ::= "int" 
          | "string" 
          | "boolean" 
          | "range" "(" ValorInteiro "," ValorInteiro ")"
