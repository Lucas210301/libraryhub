"use server";

import { revalidatePath } from "next/cache";
import api from "./api";
import { extractErrorMessage } from "./apiError";

export async function listItems() {
  try {
    const response = await api.get("/items");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function listAvailableItems() {
  try {
    const response = await api.get("/items/available");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function createBook(book) {
  try {
    await api.post("/items/books", book);
    revalidatePath("/items");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function createMagazine(magazine) {
  try {
    await api.post("/items/magazines", magazine);
    revalidatePath("/items");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function removeItem(id) {
  try {
    await api.delete(`/items/${id}`);
    revalidatePath("/items");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}
