import ErrorAlert from "@/components/ErrorAlert";
import LoanForm from "@/components/LoanForm";
import { listAvailableItems } from "@/services/itemActions";
import { listMembers } from "@/services/memberActions";

export const dynamic = "force-dynamic";

export default async function CreateLoanPage() {
  const [memberResult, itemResult] = await Promise.all([listMembers(), listAvailableItems()]);
  const error = memberResult.error ?? itemResult.error;

  return (
    <section className="space-y-6">
      <header className="space-y-1">
        <h1 className="text-2xl font-semibold">Create loan</h1>
        <p className="text-sm opacity-70">The due date comes from the type of the selected item.</p>
      </header>
      {error ? (
        <ErrorAlert message={error} />
      ) : (
        <LoanForm members={memberResult.data ?? []} items={itemResult.data ?? []} />
      )}
    </section>
  );
}
