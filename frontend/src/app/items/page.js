import Link from "next/link";
import ActionButton from "@/components/ActionButton";
import EmptyState from "@/components/EmptyState";
import ErrorAlert from "@/components/ErrorAlert";
import { listItems, removeItem } from "@/services/itemActions";

export const dynamic = "force-dynamic";

export default async function ItemsPage() {
  const { data: items = [], error } = await listItems();

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold">Items</h1>
          <p className="text-sm opacity-70">Books and magazines available in the collection.</p>
        </div>
        <Link href="/items/create" className="btn btn-primary">
          Add item
        </Link>
      </header>

      {error ? <ErrorAlert message={error} /> : null}

      {!error && items.length === 0 ? (
        <EmptyState message="No items registered yet." actionHref="/items/create" actionLabel="Add the first item" />
      ) : null}

      {items.length > 0 ? (
        <div className="overflow-x-auto rounded-lg bg-base-100 shadow-sm">
          <table className="table">
            <thead>
              <tr>
                <th>Title</th>
                <th>Type</th>
                <th>Details</th>
                <th>Loan period</th>
                <th>Status</th>
                <th className="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.id}>
                  <td className="font-medium">{item.title}</td>
                  <td>{item.type}</td>
                  <td className="text-sm opacity-70">{item.description}</td>
                  <td>{item.loanDurationInDays} days</td>
                  <td>
                    <span className={item.available ? "badge badge-success" : "badge badge-ghost"}>
                      {item.available ? "Available" : "On loan"}
                    </span>
                  </td>
                  <td>
                    <div className="flex items-start justify-end">
                      <ActionButton
                        action={removeItem.bind(null, item.id)}
                        label="Remove"
                        pendingLabel="Removing"
                        confirmMessage={`Remove ${item.title}?`}
                        className="btn btn-sm btn-outline btn-error"
                      />
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : null}
    </section>
  );
}
