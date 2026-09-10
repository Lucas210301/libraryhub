import Link from "next/link";
import ActionButton from "@/components/ActionButton";
import EmptyState from "@/components/EmptyState";
import ErrorAlert from "@/components/ErrorAlert";
import { listLoans, returnLoan } from "@/services/loanActions";

export const dynamic = "force-dynamic";

function statusOf(loan) {
  if (!loan.active) {
    return { label: "Returned", className: "badge badge-ghost" };
  }
  return loan.overdue
    ? { label: "Overdue", className: "badge badge-error" }
    : { label: "On loan", className: "badge badge-warning" };
}

export default async function LoansPage() {
  const { data: loans = [], error } = await listLoans();

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold">Loans</h1>
          <p className="text-sm opacity-70">Every item currently out and the ones already returned.</p>
        </div>
        <Link href="/loans/create" className="btn btn-primary">
          Create loan
        </Link>
      </header>

      {error ? <ErrorAlert message={error} /> : null}

      {!error && loans.length === 0 ? (
        <EmptyState message="No loans registered yet." actionHref="/loans/create" actionLabel="Create the first loan" />
      ) : null}

      {loans.length > 0 ? (
        <div className="overflow-x-auto rounded-lg bg-base-100 shadow-sm">
          <table className="table">
            <thead>
              <tr>
                <th>Item</th>
                <th>Member</th>
                <th>Loan date</th>
                <th>Due date</th>
                <th>Status</th>
                <th className="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {loans.map((loan) => {
                const status = statusOf(loan);
                return (
                  <tr key={loan.id}>
                    <td className="font-medium">{loan.item.title}</td>
                    <td>{loan.member.name}</td>
                    <td>{loan.loanDate}</td>
                    <td>{loan.dueDate}</td>
                    <td>
                      <span className={status.className}>{status.label}</span>
                    </td>
                    <td>
                      <div className="flex items-start justify-end">
                        {loan.active ? (
                          <ActionButton
                            action={returnLoan.bind(null, loan.id)}
                            label="Register return"
                            pendingLabel="Saving"
                            className="btn btn-sm btn-outline"
                          />
                        ) : (
                          <span className="text-sm opacity-70">{loan.returnDate}</span>
                        )}
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      ) : null}
    </section>
  );
}
