import { jsPDF } from "jspdf";
import { dateRangeBuilder } from "./common";

// static variable
const c_blue = "#3e89ec";
const c_black = "#333333";
const c_grey = "#8e94a7";

// size
var pdfPageWidth = 210; // width of A4 in mm
var pdfPageHeight = 320; // Height of A4 in mm
const pdfTopPadding = 20;
const pdfLeftPadding = 15;
const titleFontSize = 19;
const h1FontSize = 13;
const h2FontSize = 11;
const h3FontSize = 11;
const pFontSize = 9; // text height = 3.175
const headingLineHeight = 10;
const paragraphLineHeight = 7;

const imageWidth = 20;
const imageHeight = 20;

// positions + paddings
const startY = pdfTopPadding;
const endY = 280;
const startX = pdfLeftPadding;
const endX = pdfPageWidth - pdfLeftPadding;
const h1Padding = headingLineHeight / 2;
const defaultPaddingRight = 2;

// global data storage
let data = [];
var doc = new jsPDF();


// define the fonts
// add the font to jsPDF
if (process.env.NODE_ENV !== 'test'){
  doc.addFont(process.env.PUBLIC_URL + "/fonts/FZHTJW.TTF", "FZHTJW", "normal");
  doc.addFont(process.env.PUBLIC_URL + "/fonts/FZDHTJW.TTF", "FZDHTJW", "bold");
  doc.setFont("FZHTJW", "normal"); // set font
}

// global config
let currentXPos = pdfLeftPadding;
let currentYPos = pdfTopPadding;


/**
 * read from Redux and prepare Data object
 * the object will be used by both Resume Preview and Resume Download
 * to read the structure of data, please refer to instruction.png
 * the data format looks like sample_data.js
 */
export const prepareData = ({
  basicData,
  educationData,
  workData,
  projectData,
  volunteerData,
  messagesRP,
}) => {
  _perpareDataHeader(basicData, educationData);
  _perpareWork(workData, messagesRP);
  _perpareProject(projectData);
  _perpareVolunteer(volunteerData, messagesRP);
  return data;
};

/**
 * helper function: update Y position
 * also check if reach page limit, insert page break
 * @param increment the increment on Y
 */
const _updateCurrentYPos = (increment) => {
  if (currentYPos + increment >= endY) {
    // insert page break
    currentYPos = startY;
    data.push({ type: "pageBreak" });
  } else {
    currentYPos += increment;
  }
};

const _perpareDataHeader = (basicData, educationData) => {
  // build image
  const img = {
    type: "img",
    y: startY - imageHeight / 2,
    x: startX,
    width: imageWidth,
    height: imageHeight,
    src: basicData.avatar,
    format: "PNG",
  };
  currentXPos += imageWidth;
  data.push(img);

  // build line 1
  const title = {
    type: "title",
    y: startY,
    x: currentXPos,
    content: basicData.nameCn,
    fontSize: titleFontSize,
  };
  // need to set font size in order to get correct width
  doc.setFontSize(titleFontSize);
  currentXPos += doc.getTextWidth(basicData.nameCn) + defaultPaddingRight * 2;
  data.push(title);

  const school = {
    type: "h2",
    y: startY,
    x: currentXPos,
    content: educationData.schoolName,
  };

  // need to set font size in order to get correct width
  doc.setFontSize(h2FontSize);
  currentXPos +=
    doc.getTextWidth(educationData.schoolName) + defaultPaddingRight * 2;
  data.push(school);

  const major = {
    type: "h3",
    y: startY,
    x: currentXPos,
    content: `${educationData.major} ${dateRangeBuilder(
      educationData.startDate,
      educationData.graduateDate
    )}`,
  };
  // reset currentXPos
  currentXPos = pdfLeftPadding;
  data.push(major);

  // build line #2
  currentXPos += imageWidth;
  _updateCurrentYPos((headingLineHeight * 1) / 3);
  data.push({
    type: "background",
    y: currentYPos,
    x: currentXPos,
    color: "#f2f4f9",
  });
  _updateCurrentYPos((headingLineHeight * 2) / 3);

  // add some padding left
  currentXPos += defaultPaddingRight;
  const contact = {
    type: "h3",
    y: currentYPos,
    x: currentXPos,
    content: `${basicData.phone},  ${basicData.email}`,
  };

  doc.setFontSize(h3FontSize);
  currentXPos +=
    doc.getTextWidth(`${basicData.phone},  ${basicData.email}`) +
    defaultPaddingRight * 2;
  data.push(contact);

  const link = {
    type: "link",
    y: currentYPos,
    x: currentXPos,
    content: "linkedIn",
    url: basicData.linkedin,
  };
  data.push(link);
};

