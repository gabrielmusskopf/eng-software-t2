# Engenharia de Software - T2

Projeto de gerenciamento de tarefas desenvolvido como parte dos requisitos da disciplina de Engenharia de Software.

## Objetivos
O objetivo deste projeto é aplicar boas práticas modernas de engenharia de software e padrões de design na construção de um serviço robusto, focando no desacoplamento, testabilidade e na manutenção de um código limpo e padronizado.

## Arquitetura Hexagonal
O projeto foi estruturado seguindo os princípios da **Arquitetura Hexagonal (Ports and Adapters)**. 

A principal regra adotada é a inversão de dependência para isolar o núcleo da aplicação. A estrutura é focada na seguinte divisão de camadas:
* **Domain:** Contém as regras de negócio puras e modelos de domínio. Não possui dependências com frameworks ou detalhes de infraestrutura (com exceções)
* **Application:** Define os casos de uso e as interfaces (Portas) tanto de entrada (inbound) quanto de saída (outbound).
* **Adapter:** Contém as implementações das portas (Adaptadores). É aqui que residem os Controladores REST, acesso a banco de dados (MongoDB), etc.
* **Config:** Camada mais externa responsável por unir os adaptadores à aplicação por meio de injeção de dependência e configurações do Spring.

A integridade destas regras arquiteturais é testada e garantida continuamente na suíte de testes utilizando a biblioteca **ArchUnit**.

## Como executar

### Pré-requisitos
* Java 21 instalado.
* Docker instalado e em execução.

### Execução
Graças ao uso do módulo `spring-boot-docker-compose`, as dependências de infraestrutura contidas no arquivo `docker-compose.yml` (como o MongoDB) subirão automaticamente junto à aplicação. 

Para executar o sistema, rode o comando abaixo na raiz do projeto:

```bash
./gradlew bootRun
```

## Como contribuir e Padrões

Para garantir a qualidade e a consistência da base de código, seguimos regras rigorosas de padronização:

1. **Formatação de Código:** Utilizamos o **Spotless** configurado com o padrão *Palantir Java Format*. Se o seu código não estiver devidamente formatado, a build irá falhar. 
   Antes de commitar suas alterações, sempre execute:
   ```bash
   ./gradlew spotlessApply
   ```

2. **Testes:** Novas funcionalidades não devem ferir o isolamento das camadas estruturadas na Arquitetura Hexagonal. Sempre garanta que os testes de arquitetura implementados com **ArchUnit** passem com o comando:
   ```bash
   ./gradlew test
   ```

## Configurações

| Propriedade           | Descrição                                   | Valor padrão |
|-----------------------|---------------------------------------------|--------------|
| `app.metrics.enabled` | Habilita ou desabilita a coleta de métricas | `true`       |

## Funcionalidades

### Métricas

O sistema inclui um dashboard de métricas que exibe informações relevantes sobre as tarefas, como o número total de tarefas,
tarefas concluídas, tarefas pendentes e outras estatísticas úteis para o gerenciamento eficiente das atividades, exportadas no formato do Prometheus.

Métricas são coletadas por eventos emitidos pela aplicação. Ou seja, coletar métricas e expô-las no formato do Prometheus
não quebra o isolamento da aplicação, já que a coleta é feita em adaptadores de saída. A classe responsável é `MicrometerMetricEventHandler`.

É possível ativar/desativar a coleta de métricas através da propriedade `app.metrics.enabled` no arquivo `application.yml`. Por padrão, a coleta de métricas está habilitada.
Desabilitar a coleta de métricas faz com que o bean da classe nem seja instanciado, portanto, os eventos não serão escutados por ela. A aplicação não sofre nenhuma alteração.

| Evento                        | Tipo    | Métrica                            | Tags                                                                                                                                                                                                                                                                                                    | Descrição                                                    |
|-------------------------------|---------|------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------|
| `TaskCreatedEvent`            | counter | `api_task_operations_total`        | `operation=create`, `description=Criado`                                                                                                                                                                                                                                                                | Incrementa o contador de tarefas criadas.                    |
| `TaskDeletedEvent`            | counter | `api_task_operations_total`        | `operation=delete`, `description=Excluído`                                                                                                                                                                                                                                                              | Incrementa o contador de tarefas excluídas.                  |
| `TaskReassignedEvent`         | counter | `api_task_operations_total`        | `operation=reassign`, `description=Re-atribuído`                                                                                                                                                                                                                                                        | Incrementa o contador de tarefas reatribuídas.               |
| `TaskTitleChangedEvent`       | counter | `api_task_operations_total`        | `operation=update_title`, `description=Título atualizado`                                                                                                                                                                                                                                               | Incrementa o contador de tarefas com título alterado.        |
| `TaskDescriptionChangedEvent` | counter | `api_task_operations_total`        | `operation=update_description`, `description=Descrição atualizada`                                                                                                                                                                                                                                      | Incrementa o contador de tarefas com descrição alterada.     |
| `TaskStatusChangedEvent`      | counter | `api_task_operations_total`        | Se o status é BACKLOG, `operation=deferred` e `description=Backlog`<br/>Se o status é IN_PROCESS, `operation=stated` e `description=Em progresso`<br/>Se status é COMPLETED, `operation=comlpeted` e `description=Concluída`<br/>Se status é CANCELLED, `operation=cancelled` e `description=Cancelada` | Incrementa o contador de tarefas com status alterado.        |
| `TaskStatusChangedEvent`      | timer   | `api_task_status_duration_seconds` | `status=backlog/in_process/completed/cancelled` e `description=Backlog/Em progresso/Concluída/Cancelada`                                                                                                                                                                                                | Registra a duração que as tarefas permanecem em cada status. |
| -                             | gauge   | `api_tasks_by_status`              | `status=backlog/in_process/completed/cancelled` e `description=Backlog/Em progresso/Concluída/Cancelada`                                                                                                                                                                                                | Mede a quantidade de tarefas em cada status.                 |

Ao subir a aplicação, devido ao `spring-docker-compose` e o arquivo `docker-compose`, o Prometheus e Grafana são iniciados automaticamente nas portas `9090` e `3000`, respectivamente. Ao acessar o Grafana, use o usuário `admin` e senha `admin` para fazer login.
No diretório `dashboards` estão os arquivos de dashboard usados, basta baixar o arquivo e importá-lo no Grafana para visualizar as métricas coletadas. Exemplo:

![Dashboard de métricas](./docs/metricas.png)

