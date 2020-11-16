import { jsPDF } from "jspdf";

import { dateRangeBuilder } from "./common";
import * as Constants from "./Constants";

import store from "store";
import { actions } from "../../slicer";

// global input data
let resumeData = null;
let messages = null;

// global data storage for download PDF
let data = [];
let doc;
let currentPage = 1;

let scaleFactor = 1; // default scale factor
// some scaled variables
let headingLineHeight = Constants.headingLineHeight;
// todo: revert
let paragraphLineHeight = Constants.paragraphLineHeight;
let h1Padding = Constants.h1Padding;
let pFontSize = Constants.pFontSize;
let h2FontSize = Constants.h2FontSize;
let startY = Constants.pdfTopPadding;
let pdfTopPadding = Constants.pdfTopPadding;

// global config
let currentXPos = Constants.startX;
let currentYPos = startY;

let primaryColor = Constants.c_blue;

// helper function, 计算右侧padding
const getRightPadding = (content, numOfPadding = 1) => {
  // regex for alphabets only 
  const alphabetRegex = /^[A-Za-z0-9\s]*$/;

  // 在当前字体下 getTextWidth(text) 
  // 计算中文长度和英文长度会有偏差 
  // 需要手动给英文添加额外padding
  if (alphabetRegex.test(content)) {
    return doc.getTextWidth(content) + Constants.defaultPaddingRight * (numOfPadding + 2);
  }
  return doc.getTextWidth(content) + Constants.defaultPaddingRight * numOfPadding;

}

// 一键整页
const adjustToWholePageHelper = () => {
  // 如果简历没有preview 则无法整页
  if (data.length === 0) {
    return;
  }
  // 如果data 最后一个元素是pageBreak, 则移除最后一个元素
  if (data[data.length - 1].type === "pageBreak") {
    data.splice(-1, 1);
  }
  const lastLineHeight = data[data.length - 1].y;
  const lastLinePageAt = data[data.length - 1].page;
  const accumulateY = Constants.endY * (lastLinePageAt - 1) + lastLineHeight;
  const pageHeight = Constants.endY;
  // 以下情况不需要 (无法) 整页
  // 总长度(accumulateY)小于1页的75%
  // 总长度在1页的90% = 100% 之间
  // 总长度在第二页的95% = 100% 之间
  // 总长度在第二页的80% = 100% 之间
  if (
    accumulateY <= pageHeight * 0.75 ||
    (accumulateY >= pageHeight * 0.9 && accumulateY <= pageHeight) ||
    (accumulateY >= pageHeight * 1.8 && accumulateY <= pageHeight * 2)
  ) {
    previewResume(messages);
    return;
  } else if (accumulateY > pageHeight * 2) {
    // 超过2页
    alert("Your resume are too long!"); // todo: designer need to decide
    return;
  } else {
    // 需要不停的整页 直到达到上面标准为止
    if (accumulateY > pageHeight * 1.5) {
      // 超过1.5页, 变成2页
      scaleFactor *= 1.05;
    } else if (accumulateY > pageHeight * 1) {
      // 超过1页 (不超过1.5页) 变成1页
      scaleFactor *= 0.95;
    } else if (accumulateY > pageHeight * 0.65) {
      // 超过0.75页 变成1页
      scaleFactor *= 1.05;
    }
    // recursion 重复进行
    buildResume();
    adjustToWholePageHelper();
  }
};

/**
 * read from Redux and prepare Data object
 * the object will be used by both Resume Preview and Resume Download
 * to read the structure of data, please refer to instruction.png
 */
