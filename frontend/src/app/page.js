import Link from "next/link";

const sections = [
  { href: "/members", title: "Members", description: "Register the people allowed to borrow items." },
  { href: "/items", title: "Items", description: "Keep books and magazines in the collection." },
  { href: "/loans", title: "Loans", description: "Lend an item and register its return." },
  { href: "/reservations", title: "Reservations", description: "Queue for an item that is currently out." },
  { href: "/fines", title: "Fines", description: "Charge late returns and damaged items." }
];

export default function HomePage() {
  return (
    <section className="space-y-8">
      <header className="space-y-2">
        <h1 className="text-3xl font-semibold">LibraryHub</h1>
        <p className="max-w-xl text-sm opacity-70">
          Books stay out for fifteen days and magazines for seven. A member can hold three items at the same time, and
          nobody borrows again while a fine is open.
        </p>
      </header>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {sections.map((section) => (
          <Link key={section.href} href={section.href} className="card bg-base-100 shadow-sm hover:shadow-md">
            <div className="card-body gap-2">
              <h2 className="text-lg font-semibold">{section.title}</h2>
              <p className="text-sm opacity-70">{section.description}</p>
            </div>
          </Link>
        ))}
      </div>
    </section>
  );
}
