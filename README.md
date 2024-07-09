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
- Postgres

## Padrões de arquitetura

- Clean Architecture
- Domain Driven Design (+-)
- SOLID

## Como executar o projeto

- Clone o projeto
- Execute o comando `docker-compose up` no diretório dev-env do projeto para iniciar o banco de dados postgres
- Execute o comando `mvn clean install`
- Execute a classe CardTransactionApplication, os migrations serão executados automaticamente
- Acesse a documentação da API em http://localhost:8080/swagger-ui.html
    - Use o endpoint `POST /transactions` para criar uma transação
    - Use o endpoint `GET /transactions` para listar as transações
- Caso prefira, há um arquivo `Caju-Card-Transaction.postman_collection.json` no
  diretório `./dev-env/Caju-Card-Transaction.postman_collection.json`
  com uma
  coleção de requests para o Postman

## L4

A descrição de solucao, com diagramas e detalhes de implementação, está no arquivo [L4.md](docs/L4/L4.md)

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
- [x] Criação dos testes de integração
- [x] Criar teste para o UseCase de criacao de conta
- [x] Detalhar a solução de arquitetura da regra L4 e avaliar as etapas para uma implementação simplificada
- [x] Adicionar Swagger

## Oportunidades de melhoria

Refatorações possíveis para melhorar a solução caso fosse um projeto real, com necessidade de escala e manutenção,
visando manter a complexidade do código baixa e a redibilidade alta. Estes temas não estão diretamente relacionados a
questão L4, mas achei relevante pontuar.

### 1. Uso de eventos

É possível refatorar esta solucao para adicionar eventos de dominio, o que garantir a aderencia a DDD.

Em uma implementação simples é possível usar ApplicationEventPublisher e um componente com @EventListener do próprio
spring.
Em uma implementação mais robusta é possível usar um message broker com Kafka, particionando a fila pelo id da conta
para garantir a ordem de processamento em caso de concorrencia.

O fluxo de eventos para a autorização da transação seria:

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

### 2.Utilização do Spring Modulith para garantir a separação de módulos

Os dominios adjacentes à transação (Account, Merchant, proprio MCC) caso implementados completamente possuiriam regras
de negocio suficientes para que cada
um tenha seu proprio serviço.
Porém, mesmo no escopo reduzido da autorização de uma transação, é possível separar os dominios em módulos diferentes.

O Spring Modulith permitiria garantir a separação de módulos e a dependência unidirecional entre eles.
É possível utilizar eventos por entidade agregadora e eliminar os gateways entre dominios.
Também é possível organizar melhor os migrations, dividir melhor a execução de testes e otimizar o build.

### 3. Implementação de idempotencia com cache distribuído

É importante que transacoes financeiras possuam idempotencia, ou seja, caso exatamente a mesma transação seja recebida
mais de uma vez, a partir da segunda vez ela não é processada novamente, mas ainda conseguimos saber o resultado do
primeiro processamento.
Com redis seria possível implementar um cache com as transações mais recentes, tornando esta validação performatica nos
cenarios mais comuns (por exmeplo, envio em duplicidade onde as duplicidades acontecem em sequencia).

## Referencias usadas para o desenvolvimento

Eu não possuo experiencia pratica com processamento de pagamentos, apesar de já ter integrado com APIs e implementado
emissão de boletos.
Então pesquisei os conceitos mais importantes neste cenário e encontrei algumas referencias.

- [Designing a payment system](https://newsletter.pragmaticengineer.com/p/designing-a-payment-system)
- [ATM in concurrent environment](https://stackoverflow.com/questions/12236897/how-does-atm-work-in-concurrent-environment)
- [Implementing Domain Driven Design with Spring by Maciej Walkowiak @ Spring I/O 2024](https://www.youtube.com/watch?v=VGhg6Tfxb60)
