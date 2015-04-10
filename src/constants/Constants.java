package constants;

public class Constants
{
    private Constants()
    {
    }

    private static final String                WORKSHEET_CONFIG_FILENAME = "files/worksheet_config.xml";
    private static final WorksheetConfigReader WORKSHEET_CONFIG_READER   = new WorksheetConfigReader(WORKSHEET_CONFIG_FILENAME);
    private static final String                ETICKET_CONFIG_FILENAME   = "files/eticket_config.xml";
    private static final EticketConfigReader   ETICKET_CONFIG_READER     = new EticketConfigReader(ETICKET_CONFIG_FILENAME);

    /* SPREADSHEET COLUMN NUMBERS */
    public static final int AMOUNT_COL                        = WORKSHEET_CONFIG_READER.getAmountCol();
    public static final int FIRST_NAME_COL                    = WORKSHEET_CONFIG_READER.getFirstNameCol();
    public static final int LAST_NAME_COL                     = WORKSHEET_CONFIG_READER.getLastNameCol();
    public static final int EMAIL_COL                         = WORKSHEET_CONFIG_READER.getEmailCol();
    public static final int PHONE_COL                         = WORKSHEET_CONFIG_READER.getPhoneCol();
    public static final int STUDENT_TYPE_COL                  = WORKSHEET_CONFIG_READER.getStudentTypeCol();
    public static final int DANCER_TYPE_COL                   = WORKSHEET_CONFIG_READER.getDancerTypeCol();
    public static final int EXP_LVL_COL                       = WORKSHEET_CONFIG_READER.getExpLvlCol();
    public static final int NUMBER_REGISTRANTS_COL            = WORKSHEET_CONFIG_READER.getNumberRegistrantsCol();
    public static final int SECOND_REGISTRANT_FIRST_NAME_COL  = WORKSHEET_CONFIG_READER.getSecondRegistrantFirstNameCol();
    public static final int SECOND_REGISTRANT_LAST_NAME_COL   = WORKSHEET_CONFIG_READER.getSecondRegistrantLastNameCol();
    public static final int SECOND_REGISTRANT_DANCER_TYPE_COL = WORKSHEET_CONFIG_READER.getSecondRegistrantDancerTypeCol();
    public static final int CLASS_MIN_COL                     = WORKSHEET_CONFIG_READER.getClassMinCol();
    public static final int CLASS_MAX_COL                     = WORKSHEET_CONFIG_READER.getClassMaxCol();
    public static final int COMPLETED_REGISTRANT_COL          = WORKSHEET_CONFIG_READER.getCompletedRegistrantCol();
    public static final int ETICKET_COL                       = WORKSHEET_CONFIG_READER.getEticketCol();

    /* CELL FEED PARAMETERS NUMBERS */
    public static final int CELL_FEED_FIRST_COL = WORKSHEET_CONFIG_READER.getCellFeedFirstCol();
    public static final int CELL_FEED_LAST_COL  = WORKSHEET_CONFIG_READER.getCellFeedLastCol();

    /* E-TICKET CONSTANTS */
    public static final int   ETICKET_FIRST_NAME_X      = ETICKET_CONFIG_READER.getEticketFirstNameX();
    public static final int   ETICKET_FIRST_NAME_Y      = ETICKET_CONFIG_READER.getEticketFirstNameY();
    public static final int   ETICKET_LAST_NAME_X       = ETICKET_CONFIG_READER.getEticketLastNameX();
    public static final int   ETICKET_LAST_NAME_Y       = ETICKET_CONFIG_READER.getEticketLastNameY();
    public static final int   ETICKET_STUDENT_TYPE_X    = ETICKET_CONFIG_READER.getEticketStudentTypeX();
    public static final int   ETICKET_STUDENT_TYPE_Y    = ETICKET_CONFIG_READER.getEticketStudentTypeY();
    public static final int   ETICKET_CLASSES_X         = ETICKET_CONFIG_READER.getEticketClassesX();
    public static final int   ETICKET_FIRST_CLASS_Y     = ETICKET_CONFIG_READER.getEticketFirstClassY();
    public static final int   ETICKET_CLASS_INCREMENT_Y = ETICKET_CONFIG_READER.getEticketClassIncrementY();
    public static final float ETICKET_LARGE_FONT_SIZE   = ETICKET_CONFIG_READER.getEticketLargeFontSize();
    public static final float ETICKET_SMALL_FONT_SIZE   = ETICKET_CONFIG_READER.getEticketSmallFontSize();

    /* ARRAY OF STRINGS TO FILTER CLASSES WITH: SHOULD ALL BE LOWER CASE */
    // the filtering is used when pulling classes from the spreadsheet online
    public static final String CLASS_FILTER_STRINGS[] = {"none", "carte"};
    public static final String ETICKET_SENT           = "x";
}

