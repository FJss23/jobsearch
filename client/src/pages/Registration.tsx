function RegistrationPage() {
  return (
    <form>
      <fieldset>
        <legend>Account type</legend>
        <div>
          <label htmlFor="company">Company</label>
          <input type="radio" name="accountType" id="company" />
        </div>
        <div>
          <label htmlFor="candidate">Candidate</label>
          <input
            type="radio"
            name="accountType"
            id="candidate"
            defaultChecked={true}
          />
        </div>
      </fieldset>
      <div>
        <label htmlFor="firstName">First Name</label>
        <input type="text" name="firstName" id="fistName" />
      </div>
      <div>
        <label htmlFor="lastName">Last Name</label>
        <input type="text" name="lastName" id="lastName" />
      </div>
      <div>
        <label htmlFor="email">Email</label>
        <input type="email" name="email" id="email" />
      </div>
      <div>
        <label htmlFor="password">Password</label>
        <input type="password" name="password" id="password" />
      </div>
      <div>
        <label htmlFor="repeatPassword">Repeat Password</label>
        <input
          type="repeatPassword"
          name="repeatPassword"
          id="repeatPassword"
        />
      </div>
      <button type="submit">Sing up</button>
    </form>
  );
}

export default RegistrationPage;
