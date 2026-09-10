import Link from "next/link";

export default function EmptyState({ message, actionHref, actionLabel }) {
  return (
    <div className="flex flex-col items-center gap-4 rounded-lg bg-base-100 p-12 text-center shadow-sm">
      <p className="text-sm opacity-70">{message}</p>
      {actionHref ? (
        <Link href={actionHref} className="btn btn-primary btn-sm">
          {actionLabel}
        </Link>
      ) : null}
    </div>
  );
}
