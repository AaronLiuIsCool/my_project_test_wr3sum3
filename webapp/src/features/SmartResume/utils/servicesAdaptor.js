import moment from 'moment';

const JDBC_DATE_FORMAT = 'YYYY-MM-DD';

export function adaptBasics(basics) {
    if (basics.completed === false) {
        return undefined;
    }

    const { id, nameCn, nameEn, email, phone, city, linkedin, weblink } = basics.data;
    const data = {
        id,
        fullName: nameCn,
        alias: nameEn,
        city,
        country: 'undefined',
        email,
        phoneNumber: phone
    };
    if (typeof linkedin === 'string' && linkedin.length > 0) {
        data.profiles = data.profile || [];
        data.profiles.push({
            type: 'LINKEDIN',
            url: linkedin
        });
    }
    if (typeof linkedin === 'string' && weblink.length > 0) {
        data.profiles = data.profile || [];
        data.profiles.push({
            type: 'OTHER',
            url: weblink
        });
    }

    return data;
}

export function adaptEducation(edu) {
    const { schoolName, gpa, startDate, graduateDate,
        major, degree, city, country, highestAward, otherAward } = edu;
    const data = {
        institution: schoolName,
        major,
        gpa,
        city,
        country,
        degree,
        startDate: moment(startDate).format(JDBC_DATE_FORMAT),
        endDate: moment(graduateDate).format(JDBC_DATE_FORMAT)
    };

    if (typeof highestAward === 'string' && highestAward.length > 0) {
        data.awards = data.awards || [];
        data.awards.push({ name: highestAward });
    }
    if (typeof otherAward === 'string' && otherAward.length > 0) {
        data.awards = data.awards || [];
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
    const { workName, workCompanyName, workStartDate, workEndDate,
        workDescription, workCity, workCountry } = work;

    const data = {
        role: workName,
        organization: workCompanyName,
        city: workCity,
        country: workCountry,
        description: workDescription,
        startDate: moment(workStartDate).format(JDBC_DATE_FORMAT),
        endDate: moment(workEndDate).format(JDBC_DATE_FORMAT)
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
    const { projectRole, projectCompanyName, projectStartDate, projectEndDate,
        projectCity, projectCountry, projectDescription } = project;

    const data = {
        role: projectRole,
        organization: projectCompanyName,
        city: projectCity,
        country: projectCountry,
        description: projectDescription,
        startDate: moment(projectStartDate).format(JDBC_DATE_FORMAT),
        endDate: moment(projectEndDate).format(JDBC_DATE_FORMAT)
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
    const { certificateName, certificateIssuedDate, certificateEndDate } = cert;
    return {
        name: certificateName,
        issueDate: moment(certificateIssuedDate).format(JDBC_DATE_FORMAT),
        expirationDate: moment(certificateEndDate).format(JDBC_DATE_FORMAT)
    };
}

export function adaptCertificates(certificates) {
    if (certificates.completed === false) {
        return undefined;
    }

    return certificates.data.map(adaptCertificate);
}

export function adaptVolunteer(volunteer) {
    const { volunteerRole, volunteerCompanyName, volunteerStartDate, volunteerEndDate,
        volunteerCity, volunteerCountry, volunteerDescription } = volunteer;

    return {
        role: volunteerRole,
        organization: volunteerCompanyName,
        startDate: moment(volunteerStartDate).format(JDBC_DATE_FORMAT),
        endDate: moment(volunteerEndDate).format(JDBC_DATE_FORMAT),
        description: volunteerDescription,
        city: volunteerCity,
        country: volunteerCountry
    }
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
