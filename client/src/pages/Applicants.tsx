import { Link } from "react-router-dom";

const data: { name: string, status: string }[] = [
  { name: 'Full Stack', status: 'Pending' },
  { name: 'Full Stack', status: 'Pending' },
  { name: 'Full Stack', status: 'Pending' }
]

const ApplicantsPage = () => {
  return (
    <>
      <h1>Applicants</h1>
      <table>
        <thead>
          <tr>
            <th>Job Offer</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {data.map(row =>
            <tr>
              <td><Link to="">{row.name}</Link></td>
            </tr>
          )}
        </tbody>
      </table>

    </>
  )
}

export default ApplicantsPage;
