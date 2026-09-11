import { expect, test } from "@playwright/test";
import { aBook, aMember } from "./library";

test("queues a reservation for an item that is on loan", async ({ page }) => {
  const borrower = aMember();
  const reader = aMember();
  const book = aBook();

  await addMember(page, borrower);
  await addMember(page, reader);
  await addBook(page, book);
  await addLoan(page, borrower, `${book.title}, 15 days`);

  await addReservation(page, reader, book.title);

  const row = page.getByRole("row").filter({ hasText: book.title });
  await expect(row).toContainText(reader.name);
  await expect(row).toContainText("ACTIVE");
});

test("refuses a second reservation of the same item for the same member", async ({ page }) => {
  const borrower = aMember();
  const reader = aMember();
  const book = aBook();

  await addMember(page, borrower);
  await addMember(page, reader);
  await addBook(page, book);
  await addLoan(page, borrower, `${book.title}, 15 days`);
  await addReservation(page, reader, book.title);

  await page.goto("/reservations/create");
  await page.getByLabel("Member").selectOption({ label: optionFor(reader) });
  await page.getByLabel("Item on loan").selectOption({ label: book.title });
  await page.getByRole("button", { name: "Create reservation" }).click();

  await expect(page.getByTestId("form-error")).toContainText("already has an active reservation");
});

function optionFor(member) {
  return `${member.name} - ${member.email}`;
}

async function addMember(page, member) {
  await page.goto("/members/create");
  await page.getByLabel("Name").fill(member.name);
  await page.getByLabel("Email").fill(member.email);
  await page.getByRole("button", { name: "Save member" }).click();
  await expect(page).toHaveURL(/\/members$/);
}

async function addBook(page, book) {
  await page.goto("/items/create");
  await page.getByLabel("Title").fill(book.title);
  await page.getByLabel("Author").fill(book.author);
  await page.getByLabel("ISBN").fill(book.isbn);
  await page.getByRole("button", { name: "Save item" }).click();
  await expect(page).toHaveURL(/\/items$/);
}

async function addLoan(page, member, itemLabel) {
  await page.goto("/loans/create");
  await page.getByLabel("Member").selectOption({ label: optionFor(member) });
  await page.getByLabel("Available item").selectOption({ label: itemLabel });
  await page.getByRole("button", { name: "Create loan" }).click();
  await expect(page).toHaveURL(/\/loans$/);
}

async function addReservation(page, member, itemTitle) {
  await page.goto("/reservations/create");
  await page.getByLabel("Member").selectOption({ label: optionFor(member) });
  await page.getByLabel("Item on loan").selectOption({ label: itemTitle });
  await page.getByRole("button", { name: "Create reservation" }).click();
  await expect(page).toHaveURL(/\/reservations$/);
}
