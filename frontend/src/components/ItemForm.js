"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { createBook, createMagazine } from "@/services/itemActions";

export default function ItemForm() {
  const router = useRouter();
  const [type, setType] = useState("BOOK");
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const title = formData.get("title");

    setSaving(true);
    setError(null);
    const result =
      type === "BOOK"
        ? await createBook({ title, author: formData.get("author"), isbn: formData.get("isbn") })
        : await createMagazine({
            title,
            publisher: formData.get("publisher"),
            edition: Number(formData.get("edition"))
          });
    setSaving(false);

    if (result.error) {
      setError(result.error);
      return;
    }

    router.push("/items");
    router.refresh();
  }

  return (
    <form onSubmit={handleSubmit} className="card bg-base-100 shadow-sm">
      <div className="card-body gap-5">
        {error ? (
          <div role="alert" className="alert alert-error text-sm">
            {error}
          </div>
        ) : null}

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Type</span>
          <select
            name="type"
            value={type}
            onChange={(event) => setType(event.target.value)}
            className="select w-full"
          >
            <option value="BOOK">Book, 15 days</option>
            <option value="MAGAZINE">Magazine, 7 days</option>
          </select>
        </label>

        <label className="flex flex-col gap-1">
          <span className="text-sm font-medium">Title</span>
          <input name="title" className="input w-full" required />
        </label>

        {type === "BOOK" ? (
          <>
            <label className="flex flex-col gap-1">
              <span className="text-sm font-medium">Author</span>
              <input name="author" className="input w-full" required />
            </label>
            <label className="flex flex-col gap-1">
              <span className="text-sm font-medium">ISBN</span>
              <input name="isbn" className="input w-full" required />
            </label>
          </>
        ) : (
          <>
            <label className="flex flex-col gap-1">
              <span className="text-sm font-medium">Publisher</span>
              <input name="publisher" className="input w-full" required />
            </label>
            <label className="flex flex-col gap-1">
              <span className="text-sm font-medium">Edition</span>
              <input name="edition" type="number" min="1" className="input w-full" required />
            </label>
          </>
        )}

        <div className="flex justify-end gap-2">
          <Link href="/items" className="btn btn-ghost">
            Cancel
          </Link>
          <button type="submit" className="btn btn-primary" disabled={saving}>
            {saving ? "Saving" : "Save item"}
          </button>
        </div>
      </div>
    </form>
  );
}