const _perpareWork = (workData, messagesRP) => {
  // draw work title
  _updateCurrentYPos(h1Padding * 2);
  data.push({ type: "h1", y: currentYPos, content: messagesRP.workExperience });
  _updateCurrentYPos(h1Padding);
  data.push({ type: "underline", y: currentYPos, color: c_grey });
  _updateCurrentYPos(h1Padding);

  workData.forEach((work) => {
    // draw work detail title line
    data.push({ type: "h2", y: currentYPos, content: work.workName });

    // company name
    if (work.workCompanyName) {
      doc.setFontSize(h2FontSize);
      currentXPos =
        pdfLeftPadding +
        (work.workName
          ? doc.getTextWidth(work.workName) + defaultPaddingRight
          : 0);
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: work.workCompanyName,
      });
    }

    // date + location
    if (
      work.workStartDate ||
      work.workEndDate ||
      work.workCity ||
      work.workCountry
    )
      doc.setFontSize(h3FontSize);
    currentXPos += work.workCompanyName
      ? doc.getTextWidth(work.workCompanyName) + defaultPaddingRight
      : 0;
    data.push({
      type: "h3",
      y: currentYPos,
      x: currentXPos,
      content: `${dateRangeBuilder(work.workStartDate, work.workEndDate)} (${
        work.workCity
      } ${work.workCountry})`,
      alignment: "right",
    });

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
          pdfPageWidth - pdfLeftPadding * 2 - 4
        ); // delete 2 extra characters (bullets point)
        for (let i = 0; i < wrappedContent.length; i++) {
          if (i === 0) {
            wrappedContent[i] = "- " + wrappedContent[i];
          } else {
            // manually adjust the padding left
            wrappedContent[i] = "  " + wrappedContent[i];
          }
        }

        
        // increment the height for the next print
        const height = doc.getTextDimensions(wrappedContent).h;
        _updateCurrentYPos(height + paragraphLineHeight / 2);

        data.push({
          type: "list",
          y: currentYPos,
          content: wrappedContent,
        });
      });
    }
  });
};

const _perpareProject = (projectData) => {
  projectData.forEach((project) => {
    // draw project detail title line
    data.push({ type: "h2", y: currentYPos, content: project.projectRole });

    // project company name
    if (project.projectCompanyName) {
      doc.setFontSize(h2FontSize);
      currentXPos =
        pdfLeftPadding +
        (project.projectRole
          ? doc.getTextWidth(project.projectRole) + defaultPaddingRight
          : 0);
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: project.projectCompanyName,
      });
    }

    // date + location
    if (
      project.projectStartDate ||
      project.projectEndDate ||
      project.projectCity ||
      project.projectCountry
    )
      doc.setFontSize(h3FontSize);
    currentXPos += project.projectCompanyName
      ? doc.getTextWidth(project.projectCompanyName) + defaultPaddingRight
      : 0;
    data.push({
      type: "h3",
      y: currentYPos,
      x: currentXPos,
      content: `${dateRangeBuilder(
        project.projectStartDate,
        project.projectEndDate
      )} (${project.projectCity} ${project.projectCountry})`,
      alignment: "right",
    });

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
          pdfPageWidth - pdfLeftPadding * 2 - 4
        ); // delete 2 extra characters (bullets point)
        for (let i = 0; i < wrappedContent.length; i++) {
          if (i === 0) {
            wrappedContent[i] = "- " + wrappedContent[i];
          } else {
            // manually adjust the padding left
            wrappedContent[i] = "  " + wrappedContent[i];
          }
        }

        
        // increment the height for the next print
        const height = doc.getTextDimensions(wrappedContent).h;
        _updateCurrentYPos(height + paragraphLineHeight / 2);

        data.push({
          type: "list",
          y: currentYPos,
          content: wrappedContent,
        });
      });
    }
  });
};

