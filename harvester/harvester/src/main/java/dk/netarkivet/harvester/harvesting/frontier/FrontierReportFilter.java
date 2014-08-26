/*
 * #%L
 * Netarchivesuite - harvester
 * %%
 * Copyright (C) 2005 - 2014 The Royal Danish Library, the Danish State and University Library,
 *             the National Library of France and the Austrian National Library.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package dk.netarkivet.harvester.harvesting.frontier;

/**
 * Interface for a frontier report filter.
 *
 * Such a filter takes a frontier report as input, and filters its lines to
 * generate another frontier report.
 *
 */
public interface FrontierReportFilter {

    /**
     * Initialize the filter from arguments.
     * 
     * @param args
     *            the arguments as strings.
     */
    void init(String[] args);

    /**
     * Filters the given frontier report.
     * 
     * @param initialFrontier
     *            the report to filter.
     * @return a filtered frontier report.
     */
    InMemoryFrontierReport process(FrontierReport initialFrontier);

    /**
     * Returns a unique identifier for this filter class.
     * 
     * @return unique identifier for this filter class
     */
    String getFilterId();

}
