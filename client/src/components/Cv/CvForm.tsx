import { useState } from "react";

const CvForm = () => {
  const [experienceBlockCount, setExperienceBlockCount] = useState(0);
  const [educationBlockCount, setEducationBlockCount] = useState(0);

  const AddExperienceBlock = () => {
    return (
      <>
        <div>
          <label htmlFor="company">Company's name</label>
          <input type="text" name="company" id="company" />
        </div>
        <div>
          <label htmlFor="dateFrom">From</label>
          <input type="date" name="dateFrom" id="dateFrom" />
        </div>
        <div>
          <label htmlFor="dateTo">To</label>
          <input type="date" name="dateTo" id="dateTo" />
        </div>
        <div>
          <label htmlFor="description">Description</label>
          <textarea name="description" />
        </div>
        <button>Remove</button>
        <hr />
      </>
    );
  };

  const AddEducationBlock = () => {
    return (
      <>
        <div>
          <label htmlFor="company">Company's name</label>
          <input type="text" name="company" id="company" />
        </div>
        <div>
          <label htmlFor="dateFrom">From</label>
          <input type="date" name="dateFrom" id="dateFrom" />
        </div>
        <div>
          <label htmlFor="dateTo">To</label>
          <input type="date" name="dateTo" id="dateTo" />
        </div>
        <div>
          <label htmlFor="description">Description</label>
          <textarea name="description" />
        </div>
        <button>Remove</button>
        <hr />
      </>
    );
  };

  const addExperienceBlock = () => {
    setExperienceBlockCount((prev) => prev + 1);
  };

  const addEducationBlock = () => {
    setEducationBlockCount((prev) => prev + 1);
  };

  return (
    <form>
      <div>
        <label htmlFor="firstName">First name</label>
        <input type="text" name="firstName" id="firstName" disabled />
      </div>
      <div>
        <label htmlFor="lastName">Last name</label>
        <input type="text" name="lastName" id="lastName" disabled />
      </div>
      <div>
        <label htmlFor="email">Contact email</label>
        <input type="email" name="email" id="email" />
      </div>
      <div>
        <label htmlFor="phone">Phone</label>
        <input type="tel" name="phone" id="phone" />
      </div>
      <button onClick={addExperienceBlock} type="button">
        Add experience
      </button>
      <button onClick={addEducationBlock} type="button">
        Add education
      </button>
      <fieldset>
        <>
          <legend>Education</legend>
          {Array(educationBlockCount)
            .fill(1)
            .map((_block, index) => <AddEducationBlock key={index} />)}
        </>
      </fieldset>
      <fieldset>
        <>
          <legend>Experience</legend>
          {Array(experienceBlockCount)
            .fill(1)
            .map((_block, index) => <AddExperienceBlock key={index} />)}
        </>
      </fieldset>
      <div>
        <label htmlFor="activities">Activities</label>
        <textarea name="activities" />
      </div>
      <button type="submit">Save</button>
    </form>
  );
};

export default CvForm;
