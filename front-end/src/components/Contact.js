import React from 'react';
import '../styles/FooterPages.css';

const Contact = () => {
  return (
    <div className="main-container page-content">
      <h1>Kontakt</h1>
      <p>
        Wir freuen uns, von Ihnen zu hören! Sie können uns über die folgenden Kontaktmöglichkeiten 
        erreichen:
      </p>
      <p>
        Telefon: <a href="tel:+4930123456789">+49 30 123456789</a><br />
        E-Mail: <a href="mailto:info@superdooperstore.de">info@superdooperstore.de</a>
      </p>
      <p>
        Besuchen Sie uns auch in unserem Geschäft:<br />
        Sport Super Dooper Store<br />
        Musterstraße 123<br />
        10115 Berlin
      </p>
      <p>
        Öffnungszeiten:<br />
        Montag bis Freitag: 10:00 - 19:00 Uhr<br />
        Samstag: 10:00 - 16:00 Uhr
      </p>
    </div>
  );
};

export default Contact;
