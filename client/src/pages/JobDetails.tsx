import { useParams } from "react-router-dom";

const JobDetailsPage = () => {
  const params = useParams();
  return (
    <h1>
      Job Details page for {params.companyName} {params.jobTitle} {params.jobId}
    </h1>
  );
};

export default JobDetailsPage;
