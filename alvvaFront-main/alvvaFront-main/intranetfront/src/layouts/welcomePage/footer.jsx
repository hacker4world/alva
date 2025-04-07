import React from "react";
import "../../styles/footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="contact-info">
          <h3>Contact Us</h3>
          <p><strong>Email:</strong> alvaintranet@gmail.com</p>
          <p><strong>Phone:</strong> +216 00 000 000 </p>
          <p><strong>Address:</strong> Location </p>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2025 AlvaTorayGroup. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;
