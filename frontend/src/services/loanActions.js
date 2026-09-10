"use server";

import { revalidatePath } from "next/cache";
import api from "./api";
import { extractErrorMessage } from "./apiError";

export async function listLoans() {
  try {
    const response = await api.get("/loans");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function createLoan(loan) {
  try {
    await api.post("/loans", loan);
    revalidatePath("/loans");
    revalidatePath("/items");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function returnLoan(id) {
  try {
    await api.put(`/loans/${id}/return`);
    revalidatePath("/loans");
    revalidatePath("/items");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}