export const prepareData = (resumeData, messages) => {
  const basicData = resumeData.basic.data;
  const educationData = resumeData.education.data;
  const workData = resumeData.work.data;
  const projectData = resumeData.project.data;
  const volunteerData = resumeData.volunteer.data;
  const certificateData = resumeData.certificate.data;
  // variable reset
  data = [];
  doc = new jsPDF({ format: "A4", lineHeight: 1.5 });
  // define the fonts
  // add the font to jsPDF
  if (process.env.REACT_APP_ENV !== "test") {
    doc.addFont(
      process.env.PUBLIC_URL + "/fonts/FZHTJW.TTF",
      "FZHTJW",
      "normal"
    );
    doc.addFont(
      process.env.PUBLIC_URL + "/fonts/FZDHTJW.TTF",
      "FZDHTJW",
      "bold"
    );
    doc.setFont("FZHTJW", "normal"); // set font
  }

  // note change p font since this will break line wrap
  // todo: test
  // pFontSize = scaleFactor * Constants.pFontSize;
  pFontSize = Constants.pFontSize;
  paragraphLineHeight = scaleFactor * Constants.paragraphLineHeight;
  headingLineHeight = scaleFactor * Constants.headingLineHeight;
  currentXPos = Constants.startX;
  startY = pdfTopPadding;
  currentYPos = startY;
  currentPage = 1;

  _prepareHeader(basicData, educationData);
  _prepareEducation(educationData, messages);
  _prepareWorkAndProject(workData, projectData, messages);
  _prepareVolunteer(volunteerData, messages);
  _prepareAwardAndCertificate(educationData, certificateData, messages);

  return data;
};

/**
 * helper function: update Y position
 * also check if reach page limit, insert page break
 * @param increment the increment on Y
 */
const _updateCurrentYPos = (increment) => {
  if (currentYPos + increment >= Constants.endY - pdfTopPadding) {
    // insert page break
    currentYPos = startY;
    data.push({ type: "pageBreak" });
    currentPage += 1;
  } else {
    currentYPos += increment;
  }
};

/**
 * helper function: 更新 工作、项目、自愿者中的details 部分
 * 如果单行文字过长 转换长文字为多行段落descriptionList
 * 逐行更新descriptionList
 * @param inputDescription {string} the description need to be rendered
 */
const _updateDetails = (inputDescription) => {
  _updateCurrentYPos(headingLineHeight / 2);
  // User can write one super long sentence without break, need to auto warp the text
  // also handle the line height and bullet point alignment
  doc.setFontSize(pFontSize);
  const descriptionList = inputDescription.split("\n");
  descriptionList.forEach((content) => {
    // remove starting *
    content = content.slice(1);
    const wrappedContent = doc.splitTextToSize(
      content,
      Constants.pdfPageWidth - Constants.pdfLeftPadding * 2 - 4
    ); // delete 2 extra characters (bullets point)
    // 如果在页main最下方 content 过长的话 会超出当前页 所以需要检查wrappedContent的每一行
    for (let i = 0; i < wrappedContent.length; i++) {
      // 如果是段落显示 并且是第一行 手动加入 "-" 
      if (i === 0) {
        wrappedContent[i] = "- " + wrappedContent[i];
      } else {
        // manually adjust the padding left
        wrappedContent[i] = "  " + wrappedContent[i];
      }
    }

    // 逐行更新 wrappedContent
    wrappedContent.forEach(content => {
      data.push({
        type: "list",
        y: currentYPos,
        content: content,
        page: currentPage,
      });
      // Increment the height for the next print
      // 如果需要得到字体本身的高度 可以加在这里

      // const height = doc.getTextDimensions(wrappedContent).h / wrappedContent.length;
      // _updateCurrentYPos(height);
      _updateCurrentYPos(paragraphLineHeight);

    });
  });
}

/**
 * helper function: 更新 工作、项目、自愿者中的details 部分
 * 如果单行文字过长 转换长文字为多行段落descriptionList
 * 逐行更新awards & certificates 
 * @param inputString {string} the description need to be rendered
 * @param blockDisplay {boolean} the paragraph starts with a new line or not
 */
