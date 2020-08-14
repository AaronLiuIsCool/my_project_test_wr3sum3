const initialState = {
    show: false
};

const reducers = {
    toggleAssistant: (state, action) => {
        if (state.assistant.trigger === action.payload.trigger) {
            state.assistant.show = false;
            delete state.assistant.trigger;
            delete state.assistant.context;
            delete state.assistant.handleAction;
        } else {
            state.assistant.show = true;
            state.assistant.trigger = action.payload.trigger
            state.assistant.context = action.payload.context;
            state.assistant.handleAction = action.payload.handleAction
        }
    }
};

const selectors = {
    selectShow: ({ resume }) => resume.assistant.show,
    selectTrigger: ({ resume }) => resume.assistant.trigger,
    selectAssistantContext: ({ resume }) => resume.assistant.context,
    selectActionHandler: ({ resume }) => resume.assistant.handleAction
};

export default {
    initialState,
    reducers,
    selectors
};
