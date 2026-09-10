"use server";

import { revalidatePath } from "next/cache";
import api from "./api";
import { extractErrorMessage } from "./apiError";

export async function listFines() {
  try {
    const response = await api.get("/fines");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function fineSummary() {
  try {
    const response = await api.get("/fines/summary");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function registerDamageFine(fine) {
  try {
    await api.post("/fines/damages", fine);
    revalidatePath("/fines");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function payFine(id) {
  try {
    await api.put(`/fines/${id}/pay`);
    revalidatePath("/fines");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}
