function ChangePasswordPage() {
  return (
    <form>
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
    </form>
  );
}

export default ChangePasswordPage;
