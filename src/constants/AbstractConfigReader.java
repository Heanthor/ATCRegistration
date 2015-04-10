package constants;

import classes.XmlParserHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

class AbstractConfigReader
{
    private static final String COLUMN_TAG_NAME     = "column";
    private static final String NAME_ATTRIBUTE_KEY  = "name";
    private static final String VALUE_ATTRIBUTE_KEY = "value";

    AbstractConfigReader(String filename)
    {
        m_config = new HashMap<>();

        Element rootElement = XmlParserHelper.getRootElement(filename);

        NodeList nodeList = rootElement.getElementsByTagName(COLUMN_TAG_NAME);
        for (int i = 0; i < nodeList.getLength(); ++i)
        {
            Element element = (Element) nodeList.item(i);
            m_config.put(element.getAttribute(NAME_ATTRIBUTE_KEY), element.getAttribute(VALUE_ATTRIBUTE_KEY));
        }
    }

    int getInt(String key)
    {
        return Integer.parseInt(m_config.get(key));
    }

    float getFloat(String key)
    {
        return Float.parseFloat(m_config.get(key));
    }

    private final Map<String, String> m_config;
}
