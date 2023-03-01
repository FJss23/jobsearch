const SearchBar = () => {
  return (
    <form>
      <div>
        <label htmlFor="search">Search you next Job</label>
        <input
          type="text"
          name="search"
          id="search"
          placeholder="Position, Company name, Tags or Keywords"
        />
      </div>
      <button type="submit">Search</button>
    </form>
  );
};

export default SearchBar;
