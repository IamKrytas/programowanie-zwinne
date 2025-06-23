import { jwtDecode } from "jwt-decode";
import {toast} from "react-toastify";

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
      localStorage.setItem("accessRole", role);
      localStorage.setItem("accessSub", sub);
      localStorage.setItem("accessExp", exp.toString());
    } else {
      console.warn("Nie udało się zapisać danych z tokena");
    }
  } catch (error: any) {
    toast("Błąd dekodowania tokena: " + error);
  }
};

export const decodeRefreshToken = (refreshToken: string): void => {
  try {
    const decoded = jwtDecode<DecodedToken>(refreshToken);

    const { role, sub, exp } = decoded;

    if (role && sub && exp) {
      localStorage.setItem("refreshRole", role);
      localStorage.setItem("refreshSub", sub);
      localStorage.setItem("refreshExp", exp.toString());
    } else {
      console.warn("Nie udało się zapisać danych z tokena");
    }
  } catch (error: any) {
    toast("Błąd dekodowania tokena: " + error);
  }
};