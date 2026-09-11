# LibraryHub

Sistema de gestão de empréstimos de biblioteca, desenvolvido para a disciplina de Programação
Orientada a Objetos (Ciência da Computação — UFAPE).

O sistema controla os membros da biblioteca, os itens do acervo (livros e revistas), os empréstimos
que ligam um membro a um item, a fila de reservas de um item emprestado e as multas por atraso ou
dano.

## Stack

| Camada | Tecnologia |
| --- | --- |
| Back end | Java 21, Spring Boot 3.5, Spring Data JPA, Bean Validation |
| Banco | PostgreSQL (execução) e H2 em memória (testes) |
| Front end | Next.js (App Router), React, axios, Tailwind CSS, daisyUI |
| Testes | JUnit 5, Mockito, MockMvc, Playwright |

## Arquitetura em camadas

```
Comunicação (controller, dto, conversor)
        |
     Fachada  ..................... ponto único de acesso ao sistema
        |
Coleção de Negócio (service) ...... regras de negócio
        |
Coleção de Dados (repository) ..... JPA
        |
Classes Básicas de Negócio (model)
```

Cada camada acessa a camada de baixo apenas através de interfaces: `IFacade`, `IMemberService`,
`IItemService`, `ILoanService`, `IReservationService`, `IFineService`, `IMemberRepository`,
`IItemRepository`, `ILoanRepository`, `IReservationRepository` e `IFineRepository`.

## Regras de negócio

1. Um livro fica quinze dias com o membro; uma revista, sete dias. A data prevista de devolução é
   calculada pelo próprio item, de forma polimórfica.
2. Um item que já está emprestado não pode ser emprestado de novo.
3. Um empréstimo que já foi devolvido não pode ser devolvido de novo.
4. Um e-mail só pode pertencer a um membro.
5. Um membro pode ter no máximo três empréstimos ativos ao mesmo tempo.
6. Um membro com empréstimos ativos não pode ser removido.
7. Um item que está emprestado não pode ser removido do acervo.
8. Só é possível reservar um item que está emprestado; item disponível deve ser emprestado direto.
9. Um membro não pode ter duas reservas ativas para o mesmo item.
10. Quando o item volta ao acervo, o primeiro da fila de reserva tem prioridade sobre os demais.
11. A devolução fora do prazo gera multa automática de 1,50 por dia de atraso.
12. Um empréstimo aceita no máximo uma multa por dano, com valor definido pela gravidade.
13. Um membro com multa em aberto não faz novo empréstimo e não pode ser removido.

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/members` | Lista os membros |
| GET | `/members/{id}` | Busca um membro |
| POST | `/members` | Cadastra um membro |
| PUT | `/members/{id}` | Atualiza um membro |
| DELETE | `/members/{id}` | Remove um membro |
| GET | `/items` | Lista todos os itens |
| GET | `/items/available` | Lista apenas os itens disponíveis |
| GET | `/items/{id}` | Busca um item |
| POST | `/items/books` | Cadastra um livro |
| POST | `/items/magazines` | Cadastra uma revista |
| DELETE | `/items/{id}` | Remove um item |
| GET | `/loans` | Lista os empréstimos |
| GET | `/loans/{id}` | Busca um empréstimo |
| POST | `/loans` | Registra um empréstimo |
| PUT | `/loans/{id}/return` | Registra a devolução |
| GET | `/reservations` | Lista as reservas |
| GET | `/reservations/{id}` | Busca uma reserva |
| POST | `/reservations` | Cria uma reserva |
| PUT | `/reservations/{id}/cancel` | Cancela uma reserva |
| GET | `/fines` | Lista as multas |
| GET | `/fines/summary` | Total de multas em aberto |
| GET | `/fines/{id}` | Busca uma multa |
| POST | `/fines/damages` | Registra multa por dano |
| PUT | `/fines/{id}/pay` | Registra o pagamento de uma multa |

Status codes utilizados: 200, 201, 204, 400 (validação), 404 (não encontrado) e 409 (conflito com
uma regra de negócio).

O back end é uma API REST e não serve páginas: acessar `http://localhost:8080` na raiz devolve a
página de erro padrão do Spring. A interface fica em `http://localhost:3000`.

## Como executar

Pré-requisitos: JDK 21, Maven, Node.js 20 ou superior e PostgreSQL.

### 1. Banco

O back end espera o banco `libraryhub` em `localhost:5432`, com usuário e senha `postgres`.

```bash
PGPASSWORD=postgres psql -h 127.0.0.1 -U postgres -c 'CREATE DATABASE libraryhub;'
```

Confira as credenciais em `backend/src/main/resources/application.properties`. As tabelas são
criadas automaticamente pelo Hibernate (`ddl-auto=update`).

### 2. Back end

```bash
cd backend
mvn spring-boot:run
```

Aguarde a mensagem `Started LibraryhubApplication` e confirme com:

```bash
curl -i http://localhost:8080/members
```

### 3. Front end

Em outro terminal:

```bash
cd frontend
cp .env.local.example .env.local
npm install
npm run dev
```

O front end sobe em `http://localhost:3000` e o back end em `http://localhost:8080`.

## Como testar

### Back end

```bash
cd backend
mvn test
```

Esperado: `Tests run: 55, Failures: 0, Errors: 0`.

### Front end

```bash
cd frontend
npm run lint
npm run build
```

### Testes end-to-end

Os testes E2E usam a API real, então o **back end precisa estar rodando**. Na primeira vez, baixe os
navegadores do Playwright:

```bash
cd frontend
npx playwright install chromium
```

Depois:

```bash
npx playwright test
```

Esperado: `4 passed`.

O Playwright sobe e derruba o próprio servidor de desenvolvimento. Por isso **não deixe um
`npm run dev` rodando** ao executar os testes: duas instâncias do Next.js compartilhando a mesma
pasta `.next` se corrompem, e a porta 3000 fica presa. Se isso acontecer:

```bash
pgrep -f next-server | xargs -r kill
rm -rf frontend/.next
```

Para ver o navegador durante a execução, ou gerar o relatório em HTML:

```bash
npx playwright test --headed
npx playwright test --reporter=html && npx playwright show-report
```

Os testes criam dados a cada execução e não limpam o banco. Para zerar o banco local de
desenvolvimento:

```bash
PGPASSWORD=postgres psql -h 127.0.0.1 -U postgres -c 'DROP DATABASE libraryhub;'
PGPASSWORD=postgres psql -h 127.0.0.1 -U postgres -c 'CREATE DATABASE libraryhub;'
```

## Documentação

- `docs/ARQUITETURA.md` — diagrama de classes, camadas, exceções e mapa dos requisitos da disciplina.
- `docs/api.http` — requisições prontas para o Insomnia, o Postman ou a extensão REST Client.
- `docs/diagrama-3va.svg` — diagrama de classes do projeto.
