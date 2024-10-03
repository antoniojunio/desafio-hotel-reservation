# Hotel Reservation API

## Descrição

A **Hotel Reservation API** é uma aplicação desenvolvida para gerenciar reservas de hotéis. Ela oferece funcionalidades para criar, atualizar, listar e deletar hotéis, quartos e reservas. Esta API foi desenvolvida utilizando **Spring Boot**, com persistência de dados usando o **Hibernate** e um banco de dados relacional, além de integração com serviços de notificação via email.

## Funcionalidades

- Gerenciamento de hotéis (criar, listar, atualizar, deletar)
- Gerenciamento de quartos (criar, listar, atualizar, deletar)
- Gerenciamento de reservas de clientes
- Notificações de confirmação de reserva via email

## Tecnologias Utilizadas

- **Java 11+**
- **Spring Boot**
- **Hibernate (JPA)**
- **PostgreSQL**
- **RabbitMQ** (via Docker)
- **Docker**
- **Postman** (para testes de API)

## Rotas da API

### Hotéis
- **GET /api/hotels/search**: Pesquisa hotéis com base em critérios como destino, datas de check-in/check-out, número de quartos, etc.
- **GET /api/hotels/compare**: Compara hotéis com base em preço, localização, comodidades, e avaliações.
- **GET /api/hotels/{id}**: Busca um hotel específico pelo ID.
- **POST /api/hotels**: Cria um novo hotel.

### Reservas
- **POST /api/reservations**: Cria uma nova reserva de quarto.
- **GET /api/reservations/{id}**: Busca uma reserva específica por ID.
- **DELETE /api/reservations/{id}**: Deleta uma reserva específica por ID.

## Como rodar o projeto localmente

### Pré-requisitos

- **Java 11+**
- **Maven**
- **Docker** (para rodar o RabbitMQ)
- **PostgreSQL** (ou o banco de dados de sua escolha)

### Configuração do RabbitMQ

Para utilizar o RabbitMQ, você pode rodá-lo via Docker com o seguinte comando:

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

### Clonando o repositório

```bash
git clone https:git@github.com:antoniojunio/desafio-hotel-reservation.git

