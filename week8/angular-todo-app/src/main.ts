// src/main.ts

import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app';
import { appConfig } from './app/app.config';

/**
 * Bootstrap the Angular application
 * This is the entry point of the application
 */
bootstrapApplication(AppComponent, appConfig)
  .catch(err => {
    console.error('Error starting the application:', err);
    
    // Show user-friendly error message
    const errorDiv = document.createElement('div');
    errorDiv.innerHTML = `
      <div style="
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        font-family: 'Roboto', sans-serif;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        text-align: center;
        padding: 20px;
      ">
        <div style="
          background: rgba(255, 255, 255, 0.1);
          padding: 40px;
          border-radius: 12px;
          backdrop-filter: blur(10px);
          max-width: 500px;
        ">
          <h1 style="margin: 0 0 16px 0; font-size: 24px;">
            ⚠️ Application Error
          </h1>
          <p style="margin: 0 0 20px 0; font-size: 16px; line-height: 1.5;">
            We're sorry, but the Todo application failed to start. 
            Please refresh the page or try again later.
          </p>
          <button 
            onclick="window.location.reload()" 
            style="
              background: white;
              color: #667eea;
              border: none;
              padding: 12px 24px;
              border-radius: 6px;
              font-size: 14px;
              font-weight: 500;
              cursor: pointer;
              transition: transform 0.2s ease;
            "
            onmouseover="this.style.transform='translateY(-2px)'"
            onmouseout="this.style.transform='translateY(0)'"
          >
            🔄 Refresh Page
          </button>
        </div>
      </div>
    `;
    
    document.body.innerHTML = '';
    document.body.appendChild(errorDiv);
  });