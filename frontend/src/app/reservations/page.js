import Link from "next/link";
import ActionButton from "@/components/ActionButton";
import EmptyState from "@/components/EmptyState";
import ErrorAlert from "@/components/ErrorAlert";
import { cancelReservation, listReservations } from "@/services/reservationActions";

export const dynamic = "force-dynamic";

const statusStyles = {
  ACTIVE: "badge badge-warning",
  FULFILLED: "badge badge-success",
  CANCELLED: "badge badge-ghost"
};

export default async function ReservationsPage() {
  const { data: reservations = [], error } = await listReservations();

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold">Reservations</h1>
          <p className="text-sm opacity-70">
            The queue for items that are out. The first member in line borrows it after the return.
          </p>
        </div>
        <Link href="/reservations/create" className="btn btn-primary">
          Create reservation
        </Link>
      </header>

      {error ? <ErrorAlert message={error} /> : null}

      {!error && reservations.length === 0 ? (
        <EmptyState
          message="No reservations registered yet."
          actionHref="/reservations/create"
          actionLabel="Create the first reservation"
        />
      ) : null}

      {reservations.length > 0 ? (
        <div className="overflow-x-auto rounded-lg bg-base-100 shadow-sm">
          <table className="table">
            <thead>
              <tr>
                <th>Item</th>
                <th>Member</th>
                <th>Created</th>
                <th>Status</th>
                <th className="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map((reservation) => (
                <tr key={reservation.id}>
                  <td className="font-medium">{reservation.item.title}</td>
                  <td>{reservation.member.name}</td>
                  <td>{reservation.createdAt}</td>
                  <td>
                    <span className={statusStyles[reservation.status]}>{reservation.status}</span>
                  </td>
                  <td>
                    <div className="flex items-start justify-end">
                      {reservation.active ? (
                        <ActionButton
                          action={cancelReservation.bind(null, reservation.id)}
                          label="Cancel"
                          pendingLabel="Cancelling"
                          confirmMessage={`Cancel the reservation of ${reservation.item.title}?`}
                          className="btn btn-sm btn-outline btn-error"
                        />
                      ) : null}
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
