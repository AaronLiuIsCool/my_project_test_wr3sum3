import React, { useState, useEffect } from "react";

import { Form, Button } from "react-bootstrap";
import {generateInitials} from 'features/App/components/Actions';

import "./BasicInfoCard.scss";

const BasicInfoCard = ({
  messages,
  userData: { name, photoUrl },
  handleSave,
}) => {
  const [username, setUsername] = useState("");
  const handleUsernameChange = (e) => {
    const value = e.target.value;
    setUsername(value);
  };
  const handleUpdateUserInfo = () => {
    if (validateUserInputs(username)) {
      handleSave(username);
    }
  };
  const validateUserInputs = (name) => {
    return name !== "";
  };
  useEffect(() => {
    setUsername(name);
  }, [name, photoUrl]);
  return (
    <div className="basic-info-card">
      <div className="basic-info-card-left">
        <h2>{messages["basicInfo"]}</h2>
        <Form
          className="form"
          onSubmit={(e) => {
            e.preventDefault();
            handleUpdateUserInfo();
            // console.log("submitted!");
          }}
        >
          <Form.Group controlId="formAvatar">
            <Form.Label>{messages.avatar}</Form.Label>
            {/*  */}
            <div className="avatar-wrapper">
              {generateInitials(name, messages.defaultInitial)}
            </div>
          </Form.Group>
          <Form.Group controlId="formBasicEmail">
            <Form.Label>{messages.name}</Form.Label>
            <Form.Control
              type="text"
              placeholder="Name"
              value={username}
              onChange={handleUsernameChange}
            />
          </Form.Group>
        </Form>
      </div>
      <div className="basic-info-card-right">
        <Button onClick={handleUpdateUserInfo} className="save-btn">
          {messages.save}
        </Button>
      </div>
    </div>
  );
};
export default BasicInfoCard;
