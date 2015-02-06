package speedr.gui.helpers;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FiltersTest {


    private static boolean filtersMatch(Filters.Filter f, Filters.Filter.FilterType filterType, String args)
    {
        return f.getFilterType().equals(filterType) && f.getFilterArg().equals(args);
    }

    @Test
    public void testParseFilterText() throws Exception {

        List<Filters.Filter> filtersList = Filters.parseFilterText("type:unread from:ed abcabc");

        boolean fromEdOK = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.FROM, "ed")).count() == 1;
        boolean remainContainsOK = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.CONTAINS, "abcabc")).count() == 1;
        boolean typeOK = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.TYPE, "unread")).count() == 1;

        assertTrue("from filter OK", fromEdOK);
        assertTrue("final search filter OK", remainContainsOK);
        assertTrue("type filter OK", typeOK);
    }
}