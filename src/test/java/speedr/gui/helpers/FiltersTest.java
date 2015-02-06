package speedr.gui.helpers;

import org.junit.Test;
import speedr.sources.HasContent;

import java.util.ArrayList;
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


    @Test
    public void testFilteredCorrectly() throws Exception {

        List<HasContent> orig = new ArrayList<>();
        orig.add(() -> "HELLO WORLD! HOW ARE YOU");
        orig.add(() -> "Halp. I need somebody");
        orig.add(() -> "Not just anybody");

        List<HasContent> foundSomebody = Filters.filterList("somebody", orig);

        assertEquals(1, foundSomebody.size());
        assertTrue(foundSomebody.get(0).getContent().equals(orig.get(1).getContent()));
    }
}