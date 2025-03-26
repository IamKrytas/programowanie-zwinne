export interface User {
  id: number;
  name: string;
  email: string;
}

export interface RegisterFormData {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}