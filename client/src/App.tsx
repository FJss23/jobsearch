import React from "react";
import { Provider } from "react-redux";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ApplicationsPage, {
  loader as applicationsLoader,
} from "./pages/Applications";
import ChangePasswordPage from "./pages/ChangePassword";
import CompanyDetailPage from "./pages/CompanyDetail";
import ErrorPage from "./pages/Error";
import ForgotPasswordPage from "./pages/ForgotPassword";
import HomePage, { loader as homeJobsLoader } from "./pages/Home";
import JobApplicationsPage from "./pages/JobApplications";
import JobDetailsPage, { loader as jobDetaiLoader } from "./pages/JobDetails";
import JobsPage, { loader as jobsLoader } from "./pages/Jobs";
import LoginPage, { action as loginAction } from "./pages/Login";
import NewCvPage from "./pages/NewCv";
import RegistrationPage, {
  action as registrationAction,
} from "./pages/Registration";
import RootLayout from "./pages/Root";
import { store } from "./store/store";

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
      { path: "/login", element: <LoginPage />, action: loginAction },
      { path: "/forgot-password", element: <ForgotPasswordPage /> },
      { path: "/change-password", element: <ChangePasswordPage /> },
      { path: "/jobs", element: <JobsPage />, loader: jobsLoader },
      {
        path: "/jobs/:jobId",
        element: <JobDetailsPage />,
        loader: jobDetaiLoader,
      },
      { path: "/company/:companyId", element: <CompanyDetailPage /> },
      { path: "/cv/new", element: <NewCvPage /> },
      {
        path: "/applications",
        element: <ApplicationsPage />,
        loader: applicationsLoader,
      },
      { path: "/applicants", element: <JobApplicationsPage /> },
    ],
  },
]);

function App() {
  return (
    <React.StrictMode>
      <Provider store={store}>
        <RouterProvider router={router} />
      </Provider>
    </React.StrictMode>
  );
}

export default App;
