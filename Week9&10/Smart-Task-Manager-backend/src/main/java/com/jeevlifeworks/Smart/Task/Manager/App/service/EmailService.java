package com.jeevlifeworks.Smart.Task.Manager.App.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for sending different types of email notifications 
 * like registration confirmation, task assignment, password reset, etc.
 */
@Service
@Slf4j
public class EmailService {
	
	// Injects the JavaMailSender to send emails
	@Autowired
    private JavaMailSender mailSender;
	
	// Retrieves the sender's email address from application.properties
	@Value("${spring.mail.username}")
    private String fromEmail;

	/**
     * Sends a basic email with specified recipient, subject, and body.
     * 
     * Accepts parameter to, recipient email address
     * Accepts parameter subject subject of the email
     * Accepts parameter body, body content of the email
     */
    public void sendEmail(String to, String subject, String body) {
    	try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email successfully sent to "+ to +" with subject: "+ subject);
        } catch (Exception e) {
            log.error("Failed to send email to "+ to +". Error: "+e.getMessage());
        }
    }

    /**
     * Sends a notification email when a task is assigned to a user.
     * 
     * Accepts parameter recipientEmail, recipient's email address
     * Accepts parameter taskTitle, title of the assigned task
     * Accepts parameter managerName, name of the manager assigning the task
     */
    public void sendTaskAssignmentNotification(String recipientEmail, String taskTitle, String managerName) {
        String subject = "New Task Assignment: " + taskTitle;
        String body = "Hello,\n\nYou have been assigned a new task: " + taskTitle + ".\n\n"
                    + "Assigned by: " + managerName + "\n"
                    + "Please check the Task Manager for more details.\n\n"
                    + "Regards,\nYour Task Manager Team";
        sendEmail(recipientEmail, subject, body);
    }
    
    /**
     * Sends a verification email during user registration with a confirmation link.
     * 
     * Accepts parameter recipientEmail recipient's email address
     * Accepts parameter token, verification token
     */
    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "Complete your registration!";
        String body = "To confirm your account, please click the following link: "
                    + "http://localhost:4200/register/confirm?token=" + token;
        sendEmail(recipientEmail, subject, body);
    }

    /**
     * Sends a password reset email containing a token and a reset link.
     * 
     * Accepts parameter recipientEmail recipient's email address
     * Accepts parameter token, password reset token
     */
    public void sendPasswordResetEmail(String recipientEmail, String token) {
        String subject = "Password Reset Request";
        String body = "To reset your password, please use the following token or link: "
                    + "http://localhost:4200/reset-password?token=" + token
                    + "\nToken: " + token;
        sendEmail(recipientEmail, subject, body);
    }
    
    /**
     * Sends a reminder email if a task is overdue.
     * 
     * Accepts parameter recipientEmail recipient's email address
     * Accepts parameter taskTitle, title of the overdue task
     * Accepts parameter dueDate, due date of the task
     */
    public void sendOverdueTaskReminder(String recipientEmail, String taskTitle, LocalDate dueDate) {
        String subject = "Overdue Task Alert: " + taskTitle;
        String body = "Hello,\n\nThis is a reminder that your task '" + taskTitle + "' was due on " + dueDate + ".\n"
                    + "Please update its status in the Task Manager.\n\n"
                    + "Regards,\nYour Task Manager Team";
        sendEmail(recipientEmail, subject, body);
    }
}
