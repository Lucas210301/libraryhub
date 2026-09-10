export function extractErrorMessage(error) {
  if (error?.response) {
    return error.response.data?.message ?? `The request failed with status ${error.response.status}.`;
  }
  return "The server is unavailable. Start the backend and try again.";
}
