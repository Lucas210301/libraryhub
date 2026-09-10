"use server";

import { revalidatePath } from "next/cache";
import api from "./api";
import { extractErrorMessage } from "./apiError";

export async function listReservations() {
  try {
    const response = await api.get("/reservations");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function createReservation(reservation) {
  try {
    await api.post("/reservations", reservation);
    revalidatePath("/reservations");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function cancelReservation(id) {
  try {
    await api.put(`/reservations/${id}/cancel`);
    revalidatePath("/reservations");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}
