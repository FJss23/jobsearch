import { Company } from "../components/jobs/Job";

const company: Company = {
  id: "1",
  name: "Best company in the world",
  description: "This is the best company in the world, and you know it",
  logoUrl:
    "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.fineartamerica.com%2Fimages%2Fartworkimages%2Fmediumlarge%2F2%2F1-sunny-day-in-spring-countryside-landscape-gone-with-the-wind.jpg&f=1&nofb=1&ipt=2ca679b984ac68797e7e05d6bbb8039bf149279fa922967f3ddab679729691b9&ipo=images",
  twitter: "https://twitter.com",
  facebook: "https://facebook.com",
  instagram: "https://instagram.com",
  website: "https://website.com",
};

const CompanyDetailPage = () => {
  return (
    <>
      <h1>{company.name}</h1>
      <section>
        <ul>
          {company.twitter && (
            <li>
              <a href={`${company.twitter}`}>twitter</a>
            </li>
          )}
          {company.facebook && (
            <li>
              <a href={`${company.facebook}`}>facebook</a>
            </li>
          )}
          {company.instagram && (
            <li>
              <a href={`${company.instagram}`}>instagram</a>
            </li>
          )}
          {company.website && (
            <li>
              <a href={`${company.website}`}>website</a>
            </li>
          )}
        </ul>
      </section>
      <section>
        <img
          src={`${company.logoUrl}`}
          alt={`Logo of the company ${company.name}`}
          width="200px"
          height="200px"
        />
        <p>{company.description}</p>
      </section>
    </>
  );
};

export default CompanyDetailPage;
