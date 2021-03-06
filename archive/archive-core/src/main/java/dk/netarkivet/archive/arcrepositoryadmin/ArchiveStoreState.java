/*
 * #%L
 * Netarchivesuite - archive
 * %%
 * Copyright (C) 2005 - 2017 The Royal Danish Library, 
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
package dk.netarkivet.archive.arcrepositoryadmin;

import java.util.Date;

import dk.netarkivet.common.distribute.arcrepository.ReplicaStoreState;
import dk.netarkivet.common.exceptions.ArgumentNotValid;

/**
 * This class contains a storestate, and the time, when it was last set.
 */
public class ArchiveStoreState {

    /** The state for a specific archive, or overall state. */
    private ReplicaStoreState storestate;

    /** Time of last state change. */
    private Date lastchanged;

    /**
     * Constructor for this class. This sets the lastchanged value to Now.
     *
     * @param storestate A BitArchiveStoreState
     */
    public ArchiveStoreState(ReplicaStoreState storestate) {
        setState(storestate);
    }

    /**
     * Constructor for this class.
     *
     * @param storestate A BitArchiveStoreState
     * @param lastchanged Time for when this state was set
     */
    public ArchiveStoreState(ReplicaStoreState storestate, Date lastchanged) {
        setState(storestate, lastchanged);
    }

    /**
     * Return the current BitArchiveStoreState.
     *
     * @return the current BitArchiveStoreState
     */
    public ReplicaStoreState getState() {
        return storestate;
    }

    /**
     * Sets the current ReplicaStoreState.
     *
     * @param state The ReplicaStoreState.
     * @param lastDate The lastchanged date.
     * @throws ArgumentNotValid If the state or the lastDate is null.
     */
    public void setState(ReplicaStoreState state, Date lastDate) throws ArgumentNotValid {
        ArgumentNotValid.checkNotNull(state, "ReplicaStoreState state");
        ArgumentNotValid.checkNotNull(lastDate, "Date lastDate");

        this.storestate = state;
        this.lastchanged = lastDate;
    }

    /**
     * Sets the current ReplicaStoreState. As a sideeffect lastchanged is set to NOW.
     *
     * @param state the ReplicaStoreState.
     * @throws ArgumentNotValid If the state is null.
     */
    public void setState(ReplicaStoreState state) throws ArgumentNotValid {
        ArgumentNotValid.checkNotNull(state, "ReplicaStoreState state");
        this.storestate = state;
        this.lastchanged = new Date();
    }

    /**
     * Get the Date for when the state was lastchanged.
     *
     * @return the Date for when the state was lastchanged
     */
    public Date getLastChanged() {
        return this.lastchanged;

    }

    /**
     * Creates an string representation of this instance.
     *
     * @return The string representation of this instance.
     */
    public String toString() {
        String stringRepresentation = getState() + " " + getLastChanged().getTime();
        return stringRepresentation;
    }
}
