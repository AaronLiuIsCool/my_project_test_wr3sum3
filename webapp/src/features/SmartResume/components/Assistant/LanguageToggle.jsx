import React, { useState } from 'react'

import './LanguageToggle.scss'

const LanguageToggle = ({
  className = '',
  // currentLanguage,
  updateLanguage
}) => {
  const [active, setActive] = useState(false);

  return <div className={`${className} toggle-container ${!active && 'active'}`}>
    <div className={`language-option ${!active && 'active'}`} onClick={
      () => {
        setActive(false)
        updateLanguage('')
      }
    }>中文</div>
    <div className={`language-option ${active && 'active'}`} onClick={() => {
      // todo: add currentlanguage check
      setActive(true)
      updateLanguage('EN')
    }}>EN</div>
  </div>
}

export default LanguageToggle