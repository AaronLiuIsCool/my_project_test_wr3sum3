import React from 'react';
import { useI8n } from 'shell/i18n';
import styles from 'features/SpecialPages/Page404.module.css';
import Button from 'react-bootstrap/Button';


const ErrorPage = () => {
  const messages = useI8n();
  return (
    <div className={styles.page404}>
      <div className={styles.header}>
        <img
          src={process.env.PUBLIC_URL + "/images/logo.png"}
          alt="logo"
        />
      </div>
      <div className={styles.content}>
        <img
          src={process.env.PUBLIC_URL + "/images/error.gif"}
          alt="500 Internal Server Error"
        />
        <p>{messages.generalErrorMessage}</p>
        <Button variant="primary" className="w-100">
          {messages.refresh}
        </Button>
      </div>
    </div>
  );
};

export default ErrorPage;