const _updateInlineList = (inputString, leftStart) => {
  // User can write one super long sentence without break, need to auto warp the text
  // also handle the line height and bullet point alignment
  doc.setFontSize(pFontSize);
  const descriptionList = inputString.split("\n");
  descriptionList.forEach((content) => {
    const wrappedContent = doc.splitTextToSize(
      content,
      Constants.pdfPageWidth - Constants.pdfLeftPadding * 2 - 4
    ); // delete 2 extra characters (bullets point)
    // 如果在页main最下方 content 过长的话 会超出当前页 所以需要检查wrappedContent的每一行
    for (let i = 0; i < wrappedContent.length; i++) {
      // manually adjust the padding left
      wrappedContent[i] = "  " + wrappedContent[i];
    }

    // 逐行更新 wrappedContent
    wrappedContent.forEach(content => {
      data.push({
        type: "inlineList",
        x: leftStart,
        y: currentYPos,
        content: content,
        page: currentPage,
      });
      // increment the height for the next print
      const height = doc.getTextDimensions(wrappedContent).h / wrappedContent.length;
      _updateCurrentYPos(height);//如果需要手动调整行间距 可以加在这里
    });
    _updateCurrentYPos(paragraphLineHeight / 2);
  });
}

const _prepareHeader = (basicData, educationData) => {
  // build image
  const img = {
    type: "img",
    y: startY - Constants.imageHeight / 2,
    x: Constants.startX,
    width: Constants.imageWidth,
    height: Constants.imageHeight,
    src: basicData.avatar,
    format: "PNG",
    page: currentPage,
  };
  currentXPos += Constants.imageWidth + Constants.defaultPaddingRight;
  data.push(img);

  // build line 1
  const title = {
    type: "title",
    y: startY,
    x: currentXPos,
    content: basicData.nameCn ? basicData.nameCn : "",
    fontSize: Constants.titleFontSize,
    page: currentPage,
  };
  // need to set font size in order to get correct width
  doc.setFontSize(Constants.titleFontSize);
  currentXPos += getRightPadding(basicData.nameCn, 2);
  data.push(title);

  // start with imageWidth + user name
  _updateCurrentYPos(-(headingLineHeight * 2) / 3);

  if (basicData.email || basicData.phone || basicData.linkedin) {
    data.push({
      type: "background",
      y: currentYPos,
      x: currentXPos,
      color: "#f2f4f9",
      page: currentPage,
    });
    _updateCurrentYPos((headingLineHeight * 2) / 3);
  }

  // add some padding left
  currentXPos += Constants.defaultPaddingRight;
  const contact = {
    type: "h3",
    y: currentYPos,
    x: currentXPos,
    content:
      basicData.phone || basicData.email
        ? `${basicData.phone} ${basicData.email}`
        : "",
    page: currentPage,
  };

  doc.setFontSize(Constants.h3FontSize);
  currentXPos += getRightPadding(`${basicData.phone},  ${basicData.email}`, 2);
  data.push(contact);

  const link = {
    type: "link",
    y: currentYPos,
    x: currentXPos,
    content: basicData.linkedin ? "linkedIn" : "",
    url: basicData.linkedin,
    page: currentPage,
  };
  data.push(link);
  _updateCurrentYPos(headingLineHeight);
};

const _prepareEducation = (educationData, messages) => {
  // draw education title
  if (
    (educationData.length > 0 &&
      educationData[0].schoolName)
  ) {
    _updateCurrentYPos(h1Padding * 2);
    data.push({
      type: "h1",
      y: currentYPos,
      content: messages.educationBackground,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);
    data.push({
      type: "underline",
      y: currentYPos,
      color: Constants.c_grey,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);

    // draw each education line
    educationData.forEach((education) => {
      data.push({
        type: "list",
        color: Constants.c_grey,
        y: currentYPos,
        content: education.schoolName,
        page: currentPage,
      });

      // build awards as a string
      let schoolStr = `${education.major} ${education.degree}`;

      // shift x position
      doc.setFontSize(pFontSize);
      let currentXPos = Constants.startX + doc.getTextWidth(education.schoolName) + Constants.defaultPaddingRight * 2;
      data.push({
        type: "list",
        color: Constants.c_black,
        y: currentYPos,
        x: currentXPos,
        content: schoolStr,
        page: currentPage,
      });

      // add date
      doc.setFontSize(Constants.h3FontSize);
      currentXPos += schoolStr
        ? getRightPadding(schoolStr)
        : 0;
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: `${dateRangeBuilder(education.startDate, education.graduateDate)}`,
        alignment: "right",
        page: currentPage,
      });

      _updateCurrentYPos(paragraphLineHeight);
    });
  }
};

