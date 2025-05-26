import { Student } from "../models/Student";
import { User } from "../models/User";
import { decodeAccessToken, decodeRefreshToken } from "../utils/DecodeTokens";

// Register new user
export const registerUserService = async (userData: Student): Promise<string> => {
  const API_URL = "http://localhost:8080";
  const response = await fetch(`${API_URL}/api/v1/auth/register/student`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });

  if (!response.ok) {
    throw new Error(`Błąd: ${response.status}`);
  }
  const data = await response.json();
  return data;
}

// Login existing user
export const loginUserService = async (userData: User): Promise<string> => {
  const API_URL = "http://localhost:8080";
  const response = await fetch(`${API_URL}/api/v1/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });

  if (!response.ok) {
    throw new Error(`Błąd: ${response.status}`);
  }

  const data = await response.json();
  const accessToken = data.token;
  const refreshToken = data.refreshToken
  decodeAccessToken(accessToken);
  decodeRefreshToken(refreshToken);
  sessionStorage.setItem("token", accessToken);
  sessionStorage.setItem("refreshToken", refreshToken);
  return data;
}

export const refreshTokenService = async (): Promise<string> => {
  const refreshToken = sessionStorage.getItem("refreshToken");

  const API_URL = "http://localhost:8080";
  const response = await fetch(`${API_URL}/api/v1/auth/token/refresh`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${refreshToken}`,
    },
  });

  if (!response.ok) {
    throw new Error(`Błąd: ${response.status}`);
  }

  const data = await response.json();
  const accessToken = data.token;
  sessionStorage.setItem("token", accessToken);
  decodeAccessToken(accessToken);
  return data;
}

export const logoutUserService = async (): Promise<void> => {
  try {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("refreshToken");
    sessionStorage.removeItem("refreshSub");
    sessionStorage.removeItem("refreshRole");
    sessionStorage.removeItem("refreshExp");
    sessionStorage.removeItem("accessSub");
    sessionStorage.removeItem("accessRole");
    sessionStorage.removeItem("accessExp");
  }
  catch (error) {
    console.error("Błąd podczas wylogowywania:", error);
  }
  window.location.reload();
}