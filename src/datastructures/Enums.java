package datastructures;

import classes.AtcErr;

/**
 * This class contains a list of enums that are used to describe
 * registrants, festival properties, spreadsheet properties,
 * button values, etc. There are also a few methods used
 * to help in parsing strings into the appropriate enum type.
 *
 * @author benjamyn
 */
public class Enums
{
    public enum StudentType
    {
        UMD_STUDENT
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "UmdStudent";
                    }

                    @Override
                    public String toString()
                    {
                        return "UMD Student";
                    }
                },
        OTHER_STUDENT
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "OtherStudent";
                    }

                    @Override
                    public String toString()
                    {
                        return "Other Student";
                    }
                },
        GENERAL_ADMISSION
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "GeneralAdmission";
                    }

                    @Override
                    public String toString()
                    {
                        return "General Admission";
                    }
                };

        public abstract String getXmlTag();
    }

    public enum DancerType
    {
        FOLLOWER,
        LEADER
    }

    public enum ExperienceLevel
    {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    /* used for mapping day # to week day */
    public enum FestivalDay
    {
        FRIDAY
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "Friday";
                    }
                },
        SATURDAY
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "Saturday";
                    }
                },
        SUNDAY
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "Sunday";
                    }
                };

        public abstract String getXmlTag();
    }


    /* special pass mappings */
    public enum SpecialPassType
    {
        FULL_PASS
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "FullPass";
                    }
                },
        FRIDAY_PASS
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "FridayPass";
                    }
                },
        SATURDAY_PASS
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "SaturdayPass";
                    }
                },
        SUNDAY_PASS
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "SundayPass";
                    }
                },
        MILONGA_PASS
                {
                    @Override
                    public String getXmlTag()
                    {
                        return "MilongaPass";
                    }
                };

        public abstract String getXmlTag();
    }


    public enum SheetClientMode
    {
        FORM_SITE,
        ON_SITE
    }

    public enum RegistrationMode
    {
        EARLY_REGISTRATION,
        LATE_REGISTRATION
    }

    /* Yes/No enum for working with the On-Site registration buttons */
    public enum YesNo
    {
        YES,
        NO
    }


    /**
     * A function used to convert a string to an enum value in regards to the
     * student type. This function works by searching the input string for the
     * word (case insensitive):
     * <ol>
     * <li>UMD_STUDENT: look for word "UMD"</li>
     * <li>OTHER_STUDENT: look for word "other"</li>
     * <li>GENERAL_ADMISSION: look for word "general"</li>
     * </ol>
     * This approach was taken in case the string changed and all the configurations
     * would then have to be reset (e.g. "UMD or GMU Student" to "UMD/GMU Student").
     * If none of these strings are found, an exception is thrown, in which case
     * this method, configuration, or something needs to be modified.
     *
     * @param sType string for parsing
     * @return The appropriate student type. Returns null if the input string is
     * null or the empty string.
     */
    public static StudentType stringToStudentType(String sType)
    {
        if (sType == null || sType.compareTo("") == 0)
            return null;
        else if (sType.toLowerCase().contains("non-umd"))
            return StudentType.OTHER_STUDENT;
        else if (sType.toLowerCase().contains("umd"))
            return StudentType.UMD_STUDENT;
        else if (sType.toLowerCase().contains("general"))
            return StudentType.GENERAL_ADMISSION;
        else
        {
            AtcErr.createErrorDialog("Converting string to StudentType: could not parse: '%s'", sType);
            return null;
        }
    }

    /**
     * A function used to convert a string to an enum value in regards to the
     * dancer type. This function works by comparing the string representation
     * of the types found in {@link DancerType}
     *
     * @param dType string for parsing
     * @return The appropriate dancer type. Returns null if the input string is
     * null or the empty string.
     */
    public static DancerType stringToDancerType(String dType)
    {
        if (dType == null || dType.compareTo("") == 0)
            return null;
        else if (dType.toLowerCase().contains(DancerType.FOLLOWER.toString().toLowerCase()))
            return DancerType.FOLLOWER;
        else if (dType.toLowerCase().contains(DancerType.LEADER.toString().toLowerCase()))
            return DancerType.LEADER;
        else
        {
            AtcErr.createErrorDialog("Converting string to DancerType: could not parse: '%s'", dType);
            return null;
        }
    }

    /**
     * A function used to convert a string to an enum value in regards to the
     * experience level. This function works by comparing the string representation
     * of the types found in {@link ExperienceLevel}.
     *
     * @param eLvl string for parsing
     * @return The appropriate experience level. Returns null if the input string is
     * null or the empty string.
     */
    public static ExperienceLevel stringToExperienceLevel(String eLvl)
    {
        if (eLvl == null || eLvl.compareTo("") == 0)
            return null;
        else if (eLvl.toLowerCase().contains(ExperienceLevel.BEGINNER.toString().toLowerCase()))
            return ExperienceLevel.BEGINNER;
        else if (eLvl.toLowerCase().contains(ExperienceLevel.INTERMEDIATE.toString().toLowerCase()))
            return ExperienceLevel.INTERMEDIATE;
        else if (eLvl.toLowerCase().contains(ExperienceLevel.ADVANCED.toString().toLowerCase()))
            return ExperienceLevel.ADVANCED;
        else
        {
            AtcErr.createErrorDialog("Converting string to ExperienceLevel: could not parse: '%s'", eLvl);
            return null;
        }
    }
}
