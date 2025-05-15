import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  role: string;
  sub: string;
  exp: number;
  [key: string]: any;
}

export const decodeAccessToken = (accessToken: string): void => {
  try {
    const decoded = jwtDecode<DecodedToken>(accessToken);

    const { role, sub, exp } = decoded;

    if (role && sub && exp) {
      sessionStorage.setItem("accessRole", role);
      sessionStorage.setItem("accessSub", sub);
      sessionStorage.setItem("accessExp", exp.toString());
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
      sessionStorage.setItem("refreshRole", role);
      sessionStorage.setItem("refreshSub", sub);
      sessionStorage.setItem("refreshExp", exp.toString());
    } else {
      console.warn("Nie udało się zapisać danych z tokena");
    }
  } catch (error: any) {
    console.error("Błąd dekodowania tokena:", error);
  }
};