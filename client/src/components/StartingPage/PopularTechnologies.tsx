import styles from "./PopularTechnologies.module.css";

const technologies = [
  { id: "1", name: "Java" },
  { id: "2", name: "AWS" },
  { id: "3", name: "Javascript" },
  { id: "4", name: "Typescript" },
  { id: "5", name: "NodeJS" },
  { id: "6", name: "Spring Boot" },
  { id: "7", name: "React" },
  { id: "8", name: "React Native" },
];

function PopularTechnologies() {
  return (
    <section>
      <h2>Popular Technologies</h2>
      <ul className={styles.technologies}>
        {technologies.map((tech) => (
          <li key={tech.id} className={styles.tech}>{tech.name}</li>
        ))}
      </ul>
    </section>
  );
}

export default PopularTechnologies;
