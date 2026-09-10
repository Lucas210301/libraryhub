import ErrorAlert from "@/components/ErrorAlert";
import ReservationForm from "@/components/ReservationForm";
import { listItems } from "@/services/itemActions";
import { listMembers } from "@/services/memberActions";

export const dynamic = "force-dynamic";

export default async function CreateReservationPage() {
  const [memberResult, itemResult] = await Promise.all([listMembers(), listItems()]);
  const error = memberResult.error ?? itemResult.error;
  const itemsOnLoan = (itemResult.data ?? []).filter((item) => !item.available);

  return (
    <section className="space-y-6">
      <header className="space-y-1">
        <h1 className="text-2xl font-semibold">Create reservation</h1>
        <p className="text-sm opacity-70">An available item can be borrowed right away, so it cannot be reserved.</p>
      </header>
      {error ? (
        <ErrorAlert message={error} />
      ) : (
        <ReservationForm members={memberResult.data ?? []} items={itemsOnLoan} />
      )}
    </section>
  );
}
