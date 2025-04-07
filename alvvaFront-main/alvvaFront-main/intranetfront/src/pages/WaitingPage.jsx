import React from "react";
import "../styles/WatingPage.css"; 

function WaitingPage() {
  return (
    <div className="waiting-container">
      <p className="waiting-message">Waiting... Your account is inactive.</p>
      <div className="loadingspinner">
        <div id="square1"></div>
        <div id="square2"></div>
        <div id="square3"></div>
        <div id="square4"></div>
        <div id="square5"></div>
      </div>
    </div>
  );
}

export default WaitingPage;
