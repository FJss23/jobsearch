import { Link } from "react-router-dom";

const data: { name: string, status: string }[] = [
  { name: 'Full Stack', status: 'Pending' },
  { name: 'Full Stack', status: 'Pending' },
  { name: 'Full Stack', status: 'Pending' }
]

const JobApplicationsPage = () => {
  return (
    <>
      <h1>Job Applications</h1>
      <table>
        <thead>
          <tr>
            <th>Job Offer</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) =>
            <tr key={index}>
              <td><Link to="">{row.name}</Link></td>
              <td>{row.status}</td>
            </tr>
          )}
        </tbody>
      </table>
    </>
  );
};

export default JobApplicationsPage;
