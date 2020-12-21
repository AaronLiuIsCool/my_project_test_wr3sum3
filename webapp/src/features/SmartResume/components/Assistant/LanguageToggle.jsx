import React from 'react'

import './LanguageToggle.scss'

const LanguageToggle = ({
  className = '',
  // currentLanguage,
  updateLanguage
}) => {
  return <div className={`${className} toggle-container`}>
    <div className="language-option" onClick={() => {
      // todo: add currentlanguage check
      updateLanguage('EN')
    }}>EN</div>
    <div className="language-option" onClick={
      () => {updateLanguage('')}
    }>中文</div>
  </div>
}

export default LanguageToggle