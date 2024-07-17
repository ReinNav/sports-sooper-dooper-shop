import React from 'react';

import '../styles/FooterPages.css';

const PrivacyPolicy = () => {
  return (
    <div className="main-container page-content">
      <h1>Datenschutzrichtlinie</h1>
      <p>
        Ihre Privatsphäre ist uns wichtig. Diese Datenschutzrichtlinie erläutert, welche persönlichen 
        Daten wir sammeln und wie wir sie verwenden.
      </p>
      <h2>1. Erhebung und Verarbeitung persönlicher Daten</h2>
      <p>
        Wir erheben und verarbeiten persönliche Daten nur, wenn dies notwendig ist, um Ihnen unsere 
        Dienstleistungen zu bieten oder wenn Sie uns ausdrücklich Ihre Zustimmung gegeben haben.
      </p>
      <h2>2. Verwendung persönlicher Daten</h2>
      <p>
        Die persönlichen Daten, die wir sammeln, werden ausschließlich verwendet, um Ihre Bestellungen 
        zu bearbeiten, unseren Service zu verbessern und Sie über unsere Produkte zu informieren.
      </p>
      <h2>3. Weitergabe persönlicher Daten</h2>
      <p>
        Wir geben Ihre persönlichen Daten nicht an Dritte weiter, es sei denn, dies ist zur 
        Vertragserfüllung erforderlich oder Sie haben ausdrücklich zugestimmt.
      </p>
      <h2>4. Ihre Rechte</h2>
      <p>
        Sie haben das Recht, jederzeit Auskunft über die von uns gespeicherten persönlichen Daten zu 
        erhalten, diese zu korrigieren oder deren Löschung zu verlangen.
      </p>
      <p>
        Bei Fragen zur Verarbeitung Ihrer persönlichen Daten können Sie uns jederzeit kontaktieren:
      </p>
      <p>
        E-Mail: <a href="mailto:datenschutz@superdooperstore.de">datenschutz@superdooperstore.de</a>
      </p>
    </div>
  );
};

export default PrivacyPolicy;
