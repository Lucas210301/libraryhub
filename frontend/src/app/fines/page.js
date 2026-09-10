import Link from "next/link";
import ActionButton from "@/components/ActionButton";
import EmptyState from "@/components/EmptyState";
import ErrorAlert from "@/components/ErrorAlert";
import { fineSummary, listFines, payFine } from "@/services/fineActions";

export const dynamic = "force-dynamic";

function money(value) {
  return Number(value).toFixed(2);
}

export default async function FinesPage() {
  const [fineResult, summaryResult] = await Promise.all([listFines(), fineSummary()]);
  const fines = fineResult.data ?? [];
  const error = fineResult.error ?? summaryResult.error;
  const totalUnpaid = summaryResult.data?.totalUnpaid ?? 0;

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold">Fines</h1>
          <p className="text-sm opacity-70">
            Late returns are charged automatically. Damage is registered by the staff.
          </p>
        </div>
        <Link href="/fines/create" className="btn btn-primary">
          Register damage
        </Link>
      </header>

      {error ? <ErrorAlert message={error} /> : null}

      {!error ? (
        <div className="rounded-lg bg-base-100 p-4 shadow-sm">
          <p className="text-sm opacity-70">Open balance</p>
          <p className="text-2xl font-semibold">{money(totalUnpaid)}</p>
        </div>
      ) : null}

      {!error && fines.length === 0 ? (
        <EmptyState message="No fines registered yet." actionHref="/fines/create" actionLabel="Register a damage fine" />
      ) : null}

      {fines.length > 0 ? (
        <div className="overflow-x-auto rounded-lg bg-base-100 shadow-sm">
          <table className="table">
            <thead>
              <tr>
                <th>Member</th>
                <th>Item</th>
                <th>Type</th>
                <th>Reason</th>
                <th>Amount</th>
                <th>Status</th>
                <th className="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {fines.map((fine) => (
                <tr key={fine.id}>
                  <td className="font-medium">{fine.loan.member.name}</td>
                  <td>{fine.loan.item.title}</td>
                  <td>{fine.type}</td>
                  <td className="text-sm opacity-70">{fine.reason}</td>
                  <td>{money(fine.amount)}</td>
                  <td>
                    <span className={fine.paid ? "badge badge-ghost" : "badge badge-error"}>
                      {fine.paid ? "Paid" : "Open"}
                    </span>
                  </td>
                  <td>
                    <div className="flex items-start justify-end">
                      {fine.paid ? (
                        <span className="text-sm opacity-70">{fine.paymentDate}</span>
                      ) : (
                        <ActionButton
                          action={payFine.bind(null, fine.id)}
                          label="Register payment"
                          pendingLabel="Saving"
                          className="btn btn-sm btn-outline"
                        />
                      )}
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
