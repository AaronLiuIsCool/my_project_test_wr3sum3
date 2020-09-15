import {
  EditorState,
  Modifier,
  SelectionState,
  ContentBlock,
  genKey,
} from "draft-js";
// helper functions implemented by SangYu
export const addBulletPoint = (editorState) => {
  const contentState = editorState.getCurrentContent();
  let contentStateWithBulletPoint = contentState;
  let blocksMap = contentState.getBlockMap();
  blocksMap.forEach((block) => {
    if (block.getType() === "unordered-list-item") {
      return;
    }

    const selectionState = SelectionState.createEmpty(block.getKey());
    const updateSelection = selectionState.merge({
      anchorOffset: 0,
      focusOffset: block.getText().length,
    });
    contentStateWithBulletPoint = Modifier.setBlockType(
      contentStateWithBulletPoint,
      updateSelection,
      "unordered-list-item"
    );
  });
  return EditorState.push(
    editorState,
    contentStateWithBulletPoint,
    "change-block-type"
  );
};
export const removeStylesForSelection = (editorState) => {
  const contentState = editorState.getCurrentContent();
  const styles = editorState.getCurrentInlineStyle();
  const selection = editorState.getSelection();
  const contentStateWithoutStyle = contentState;
  const removeStyles = styles.reduce(
    (state, style) => Modifier.removeInlineStyle(state, selection, style),
    contentStateWithoutStyle
  );
  return EditorState.push(editorState, removeStyles, "change-inline-style");
};
export const addBlock = (editorState, text, ranges) => {
  const newBlock = new ContentBlock({
    key: genKey(),
    type: "unordered-list-item",
    text,
  });
  const contentState = editorState.getCurrentContent();
  const newBlockMap = contentState.getBlockMap().set(newBlock.key, newBlock);
  let newContentState = contentState.set("blockMap", newBlockMap);
  editorState = EditorState.push(editorState, newContentState, "add-block");

  ranges.forEach((range) => {
    let selectionState = SelectionState.createEmpty(newBlock.key);
    selectionState = selectionState.merge({
      anchorOffset: range.offset,
      focusOffset: range.offset + range.length,
    });
    newContentState = Modifier.applyInlineStyle(
      newContentState,
      selectionState,
      "green"
    );
  });
  return EditorState.push(editorState, newContentState, "change-inline-styles");
};
