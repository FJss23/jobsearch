function ForgotPasswordPage() {
  return (
    <form>
      <div>
        <label htmlFor="email">Email</label>
        <input type="email" name="email" id="email" />
      </div>
      <button type="submit">Submit</button>
    </form>
  )
}

export default ForgotPasswordPage;
