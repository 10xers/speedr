package speedr.core.notthings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Speedr / Ed
 * 06/02/2015 22:07
 */
public class Filters {

    private static final Pattern filterArg = Pattern.compile("(\\w+):\\s*([^\\s]+)");

    public static class Filter {
        public enum FilterType {
            FROM,
            TYPE,
            CONTAINS
        }

        private FilterType filterType;
        private String filterArg;

        public Filter(FilterType filterType, String filterArg) {
            this.filterType = filterType;
            this.filterArg = filterArg;
        }

        public FilterType getFilterType() {
            return filterType;
        }

        public String getFilterArg() {
            return filterArg;
        }
    }


    public static List<HasContent> filterList(String filterText, List<HasContent> list)
    {
        List<HasContent> ret = new ArrayList<>();
        List<Filter> activeFilters = parseFilterText(filterText);

        for(HasContent c : list)
        {
            boolean passes = true;

            for (Filter f : activeFilters)

            switch(f.getFilterType())
            {
                case CONTAINS:
                    passes = passes && c.getContent().contains(f.getFilterArg());
                    break;
                case FROM:
                    break;
                case TYPE:
                    break;
                default:
                    throw new UnsupportedOperationException("failed to evaluate filter type");
            }

            if (passes)
                ret.add(c);
        }

        return ret;
    }


    public static List<Filter> parseFilterText(String filterText)
    {
        String strippedFilterText = filterText;
        List<Filter> filters = new ArrayList<>();
        Matcher m = filterArg.matcher(filterText);

        while (m.find())
        {
            String filterType = m.group(1);
            String filterArg = m.group(2);

            Filter.FilterType type = filterTypeFromString(filterType);

            if (null!=type) {
                filters.add(new Filter(type, filterArg));
                strippedFilterText = strippedFilterText.replaceAll(filterType+":\\s*?"+filterArg, "");
            }

        }

        if (!strippedFilterText.trim().isEmpty())
            filters.add(new Filter(Filter.FilterType.CONTAINS, strippedFilterText.trim()));

        return filters;
    }

    private static Filter.FilterType filterTypeFromString(String proposed)
    {
        String proposedLower = proposed.toLowerCase();

        switch(proposed)
        {
            case "from":
                return Filter.FilterType.FROM;
            case "type":
                return Filter.FilterType.TYPE;
            case "contains:":
                return Filter.FilterType.CONTAINS;
            default:
                return null;
        }
    }


}
