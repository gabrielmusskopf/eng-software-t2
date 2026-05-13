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