const _prepareWorkAndProject = (workData, projectData, messages) => {
  _updateCurrentYPos(h1Padding);
  // draw work title
  if ((workData.length > 0 && workData[0].workName) || (projectData.length > 0 && projectData[0].projectRole)) {
    data.push({
      type: "h1",
      y: currentYPos,
      content: messages.workExperience,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);
    data.push({
      type: "underline",
      y: currentYPos,
      color: Constants.c_grey,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);
  }

  // deal with work 
  workData.forEach((work) => {
    _updateCurrentYPos(h1Padding * 0.5);
    // draw work detail title line
    if (work.workName) {
      data.push({
        type: "h2",
        y: currentYPos,
        content: work.workName,
        page: currentPage,
      });
    }

    // company name
    if (work.workCompanyName) {
      doc.setFontSize(h2FontSize);
      currentXPos =
        Constants.startX +
        (work.workName
          ? getRightPadding(work.workName)
          : 0);
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: work.workCompanyName,
        page: currentPage,
      });
    }

    // date + location
    if (
      work.workStartDate ||
      work.workEndDate ||
      work.workCity ||
      work.workCountry
    ) {
      doc.setFontSize(Constants.h3FontSize);
      currentXPos += work.workCompanyName
        ? getRightPadding(work.workCompanyName)
        : 0;
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: `${dateRangeBuilder(work.workStartDate, work.workEndDate)}  ${work.workCity
          } ${work.workCountry}`,
        alignment: "right",
        page: currentPage,
      });
    }
    _updateCurrentYPos(h1Padding * 0.5);
    // render work details
    work.workDescription && _updateDetails(work.workDescription);
  });

  // deal with project
  projectData.forEach((project) => {
    _updateCurrentYPos(h1Padding * 0.5);
    if (project.projectRole) {
      // draw project detail title line
      data.push({ type: "h2", y: currentYPos, content: project.projectRole });
    }
    // project company name
    if (project.projectCompanyName) {
      doc.setFontSize(h2FontSize);
      currentXPos =
        Constants.startX +
        (project.projectRole
          ? getRightPadding(project.projectRole)
          : 0);
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: project.projectCompanyName,
        page: currentPage,
      });
    }
    // date + location
    if (
      project.projectStartDate ||
      project.projectEndDate ||
      project.projectCity ||
      project.projectCountry
    ) {
      doc.setFontSize(Constants.h3FontSize);
      currentXPos += project.projectCompanyName
        ? getRightPadding(project.projectCompanyName)
        : 0;
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: `${dateRangeBuilder(
          project.projectStartDate,
          project.projectEndDate
        )} ${project.projectCity} ${project.projectCountry}`,
        alignment: "right",
        page: currentPage,
      });
    }
    _updateCurrentYPos(h1Padding * 0.5);

    // render project details
    project.projectDescription && _updateDetails(project.projectDescription);
  });
};

