import ItemForm from "@/components/ItemForm";

export default function CreateItemPage() {
  return (
    <section className="space-y-6">
      <header className="space-y-1">
        <h1 className="text-2xl font-semibold">Add item</h1>
        <p className="text-sm opacity-70">The type defines how long the item can stay with a member.</p>
      </header>
      <ItemForm />
    </section>
  );
}
