import Link from "next/link";

const links = [
  { href: "/members", label: "Members" },
  { href: "/items", label: "Items" },
  { href: "/loans", label: "Loans" },
  { href: "/reservations", label: "Reservations" },
  { href: "/fines", label: "Fines" }
];

export default function NavBar() {
  return (
    <header className="navbar bg-base-100 shadow-sm">
      <div className="mx-auto flex w-full max-w-5xl flex-wrap items-center gap-4 px-4">
        <Link href="/" className="text-lg font-semibold">
          LibraryHub
        </Link>
        <nav className="flex flex-wrap gap-1">
          {links.map((link) => (
            <Link key={link.href} href={link.href} className="btn btn-ghost btn-sm">
              {link.label}
            </Link>
          ))}
        </nav>
      </div>
    </header>
  );
}
