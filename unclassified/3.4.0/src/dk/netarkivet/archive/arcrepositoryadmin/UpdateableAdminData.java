/* File:             $Id$
 * Revision:         $Revision$
 * Author:           $Author$
 * Date:             $Date$
 * The Netarchive Suite - Software to harvest and preserve websites
 * Copyright 2004-2007 Det Kongelige Bibliotek and Statsbiblioteket, Denmark
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package dk.netarkivet.archive.arcrepositoryadmin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.netarkivet.archive.arcrepository.distribute.StoreMessage;
import dk.netarkivet.common.distribute.arcrepository.BitArchiveStoreState;
import dk.netarkivet.common.exceptions.ArgumentNotValid;
import dk.netarkivet.common.exceptions.IOFailure;
import dk.netarkivet.common.exceptions.PermissionDenied;
import dk.netarkivet.common.exceptions.UnknownID;


/**
 * Class for accessing and manipulating the administrative data for
 * the ArcRepository.
 * In the current implementation, it consists of a file with a number of lines
 * of the form: <filename> <checksum> <state> <timestamp-for-last-state-change>
       [,<bitarchive> <storestatus> <timestamp-for-last-state-change>]*
 *
 * If a line in the admin data file is corrupt, the entry is removed
 * from admindata.
 *
 * Notes: If the admindata file does not exist on start-up, the
 * file is created in the constructor.
 * If the admindata file on start-up is the oldversion,
 * the admindata file is migrated to the new version.
 *
 */
public class UpdateableAdminData extends AdminData {
    /** Logger for this class. */
    private Log log = LogFactory.getLog(getClass().getName());

    /** the singleton for the UpdateableAdminData class. */
    private static UpdateableAdminData instance;

    /**
     * Constructor for the UpdateableAdminData class.
     * Reads the admindata file if it exists, creates it otherwise
     * @throws PermissionDenied if admin data directory is not accessible
     * @throws IOFailure if there is trouble reading or creating
     * the admin data file
     */
    private UpdateableAdminData() throws IOFailure {
        super();
        if (!adminDataFile.exists()) {
            log.info("Creating new admin data file " + adminDataFile);
        }
        // Always rewrite after read, as we're cutting out old entries
        // to shorten the file.
        write();
        log.debug("AdminData created");
    }

    /** Get the singletont instance.
     * @return The singleton
     * */
    public static UpdateableAdminData getInstance() {
        if (UpdateableAdminData.instance == null) {
            UpdateableAdminData.instance = new UpdateableAdminData();
        }
        return UpdateableAdminData.instance;
    }

    /**
     * Add new entry to the admin data, and persist it to disk.
     * @param filename  A filename
     * @param replyInfo A replyInfo for this entry (may be null)
     * @param checksum  The Checksum for this file
     * @param generalState The generalState for this entry
     */
    public void addEntry(String filename, StoreMessage replyInfo,
                         String checksum, ArchiveStoreState generalState) {
        addEntry(filename, replyInfo, checksum, generalState, true);
    }

    /**
     * Add new entry to the ddmin data, and persist it to disk,
     * if persistNow set to true.
     * @param filename  A filename
     * @param replyInfo A replyInfo for this entry (may be null)
     * @param checksum  The Checksum for this file
     * @param generalState The generalState for this entry
     * @param persistNow Shall we persist this entry now?
     */
    public void addEntry(String filename, StoreMessage replyInfo,
            String checksum, ArchiveStoreState generalState,
            boolean persistNow) {
        ArgumentNotValid.checkNotNullOrEmpty(filename, "String filename");
        ArgumentNotValid.checkNotNullOrEmpty(checksum, "String checksum");
        ArgumentNotValid.checkNotNull(generalState, "generalState");
        storeEntries.put(filename,
                new ArcRepositoryEntry(filename, checksum, replyInfo));
        if (persistNow) {
            //Persist the new entry
            //Note: This appends the new entry to the end of the admindata file
            write(filename);
        }
    }


    /**
     * Records the replyInfo (StoreMessage object) so that it can be retrieved
     * using the given file name.
     *
     * @param arcfileName An arc file that someone is trying to store.
     * @param replyInfo   A StoreMessage object related to this filename.
     * @throws UnknownID if no info has been registered for arcfilename
     */
    public void setReplyInfo(String arcfileName, StoreMessage replyInfo)
            throws UnknownID {
        ArgumentNotValid.checkNotNullOrEmpty(arcfileName, "arcfileName");
        ArgumentNotValid.checkNotNull(replyInfo, "replyInfo");
        if (!hasEntry(arcfileName)) {
            throw new UnknownID("Cannot set replyinfo '" + replyInfo
                    + "' for unregistered file '" + arcfileName + "'");
        }
        ArcRepositoryEntry entry = storeEntries.get(arcfileName);
        entry.setReplyInfo(replyInfo); //TODO: Should this be persisted
    }

    /**
     * Removes the replyInfo associated with arcfileName.
     *
     * @param arcfileName A file that we are trying to store.
     * @return the replyInfo associated with arcfileName.
     * @throws UnknownID If the arcfilename is not known.
     *         or no replyInfo is associated with arcfileName.
     */
    public StoreMessage removeReplyInfo(String arcfileName)
            throws UnknownID {
        ArgumentNotValid.checkNotNullOrEmpty(arcfileName, "arcfileName");
        if (!hasEntry(arcfileName)) {
            throw new UnknownID("Cannot get reply info for unregistered file '"
                    + arcfileName + "'");
        }
        if (!hasReplyInfo(arcfileName)) {
            throw new UnknownID("replyInfo not set for " + arcfileName);
        }
        ArcRepositoryEntry entry = storeEntries.get(arcfileName);
        return entry.getAndRemoveReplyInfo();
    }

