import DamageFineForm from "@/components/DamageFineForm";
import ErrorAlert from "@/components/ErrorAlert";
import { listLoans } from "@/services/loanActions";

export const dynamic = "force-dynamic";

export default async function CreateFinePage() {
  const { data: loans = [], error } = await listLoans();

  return (
    <section className="space-y-6">
      <header className="space-y-1">
        <h1 className="text-2xl font-semibold">Register damage</h1>
        <p className="text-sm opacity-70">Each loan can carry a single damage fine.</p>
      </header>
      {error ? <ErrorAlert message={error} /> : <DamageFineForm loans={loans} />}
    </section>
  );
}
