import { Provider } from "react-redux";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import RequireAuth from "./components/Auth/RequireAuth";
import ChangePasswordPage from "./pages/ChangePassword";
import Configuration from "./pages/Configuration";
import ErrorPage from "./pages/Error";
import ForgotPasswordPage from "./pages/ForgotPassword";
import HomePage, { loader as homeLoader } from "./pages/Home";
import JobDetailsPage, { loader as jobDetaiLoader } from "./pages/JobDetails";
import LoginPage, { action as loginAction } from "./pages/Login";
import RegistrationPage, {
  action as registrationAction,
} from "./pages/Registration";
import RootLayout from "./pages/Root";
import StatisticsPage from "./pages/Statistics";
import Welcome from "./pages/Welcome";
import { store } from "./store/store";
import { ADMIN, CANDIDATE } from "./types/Roles";

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    errorElement: <ErrorPage />,
    children: [
      {
        index: true,
        element: <HomePage />,
        loader: homeLoader,
      },
      {
        path: "/jobs/:jobId",
        element: <JobDetailsPage />,
        loader: jobDetaiLoader,
      },
      {
        path: "/statistics",
        element: <StatisticsPage />,
      },
      {
        path: "/registration",
        element: <RegistrationPage />,
        action: registrationAction,
      },
      {
        path: "/login",
        element: <LoginPage />,
        action: loginAction,
      },
      { path: "/forgot-password", element: <ForgotPasswordPage /> },
      { path: "/change-password", element: <ChangePasswordPage /> },
      {
        path: "/admin",
        element: <RequireAuth role={ADMIN} />,
        children: [
          {
            path: "welcome",
            element: <Welcome />,
          },
        ],
      },
      {
        path: "/user",
        element: <RequireAuth role={CANDIDATE} />,
        children: [
          {
            path: "configuration",
            element: <Configuration />,
          },
        ],
      },
    ],
  },
]);

function App() {
  return (
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  );
}

export default App;
