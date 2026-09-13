# Guia da terceira VA — branch, testes e entrega

Continuação do `GUIA_PASSO_A_PASSO.md`. Aqui está só o que muda para a terceira VA.

## 1. Criar e publicar a branch

O código já está commitado na `main` (11 commits, até `2cb8ea5`). O professor pede uma branch
específica para a terceira VA, cujo nome deve constar no relatório. Crie a partir do estado atual:

```bash
cd libraryhub
git checkout main
git pull
git checkout -b terceira-va
git push -u origin terceira-va
```

A branch aponta para o mesmo histórico, então os 11 commits aparecem nela. Confira os hashes que vão
para a matriz do relatório:

```bash
git log --oneline -11
```

Se o repositório estiver privado, dê acesso ao professor em Settings → Collaborators.

## 2. O que foi acrescentado

**Back end — 24 arquivos novos e 5 alterados**

```
business/model/     Reservation, ReservationStatus, Fine, OverdueFine,
                    DamagedItemFine, FineType, DamageSeverity        (novos)
                    Loan                                             (alterado: countDaysLate)
data/               IReservationRepository, IFineRepository           (novos)
business/service/   IReservationService, ReservationService,
                    IFineService, FineService                         (novos)
business/facade/    IFacade, Facade                                   (alterados: 7 operações novas)
communication/      ReservationController, FineController,
                    ReservationDTORequest, DamageFineDTORequest,
                    ReservationDTOResponse, FineDTOResponse,
                    FineSummaryDTOResponse, ReservationConverter,
                    FineConverter                                     (novos)
                    LoanController, MemberController                  (alterados: novos catch)
exception/          9 exceções novas
```

**Testes — 5 arquivos novos**

```
business/model/ReservationTest.java            unitário
business/model/FineTest.java                   unitário
business/service/ReservationServiceTest.java   unitário com Mockito
integration/ReservationFineIntegrationTest.java integração
api/ReservationControllerApiTest.java          API
```

**Front end — 8 arquivos novos e 2 alterados**

```
services/reservationActions.js, fineActions.js        (novos)
components/ReservationForm.js, DamageFineForm.js      (novos)
app/reservations/page.js, app/reservations/create/page.js,
app/fines/page.js, app/fines/create/page.js           (novos)
components/NavBar.js, app/page.js                     (alterados)
tests/reservations.spec.js                            (novo)
```

## 3. Banco de dados

Nada a fazer na mão. Com `spring.jpa.hibernate.ddl-auto=update`, o Hibernate cria as tabelas
`reservation` e `fine` na primeira execução. Para conferir:

```bash
docker exec -it libraryhub-db psql -U postgres -d libraryhub -c "\dt"
docker exec -it libraryhub-db psql -U postgres -d libraryhub -c "\d fine"
```

A tabela `fine` usa herança de tabela única: a coluna `fine_type` guarda `OVERDUE` ou `DAMAGE`, e as
colunas `days_late` e `severity` ficam nulas conforme o tipo.

## 4. Testar o back end

```bash
cd backend
mvn test
```

Rodando um tipo por vez, útil para mostrar cada requisito separadamente:

```bash
mvn test -Dtest=ReservationTest
mvn test -Dtest=FineTest
mvn test -Dtest=ReservationServiceTest
mvn test -Dtest=ReservationFineIntegrationTest
mvn test -Dtest=ReservationControllerApiTest
```

## 5. Testar a API na mão

Com o back end no ar, as requisições estão em `docs/api.http`. Pela linha de comando:

```bash
# preparação
curl -s -X POST http://localhost:8080/members -H "Content-Type: application/json" \
  -d '{"name": "Ana Souza", "email": "ana@ufape.edu.br"}'
curl -s -X POST http://localhost:8080/members -H "Content-Type: application/json" \
  -d '{"name": "Bruno Lima", "email": "bruno@ufape.edu.br"}'
curl -s -X POST http://localhost:8080/items/books -H "Content-Type: application/json" \
  -d '{"title": "Clean Code", "author": "Robert Martin", "isbn": "9780132350884"}'

# reservar item disponível, espera 409
curl -i -X POST http://localhost:8080/reservations -H "Content-Type: application/json" \
  -d '{"memberId": 1, "itemId": 1}'

# emprestar e então reservar, espera 201
curl -s -X POST http://localhost:8080/loans -H "Content-Type: application/json" \
  -d '{"memberId": 1, "itemId": 1}'
curl -i -X POST http://localhost:8080/reservations -H "Content-Type: application/json" \
  -d '{"memberId": 2, "itemId": 1}'

# devolver e tentar emprestar para um terceiro, espera 409 por causa da fila
curl -s -X PUT http://localhost:8080/loans/1/return
curl -i -X POST http://localhost:8080/loans -H "Content-Type: application/json" \
  -d '{"memberId": 1, "itemId": 1}'

# multa por dano e bloqueio do membro
curl -i -X POST http://localhost:8080/fines/damages -H "Content-Type: application/json" \
  -d '{"loanId": 1, "severity": "SEVERE"}'
curl -s http://localhost:8080/fines/summary
curl -i -X DELETE http://localhost:8080/members/1

# pagar e conferir
curl -i -X PUT http://localhost:8080/fines/1/pay
curl -i -X PUT http://localhost:8080/fines/1/pay
```

## 6. Testar a interface

```bash
cd frontend
npm run dev          # em um terminal, com o back end no ar
npx playwright test  # em outro
```

O roteiro manual está no anexo do `RELATORIO_3VA.md`, passo a passo.

## 7. Commits desta entrega

Os commits já estão feitos. Para conferência:

| Data | Hash | Escopo |
| --- | --- | --- |
| 10/09 | `b9485db` | Estrutura inicial do repositório |
| 10/09 | `1e44b23` | Back end, testes e telas, incluindo reservas e multas |
| 11/09 | `6c2b69d` | README revisado |
| 11/09 | `b72d3ee` | Raiz do workspace do Next.js |
| 11/09 | `2c62c0c` | `.gitignore` dos artefatos do Playwright |
| 11/09 | `7ee3e00` | Reúso do `ErrorAlert` nos formulários |
| 11/09 | `daa0cb3` | Banner de erro alvo por `data-testid` |
| 11/09 | `9aab374` | Suíte em série com orçamento maior |
| 11/09 | `93e6adf` | E-mail ao lado do nome no seletor de membro |
| 11/09 | `e1edd98` | Fixtures a partir de dados reais |
| 11/09 | `2cb8ea5` | README sobre fixtures e seletor |

O relatório e as figuras ainda precisam entrar no repositório:

```bash
git add RELATORIO_3VA.md diagrama-3va.png arquitetura-3va.png GUIA_3VA.md
git commit -m "docs: add the third assessment report"
git push
```

## 8. Antes de entregar

- [ ] `mvn test` passa inteiro, incluindo os testes da segunda VA
- [ ] `npx playwright test` passa
- [ ] O roteiro manual do anexo do relatório funciona do começo ao fim
- [ ] A branch `terceira-va` está publicada no GitHub
- [ ] O nome da branch está escrito no relatório
- [ ] O sobrenome está preenchido na capa e no cabeçalho do relatório
- [ ] O professor tem acesso ao repositório
- [ ] O relatório tem entre 5 e 10 páginas sem contar a capa
- [ ] `README.md` e `docs/ARQUITETURA.md` batem com o código
