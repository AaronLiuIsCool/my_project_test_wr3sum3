export function adaptBasics(basics = {}) {
    const { id, fullName = '', alias = '',
        city = '', country = '', email = '',
        phoneNumber = '', profiles = [], avatar="" } = basics;
    let linkedin = '';
    let weblink = '';
    profiles.forEach(({ type, url }) => {
        switch (type) {
            case 'LINKEDIN':
                linkedin = url;
                break;
            case 'OTHER':
                weblink = url;
                break;
            default:
                break;
        }
    })

    const state = {
        completed: id !== undefined,
        data: {
            id,
            avatar,
            nameCn: fullName,
            nameEn: alias,
            email,
            phone: phoneNumber,
            city,
            country,
            linkedin,
            weblink
        }
    }
    return state;
}

export function adaptEducation(edu = {}) {
    const { id, institution = '', gpa = '', startDate = '', endDate = '',
        major = '', degree = '', city = '', country = '', awards = [] } = edu;
    const data = {
        id,
        schoolName: institution,
        gpa,
        startDate,
        graduateDate: endDate,
        major,
        degree,
        city,
        country,
        highestAward: awards[0]?.name,
        otherAward: awards[1]?.name
    }

    return data;
}

export function adaptEducations(educations = []) {
    const data = educations.length === 0 ? [adaptEducation()] : educations.map(adaptEducation);
    const state = {
        completed: educations[0]?.id !== undefined,
        data
    };
    return state;
}

export function adaptWork(work = {}) {
    const { city = '', country = '', description = '',
        endDate = '', id, organization = '', role = '', startDate = '' } = work;
    const data = {
        id,
        workName: role,
        currentWorkFlag: false,
        workCompanyName: organization,
        workStartDate: startDate,
        workEndDate: endDate,
        workDescription: description,
        workCity: city,
        workCountry: country
    };

    return data;
}

export function adaptWorks(works = []) {
    const data = works.length === 0 ? [adaptWork()] : works.map(adaptWork);
    const state = {
        completed: works[0]?.id !== undefined,
        data
    };

    return state;
}

export function adaptProject(project = {}) {
    const { city = '', country = '', description = '', endDate = '',
        id, organization = '', role = '', startDate = '' } = project
    const data = {
        id,
        projectRole: role,
        currentProjectFlag: false,
        projectCompanyName: organization,
        projectStartDate: startDate,
        projectEndDate: endDate,
        projectCity: city,
        projectCountry: country,
        projectDescription: description
    }

    return data;
}

export function adaptProjects(projects = []) {
    const data = projects.length === 0 ? [adaptProject()] : projects.map(adaptProject);
    const state = {
        completed: projects[0]?.id !== undefined,
        data
    };
    return state;
}

export function adaptCertificate(cert = {}) {
    const { name = '', issueDate = '', expirationDate = '', id } = cert;
    const data = {
        id,
        certificateName: name,
        validCertificateFlag: !expirationDate,
        certificateIssuedDate: issueDate,
        certificateEndDate: expirationDate,
    };

    return data;
}

export function adaptCertificates(certificates = []) {
    const data = certificates.length === 0 ? [adaptCertificate()] : certificates.map(adaptCertificate);
    const state = {
        completed: certificates[0]?.id !== undefined,
        data
    };
    return state;
}

export function adaptVolunteer(volunteer = {}) {
    const { city = '', country = '', description = '', endDate = '',
        id, organization = '', role = '', startDate = '' } = volunteer
    const data = {
        id,
        volunteerRole: role,
        currentVolunteerFlag: false,
        volunteerCompanyName: organization,
        volunteerStartDate: startDate,
        volunteerEndDate: endDate,
        volunteerCity: city,
        volunteerCountry: country,
        volunteerDescription: description,
    };

    return data;
}

export function adaptVolunteers(volunteers = []) {
    const data = volunteers.length === 0 ? [adaptVolunteer()] : volunteers.map(adaptVolunteer);
    const state = {
        completed: volunteers[0]?.id !== undefined,
        data
    };
    return state;
}

export function resumeAdaptor(resume) {
    return {
        id: resume.id,
        photoReference: { url: resume.photoReference },
        basic: adaptBasics(resume.basicInfo),
        education: adaptEducations(resume.educations),
        work: adaptWorks(resume.workExperiences),
        project: adaptProjects(resume.projectExperiences),
        certificate: adaptCertificates(resume.certificates),
        volunteer: adaptVolunteers(resume.volunteerExperiences)
    };
}
