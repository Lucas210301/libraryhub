"use server";

import { revalidatePath } from "next/cache";
import api from "./api";
import { extractErrorMessage } from "./apiError";

export async function listMembers() {
  try {
    const response = await api.get("/members");
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function findMember(id) {
  try {
    const response = await api.get(`/members/${id}`);
    return { data: response.data };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function createMember(member) {
  try {
    await api.post("/members", member);
    revalidatePath("/members");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function updateMember(id, member) {
  try {
    await api.put(`/members/${id}`, member);
    revalidatePath("/members");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}

export async function removeMember(id) {
  try {
    await api.delete(`/members/${id}`);
    revalidatePath("/members");
    return { success: true };
  } catch (error) {
    return { error: extractErrorMessage(error) };
  }
}
