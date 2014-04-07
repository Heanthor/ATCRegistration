package classes;

import org.w3c.dom.Element;
import datastructures.AccountInformation;


/**
 * This class provides an interface to get user account information, file
 * locations, and other configurable data. It first parses the config.xml file
 * and then parses the user account information xml file.
 *
 * @author benjamyn
 */
public class Configuration
{
    private final String configFile = "files/config.xml";

    private class EmailInfo
    {
        public String emailSubject;
        public String emailBodyFile;
    }

    public enum EmailType
    {
        ETICKET,
        PREFESTIVAL,
        THANK_YOU,
        NUMBER_EMAIL_TYPES
    }

    private EmailInfo[] emailInfos;
    private String      eticketFile;
    private String      festivalClassesFile;
    private String      accountInfoFile;

    private int                taxPercent;
    private AccountInformation accInfo;

    // XML structure information
    private final String   eticketFileTag         = "eTicketFile";
    private final String   festivalClassesFileTag = "FestivalClassesFile";
    private final String   accountInfoFileTag     = "AccountInfoFile";
    private final String   taxPercentTag          = "TaxPercent";
    private final String[] emailInfoTags          =
            {
                    "eTicketEmail", "PreFestivalEmail", "ThankYouEmail"
            };
    private final String   emailInfoSubjectTag    = "EmailSubject";
    private final String   emailInfoBodyFileTag   = "EmailBodyFile";

    // XML tags and info for the account information
    private enum RegistrationInfo
    {
        SPREADSHEET_NAME,
        FORMSITE_WORKSHEET,
        ONSITE_WORKSHEET,
        NUMBER_INFOS
    }

    private final String[] RegistrationInfoTags =
            {
                    "SpreadsheetName", "FormsiteWorksheet", "OnsiteWorksheet"
            };

    public Configuration()
    {
        emailInfos = new EmailInfo[EmailType.NUMBER_EMAIL_TYPES.ordinal()];
        for (int i = 0; i < emailInfos.length; ++i)
        {
            emailInfos[i] = new EmailInfo();
        }
        parseConfigFile();
    }

    /*
     * Methods to parse the configuration XML file
     */
    private void parseConfigFile()
    {
        // get root element ("Configuration")
        Element rootEle = XmlParserHelper.getRootElement(configFile);

        // parse file names
        eticketFile = XmlParserHelper.getContent(rootEle, eticketFileTag);
        festivalClassesFile = XmlParserHelper.getContent(rootEle, festivalClassesFileTag);
        accountInfoFile = XmlParserHelper.getContent(rootEle, accountInfoFileTag);
        taxPercent = XmlParserHelper.getContentInteger(rootEle, taxPercentTag);

        // parse email information
        for (int i = 0; i < EmailType.NUMBER_EMAIL_TYPES.ordinal(); ++i)
        {
            parseEmailInfo(XmlParserHelper.getSingleElement(rootEle, emailInfoTags[i]), emailInfos[i]);
        }

        // parse account information
        parseAccountInfo();
    }

    private void parseEmailInfo(Element emailInfoEle, EmailInfo emailInfo)
    {
        emailInfo.emailSubject = XmlParserHelper.getContent(emailInfoEle, emailInfoSubjectTag);
        emailInfo.emailBodyFile = XmlParserHelper.getContent(emailInfoEle, emailInfoBodyFileTag);
    }

    private void parseAccountInfo()
    {
        // root element "AccountInformation"
        Element rootEle = XmlParserHelper.getRootElement(accountInfoFile);

        // parse account email information
        Element accEmailEle = XmlParserHelper.getSingleElement(rootEle, "Email");
        String userName = XmlParserHelper.getContent(accEmailEle, "UserName");
        String pword = XmlParserHelper.getContent(accEmailEle, "Password");

        // parse the registrations for early/late
        String[] earlyRegInfo = new String[RegistrationInfo.NUMBER_INFOS.ordinal()];
        String[] lateRegInfo = new String[RegistrationInfo.NUMBER_INFOS.ordinal()];

        Element earlyRegEle = XmlParserHelper.getSingleElement(rootEle, "EarlyRegistration");
        parseRegistrationInfo(earlyRegEle, earlyRegInfo);
        Element lateRegEle = XmlParserHelper.getSingleElement(rootEle, "LateRegistration");
        parseRegistrationInfo(lateRegEle, lateRegInfo);

        // reorder strings, in terms of (early, late)
        String[][] stringInversion =
                {
                        // spreadsheets
                        {earlyRegInfo[0], lateRegInfo[0]},
                        // formsite
                        {earlyRegInfo[1], lateRegInfo[1]},
                        // onsite
                        {earlyRegInfo[2], lateRegInfo[2]}
                };

        accInfo = new AccountInformation(userName, pword, stringInversion[0],
                                         stringInversion[1], stringInversion[2]);
    }

    private void parseRegistrationInfo(Element regInfoEle, String[] regInfo)
    {
        for (int i = 0; i < RegistrationInfo.NUMBER_INFOS.ordinal(); ++i)
            regInfo[i] = XmlParserHelper.getContent(regInfoEle, RegistrationInfoTags[i]);
    }

    /*
     * Methods to provide user with interface to get information
     */
    public String getEmailSubject(EmailType emailType)
    {
        return emailInfos[emailType.ordinal()].emailSubject;
    }

    public String getEmailBodyFile(EmailType emailType)
    {
        return emailInfos[emailType.ordinal()].emailBodyFile;
    }

    public String getETicketFile()
    {
        return eticketFile;
    }

    public String getFestivalClassesFile()
    {
        return festivalClassesFile;
    }

    public final AccountInformation getAccountInformation()
    {
        return accInfo;
    }

    public int getTaxPercent()
    {
        return taxPercent;
    }
}
