import { expect, test } from "@playwright/test";

function uniqueEmail() {
  return `playwright.${Date.now()}@ufape.edu.br`;
}

async function addMember(page, name, email) {
  await page.goto("/members/create");
  await page.getByLabel("Name").fill(name);
  await page.getByLabel("Email").fill(email);
  await page.getByRole("button", { name: "Save member" }).click();
}

test("adds a member and shows it in the list", async ({ page }) => {
  const email = uniqueEmail();

  await addMember(page, "Playwright Member", email);

  await expect(page).toHaveURL(/\/members$/);
  await expect(page.getByRole("cell", { name: email })).toBeVisible();
});

test("shows an error when the email is already registered", async ({ page }) => {
  const email = uniqueEmail();
  await addMember(page, "First Member", email);

  await addMember(page, "Second Member", email);

  await expect(page.getByTestId("form-error")).toContainText(email);
});
