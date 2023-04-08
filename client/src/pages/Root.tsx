import { Outlet } from "react-router-dom";
import Footer from "../components/Layout/Footer";
import MainHeader from "../components/Layout/MainHeader";
import styles from "./Root.module.css";

function RootLayout() {
  return (
    <>
      <MainHeader />
      <main className={styles.content}>
        <Outlet />
      </main>
      <Footer />
    </>
  );
}

export default RootLayout;
