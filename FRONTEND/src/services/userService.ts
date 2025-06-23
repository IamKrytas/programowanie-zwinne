import { Student } from "../models/Student";
import { LoginCredentials } from "../models/auth/LoginCredentials.ts";
import { decodeAccessToken, decodeRefreshToken } from "../utils/DecodeTokens";
import JwtTokenPair from "../models/auth/JwtTokenPair.ts";
import {toast} from "react-toastify";

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
export const loginUserService = async (userData: LoginCredentials): Promise<JwtTokenPair> => {
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

  const data: JwtTokenPair = await response.json();
  const accessToken = data.accessToken;
  const refreshToken = data.refreshToken
  decodeAccessToken(accessToken);
  decodeRefreshToken(refreshToken);
  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", refreshToken);
  return data;
}

export const refreshTokenService = async (): Promise<string> => {
  const refreshToken = localStorage.getItem("refreshToken");

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
  const accessToken = data.accessToken;
  localStorage.setItem("accessToken", accessToken);
  decodeAccessToken(accessToken);
  return data;
}

export const logoutUserService = async (): Promise<void> => {
  try {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("refreshSub");
    localStorage.removeItem("refreshRole");
    localStorage.removeItem("refreshExp");
    localStorage.removeItem("accessSub");
    localStorage.removeItem("accessRole");
    localStorage.removeItem("accessExp");
  }
  catch (error) {
    toast("Błąd podczas wylogowywania: " + error);
  }
  window.location.reload();
}