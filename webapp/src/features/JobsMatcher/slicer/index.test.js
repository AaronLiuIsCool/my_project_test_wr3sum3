import jobsMatcherReducer, { addBookmark, removeBookmark, setBookmarkedList } from '.';

describe("jobsMatcher slicer tests", () => {
  const initialState = {
    bookmarked: []
  };
  test("it should return correct initial state", () => {
    expect(jobsMatcherReducer(undefined, {})).toEqual(initialState);
  });

  test("it should get correct action for addBookmark", () => {
    expect(addBookmark("test")).toEqual({
      "payload": "test",
      "type": "jobs/addBookmark"
    });
  });

  test("it should get correct action for removeBookmark", () => {
    expect(removeBookmark("test")).toEqual({
      "payload": "test",
      "type": "jobs/removeBookmark"
    });
  });

  test("it should get correct action for setBookmarkedList", () => {
    expect(setBookmarkedList("test")).toEqual({
      "payload": "test",
      "type": "jobs/setBookmarkedList"
    });
  });

  test("it should update bookmarked correctly", () => {
    let state = jobsMatcherReducer(initialState, addBookmark('test'));
    expect(state).toEqual({
      "bookmarked": ["test"]
    });

    state = jobsMatcherReducer(state, addBookmark('test1'))
    expect(state).toEqual({
      "bookmarked": ["test", "test1"]
    });

    state = jobsMatcherReducer(state, addBookmark('test1'))
    expect(state).toEqual({
      "bookmarked": ["test", "test1"]
    });

    state = jobsMatcherReducer(state, removeBookmark('test1'))
    expect(state).toEqual({
      "bookmarked": ["test"]
    });

    state = jobsMatcherReducer(state, removeBookmark('3090'))
    expect(state).toEqual({
      "bookmarked": ["test"]
    });

    state = jobsMatcherReducer(state, setBookmarkedList(['id1', 'id2', 'id3']))
    expect(state).toEqual({
      "bookmarked": ['id1', 'id2', 'id3']
    });
  });
});
