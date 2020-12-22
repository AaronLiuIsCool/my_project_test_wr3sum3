import moment from 'moment';

const JDBC_DATE_FORMAT = 'YYYY-MM-DD';

function getDateString(date) {
    // if no date given, we use current time
    const dateValue = (date? moment(date) : moment())
    return dateValue.isValid() ? dateValue.format(JDBC_DATE_FORMAT) : undefined;
}

export function adaptBasics(basics) {
    if (basics.completed === false) {
        return undefined;
    }

    const { id, nameCn, email, phone, city, linkedin, weblink, country } = basics.data;
    const data = {
        id,
        fullName: nameCn ? nameCn: "   ",
        city: city? city: "  ",
        country : country ? country : "  ",
        email : email ? email : " ",
        phoneNumber: phone ? phone : " "
    };

    if (typeof linkedin === 'string' && linkedin.length > 0) {
        data.profiles = data.profiles || [];
        data.profiles.push({
            type: 'LINKEDIN',
            url: linkedin
        });
    }
    if (typeof weblink === 'string' && weblink.length > 0) {
        data.profiles = data.profiles || [];
        data.profiles.push({
            type: 'OTHER',
            url: weblink
        });
    }

    return data;
}

export function adaptEducation(edu) {
    const { id, schoolName, gpa, startDate, graduateDate,
        major, degree, city, country, highestAward, otherAward } = edu;
    const data = {
        id,
        institution: schoolName,
        major: major ? major : "  ",
        city: city? city: "  ",
        country: country ? country : "  ",
        degree: degree ? degree : "  ",
        startDate: getDateString(startDate),
        endDate: getDateString(graduateDate)
    };

    try {
        const inputGPA = Number(gpa);
        if (gpa <= 4 && gpa >= 1) {
            data['gpa'] = inputGPA.toString();
        }
    }
    finally {
        if (!data['gpa']){
            data['gpa'] = '4';
        }
      }

    
    data.awards = data.awards || [];
    if (highestAward) {
        data.awards.push({ name: highestAward });
    }
    if (otherAward) {
        data.awards.push({ name: otherAward });
    }

    return data;
}

export function adaptEducations(educations) {
    if (educations.completed === false) {
        return undefined;
    }

    return educations.data.map(adaptEducation);
}

export function adaptWork(work) {
    const { id, workName, workCompanyName, workStartDate, workEndDate,
        workDescription, workCity, workCountry } = work;

    const data = {
        id,
        role: workName ? workName : "  ",
        organization: workCompanyName ? workCompanyName : "  ",
        city: workCity ? workCity : "  ",
        country:  workCountry ? workCountry : "  ",
        description: workDescription ? workDescription : "  ",
        startDate: getDateString(workStartDate),
        endDate: getDateString(workEndDate)
    }

    return data;
}

export function adaptWorks(works) {
    if (works.completed === false) {
        return undefined;
    }

    return works.data.map(adaptWork);
}

export function adaptProject(project) {
    const { id, projectRole, projectCompanyName, projectStartDate, projectEndDate,
        projectCity, projectCountry, projectDescription } = project;

    const data = {
        id,
        role: projectRole ? projectRole : "  ",
        organization: projectCompanyName ? projectCompanyName : "  ",
        city: projectCity ? projectCity : "  ",
        country: projectCountry ? projectCountry : "  ",
        description: projectDescription ? projectDescription : "  ",
        startDate: getDateString(projectStartDate),
        endDate: getDateString(projectEndDate)
    };

    return data;
}

export function adaptProjects(projects) {
    if (projects.completed === false) {
        return undefined;
    }

    return projects.data.map(adaptProject);
}

export function adaptCertificate(cert) {
    const { id, certificateName, certificateIssuedDate, certificateEndDate } = cert;
    return {
        id,
        name: certificateName,
        issueDate: getDateString(certificateIssuedDate),
        expirationDate: getDateString(certificateEndDate)
    };
}

export function adaptCertificates(certificates) {
    if (certificates.completed === false) {
        return undefined;
    }

    return certificates.data.map(adaptCertificate);
}

export function adaptVolunteer(volunteer) {
    const { id, volunteerRole, volunteerCompanyName, volunteerStartDate, volunteerEndDate,
        volunteerCity, volunteerCountry, volunteerDescription } = volunteer;

    
    const data = {
        id,
        role: volunteerRole,
        organization: volunteerCompanyName,
        startDate: getDateString(volunteerStartDate),
        endDate: getDateString(volunteerEndDate),
        description: volunteerDescription,
        city: volunteerCity,
        country: volunteerCountry
    };
    return data;
}

export function adaptVolunteers(volunteers) {
    if (volunteers.completed === false) {
        return undefined;
    }

    return volunteers.data.map(adaptVolunteer);
}

export function resumeAdaptor(resume, type = 'new') {
    return {
        language: 'zh',
        basicInfo: adaptBasics(resume.basic),
        educations: adaptEducations(resume.education),
        workExperiences: adaptWorks(resume.work),
        projectExperiences: adaptProjects(resume.project),
        certificates: adaptCertificates(resume.certificate),
        volunteerExperiences: adaptVolunteers(resume.volunteer)
    };
}


export function resumeAdaptorForSDK(resume) {
    const basic = { ...resume.basic, completed: true };
    const work = { ...resume.work, completed: true };
    const education = { ...resume.education, completed: true };
    const project = { ...resume.project, completed: true };
    let certificate = null;
    if (resume.certificate.data.name) {
        certificate = { ...resume.certificate, completed: true };
    }

    const result = {
        language: 'zh',
        basicInfo: adaptBasics(basic),
        educations: adaptEducations(education),
        workExperiences: adaptWorks(work),
        projectExperiences: adaptProjects(project),
        certificates: (certificate ? adaptCertificates(certificate) : []),
        volunteerExperiences: [] // note: resumeSDK doesn't have any volunteer related results
    };

    if (!education.schoolName){
        delete result['educations'];
    }

    return result;
}