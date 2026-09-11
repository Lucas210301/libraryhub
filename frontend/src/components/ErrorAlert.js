export default function ErrorAlert({ message }) {
  return (
    <div role="alert" data-testid="form-error" className="alert alert-error text-sm">
      {message}
    </div>
  );
}
