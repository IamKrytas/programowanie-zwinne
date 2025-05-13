import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  role: string;
  sub: string;
  [key: string]: any;
}

export const decodeAccessToken = (accessToken: string): void => {
  try {
    const decoded = jwtDecode<DecodedToken>(accessToken);

    const { role, sub, exp } = decoded;

    if (role && sub && exp) {
      sessionStorage.setItem("role", role);
      sessionStorage.setItem("sub", sub);
      sessionStorage.setItem("exp", exp.toString());
    } else {
      console.warn("Nie udało się zapisać danych z tokena");
    }
  } catch (error: any) {
    console.error("Błąd dekodowania tokena:", error);
  }
};

export const decodeRefreshToken = (refreshToken: string): void => {
  try {
    const decoded = jwtDecode<DecodedToken>(refreshToken);

    const { role, sub, exp } = decoded;

    if (role && sub && exp) {
      sessionStorage.setItem("role", role);
      sessionStorage.setItem("sub", sub);
      sessionStorage.setItem("exp", exp.toString());
    } else {
      console.warn("Nie udało się zapisać danych z tokena");
    }
  } catch (error: any) {
    console.error("Błąd dekodowania tokena:", error);
  }
};