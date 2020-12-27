import React, { useEffect, useState } from 'react';
import Dropdown from './Dropdown';
import './DropdownGroup.scss';
const DropdownGroup = ({ industries, titles, onSelect, messages }) => {
  const [industry, setIndustry] = useState('');
  const handleIndustryChange = (industry) => {
    setIndustry(industry);
    onSelect(industry, titles[industry][0]);
  };
  const handleSearch = (title) => {
    onSelect(industry || industries[0], title);
  };
  useEffect(() => {
    setIndustry('')
  }, [industries])
  return (
    <div className="suggestion-dropdown">
      <Dropdown
        className="dropdown-1"
        options={industries}
        onSelect={handleIndustryChange}
      />
      {industries.length > 0 && (
        <Dropdown
          className="dropdown-2"
          options={titles[industry || industries[0]]}
          onSelect={handleSearch}
        />
      )}
    </div>
  );
};
export default DropdownGroup;
