import MemberForm from "@/components/MemberForm";

export default function CreateMemberPage() {
  return (
    <section className="space-y-6">
      <header className="space-y-1">
        <h1 className="text-2xl font-semibold">Add member</h1>
        <p className="text-sm opacity-70">Each email can belong to a single member.</p>
      </header>
      <MemberForm />
    </section>
  );
}
