import { useContext } from "react";
import {
    createBrowserRouter, RouterProvider
} from "react-router-dom";
import RequireAuth from "./components/Auth/RequireAuth";
import ChangePasswordPage from "./pages/ChangePassword";
import Configuration from "./pages/Configuration";
import ErrorPage from "./pages/Error";
import ForgotPasswordPage from "./pages/ForgotPassword";
import HomePage, { loader as homeJobsLoader } from "./pages/Home";
import JobDetailsPage, { loader as jobDetaiLoader } from "./pages/JobDetails";
import LoginPage, { action as loginAction } from "./pages/Login";
import RegistrationPage, {
    action as registrationAction
} from "./pages/Registration";
import RootLayout from "./pages/Root";
import { AuthContext } from "./store/authContext";

function App() {
  const authCtx = useContext(AuthContext);

  const router = createBrowserRouter([
    {
      path: "/",
      element: <RootLayout />,
      errorElement: <ErrorPage />,
      children: [
        { index: true, element: <HomePage />, loader: homeJobsLoader },
        {
          path: "/registration",
          element: <RegistrationPage />,
          action: registrationAction,
        },
        {
          path: "/login",
          element: <LoginPage />,
          action: loginAction(authCtx),
        },
        { path: "/forgot-password", element: <ForgotPasswordPage /> },
        { path: "/change-password", element: <ChangePasswordPage /> },
        {
          path: "/configuration",
          element: (
            <RequireAuth>
              <Configuration />
            </RequireAuth>
          ),
        },
        {
          path: "/jobs/:jobId",
          element: <JobDetailsPage />,
          loader: jobDetaiLoader,
        },
      ],
    },
  ]);

  return <RouterProvider router={router} />;
}

export default App;
