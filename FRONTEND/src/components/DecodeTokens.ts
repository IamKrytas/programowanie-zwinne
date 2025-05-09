import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  role: string;
  sub: string;
  [key: string]: any;
}

export const decodeAccessToken = (accessToken: string): void => {
  try {
    const decoded = jwtDecode<DecodedToken>(accessToken);

    const { role, sub } = decoded;

    if (role && sub) {
      sessionStorage.setItem("role", role);
      sessionStorage.setItem("sub", sub);
    } else {
      console.warn("Nie udało się zapisać danych z tokena:", decoded);
    }
  } catch (error: any) {
    console.error("Nie udało się zdekodować tokena:", error?.message || error);
  }
};