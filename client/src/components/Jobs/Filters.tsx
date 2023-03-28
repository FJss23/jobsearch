import styles from "./Filters.module.css";

function Filters() {
  return (
    <section>
      <form>
        <div className={styles.filterSection}>
          <label htmlFor="search">
            Keywords
          </label>
          <input
            type="text"
            name="search"
            id="search"
            placeholder="Position, Company name, Tags or Keywords"
          />
        </div>
        <div className={styles.filterSection}>
          <label htmlFor="salary">Salary</label>
          <input type="range" name="salary" id="salary" min={0} max={100} />
        </div>
        <fieldset className={styles.filterSection}>
          <legend className={styles.filterTitle}>Type of Work</legend>
          <div>
            <label htmlFor="onSite">On Site</label>
            <input type="checkbox" name="onSite" id="onSite" />
          </div>
          <div>
            <label htmlFor="hybrid">Hybrid</label>
            <input type="checkbox" name="hybrid" id="hybrid" />
          </div>
          <div>
            <label htmlFor="remote">Remote</label>
            <input type="checkbox" name="remote" id="remote" />
          </div>
        </fieldset>
        <fieldset className={styles.filterSection}>
          <legend className={styles.filterTitle}>Type of Workday</legend>
          <div>
            <label htmlFor="fullTime">Full Time</label>
            <input type="checkbox" name="fullTime" id="fullTime" />
          </div>
          <div>
            <label htmlFor="partTime">Part Time</label>
            <input type="checkbox" name="partTime" id="partTime" />
          </div>
          <div>
            <label htmlFor="perHour">Per Hour</label>
            <input type="checkbox" name="perHour" id="perHour" />
          </div>
        </fieldset>
        <button type="submit">Search</button>
      </form>
    </section>
  );
}

export default Filters;
