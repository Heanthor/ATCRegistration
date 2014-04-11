package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Class to encapsulate the java emailing mechanism
 * <p/>
 * The default behavior of the an Emailer is to:
 * <ul>
 * <li>send attachments</li>
 * <li>not send each email separately but with every recipient CC'd</li>
 * </ul>
 *
 * @author benjamyn
 */
public class Emailer
{
    private String    userName;
    private String    passwd;
    private Session   session;
    private Transport transport;

    private List<String> recipients = new LinkedList<>();
    private String subject;
    private String bodyFile;
    private List<String> attachments     = new LinkedList<String>();
    private boolean      sendAttachments = true;
    private boolean      separateEmails  = false;

    /**
     * Initializes the mailing parameters, readying the object
     * for sending email
     *
     * @param userName gmail account user name
     * @param passwd   gmail account password
     */
    public Emailer(String userName, String passwd)
    {
        this.userName = userName;
        this.passwd = passwd;

        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.user", this.userName);
        props.setProperty("mail.smtp.password", this.passwd);
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");

        session = Session.getDefaultInstance(props, null);
        try
        {
            transport = session.getTransport("smtp");
            transport.connect(userName, passwd);
        }
        catch (MessagingException e)
        {
            new AtcErr("Could not connect to account with username '%s' and password '%s'",
                       userName, passwd);
        }
    }

    public void setSubjectLine(String subjLine)
    {
        subject = subjLine;
    }

    public void setBodyFile(String bodyFile)
    {
        this.bodyFile = bodyFile;
    }

    public void removeAllRecipients()
    {
        recipients.clear();
    }

    public void addRecipient(String recipient)
    {
        recipients.add(recipient);
    }

    public void sendAttachments(boolean bool)
    {
        sendAttachments = bool;
    }

    /**
     * Changes the Emailer property on whether to send a separate email
     * to each of the recipients added to the current emailing list
     * or to CC them all one the same email.
     *
     * @param bool
     */
    public void setSeperateEmails(boolean bool)
    {
        separateEmails = bool;
    }

    public void addAttachment(String attachmentFile)
    {
        attachments.add(attachmentFile);
    }

    public void removeAttachments()
    {
        attachments.clear();
    }

    /**
     * Sends the email current configured for the Emailer
     */
    public void sendEmail()
    {
        // perform some checks to make sure a sane email can be sent
        if (recipients.size() == 0)
            new AtcErr("No one to email to");
        if (subject == null)
            new AtcErr("No subject for email");
        if (bodyFile == null)
            new AtcErr("No body file for email");

        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));

            // set subject
            message.setSubject(subject);

            // create multipart message
            Multipart multipart = new MimeMultipart();

            // add body
            multipart.addBodyPart(getMessageBodyPart());

            // any attachments?
            if (sendAttachments)
                for (String attachmentFile : attachments)
                    multipart.addBodyPart(getAttachmentBodyPart(attachmentFile));

            // add content and attachments
            message.setContent(multipart);

            // add recipients
            if (separateEmails)
            {
                for (String recip : recipients)
                {
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recip));
                    // send message
                    transport.sendMessage(message, message.getAllRecipients());
                }
            }
            else
            {
                for (String recip : recipients)
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recip));
                // send message
                transport.sendMessage(message, message.getAllRecipients());
            }
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }
    }

    private BodyPart getMessageBodyPart() throws MessagingException
    {
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(getBody(), "text/html");
        return messageBodyPart;
    }

    private String getBody()
    {
        String body = "";

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(bodyFile));
            String line = reader.readLine();

            while (line != null)
            {
                body += line + "\n";
                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e)
        {
            new AtcErr("Could not read email body file '%s'", bodyFile);
        }

        return body;
    }

    private BodyPart getAttachmentBodyPart(String attachmentFile) throws MessagingException
    {
        BodyPart abp = new MimeBodyPart();
        DataSource source = new FileDataSource(attachmentFile);
        abp.setDataHandler(new DataHandler(source));
        abp.setFileName(attachmentFile);
        return abp;
    }

    /**
     * Resets the email properties.
     * This method should be called before any email is going to be
     * created and sent.
     */
    public void resetEmail()
    {
        removeAllRecipients();
        subject = bodyFile = null;
        attachments.clear();
    }

    public String toString()
    {
        return "\n\tFrom: " + userName +
               "\n\tTo: " + (separateEmails ? "" : "(" + recipients.size() + ") " + recipients) +
               "\n\tBcc: " + (separateEmails ? "(" + recipients.size() + ") " + recipients : "") +
               "\n\tSubject: " + subject +
               "\n\tBody: " + getBody() +
               "\n\tAttachments: " + (sendAttachments ? attachments.toString() : "");
    }
}
