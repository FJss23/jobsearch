import { JwtPayload } from "jwt-decode";

export interface User {
  firstName: string;
  email: string;
  role: string;
  accessToken?: string;
}

export interface UserJwtPayload extends JwtPayload {
  scope?: string,
  name?: string
}