const _prepareVolunteer = (volunteerData, messages) => {
  _updateCurrentYPos(h1Padding);

  // draw Volunteer title
  if (
    volunteerData.length > 0 &&
    (volunteerData[0].volunteerRole || volunteerData[0].volunteerCompanyName)
  ) {
    data.push({
      type: "h1",
      y: currentYPos,
      content: messages.studentWorkAndVolunteer,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);
    data.push({
      type: "underline",
      y: currentYPos,
      color: Constants.c_grey,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);
  }

  volunteerData.forEach((volunteer) => {
    _updateCurrentYPos(h1Padding * 0.5);
    if (volunteer.volunteerCompanyName) {
      // draw volunteer detail title line
      data.push({
        type: "h2",
        y: currentYPos,
        content: volunteer.volunteerCompanyName,
        page: currentPage,
      });
    }

    // volunteer company name
    if (volunteer.volunteerRole) {
      doc.setFontSize(h2FontSize);
      currentXPos =
        Constants.startX +
        (volunteer.volunteerCompanyName
          ? getRightPadding(volunteer.volunteerCompanyName)
          : 0);
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: volunteer.volunteerRole,
        page: currentPage,
      });
    }

    // date + location
    if (
      volunteer.volunteerStartDate ||
      volunteer.volunteerEndDate ||
      volunteer.volunteerCity ||
      volunteer.volunteerCountry
    ) {
      doc.setFontSize(Constants.h3FontSize);
      currentXPos += volunteer.volunteerRole
        ? getRightPadding(volunteer.volunteerRole)
        : 0;
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: `${dateRangeBuilder(
          volunteer.volunteerStartDate,
          volunteer.volunteerEndDate
        )} ${volunteer.volunteerCity} ${volunteer.volunteerCountry}`,
        alignment: "right",
        page: currentPage,
      });
    }
    _updateCurrentYPos(h1Padding * 0.5);

    // render volunteer details
    volunteer.volunteerDescription &&
      _updateDetails(volunteer.volunteerDescription);
  });
};

// return an list of awards from educationData list
const _getAwards = (educationData) => {
  const awards = [];

  if (educationData.length > 0) {
    educationData.forEach((edu) => {
      if (edu.highestAward) {
        awards.push(edu.highestAward);
      }
      if (edu.otherAward) {
        awards.push(edu.otherAward);
      }
    });
  }
  return awards;
}

