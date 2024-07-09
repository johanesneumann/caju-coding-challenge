# Serviço que autoriza transações de cartão

- Desafio tecnico da empresa Caju para a vaga de desenvolvedor backend senior
- [Enunciado do desafio](docs/desafio.txt)

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Junit e Mockito
- Maven
- Docker
- Swagger

## Padrões de arquitetura

- Clean Architecture
- Domain Driven Design
- SOLID

## Como rodar o projeto

- Clone o projeto
- Execute o comando `mvn clean install`
- Execute a classe CardTransactionApplication

## Refinamento das regras de negocio da autorização

1. Receber a transação via HTTP.
2. Substituir o MCC baseado no nome do comerciante, se aplicável.
3. Identificar a categoria de saldo com base no MCC:
    - "5411" ou "5412" -> FOOD
    - "5811" ou "5812" -> MEAL
    - Qualquer outro valor -> CASH
4. Verificar se há saldo suficiente na categoria identificada.
5. Se houver saldo suficiente, aprovar a transação e diminuir o saldo.
6. Se não houver saldo suficiente, verificar a categoria CASH.
7. Se houver saldo suficiente em CASH, aprovar a transação e diminuir o saldo de CASH.
8. Se não houver saldo suficiente em nenhuma categoria, rejeitar a transação por saldo insuficiente com resposta {"
   code": "51"}.
9. Se ocorrer qualquer outro problema durante o processamento, rejeitar a transação por motivo de erro com resposta {"
   code": "07"}.
10. Garantir que apenas uma transação por conta seja processada ao mesmo tempo utilizando locks ou outra estratégia de
    sincronização.
11. Retornar a resposta apropriada.
12. O código de retorno HTTP deve ser sempre 200, independentemente do resultado do processamento da transação.

## Etapas de desenvolvimento

- [x] Implementação das entidades de domínio
- [x] Criação dos repositórios
- [x] Implementação dos serviços de aplicação
- [x] Implementação dos testes de unidade
- [ ] Implementar a substituicao do mcc pelo nome do merchant
- [x] Adicionar suporte a persistencia com banco de dados
- [x] Criação dos controladores REST
- [ ] Criação dos testes de integração
- [ ] Criar teste para o UseCase de criacao de conta
- [ ] Refatorar a solucao incluindo eventos de domínio para garantir aderencia a DDD
- [ ] Detalhar a solução de arquitetura da regra L4 e avaliar as etapas para uma implementação simplificada
- [ ] Adicionar Swagger

## L4 e outras oportunidades de melhoria

A
É possível refatorar esta solucao para adicionar eventos de dominio para garantir a aderencia a DDD. O fluxo de eventos
para a autorização da transaçaõ seria:
Em uma implementação simples é possível usar ApplicationEventPublisher e um componente com @EventListener do próprio
spring.
Em uma implementação mais robusta é possível usar um message broker com Kafka, particionando a fila pelo id da conta
para garantir a ordem de processamento em caso de concorrencia.

1. Receber a transação via HTTP.
2. Use case em Transacion processa a transacao (ProcessTransactionUseCase)
   2.1 Aplica as regras de negocio da transacao
   2.2 Persiste a transacao e emite o evento de transacao recebida
3. DomainEventListener de account trata o evento de transacao recebida
   3.1 Aplica as regras de negocio da conta
   3.2 Persiste a conta e emite o evento de transacao processada com sucesso|falha
4. DomainEventListener de transaction trata o evento de transacao processada
   4.1 Atualiza a transacao com o status da transacao
5. Retorna o resultado do processamento




