import { createContext, ReactNode, useState } from "react";
import { User } from "../types/User";

export type AuthContextType = {
  isLoggedIn: boolean;
  authUser?: User;
  onLogout: () => void;
  onLogin: (user: User) => void;
};

export const AuthContext = createContext<AuthContextType>({
  isLoggedIn: false,
  authUser: undefined,
  onLogout: () => {},
  onLogin: () => {},
});

interface Props {
  children: ReactNode;
}

export const AuthContextProvider = ({ children }: Props) => {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [authUser, setAuthUser] = useState<User | undefined>(undefined);

  const loginHandler = (user: User) => {
    setIsLoggedIn(true);
    setAuthUser(user);
    console.log('login', user);
  };

  const logoutHandler = () => {
    setIsLoggedIn(false);
    setAuthUser(undefined);
    console.log('logout',);
  };

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn: isLoggedIn,
        onLogout: logoutHandler,
        onLogin: loginHandler,
        authUser: authUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
