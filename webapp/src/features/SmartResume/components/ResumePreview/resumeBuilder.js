// todo: after this issue has been released, we can upgrade the version of jsPDF to the lastest version and import using the following way：
import { jsPDF } from "jspdf";
import { dateRangeBuilder } from "./common";
import * as Constants from "./Constants";

import store from 'store';

// global data storage for download PDF
let data = [];
let doc;
let currentPage = 1;

let scaleFactor = 1; // deafult scale factor
// some scaled variables
let headingLineHeight = scaleFactor * Constants.headingLineHeight;
let paragraphLineHeight = scaleFactor * Constants.paragraphLineHeight;
let h1Padding = scaleFactor * Constants.h1Padding;
let pFontSize = scaleFactor * Constants.pFontSize;
let startY = scaleFactor * Constants.pdfTopPadding;
let pdfTopPadding = scaleFactor * Constants.pdfTopPadding;

// global config
let currentXPos = Constants.startX;
let currentYPos = startY;

// 一键整页
export const adjustToWholePage = (resumeDataSet) => {
  const lastLineHeight = data[data.length - 1].y;
  const lastLinePageAt = data[data.length - 1].page;

  const accumulateY = Constants.endY * (lastLinePageAt - 1) + lastLineHeight;

  if (accumulateY > Constants.endY * 2) {
    alert("Your resume are too long!"); // todo: designer need to decide
  } else if (accumulateY > Constants.endY * 1.5) {
    // make it to two page
    // scaleFactor = (Constants.endY * 2) / accumulateY;
    scaleFactor = (Constants.endY ) / lastLineHeight;
  } else if (accumulateY > Constants.endY * 1) {
    // make it to single page
    scaleFactor = Constants.endY / accumulateY;
  } else if (accumulateY > Constants.endY * 0.75) {
    // make it to single page
    scaleFactor = Constants.endY / accumulateY;
  }
  previewResume();
};

// do some small adjustments to fit the content into one page
const dataAdjustAfterBuild = (dataSet) => {
  // if a new page start with only a few line (15mm or less)
  // reduce the top padding
  if (data.length > 0) {
    const lastLineHeight = data[data.length - 1].y;
    const lastLinePageAt = data[data.length - 1].page;
    if(lastLinePageAt > 1 && lastLineHeight < 40) {
      scaleFactor = scaleFactor * 0.95;
      prepareData(dataSet);
    }
    // if pageBreak happend at the the last item, remove
    else if (data[data.length - 1].type === "pageBreak") {
      data.pop();
    }
  }
};

/**
 * read from Redux and prepare Data object
 * the object will be used by both Resume Preview and Resume Download
 * to read the structure of data, please refer to instruction.png
 * the data format looks like sample_data.js
 */
