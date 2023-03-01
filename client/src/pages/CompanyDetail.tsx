import { useParams } from "react-router-dom";

const CompanyDetailPage = () => {
  const params = useParams();

  return <h1>This is the company's detail page for {params.companyName} </h1>
}

export default CompanyDetailPage;
