"use client";

import { useRouter } from "next/navigation";
import { useState, useTransition } from "react";

export default function ActionButton({ action, label, pendingLabel, confirmMessage, className = "btn btn-sm" }) {
  const router = useRouter();
  const [error, setError] = useState(null);
  const [pending, startTransition] = useTransition();

  function handleClick() {
    if (confirmMessage && !window.confirm(confirmMessage)) {
      return;
    }
    setError(null);
    startTransition(async () => {
      const result = await action();
      if (result?.error) {
        setError(result.error);
        return;
      }
      router.refresh();
    });
  }

  return (
    <div className="flex flex-col items-end gap-1">
      <button type="button" className={className} onClick={handleClick} disabled={pending}>
        {pending ? (pendingLabel ?? "Working") : label}
      </button>
      {error ? <span className="text-xs text-error">{error}</span> : null}
    </div>
  );
}
