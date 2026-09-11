import { expect, test } from "@playwright/test";

test("queues a reservation for an item that is on loan", async ({ page }) => {
  const stamp = Date.now();
  const borrower = `Borrower ${stamp}`;
  const reader = `Reader ${stamp}`;
  const title = `Playwright Book ${stamp}`;

  await addMember(page, borrower, `borrower.${stamp}@ufape.edu.br`);
  await addMember(page, reader, `reader.${stamp}@ufape.edu.br`);
  await addBook(page, title);
  await addLoan(page, borrower, `${title}, 15 days`);

  await addReservation(page, reader, title);

  const row = page.getByRole("row").filter({ hasText: title });
  await expect(row).toContainText(reader);
  await expect(row).toContainText("ACTIVE");
});

test("refuses a second reservation of the same item for the same member", async ({ page }) => {
  const stamp = Date.now();
  const borrower = `Borrower ${stamp}`;
  const reader = `Reader ${stamp}`;
  const title = `Playwright Book ${stamp}`;

  await addMember(page, borrower, `borrower.${stamp}@ufape.edu.br`);
  await addMember(page, reader, `reader.${stamp}@ufape.edu.br`);
  await addBook(page, title);
  await addLoan(page, borrower, `${title}, 15 days`);
  await addReservation(page, reader, title);

  await page.goto("/reservations/create");
  await page.getByLabel("Member").selectOption({ label: reader });
  await page.getByLabel("Item on loan").selectOption({ label: title });
  await page.getByRole("button", { name: "Create reservation" }).click();

  await expect(page.getByTestId("form-error")).toContainText("already has an active reservation");
});

async function addMember(page, name, email) {
  await page.goto("/members/create");
  await page.getByLabel("Name").fill(name);
  await page.getByLabel("Email").fill(email);
  await page.getByRole("button", { name: "Save member" }).click();
  await expect(page).toHaveURL(/\/members$/);
}

async function addBook(page, title) {
  await page.goto("/items/create");
  await page.getByLabel("Title").fill(title);
  await page.getByLabel("Author").fill("Playwright Author");
  await page.getByLabel("ISBN").fill(`978${Date.now()}`);
  await page.getByRole("button", { name: "Save item" }).click();
  await expect(page).toHaveURL(/\/items$/);
}

async function addLoan(page, memberName, itemLabel) {
  await page.goto("/loans/create");
  await page.getByLabel("Member").selectOption({ label: memberName });
  await page.getByLabel("Available item").selectOption({ label: itemLabel });
  await page.getByRole("button", { name: "Create loan" }).click();
  await expect(page).toHaveURL(/\/loans$/);
}

async function addReservation(page, memberName, itemTitle) {
  await page.goto("/reservations/create");
  await page.getByLabel("Member").selectOption({ label: memberName });
  await page.getByLabel("Item on loan").selectOption({ label: itemTitle });
  await page.getByRole("button", { name: "Create reservation" }).click();
  await expect(page).toHaveURL(/\/reservations$/);
}
