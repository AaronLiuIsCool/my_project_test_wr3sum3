import React, { useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';
import { previewResume, wholePageCheck } from './ResumePreview/resumeBuilder';

import { selectLanguage, selectUserId } from 'features/App/slicer';
import { actions, basicSelectors, educationSelectors, workSelectors, projectSelectors, volunteerSelectors, certificateSelectors, resumeBuilderSelectors } from 'features/SmartResume/slicer';
import { I8nContext } from 'shell/i18n';
import AccountServices from 'shell/services/AccountServices';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';
import { generateBasicFormRating, generateLayoutRating, generateEducationRatings, generateSuggestions, isDescending, extractDate, generateCertificeRating } from '../utils/resume';
import LeftNav from './LeftNav';
import ExperiencesForm from './ExperiencesForm';
import Assistant from './Assistant';
import ResumePreview from './ResumePreview';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('ResumeStarter');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();

const getBase64FromUrl = async (url) => {
    const data = await fetch(url);
    const blob = await data.blob();
    return new Promise((resolve) => {
        const reader = new FileReader();
        reader.readAsDataURL(blob); 
        reader.onloadend = function() {
        const base64data = reader.result;   
        resolve(base64data);
        }
    });
}

async function getResume(dispatch, resumeId) {
    if (resumeId) {
        try {
            const resumeData = await resumeServices.getResume(resumeId);

            // convert url to base64 string and store avatar
            if (resumeData.photoReference){
                const base64 = await getBase64FromUrl(resumeData.photoReference);
                resumeData.basicInfo['avatar'] = base64;

            }
            await dispatch(actions.setResume(resumeData));
        } catch (exception) {
            logger.error(exception);
        }
        return;
    }
}

async function getAccountInfoAndSetResumeName(dispatch, userId, resumeId) {
    try {
        const responseJson = await accountServices.getAccountInfo(userId);
        if (responseJson.success) {
            const { resumes } = responseJson.account;
            const resume = resumes.find((item) => item.resumeId === resumeId);
            dispatch(actions.setAlias(resume?.alias));
        } else {
            logger.error(responseJson.message);
        }
    } catch (exception) {
        logger.error(exception);
    }
}
const SmartResume = ({ useObserver = false, resumeId }) => {
    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;
    const { language:resumeLanguage } = useSelector(resumeBuilderSelectors.selectResumeBuilder).data;
    
    
    const afterLoading = useRef(false)
    const education = useSelector(educationSelectors.selectEducation);
    const basic = useSelector(basicSelectors.selectBasic);
    const work = useSelector(workSelectors.selectWork);
    const project = useSelector(projectSelectors.selectProject);
    const volunteer = useSelector(volunteerSelectors.selectVolunteer);
    const certificate = useSelector(certificateSelectors.selectCertificate);
    
    const handleFormatRating = () => {
        const layoutRating = generateLayoutRating(wholePageCheck(resumeLanguage), messages)
        dispatch(actions.updateLayoutRating(layoutRating))
    }
    
    const handleBasicFormRating = async ({ avatar, linkedin, weblink }, completed) => {
        const basicRating = generateBasicFormRating({ avatar, linkedin, weblink, messages }, completed)
        // const layoutRating = generateLayoutRating(wholePageCheck(resumeLanguage), messages)
        // dispatch(actions.updateLayoutRating(layoutRating))
        dispatch(actions.updateBasicInfoRating(basicRating))
        
    }
     
    const handleEducationFormRatings = async (education, educations = []) => {
        // const { educations } = await resumeServices.getRatings(resumeId);
        const schools = educations || [];
        dispatch(actions.clearEducationInfo())
        const res = generateEducationRatings(education.data, schools, messages, education.completed)        
        res.forEach((item, index) => {
            dispatch(actions.updateEducationRating({
                index,
                details: item
            }))
        })
    }
    const handleWorkFormRating = async (data, workExperiences = []) => {
        // const { workExperiences } = await resumeServices.getRatings(resumeId);
        const {
            companyArr,
            keywordsArr,
            quantifyArr,
            expArr,
            sortedArr
        } = generateSuggestions(workExperiences, 'workXp', 'work', isDescending(extractDate(data, 'workStartDate')), messages)
        dispatch(actions.updateWorkRating({ 
            'amount': expArr,
            'company': companyArr,
            'keywords': keywordsArr,
            'quantify': quantifyArr,
            'sorted': sortedArr,
        }));
    }
    const handleProjectFormRating = async (data, projectExperiences = []) => {        
        // const { projectExperiences }  = await resumeServices.getRatings(resumeId);
        const {
            companyArr,
            keywordsArr,
            quantifyArr,
            expArr,
            sortedArr
        } = generateSuggestions(projectExperiences, 'projectXp', 'project', isDescending(extractDate(data, 'projectStartDate')), messages)
        dispatch(actions.updateProjectRating({ 
            'amount': expArr,
            'company': companyArr,
            'keywords': keywordsArr,
            'quantify': quantifyArr,
            'sorted': sortedArr,
        }));
    }
    const handleVolunteerFormRating = async (data, volunteerExperiences) => {
        const {
            companyArr,
            keywordsArr,
            quantifyArr,
            expArr,
            sortedArr
        } = generateSuggestions(volunteerExperiences, 'otherXp', 'volunteer', isDescending(extractDate(data, 'volunteerStartDate')), messages)

        dispatch(actions.updateVolunteerRating({ 
            'amount': expArr,
            'company': companyArr,
            'keywords': keywordsArr,
            'quantify': quantifyArr,
            'sorted': sortedArr,
        }));
    }
    const handleCertificateFormRating = (certificate) => {
        const certRating = generateCertificeRating(certificate.length, messages);
        dispatch(actions.updateCertificateRating({details: certRating}))
    }
    
    useEffect(() => {
      if (!afterLoading.current) {
        setTimeout(() => {
            updateRating();
        }, 200);
      }
    });

    const updateRating = async () => {
      dispatch(actions.updateToInitalState())
      const ratings = await resumeServices.getRatings(resumeId);
      
      const { educations, workExperiences, projectExperiences, volunteerExperiences } = ratings;
      handleBasicFormRating(basic.data, basic.completed);
      handleEducationFormRatings(education, educations);
      handleWorkFormRating(work.data, workExperiences);
      handleProjectFormRating(project.data, projectExperiences);
      handleVolunteerFormRating(volunteer.data, volunteerExperiences)
      handleCertificateFormRating(certificate.data)
      handleFormatRating();
    };
    
    const eventListenerCallback = () => {
        updateRating()
    }
    useEffect(() => { // TODO: revisit this afterwards + adding test coverage
        window.addEventListener('update-rating', eventListenerCallback);
        return () => {
            window.removeEventListener('update-rating', eventListenerCallback);
        };
    })
    
    useEffect(() => {
        const updatePreview = async () => {
            await Promise.all([
                getResume(dispatch, resumeId),
                getAccountInfoAndSetResumeName(dispatch, userId, resumeId)
            ]);
            previewResume(resumeLanguage);
            afterLoading.current = true
        }
        updatePreview();
        return () => {
          dispatch(
            actions.toggleAssistant({
              trigger: 'default',
              context: {},
            })
          );
        };
        
    }, []); // eslint-disable-line
    
    return (
        <I8nContext.Provider value={messages}>
            <div className="features smart-resume">
                <div className="overlay">
                    <LeftNav />
                    <Assistant />
                    <ResumePreview />
                </div>
                <ExperiencesForm useObserver={useObserver} />
            </div>
        </I8nContext.Provider>
    );
};

SmartResume.propTypes = {
    useObserver: PropTypes.bool
};

export default SmartResume;