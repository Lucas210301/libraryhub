import "./globals.css";
import NavBar from "@/components/NavBar";

export const metadata = {
  title: "LibraryHub",
  description: "Loan management for the university library"
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className="min-h-screen bg-base-200">
        <NavBar />
        <main className="mx-auto w-full max-w-5xl px-4 py-10">{children}</main>
      </body>
    </html>
  );
}
