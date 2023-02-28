import { Outlet } from "react-router-dom";
import MainNavigation from "../components/navigation/MainNavigation";

function RootLayout() {
  return (
    <>
      <main>
        <MainNavigation />
        <Outlet />
      </main>
    </>
  )
}

export default RootLayout;
