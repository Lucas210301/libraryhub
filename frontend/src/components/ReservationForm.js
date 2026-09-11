"use client";

import Link from "next/link";
import ErrorAlert from "@/components/ErrorAlert";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { createReservation } from "@/services/reservationActions";

export default function ReservationForm({ members, items }) {
  const router = useRouter();
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);
  const ready = members.length > 0 && items.length > 0;

  async function handleSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);

    setSaving(true);
    setError(null);
    const result = await createReservation({
      memberId: Number(formData.get("memberId")),
      itemId: Number(formData.get("itemId"))
    });
    setSaving(false);

    if (result.error) {
      setError(result.error);
      return;
    }

    router.push("/reservations");
    router.refresh();
  }

  return (
    <form onSubmit={handleSubmit} className="card bg-base-100 shadow-sm">
      <div className="card-body gap-5">
        {error ? <ErrorAlert message={error} /> : null}

        {ready ? null : (
          <div role="alert" className="alert text-sm">
            Only items that are currently on loan can be reserved. Register a loan first.
          </div>
        )}

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Member</span>
          <select name="memberId" className="select w-full" required disabled={!ready}>
            {members.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </select>
        </label>

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Item on loan</span>
          <select name="itemId" className="select w-full" required disabled={!ready}>
            {items.map((item) => (
              <option key={item.id} value={item.id}>
                {item.title}
              </option>
            ))}
          </select>
        </label>

        <div className="flex justify-end gap-2">
          <Link href="/reservations" className="btn btn-ghost">
            Cancel
          </Link>
          <button type="submit" className="btn btn-primary" disabled={saving || !ready}>
            {saving ? "Saving" : "Create reservation"}
          </button>
        </div>
      </div>
    </form>
  );
}
