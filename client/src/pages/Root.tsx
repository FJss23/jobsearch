import { Outlet } from "react-router-dom";
import Footer from "../components/Layout/Footer";
import MainNavigation from "../components/Layout/MainNavigation";
import styles from "./Root.module.css";

function RootLayout() {
  return (
    <>
      <MainNavigation />
      <main className={styles.content}>
        <Outlet />
      </main>
      <Footer />
    </>
  );
}

export default RootLayout;
