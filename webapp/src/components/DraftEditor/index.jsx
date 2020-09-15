import {
    Editor,
    EditorState,
    Modifier,
    SelectionState,
    convertToRaw,
    convertFromRaw,
    ContentBlock,
    genKey
} from 'draft-js';
import React, { useEffect, useState, useRef } from 'react';
import './index.scss';
import {
    addBulletPoint,
    removeStylesForSelection,
    addBlock
} from './editorHelper';
const colorStyleMap = {
    red: {
        color: 'rgba(255, 0, 0, 1.0)'
    },
    orange: {
        color: 'rgba(255, 127, 0, 1.0)'
    },
    yellow: {
        color: 'rgba(180, 180, 0, 1.0)'
    },
    green: {
        color: 'rgba(0, 180, 0, 1.0)'
    },
    blue: {
        color: 'rgba(0, 0, 255, 1.0)'
    },
    indigo: {
        color: 'rgba(75, 0, 130, 1.0)'
    },
    violet: {
        color: 'rgba(127, 0, 255, 1.0)'
    },
    toImprove: {
        textDecoration: 'underline',
        textDecorationColor: 'red'
    }
};

const DraftEditor = ({ texts, handleChangeCallback, label, eventName }) => {
    const [localState, _setLocalState] = useState(EditorState.createEmpty());
    const localStateRef = useRef(localState);

    const setLocalState = (data) => {
        localStateRef.current = data;
        _setLocalState(data);
    };

    const didMount = useRef(false);

    useEffect(() => {
        const lines = texts.split('\n');
        init(lines);

        return () => {
            window.removeEventListener(eventName, eventListenerCallback);
        };
    }, []);

    const init = (lines) => {
        const rawContentState = {
            entityMap: {},
            blocks: lines.map((line) => ({
                key: genKey(),
                text: line,
                type: 'unordered-list-item',
                depth: 0,
                inlineStyleRanges: [],
                entityRanges: []
            }))
        };
        setLocalState(
            EditorState.createWithContent(convertFromRaw(rawContentState))
        );
        didMount.current = true;

        window.addEventListener(eventName, eventListenerCallback);
    };

    const eventListenerCallback = (e) => {
        {
            const { data, type } = e.detail;
            const blocks = convertToRaw(
                localStateRef.current.getCurrentContent()
            ).blocks;
            const blockLength = blocks.length;

            switch (type) {
                case 'highlight-keyword':
                    const { offsets, text } = data
                    if (blockLength === 1 && blocks[0].text === '') {
                        const rawContentState = {
                            entityMap: {},
                            blocks: [
                                {
                                    key: genKey(),
                                    text,
                                    type: 'unordered-list-item',
                                    depth: 0,
                                    inlineStyleRanges: [...offsets],
                                    entityRanges: []
                                }
                            ]
                        };
                        setLocalState(
                            EditorState.createWithContent(
                                convertFromRaw(rawContentState)
                            )
                        );
                        handleChangeCallback(text);
                    } else {
                        setLocalState(
                            addBlock(localStateRef.current, text, offsets)
                        );
                        const rawContentState = convertToRaw(
                            addBlock(
                                localStateRef.current,
                                text,
                                offsets
                            ).getCurrentContent()
                        );
                        const value = rawContentState.blocks.reduce(
                            (acc, cur, index) => {
                                return index === 0
                                    ? acc + cur.text
                                    : acc + '\n' + cur.text;
                            },
                            ''
                        );
                        handleChangeCallback(value);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    const handleChange = (editorState) => {
        // update local state
        setLocalState(removeStylesForSelection(addBulletPoint(editorState)));

        const rawContentState = convertToRaw(editorState.getCurrentContent());
        const value = rawContentState.blocks.reduce((acc, cur, index) => {
            return index === 0 ? acc + cur.text : acc + '\n' + cur.text;
        }, '');

        // update store
        handleChangeCallback(value);
    };

    return (
        <div className="draftjs-editor-container form_item">
            <label className="form-label">{label}</label>
            <Editor
                customStyleMap={colorStyleMap}
                spellCheck={true}
                editorState={localState}
                onChange={handleChange}
            ></Editor>
        </div>
    );
};

export default DraftEditor;