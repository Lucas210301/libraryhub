import ErrorAlert from "@/components/ErrorAlert";
import MemberForm from "@/components/MemberForm";
import { findMember } from "@/services/memberActions";

export const dynamic = "force-dynamic";

export default async function EditMemberPage({ params }) {
  const { id } = await params;
  const { data: member, error } = await findMember(id);

  return (
    <section className="space-y-6">
      <header className="space-y-1">
        <h1 className="text-2xl font-semibold">Edit member</h1>
        <p className="text-sm opacity-70">Change the name or the email of this member.</p>
      </header>
      {error ? <ErrorAlert message={error} /> : <MemberForm member={member} />}
    </section>
  );
}
