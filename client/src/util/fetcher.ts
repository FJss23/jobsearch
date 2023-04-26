import { store } from "../store/store";

async function refreshTokenReq() {
  try {
    const response = await fetch(
      "http://localhost:8080/api/v1/auth/access-token",
      {
        method: "POST",
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error("Failed to refresh the access token");
    }

    const content = await response.json();
    return content.token;
  } catch (err) {
    throw new Error("Failed to refresh the access token");
  }
}

export async function authFetch(
  url: string,
  options: RequestInit | undefined,
  retry = true
): Promise<Response> {
  const accessToken = store.getState().auth.token;

  const headers = {
    ...(options?.headers || {}),
    Authorization: `Bearer ${accessToken}`,
  };

  const response = await fetch(url, { ...options, headers });

  if (response.status === 401 && retry) {
    const newAccessToken = await refreshTokenReq();
    headers.Authorization = `Bearer ${newAccessToken}`;

    const response = await fetch(url, { ...options, headers });
    return response
  }

  return response;
}
