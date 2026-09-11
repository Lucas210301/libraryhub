"use client";

import Link from "next/link";
import ErrorAlert from "@/components/ErrorAlert";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { registerDamageFine } from "@/services/fineActions";

export default function DamageFineForm({ loans }) {
  const router = useRouter();
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);
  const ready = loans.length > 0;

  async function handleSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);

    setSaving(true);
    setError(null);
    const result = await registerDamageFine({
      loanId: Number(formData.get("loanId")),
      severity: formData.get("severity")
    });
    setSaving(false);

    if (result.error) {
      setError(result.error);
      return;
    }

    router.push("/fines");
    router.refresh();
  }

  return (
    <form onSubmit={handleSubmit} className="card bg-base-100 shadow-sm">
      <div className="card-body gap-5">
        {error ? <ErrorAlert message={error} /> : null}

        {ready ? null : (
          <div role="alert" className="alert text-sm">
            A damage fine is attached to a loan. Register a loan first.
          </div>
        )}

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Loan</span>
          <select name="loanId" className="select w-full" required disabled={!ready}>
            {loans.map((loan) => (
              <option key={loan.id} value={loan.id}>
                {loan.id}, {loan.item.title}, {loan.member.name}
              </option>
            ))}
          </select>
        </label>

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Severity</span>
          <select name="severity" className="select w-full" required disabled={!ready}>
            <option value="LIGHT">Light damage, 25.00</option>
            <option value="SEVERE">Severe damage, 60.00</option>
          </select>
        </label>

        <div className="flex justify-end gap-2">
          <Link href="/fines" className="btn btn-ghost">
            Cancel
          </Link>
          <button type="submit" className="btn btn-primary" disabled={saving || !ready}>
            {saving ? "Saving" : "Register fine"}
          </button>
        </div>
      </div>
    </form>
  );
}
