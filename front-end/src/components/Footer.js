import React from 'react';

import '../styles/Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-content">
        {/* <p>&copy; {new Date().getFullYear()} Sport Super Dooper Store. Alle Rechte vorbehalten.</p> */}
        <nav className="footer-nav">
          <ul>
            <li><a href="/about">Über uns</a></li>
            <li><a href="/contact">Kontakt</a></li>
            <li><a href="/privacy">Datenschutzrichtlinie</a></li>
            <li><a href="/terms">Nutzungsbedingungen</a></li>
          </ul>
        </nav>
        <p className='copyright'>&copy; 2024 Sport Super Dooper Store. Alle Rechte vorbehalten.</p>
      </div>
    </footer>
  );
};

export default Footer;
