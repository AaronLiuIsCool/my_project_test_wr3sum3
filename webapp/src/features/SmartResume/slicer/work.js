import {
    validateDate,
    validateString,
    validateNonEmptyString
} from "utils/validator";

import { detectChangesForAllItem } from "./common";

export function anyWorkChanges(works) {
    return detectChangesForAllItem(works, work);
}

export function validateWork(data) {
    return Object.keys(data).every(key => validateWorkEntry(key, data[key]));
}

export function validateWorkEntry(name, value) {
    switch (name) {
        case "workName":
        case "workCompanyName":
        case "workCity":
        case "workCountry":
        case "workDescription":
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return undefined;
            } else {
                return false;
            }
        case "currentWorkFlag":
            return (
                value === true ||
                value === false ||
                toString.call(value) === "[object Boolean]"
            );
        case "workStartDate":
        case "workEndDate":
            return typeof value === "string" && validateDate(value);
        default:
            return true;
    }
}

// export const work = {
//     id: undefined,
//     workName: "",
//     currentWorkFlag: false,
//     workCompanyName: "",
//     workStartDate: "",
//     workEndDate: "",
//     workDescription: "",
//     workCity: "",
//     workCountry: ""
// };

// for testing
export const work = {
    id: undefined,
    workName: "金融分析师",
    currentWorkFlag: false,
    workCompanyName: "金沙江创投，远程实习",
    workStartDate: "",
    workEndDate: "",
    workDescription: "在1个月内阅读超过2000页的中英文材料，并对资料进行整合和总结，向上司汇报核心内容。\n习期间独立完成了30页的船舶公司上市风险分析报告，并根据上司的反馈对报告进行修改和完善。\n对客户公司的内部政策进行风险评估，为客户设计并改进内部风险管控体系，对风险较高的领域提出相应建议。\n运用Excel VBA建立风险分析模型，对客户提供的数据进行量化分析，并绘制统计图表，制作PPT向客户展示。\n在1个月内阅读超过2000页的中英文材料，并对资料进行整合和总结，向上司汇报核心内容。\n习期间独立完成了30页的船舶公司上市风险分析报告，并根据上司的反馈对报告进行修改和完善。\n对客户公司的内部政策进行风险评估，为客户设计并改进内部风险管控体系，对风险较高的领域提出相应建议。\n运用Excel VBA建立风险分析模型，对客户提供的数据进行量化分析，并绘制统计图表，制作PPT向客户展示。",
    workCity: "北京",
    workCountry: "中国"
};


const initialState = {
    completed: false,
    data: [{ ...work }]
};

function updateField(state, idx, field, value) {
    state.work.data[idx][field] = value;
}

const reducers = {
    completeWork: (state) => {
        state.work.completed = true;
    },
    addNewWork: state => {
        state.work.data.push({ ...work });
    },
    updateWorkId: (state, action) => {
        updateField(state, action.payload.index, "id", action.payload.id);
    },
    updateWorkName: (state, action) => {
        updateField(state, action.payload.index, "workName", action.payload.value);
    },
    updateCurrentWorkFlag: (state, action) => {
        // convert string to boolean
        updateField(
            state,
            action.payload.index,
            "currentWorkFlag",
            action.payload.value === "true"
        );
    },
    updateWorkCompanyName: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workCompanyName",
            action.payload.value
        );
    },
    updateWorkStartDate: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workStartDate",
            action.payload.value
        );
    },
    updateWorkEndDate: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workEndDate",
            action.payload.value
        );
    },
    updateWorkCity: (state, action) => {
        updateField(state, action.payload.index, "workCity", action.payload.value);
    },
    updateWorkCountry: (state, action) => {
        updateField(state, action.payload.index, "workCountry", action.payload.value);
    },
    updateWorkDescription: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workDescription",
            action.payload.value
        );
    },
    appendWorkDescription: (state, action) => {
      const index = action.payload.index;
      const value = state.work.data[index].workDescription.length === 0 ?
        action.payload.value : `${state.work.data[index].workDescription}\n${action.payload.value}`;
      updateField( state, index, "workDescription", value);
    }
};

const selectors = {
    selectWork: ({ resume }) => resume.work
};

export default {
    initialState,
    reducers,
    selectors
};