export const prepareData = ({
  basic,
  education,
  work,
  project,
  volunteer,
  messagesRP,
}) => {
  const basicData = basic.data;
  const educationData = education.data;
  const workData = work.data;
  const projectData = project.data;
  const volunteerData = volunteer.data;
  // variable reset
  data = [];
  doc = new jsPDF("A4");
  // define the fonts
  // add the font to jsPDF
  if (process.env.NODE_ENV !== "test") {
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

  // reset scaled variables
  pdfTopPadding = scaleFactor * pdfTopPadding;
  headingLineHeight = scaleFactor * headingLineHeight;
  // scaleFactor should have small effect to paragraphLineHeight:
  if (scaleFactor > 1){
    paragraphLineHeight = scaleFactor * 0.9 * paragraphLineHeight;
  }
  else{
    paragraphLineHeight = scaleFactor * 1.11 * paragraphLineHeight;
  }
  
  h1Padding = scaleFactor * h1Padding;
  // note change p font since this will break line wrap
  // pFontSize = scaleFactor * Constants.pFontSize;

  currentXPos = Constants.startX;
  startY = pdfTopPadding;
  currentYPos = startY;
  currentPage = 1;

  _perpareDataHeader(basicData, educationData);
  _perpareWork(workData, messagesRP);
  _perpareProject(projectData);
  _perpareVolunteer(volunteerData, messagesRP);

  dataAdjustAfterBuild({
    basic,
    education,
    work,
    project,
    volunteer,
    messagesRP,
  });

  return data;
};

/**
 * helper function: update Y position
 * also check if reach page limit, insert page break
 * @param increment the increment on Y
 */
const _updateCurrentYPos = (increment) => {
  if (currentYPos + increment >= Constants.endY) {
    // insert page break
    currentYPos = startY;
    data.push({ type: "pageBreak" });
    currentPage += 1;
  } else {
    currentYPos += increment;
  }
};

const _perpareDataHeader = (basicData, educationData) => {
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
  currentXPos += Constants.imageWidth;
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
  currentXPos +=
    doc.getTextWidth(basicData.nameCn) + Constants.defaultPaddingRight * 2;
  data.push(title);

  const schoolName = educationData.schoolName ? educationData.schoolName : "";
  const school = {
    type: "h2",
    y: startY,
    x: currentXPos,
    content: schoolName,
    page: currentPage,
  };

  // need to set font size in order to get correct width
  doc.setFontSize(Constants.h2FontSize);
  currentXPos +=
    doc.getTextWidth(schoolName) +
    Constants.defaultPaddingRight * 2;
  data.push(school);

  const major = {
    type: "h3",
    y: startY,
    x: currentXPos,
    content: `${
      educationData.major ? educationData.major : ""
    } ${dateRangeBuilder(educationData.startDate, educationData.graduateDate)}`,
    page: currentPage,
  };
  // reset currentXPos
  currentXPos = Constants.startX;
  data.push(major);

  // build line #2
  currentXPos += Constants.imageWidth;

  _updateCurrentYPos((headingLineHeight * 1) / 3);

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
  currentXPos +=
    doc.getTextWidth(`${basicData.phone},  ${basicData.email}`) +
    Constants.defaultPaddingRight * 2;
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
};

const _perpareWork = (workData, messagesRP) => {
  _updateCurrentYPos(h1Padding * 2);
  // draw work title
  if (
    workData.length > 0 &&
    (workData[0].workName || workData[0].workCompanyName)
  ) {
    data.push({
      type: "h1",
      y: currentYPos,
      content: messagesRP.workExperience,
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
      doc.setFontSize(Constants.h2FontSize);
      currentXPos =
        Constants.startX +
        (work.workName
          ? doc.getTextWidth(work.workName) + Constants.defaultPaddingRight
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
        ? doc.getTextWidth(work.workCompanyName) + Constants.defaultPaddingRight
        : 0;
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: `${dateRangeBuilder(work.workStartDate, work.workEndDate)}  ${
          work.workCity
        } ${work.workCountry}`,
        alignment: "right",
        page: currentPage,
      });
    }
    _updateCurrentYPos(h1Padding * 0.75);

    // work details
    if (work.workDescription) {
      _updateCurrentYPos(headingLineHeight / 2);

      // User can write one super long sentence without break, need to auto warp the text
      // also handle the line height and bullet point alignment
      doc.setFontSize(pFontSize);
      const descriptionList = work.workDescription.split("\n");
      
      descriptionList.forEach((content) => {
        const wrappedContent = doc.splitTextToSize(
          content,
          Constants.pdfPageWidth - Constants.pdfLeftPadding * 2 - 4
        ); // delete 2 extra characters (bullets point)

        for (let i = 0; i < wrappedContent.length; i++) {
          if (i === 0) {
            wrappedContent[i] = "- " + wrappedContent[i];
          } else {
            // manually adjust the padding left
            wrappedContent[i] = "  " + wrappedContent[i];
          }
        }

        data.push({
          type: "list",
          y: currentYPos,
          content: wrappedContent,
          page: currentPage,
        });

        // increment the height for the next print
        const height = doc.getTextDimensions(wrappedContent).h;
        _updateCurrentYPos(height + paragraphLineHeight / 2);
      });
    }
  });
};

