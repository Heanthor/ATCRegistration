package constants;

class EticketConfigReader extends AbstractConfigReader
{
    EticketConfigReader(String filename)
    {
        super(filename);
    }

    public int getEticketFirstNameX()
    {
        return getInt(EticketConfigConstants.ETICKET_FIRST_NAME_X);
    }

    public int getEticketFirstNameY()
    {
        return getInt(EticketConfigConstants.ETICKET_FIRST_NAME_Y);
    }

    public int getEticketLastNameX()
    {
        return getInt(EticketConfigConstants.ETICKET_LAST_NAME_X);
    }

    public int getEticketLastNameY()
    {
        return getInt(EticketConfigConstants.ETICKET_LAST_NAME_Y);
    }

    public int getEticketStudentTypeX()
    {
        return getInt(EticketConfigConstants.ETICKET_STUDENT_TYPE_X);
    }

    public int getEticketStudentTypeY()
    {
        return getInt(EticketConfigConstants.ETICKET_STUDENT_TYPE_Y);
    }

    public int getEticketClassesX()
    {
        return getInt(EticketConfigConstants.ETICKET_CLASSES_X);
    }

    public int getEticketFirstClassY()
    {
        return getInt(EticketConfigConstants.ETICKET_FIRST_CLASS_Y);
    }

    public int getEticketClassIncrementY()
    {
        return getInt(EticketConfigConstants.ETICKET_CLASS_INCREMENT_Y);
    }

    public float getEticketLargeFontSize()
    {
        return getFloat(EticketConfigConstants.ETICKET_LARGE_FONT_SIZE);
    }

    public float getEticketSmallFontSize()
    {
        return getFloat(EticketConfigConstants.ETICKET_SMALL_FONT_SIZE);
    }
}
