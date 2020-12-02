import React, { useState } from "react";

import { Button, Modal, Form } from "react-bootstrap";

import "./AccManagementCard.scss";

const AccManagementCard = ({
  messages,
  userData: { email, loginType, id },
  handleRequestPasswordReset,
  handleBindEmailAddress,
}) => {
  const [showModal, setShowModal] = useState(false);
  const [goodToGo, setGoodToGo] = useState(false);
  const handleClose = () => {
    setShowModal(false);
  };
  const updateEmail = () => {
    const userId = id;
    handleBindEmailAddress(email, userId);
    console.log("email Updated!!!!!!!!");
    handleClose();
  };
  const handleOpen = () => {
    setShowModal(true);
  };

  const validateEmail = (email) => {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
  };

  const handleInputChange = (e) => {
    const { value } = e.target;
    if (validateEmail(value)) {
      setGoodToGo(true);
    } else {
      setGoodToGo(false);
    }
  };

  return (
    <>
      <Modal
        className="email-modal"
        show={showModal}
        onHide={handleClose}
        animation={false}
      >
        <Modal.Body>
          <h2>{messages.enterEmailAddress}</h2>
          <input
            ref={(input) => input && input.focus()}
            id="email-inputing"
            onChange={handleInputChange}
          />
          <div className="buttons">
            <Button
              className={goodToGo ? "goodtogo" : ""}
              variant="primary"
              onClick={updateEmail}
            >
              {messages.save}
            </Button>
          </div>
        </Modal.Body>
      </Modal>
      <div className="acc-management-card">
        <h2>{messages["accountManagement"]}</h2>
        <Form
          className="form"
          onSubmit={(e) => {
            e.preventDefault();
            console.log("submitted!");
          }}
        >
          <Form.Group controlId="formWechat">
            <Form.Label>{messages.wechat}</Form.Label>
            <div
              className={
                loginType !== "normal"
                  ? "binded input-wrapper"
                  : "input-wrapper"
              }
            >
              <Form.Control disabled type="text" />
              <Form.Text>
                {loginType !== "normal" ? messages.binded : messages.notBinded}
              </Form.Text>
            </div>
          </Form.Group>
          <Form.Group controlId="formEmail">
            <Form.Label>{messages.email}</Form.Label>
            <div className={email ? "binded input-wrapper" : "input-wrapper"}>
              <Form.Control value={email} disabled type="text" />
              <Form.Text>
                {email ? messages.binded : messages.notBinded}
              </Form.Text>
            </div>
            <Form.Text
              className="action-item"
              onClick={() => {
                console.log("modal show");
                handleOpen();
              }}
            >
              {email ? messages.updateEmail : messages.bind}
            </Form.Text>
          </Form.Group>
          <Form.Group controlId="formReset">
            <Form.Label>{messages.password}</Form.Label>
            <Button
              onClick={() => {
                handleRequestPasswordReset(email);
              }}
              className="reset-btn"
            >
              {messages.reset}
            </Button>
          </Form.Group>
        </Form>
      </div>
    </>
  );
};
export default AccManagementCard;