const _perpareProject = (projectData) => {
  projectData.forEach((project) => {
    _updateCurrentYPos(h1Padding * 0.5);
    if (project.projectRole) {
      // draw project detail title line
      data.push({ type: "h2", y: currentYPos, content: project.projectRole });
    }

    // project company name
    if (project.projectCompanyName) {
      doc.setFontSize(Constants.h2FontSize);
      currentXPos =
        Constants.startX +
        (project.projectRole
          ? doc.getTextWidth(project.projectRole) +
            Constants.defaultPaddingRight
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
        ? doc.getTextWidth(project.projectCompanyName) +
          Constants.defaultPaddingRight
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
    _updateCurrentYPos(h1Padding * 0.75);

    // project details
    if (project.projectDescription) {
      _updateCurrentYPos(headingLineHeight / 2);

      // User can write one super long sentence without break, need to auto warp the text
      // also handle the line height and bullet point alignment
      doc.setFontSize(pFontSize);
      const descriptionList = project.projectDescription.split("\n");
      descriptionList.forEach((content) => {
        const wrappedContent = doc.splitTextToSize(
          content,
          Constants.pdfPageWidth - Constants.pdfLeftPadding * 2 - 4
        ); // delete 2 extra characters (bullets point)
        for (let i = 0; i < wrappedContent.length; i++) {
          if (i === 0) {
            wrappedContent[i] = "- " + wrappedContent[i];
          } else {
            // manually adjust the padding left
            wrappedContent[i] = "  " + wrappedContent[i];
          }
        }

        data.push({
          type: "list",
          y: currentYPos,
          content: wrappedContent,
          page: currentPage,
        });
        // increment the height for the next print
        const height = doc.getTextDimensions(wrappedContent).h;
        _updateCurrentYPos(height + paragraphLineHeight / 2);
      });
    }
  });
};

const _perpareVolunteer = (volunteerData, messagesRP) => {
  _updateCurrentYPos(h1Padding * 2);

  // draw Volunteer title
  if (
    volunteerData.length > 0 &&
    (volunteerData[0].volunteerRole || volunteerData[0].volunteerCompanyName)
  ) {
    data.push({
      type: "h1",
      y: currentYPos,
      content: messagesRP.studentWorkAndVolunteer,
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
    if (volunteer.volunteerName){
      // draw volunteer detail title line
    data.push({
      type: "h2",
      y: currentYPos,
      content: volunteer.volunteerName,
      page: currentPage,
    });
    }
    
    // volunteer company name
    if (volunteer.volunteerRole) {
      doc.setFontSize(Constants.h2FontSize);
      currentXPos =
        Constants.startX +
        (volunteer.volunteerName
          ? doc.getTextWidth(volunteer.volunteerName) +
            Constants.defaultPaddingRight
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
        ? doc.getTextWidth(volunteer.volunteerRole) +
          Constants.defaultPaddingRight
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

    _updateCurrentYPos(h1Padding * 0.75);

    // volunteer details
    if (volunteer.volunteerDescription) {
      _updateCurrentYPos(headingLineHeight / 2);

      // User can write one super long sentence without break, need to auto warp the text
      // also handle the line height and bullet point alignment
      doc.setFontSize(pFontSize);
      const descriptionList = volunteer.volunteerDescription.split("\n");
      descriptionList.forEach((content) => {
        const wrappedContent = doc.splitTextToSize(
          content,
          Constants.pdfPageWidth - Constants.pdfLeftPadding * 2 - 4
        ); // delete 2 extra characters (bullets point)
        for (let i = 0; i < wrappedContent.length; i++) {
          if (i === 0) {
            wrappedContent[i] = "- " + wrappedContent[i];
          } else {
            // manually adjust the padding left
            wrappedContent[i] = "  " + wrappedContent[i];
          }
        }

        data.push({
          type: "list",
          y: currentYPos,
          content: wrappedContent,
          page: currentPage,
        });
        // increment the height for the next print
        const height = doc.getTextDimensions(wrappedContent).h;
        _updateCurrentYPos(height + paragraphLineHeight / 2);
      });
    }
  });
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

const buildResume = (resumeDataSet) => {
  prepareData(resumeDataSet);
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
        d.color = Constants.c_blue;
        d.fontSize = Constants.h2FontSize;
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
        d.color = Constants.c_blue;
        doc.setTextColor(d.color);
        doc.textWithLink(d.content, d.x, d.y, { url: d.url });
        // text underline
        doc.setDrawColor(d.color);
        doc.line(d.x, d.y, d.x + doc.getTextWidth(d.content), d.y);
        break;
      case "list":
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
  // console.log('data', data);
};

export const downloadPDF = (resumeDataSet) => {
  buildResume(resumeDataSet);
  doc.save("resume.pdf");
};

export const previewResume = () => {
  const resumeDataSet = store.getState().resume;
  buildResume(resumeDataSet);
  const iframe = document.createElement("iframe");
  iframe.setAttribute("style", "height:100%; width:100%;");
  document
    .getElementById("displayPDF")
    .replaceChild(iframe, document.querySelector("#displayPDF iframe"));
  iframe.src = doc.output("datauristring") + "#zoom=FitH";
};