const _prepareAwardAndCertificate = (educationData, certificateData, messages) => {
  const awards = _getAwards(educationData);
  // draw awards + certificate title
  if (
    (awards.length > 0) ||
    (certificateData.length > 0 &&
      certificateData[0].certificateName)
  ) {
    _updateCurrentYPos(h1Padding);
    data.push({
      type: "h1",
      y: currentYPos,
      content: messages.certificateAndAward,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding);
    data.push({
      type: "underline",
      y: currentYPos,
      color: Constants.c_grey,
      page: currentPage,
    });
    _updateCurrentYPos(h1Padding * 1.5);
    // draw rewards
    if (awards.length > 0) {
      // draw award title
      data.push({
        type: "h2",
        y: currentYPos,
        content: messages.awards,
        page: currentPage,
      });

      // build awards as a string
      let awardStr = "";
      awards.forEach((award) => {
        awardStr += " * " + award + " ";
      });
      // shift x position
      doc.setFontSize(h2FontSize);
      const currentXPos = Constants.startX + doc.getTextWidth(messages.awards) + Constants.defaultPaddingRight;
      doc.setFontSize(pFontSize);
      _updateInlineList(awardStr, currentXPos);
    }
    // draw certificates
    if (certificateData.length > 0 && certificateData[0].certificateName) {
      // draw certificate title
      data.push({
        type: "h2",
        y: currentYPos,
        content: messages.certificate,
        page: currentPage,
      });
      // build certificate as a string
      let certificateStr = "";
      certificateData.forEach((certificate) => {
        if (!certificate.validCertificateFlag) {
          certificateStr += ` *  ${certificate.certificateName} ${certificate.certificateEndDate ? certificate.certificateEndDate + messages.expiredAt : ""}`;
        }
        else {
          certificateStr += ` *  ${certificate.certificateName} ${messages.validForever}`;
        }
      });
      // shift x position
      doc.setFontSize(h2FontSize);
      const currentXPos = Constants.startX + doc.getTextWidth(messages.certificate) + Constants.defaultPaddingRight;
      doc.setFontSize(pFontSize);
      _updateInlineList(certificateStr, currentXPos);
    }
  }



};

const drawText = ({
  content = "",
  x = Constants.startX,
  y = startY,
  fontSize = pFontSize,
  color = Constants.c_black,
  bold: isBold = false,
  alignment = "left",
}) => {
  doc.setTextColor(color);
  doc.setFontSize(fontSize);
  if (isBold) {
    doc.setFont("FZDHTJW", "bold"); // set font
  }
  if (alignment === "right") {
    x = Constants.endX;
  }
  doc.text(content, x, y, null, null, alignment);
  if (isBold) {
    doc.setFont("FZHTJW", "normal"); // set font
  }
};

const buildResume = () => {
  prepareData(resumeData, messages);
  // 如果最后一行是换页 则移除该数据
  if (data[data.length - 1].type === "pageBreak") {
    data.splice(-1, 1);
  }
  data.forEach((d) => {
    d.doc = doc;
    switch (d.type) {
      case "img":
        if (d.src) doc.addImage(d.src, d.format, d.x, d.y, d.width, d.height);
        break;
      case "title":
        d.color = Constants.c_black;
        d.fontSize = Constants.titleFontSize;
        drawText(d);
        break;
      case "h1":
        d.color = Constants.c_black;
        d.fontSize = Constants.h1FontSize;
        drawText(d);
        break;
      case "underline":
        doc.setDrawColor(d.color);
        doc.line(Constants.startX, d.y, Constants.endX, d.y);
        break;
      case "h2":
        // apply h2 style
        d.color = primaryColor;
        d.fontSize = h2FontSize;
        d.bold = true;
        drawText(d);
        break;
      case "h3":
        // apply h3 style
        d.color = Constants.c_grey;
        d.fontSize = Constants.h3FontSize;
        drawText(d);
        break;
      case "link":
        d.color = primaryColor;
        doc.setTextColor(d.color);
        doc.textWithLink(d.content, d.x, d.y, { url: d.url });
        // text underline
        doc.setDrawColor(d.color);
        doc.line(d.x, d.y, d.x + doc.getTextWidth(d.content), d.y);
        break;
      case "list":
        d.color = d.color ? d.color : Constants.c_black;
        d.fontSize = pFontSize;
        drawText(d);
        break;
      case "inlineList":
        d.color = Constants.c_black;
        d.fontSize = pFontSize;
        drawText(d);
        break;
      case "pageBreak":
        doc.addPage("a4");
        break;
      case "background":
        doc.setDrawColor(d.color);
        doc.setFillColor(d.color);
        // http://raw.githack.com/MrRio/jsPDF/master/docs/jsPDF.html#roundedRect
        const height = headingLineHeight;
        doc.roundedRect(d.x, d.y, Constants.endX - d.x, height, 4, 4, "FD");
        break;
      default:
        console.log("default type");
    }
  });
};

export const downloadPDF = (messagePR) => {
  resumeData = store.getState().resume;
  primaryColor = resumeData?.resumeBuilder?.data?.color;
  messages = messagePR;
  buildResume();
  doc.save(`${resumeData?.alias}.pdf`);
};

export const previewResume = (messagePR) => {
  resumeData = store.getState().resume;
  primaryColor = resumeData.resumeBuilder.data.color;
  messages = messagePR;
  buildResume();
  let base64Data;
  const buildBase64Str = async () => {
    new Promise((resolve, reject) => {
      base64Data = doc.output("datauristring");
      resolve();
    });
  };
  buildBase64Str().then((res) => {
    store.dispatch(actions.updateResumeBase64Str({ value: base64Data }));
  });
};


export const adjustToWholePage = (messagePR) => {
  resumeData = store.getState().resume;
  messages = messagePR;
  adjustToWholePageHelper();
};

export const wholePageCheck = (messagePR) => {
  resumeData = store.getState().resume;
  messages = messagePR;
  prepareData(resumeData, messages);
  return data;
};