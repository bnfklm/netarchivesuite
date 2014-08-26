/*
 * #%L
 * Netarchivesuite - harvester - test
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

package dk.netarkivet.viewerproxy;

import java.io.File;

/**
 * Constants for shared use by viewerproxy unit tests.
 */
public class TestInfo {

    private static final File BASE_DIR = new File("tests/dk/netarkivet/viewerproxy/data");

    static final File WORKING_DIR = new File(BASE_DIR, "working");
    static final File ORIGINALS_DIR = new File(BASE_DIR, "input");
    static final File METADATA_DIR = new File(BASE_DIR, "metadata");

    /**
     * An archive directory to work on.
     */
    static final File ARCHIVE_DIR = new File(WORKING_DIR, "bitarchive1");
    public static final File ZIPPED_INDEX_DIR = new File(WORKING_DIR, "2-3-cache");
    public static final File ZIPPED_INDEX_DIR2 = new File(WORKING_DIR, "2-4-3-5-cache");

}