const _perpareVolunteer = (volunteerData, messagesRP) => {
  // draw work title
  _updateCurrentYPos(h1Padding * 2);
  data.push({
    type: "h1",
    y: currentYPos,
    content: messagesRP.studentWorkAndVolunteer,
  });
  _updateCurrentYPos(h1Padding);
  data.push({ type: "underline", y: currentYPos, color: c_grey });
  _updateCurrentYPos(h1Padding);

  volunteerData.forEach((volunteer) => {
    // draw volunteer detail title line
    data.push({ type: "h2", y: currentYPos, content: volunteer.volunteerName });

    // volunteer company name
    if (volunteer.volunteerRole) {
      doc.setFontSize(h2FontSize);
      currentXPos =
        pdfLeftPadding +
        (volunteer.volunteerName
          ? doc.getTextWidth(volunteer.volunteerName) + defaultPaddingRight
          : 0);
      data.push({
        type: "h3",
        y: currentYPos,
        x: currentXPos,
        content: volunteer.volunteerRole,
      });
    }

    // date + location
    if (
      volunteer.volunteerStartDate ||
      volunteer.volunteerEndDate ||
      volunteer.volunteerCity ||
      volunteer.volunteerCountry
    )
      doc.setFontSize(h3FontSize);
    currentXPos += volunteer.volunteerRole
      ? doc.getTextWidth(volunteer.volunteerRole) + defaultPaddingRight
      : 0;
    data.push({
      type: "h3",
      y: currentYPos,
      x: currentXPos,
      content: `${dateRangeBuilder(
        volunteer.volunteerStartDate,
        volunteer.volunteerEndDate
      )} (${volunteer.volunteerCity} ${volunteer.volunteerCountry})`,
      alignment: "right",
    });

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
          pdfPageWidth - pdfLeftPadding * 2 - 4
        ); // delete 2 extra characters (bullets point)
        for (let i = 0; i < wrappedContent.length; i++) {
          if (i === 0) {
            wrappedContent[i] = "- " + wrappedContent[i];
          } else {
            // manually adjust the padding left
            wrappedContent[i] = "  " + wrappedContent[i];
          }
        }

        
        // increment the height for the next print
        const height = doc.getTextDimensions(wrappedContent).h;
        _updateCurrentYPos(height + paragraphLineHeight / 2);
        data.push({
          type: "list",
          y: currentYPos,
          content: wrappedContent,
        });
      });
    }
  });
};

const drawText = ({
  content = "",
  x = startX,
  y = startY,
  fontSize = pFontSize,
  color = c_black,
  bold: isBold = false,
  alignment = "left",
}) => {
  doc.setTextColor(color);
  doc.setFontSize(fontSize);
  if (isBold) {
    doc.setFont("FZDHTJW", "bold"); // set font
  }
  // .text (text, x, y, flags, angle, align);
  if (alignment === "right") {
    // x = endX - doc.getTextWidth(content);
    x = endX;
  }
  doc.text(content, x, y, null, null, alignment);
  if (isBold) {
    doc.setFont("FZHTJW", "normal"); // set font
  }
};

export const downloadPDF = (resumeDataSet) => {
  prepareData(resumeDataSet);
  data.forEach((d) => {
    d.doc = doc;
    switch (d.type) {
      case "img":
        if (d.src) doc.addImage(d.src, d.format, d.x, d.y, d.width, d.height);
        break;
      case "title":
        d.color = c_black;
        d.fontSize = titleFontSize;
        drawText(d);
        break;
      case "h1":
        d.color = c_black;
        d.fontSize = h1FontSize;
        drawText(d);
        break;
      case "underline":
        doc.setDrawColor(d.color);
        doc.line(startX, d.y, endX, d.y);
        break;
      case "h2":
        // apply h2 style
        d.color = c_blue;
        d.fontSize = h2FontSize;
        d.bold = true;
        drawText(d);
        break;
      case "h3":
        // apply h3 style
        d.color = c_grey;
        d.fontSize = h3FontSize;
        drawText(d);
        break;
      case "link":
        d.color = c_blue;
        doc.setTextColor(d.color);
        doc.textWithLink(d.content, d.x, d.y, { url: d.url });
        // text underline
        doc.setDrawColor(d.color);
        doc.line(d.x, d.y, d.x + doc.getTextWidth(d.content), d.y);
        break;
      case "list":
        d.color = c_black;
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
        doc.roundedRect(d.x, d.y, endX - d.x, height, 4, 4, "FD");
        break;
      default:
        console.log("default type");
    }
  });
  doc.save("resume.pdf");
};
