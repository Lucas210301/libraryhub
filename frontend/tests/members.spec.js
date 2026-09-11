import { expect, test } from "@playwright/test";
import { aMember } from "./library";

async function addMember(page, name, email) {
  await page.goto("/members/create");
  await page.getByLabel("Name").fill(name);
  await page.getByLabel("Email").fill(email);
  await page.getByRole("button", { name: "Save member" }).click();
}

test("adds a member and shows it in the list", async ({ page }) => {
  const { name, email } = aMember();

  await addMember(page, name, email);

  await expect(page).toHaveURL(/\/members$/);
  await expect(page.getByRole("cell", { name: email })).toBeVisible();
});

test("shows an error when the email is already registered", async ({ page }) => {
  const { name, email } = aMember();
  await addMember(page, name, email);

  await addMember(page, "Beatriz Antunes", email);

  await expect(page.getByTestId("form-error")).toContainText(email);
});
