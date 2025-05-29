// src/components/BalloonBackground.js
import React from "react";
import "./BalloonBackground.css";

const BalloonBackground = () => {
  return (
    <div className="balloon-container">
      {[...Array(15)].map((_, i) => (
        <div key={i} className="balloon" style={{ animationDelay: `${i * 2}s` }} />
      ))}
    </div>
  );
};

export default BalloonBackground;
