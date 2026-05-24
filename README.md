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

## Testando a Integração com o Discord Webhook Manualmente

Este documento lista os passos manuais que você precisa realizar para habilitar e verificar a funcionalidade de notificação via Discord Webhook.

A implementação funciona **sem necessidade de nenhuma chave de API**, pois os Webhooks de Entrada (Incoming Webhooks) do Discord exigem apenas uma URL que já contém o segredo embutido.

---

### 1. Crie uma URL de Webhook no Discord para cada usuário que deseja receber notificações

Para cada usuário no sistema que deseja receber notificações no Discord, você (ou o usuário) deve:

1. Abrir o Discord → escolher o **Servidor** e **Canal** onde as mensagens devem aparecer.
2. Clicar no ícone de engrenagem do canal → **Integrações** → **Webhooks** → **Novo Webhook**.
3. Dar um nome (por exemplo, "Task Manager"), opcionalmente escolher um avatar, e clicar em **Copiar URL do Webhook**.
4. A URL será parecida com isso:
   `https://discord.com/api/webhooks/<webhook_id>/<webhook_token>`

> Trate essa URL como uma senha: qualquer pessoa com ela pode postar mensagens no canal. Não é necessário criar um bot no Discord, aplicativo OAuth, Client ID ou Client Secret. Apenas a URL do webhook é suficiente.

---

### 2. Registre a URL do Webhook para cada usuário

Com a aplicação rodando (`./gradlew bootRun`), utilize uma ferramenta de requisições HTTP (como Postman, Insomnia ou similar) e chame a API:

```http
PUT /users/{userId}/webhooks/discord
Content-Type: application/json

{
  "webhookUrl": "https://discord.com/api/webhooks/123456789/abcdef-token-here",
  "username": "Task Manager"
}
```

- `userId` — ID do usuário existente (deve ser criado antes, através de `POST /users`).
- `webhookUrl` — A URL copiada do Discord no passo 1.
- `username` *(opcional)* — Substitui o nome de exibição do bot nas mensagens enviadas para este usuário.

Você também pode usar:
- `GET /users/{userId}/webhooks/discord` — para consultar o webhook (a URL será ofuscada na resposta, a resposta de sucesso deve retornar o status `200 OK`).
- `DELETE /users/{userId}/webhooks/discord` — para remover o webhook de forma lógica (status `200 OK`, as notificações serão interrompidas).

Um usuário sem webhook registrado **não recebe notificações**, sendo esse o comportamento esperado (opt-in).

---

### 3. Dispare uma notificação para verificar

As notificações são disparadas em três eventos principais:

| Evento     | Quando ocorre                                    | Quem é notificado                        |
|------------|--------------------------------------------------|------------------------------------------|
| `CREATED`  | Uma nova tarefa é criada (via `POST /tasks`)     | O **criador** da tarefa                  |
| `ASSIGNED` | Uma tarefa é associada ou transferida a alguém   | O novo **responsável** (assignee)        |
| `FINISHED` | O status da tarefa muda para `FINISHED`          | O **criador** e o **responsável**        |

Para testar o fluxo manual:

1. Chame `POST /users` — crie o usuário A e o usuário B. Salve ambos os `id`s (resposta retornará sucesso `201 Created`).
2. Chame `PUT /users/{userA-id}/webhooks/discord` — registre uma URL de webhook para o usuário A (resposta de sucesso retornará `200 OK`).
3. Chame `PUT /users/{userB-id}/webhooks/discord` — registre uma URL para o usuário B (pode ser um webhook diferente).
4. Crie uma tarefa usando `POST /tasks`:
   ```json
   {
     "title": "Test integration",
     "description": "verify webhook",
     "creatorId": "<userA-id>",
     "assigneeId": "<userB-id>"
   }
   ```
5. A requisição de criar tarefa deve retornar um sucesso `201 Created`. Verifique os canais no Discord — deve aparecer uma mensagem para o A (`Task created`) e outra para o B (`Task assigned`).
6. Envie `PUT /tasks/{id}` com o body `{"status": "FINISHED"}` — a resposta deve ser `200 OK` e tanto A quanto B receberão um aviso de `Task finished` nos respectivos canais do Discord.

---

### 4. (Opcional) Ajustar o comportamento do Discord no `application.yml`

As configurações padrão estão em `src/main/resources/application.yml` e podem ser modificadas:

```yaml
app:
  discord:
    enabled: true              # Mude para false para desabilitar as notificações 
    default-username: Task Manager
    connect-timeout-ms: 3000
    read-timeout-ms: 5000
```
Por exemplo, defina `enabled: false` para desenvolvimento local offline, sem envio de requisições webhooks para a rede externa.

---

### 5. Executar os Testes Relacionados

A nova funcionalidade integra-se com a suíte existente, garantindo que testes relacionados a regras e envios como `CreateTaskImplTest`, `UpdateTaskImplTest`, `DiscordNotificationAdapterTest`, etc., funcionem perfeitamente. Lembre-se, testando na raiz:

```bash
./gradlew test
```

Se desejar formatar antes de enviar para o repositório, rode:
```bash
./gradlew spotlessApply
```

---

### 6. (Opcional) Segurança dos endpoints do Webhook

Atualmente, `SecurityConfig` permite todas as requisições. Contudo, futuramente nas implementações de JWT:
- Restrinja as requisições de `PUT/GET/DELETE /users/{userId}/webhooks/discord` para que apenas o próprio usuário autenticado possa alterar seu webhook.
- O campo `creatorId` em `POST /tasks` deve ser inferido a partir do Principal, e não do payload da requisição.
Esses pontos estão fora do escopo do sistema de Discord, mas são cruciais para a segurança real da aplicação ao gerenciar JWT.
