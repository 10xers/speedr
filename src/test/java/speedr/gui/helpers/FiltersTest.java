package speedr.gui.helpers;

import org.junit.Test;
import speedr.sources.HasContent;
import speedr.sources.email.Email;

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

        List<Filters.Filter> filtersList = Filters.parseFilterText("type:unread from:ed subject:madness abcabc");

        boolean fromEdOK = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.FROM, "ed")).count() == 1;
        boolean remainContainsOK = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.CONTAINS, "abcabc")).count() == 1; // case insensitive
        boolean typeOK = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.TYPE, "unread")).count() == 1;
        boolean subjectOk = filtersList.stream().filter((f) -> filtersMatch(f, Filters.Filter.FilterType.SUBJECT, "madness")).count() == 1;

        assertTrue("from filter OK", fromEdOK);
        assertTrue("final search filter OK", remainContainsOK);
        assertTrue("type filter OK", typeOK);
        assertTrue("subject filter OK", subjectOk);
    }


    @Test
    public void testFilteredCorrectly() throws Exception {

        List<Email> orig = new ArrayList<>();

        orig.add(new Email("ed@ed.com", "this is the best", "HELLO WORLD! Halp HOW ARE YOU", false));
        orig.add(new Email("ed@noted.com", "second",  "Halp. I need somebody", false));
        orig.add(new Email("ed@ed.com", "second",  "Not just anybody", true));

        List<Email> foundSomebody = Filters.filterList("somebody", orig);

        assertEquals(1, foundSomebody.size());
        assertTrue(foundSomebody.get(0).getContent().equals(orig.get(1).getContent()));

        List<Email> foundHalp = Filters.filterList("halp", orig);

        assertEquals(2, foundHalp.size());
        assertTrue(foundHalp.get(0).getContent().equals(orig.get(0).getContent()));
        assertTrue(foundHalp.get(1).getContent().equals(orig.get(1).getContent()));

        List<Email> findMe = Filters.filterList("from:ed@ed.com", orig);

        assertEquals(2, findMe.size());
        assertTrue(findMe.get(0).getFrom().equals("ed@ed.com"));
        assertTrue(findMe.get(1).getFrom().equals("ed@ed.com"));

        List<Email> findSubject = Filters.filterList("subject:second", orig);
        assertEquals(2, findSubject.size());
        assertTrue(findSubject.get(0).getSubject().equals("second"));
        assertTrue(findSubject.get(1).getSubject().equals("second"));

        List<Email> findUnread = Filters.filterList("type:unread", orig);
        assertEquals(2, findUnread.size());
        assertFalse(findUnread.get(0).isRead());
        assertFalse(findUnread.get(1).isRead());

    }
}