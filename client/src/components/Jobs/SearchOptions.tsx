function SearchOptions() {
  return (
    <>
      <div>
        <label htmlFor="salary">Salary</label>
        <input type="range" name="salary" id="salary" min={0} max={100} />
      </div>
      <fieldset>
        <legend>Type of Work</legend>
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
      <fieldset>
        <legend>Type of Workday</legend>
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
    </>
  );
}

export default SearchOptions;
