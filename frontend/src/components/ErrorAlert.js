export default function ErrorAlert({ message }) {
  return (
    <div role="alert" className="alert alert-error text-sm">
      {message}
    </div>
  );
}
