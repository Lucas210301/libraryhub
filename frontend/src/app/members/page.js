import Link from "next/link";
import ActionButton from "@/components/ActionButton";
import EmptyState from "@/components/EmptyState";
import ErrorAlert from "@/components/ErrorAlert";
import { listMembers, removeMember } from "@/services/memberActions";

export const dynamic = "force-dynamic";

export default async function MembersPage() {
  const { data: members = [], error } = await listMembers();

  return (
    <section className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold">Members</h1>
          <p className="text-sm opacity-70">People allowed to borrow items from the library.</p>
        </div>
        <Link href="/members/create" className="btn btn-primary">
          Add member
        </Link>
      </header>

      {error ? <ErrorAlert message={error} /> : null}

      {!error && members.length === 0 ? (
        <EmptyState
          message="No members registered yet."
          actionHref="/members/create"
          actionLabel="Add the first member"
        />
      ) : null}

      {members.length > 0 ? (
        <div className="overflow-x-auto rounded-lg bg-base-100 shadow-sm">
          <table className="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th className="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {members.map((member) => (
                <tr key={member.id}>
                  <td className="font-medium">{member.name}</td>
                  <td>{member.email}</td>
                  <td>
                    <div className="flex items-start justify-end gap-2">
                      <Link href={`/members/${member.id}`} className="btn btn-sm btn-outline">
                        Edit
                      </Link>
                      <ActionButton
                        action={removeMember.bind(null, member.id)}
                        label="Remove"
                        pendingLabel="Removing"
                        confirmMessage={`Remove ${member.name}?`}
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
