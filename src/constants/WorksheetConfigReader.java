package constants;

class WorksheetConfigReader extends AbstractConfigReader
{
    WorksheetConfigReader(String filename)
    {
        super(filename);
    }

    public int getAmountCol()
    {
        return getInt(WorksheetConfigConstants.AMOUNT_COL);
    }

    public int getFirstNameCol()
    {
        return getInt(WorksheetConfigConstants.FIRST_NAME_COL);
    }

    public int getLastNameCol()
    {
        return getInt(WorksheetConfigConstants.LAST_NAME_COL);
    }

    public int getEmailCol()
    {
        return getInt(WorksheetConfigConstants.EMAIL_COL);
    }

    public int getPhoneCol()
    {
        return getInt(WorksheetConfigConstants.PHONE_COL);
    }

    public int getStudentTypeCol()
    {
        return getInt(WorksheetConfigConstants.STUDENT_TYPE_COL);
    }

    public int getDancerTypeCol()
    {
        return getInt(WorksheetConfigConstants.DANCER_TYPE_COL);
    }

    public int getExpLvlCol()
    {
        return getInt(WorksheetConfigConstants.EXP_LVL_COL);
    }

    public int getNumberRegistrantsCol()
    {
        return getInt(WorksheetConfigConstants.NUMBER_REGISTRANTS_COL);
    }

    public int getSecondRegistrantFirstNameCol()
    {
        return getInt(WorksheetConfigConstants.SECOND_REGISTRANT_FIRST_NAME_COL);
    }

    public int getSecondRegistrantLastNameCol()
    {
        return getInt(WorksheetConfigConstants.SECOND_REGISTRANT_LAST_NAME_COL);
    }

    public int getSecondRegistrantDancerTypeCol()
    {
        return getInt(WorksheetConfigConstants.SECOND_REGISTRANT_DANCER_TYPE_COL);
    }

    public int getClassMinCol()
    {
        return getInt(WorksheetConfigConstants.CLASS_MIN_COL);
    }

    public int getClassMaxCol()
    {
        return getInt(WorksheetConfigConstants.CLASS_MAX_COL);

    }

    public int getCompletedRegistrantCol()
    {
        return getInt(WorksheetConfigConstants.COMPLETED_REGISTRANT_COL);
    }

    public int getEticketCol()
    {
        return getInt(WorksheetConfigConstants.ETICKET_COL);
    }

    public int getCellFeedFirstCol()
    {
        return getInt(WorksheetConfigConstants.CELL_FEED_FIRST_COL);
    }

    public int getCellFeedLastCol()
    {
        return getInt(WorksheetConfigConstants.CELL_FEED_LAST_COL);
    }
}
