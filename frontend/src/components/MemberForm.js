"use client";

import Link from "next/link";
import ErrorAlert from "@/components/ErrorAlert";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { createMember, updateMember } from "@/services/memberActions";

export default function MemberForm({ member }) {
  const router = useRouter();
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const payload = { name: formData.get("name"), email: formData.get("email") };

    setSaving(true);
    setError(null);
    const result = member ? await updateMember(member.id, payload) : await createMember(payload);
    setSaving(false);

    if (result.error) {
      setError(result.error);
      return;
    }

    router.push("/members");
    router.refresh();
  }

  return (
    <form onSubmit={handleSubmit} className="card bg-base-100 shadow-sm">
      <div className="card-body gap-5">
        {error ? <ErrorAlert message={error} /> : null}

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Name</span>
          <input name="name" defaultValue={member?.name ?? ""} className="input w-full" required />
        </label>

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Email</span>
          <input name="email" type="email" defaultValue={member?.email ?? ""} className="input w-full" required />
        </label>

        <div className="flex justify-end gap-2">
          <Link href="/members" className="btn btn-ghost">
            Cancel
          </Link>
          <button type="submit" className="btn btn-primary" disabled={saving}>
            {saving ? "Saving" : "Save member"}
          </button>
        </div>
      </div>
    </form>
  );
}