    /**
     * Sets the store state for the given file on the given bitarchive.
     *
     * @param arcfileName  A file that is being stored.
     * @param bitarchiveID A bitarchive.
     * @param state        The state of upload of arcfileName on bitarchiveID.
     * @throws UnknownID If the arcfile does not have a store entry.
     * @throws ArgumentNotValid If the arguments are null or empty
     */
    public void setState(String arcfileName, String bitarchiveID,
                         BitArchiveStoreState state)
            throws UnknownID {
        ArgumentNotValid.checkNotNullOrEmpty(arcfileName, "arcfileName");
        ArgumentNotValid.checkNotNullOrEmpty(bitarchiveID, "bitarchiveID");
        ArgumentNotValid.checkNotNull(state, "state");
        if (!hasEntry(arcfileName)) {
            final String message = "Unregistered file '" + arcfileName
                                + "' cannot be set to state " + state + " in '"
                                + bitarchiveID + "'";
            log.warn(message);
            throw new UnknownID(message);
        }

        // TODO: What is this good for?
        // Only used by the toString() method.
        if (!knownBitArchives.contains(bitarchiveID)) {
            knownBitArchives.add(bitarchiveID);
        }
        storeEntries.get(arcfileName).setStoreState(bitarchiveID, state);
        write(arcfileName); // Add entry for arcfileName in persistent storage.
    }

    /**
     * Set/update the checksum for a given arcfileName in the admindata.
     * @param arcfileName   Unique name of file for which to store checksum
     * @param checkSum
     *      The generated (MD5) checksum to be stored in reference table
     * @throws UnknownID if the file is not already registered.
     */
    public void setCheckSum(String arcfileName, String checkSum) {
        ArgumentNotValid.checkNotNullOrEmpty(arcfileName, "arcfileName");
        ArgumentNotValid.checkNotNullOrEmpty(checkSum, "checkSum");
        if (!hasEntry(arcfileName)) {
            throw new UnknownID("Cannot change checksum for unregistered file '"
                    + arcfileName + "'");
        }
        log.trace("Changing checksum for " + arcfileName
                + " from " + getCheckSum(arcfileName)
                + " to " + checkSum);
        storeEntries.get(arcfileName).setChecksum(checkSum);
        write(); // Write everything to persistent storage
    }


    /**
     * Write all the admin data to file.
     * This overwrites the previous file and writes data for all entries.
     * This operation can be rather time-consuming if there is a lot of
     * data.
     * We expect to only do this a) when creating a new admindata (in order
     * to flush out repeated entries created during uploads) and b)
     * when performing a correct (to ensure that an arcfile only has one
     * checksum).
     * The write is done atomically, i.e. either the old file is kept or the
     * entire new file is written.
     * @throws IOFailure on trouble writing to file
     */
    private void write() {
        // First write admindata to a temporary file.
        final File adminDataStore = adminDataFile;
        final File tmpDataStore =
                new File(adminDir, AdminData.ADMIN_FILE_NAME + ".tmp");
        final File backupDataStore =
                new File(adminDir, AdminData.ADMIN_FILE_NAME + ".backup");
        PrintWriter writer = null;
        try {
            final FileWriter out = new FileWriter(tmpDataStore);
            writer = new PrintWriter(out);
            writer.println(versionNumber);
            for (Map.Entry<String, ArcRepositoryEntry> entry
                    : storeEntries.entrySet()) {
                final String arcfilename = entry.getKey();
                final ArcRepositoryEntry arcrepentry = entry.getValue();
                write(writer, arcfilename, arcrepentry);
            }
            writer.flush();
            writer.close();
            writer = null;
            adminDataStore.renameTo(backupDataStore);
            tmpDataStore.renameTo(adminDataStore);
        } catch (IOException e) {
            throw new IOFailure("Failed to write admin data to '"
                    + adminDataFile.getPath() + "'", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            // Delete the temporary file if write failed.
            tmpDataStore.delete();
            if (!adminDataStore.exists()) {
                backupDataStore.renameTo(adminDataStore);
            } else {
                backupDataStore.delete();
            }
        }

    }

    /** Write a single entry to the admin data file.
     * This uses the ArcRepositoryEntry.output() method.
     *
     * @param writer The output stream
     * @param arcfilename  the filename which entry is to be written
     * @param arcrepentry The data kept for this arcfile
     * @throws ArgumentNotValid if arcrepentry.getFilename() != arcfilename
     * @throws PermissionDenied
     */
    private void write(PrintWriter writer, String arcfilename,
            final ArcRepositoryEntry arcrepentry)
        throws PermissionDenied {
        ArgumentNotValid.checkTrue(
                arcrepentry.getFilename().equals(arcfilename),
                "arcrepentry.getFilename() is not equal to arcfilename (!!)");

        arcrepentry.output(writer);
        writer.println();
    }

    /** Write a particular entry to the admin data file.
     * This will append the data to the end of the file.
     * @param filename the name of the file which entry is to written
     *  to admin data file
     *  @throws PermissionDenied (does it?)
     */
    private void write(String filename) throws PermissionDenied {
        ArcRepositoryEntry entry = storeEntries.get(filename);
        File adminDataStore = adminDataFile;
        PrintWriter writer = null;
        try {
            final FileWriter out = new FileWriter(adminDataStore, true);
            writer = new PrintWriter(out);
            write(writer, filename, entry);
        } catch (IOException e) {
            throw new IOFailure("Failed to write admin data for '"
                    + filename + "' to '" + adminDataFile.getName() + "'", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        log.debug("appending entry for filename '" + filename
                + "' to admin.data");
    }

    /** Makes sure all data is written to disk. */
    public void close() {
        if (instance != null) {
            write(); // This rewrites all admindata onto disk
        }
        instance = null;
    }
}