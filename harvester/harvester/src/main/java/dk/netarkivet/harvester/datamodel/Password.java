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

package dk.netarkivet.harvester.datamodel;

import dk.netarkivet.common.exceptions.ArgumentNotValid;
import dk.netarkivet.common.utils.Named;

/**
 * Immutable password class. Represents a http credentials password. See RFC
 * 2617: HTTP Authentication: Basic and Digest Access Authentication
 */

public final class Password implements Named {

    /** The name of the Password. Used for sorting. */
    private final String name;
    /** Any comments given to the Password. */
    private final String comments;
    /** The domain where this Password is used. */
    private final String passwordDomain;
    /**
     * The realm for this Password. Defines a specific protection space on a
     * webserver. See RFC 2617, section 1.2
     */
    private final String realm;
    /** The username. */
    private final String username;
    /** The password. */
    private final String password;

    /** ID autogenerated by DB, ignored otherwise. */
    private Long id;

    /**
     * Create a new password object with the given information.
     *
     * @param name
     *            The name of the Password
     * @param comments
     *            Any comments
     * @param passwordDomain
     *            the domain where this Password is used
     * @param realm
     *            the realm of the Password
     * @param username
     *            the username
     * @param password
     *            the password
     */
    public Password(String name, String comments, String passwordDomain, String realm, String username, String password) {
        ArgumentNotValid.checkNotNullOrEmpty(name, "name");
        ArgumentNotValid.checkNotNull(comments, "comments");
        ArgumentNotValid.checkNotNullOrEmpty(passwordDomain, "passwordDomain");
        ArgumentNotValid.checkNotNullOrEmpty(realm, "realm");
        ArgumentNotValid.checkNotNullOrEmpty(username, "username");
        ArgumentNotValid.checkNotNullOrEmpty(password, "password");

        this.name = name;
        this.comments = comments;
        this.passwordDomain = passwordDomain;
        this.realm = realm;
        this.username = username;
        this.password = password;
    }

    /**
     * @return the name of the Password
     */
    public String getName() {
        return name;
    }

    /**
     * @return the comments for the Password
     */
    public String getComments() {
        return comments;
    }

    /**
     * @return the domain for the Password
     */
    public String getPasswordDomain() {
        return passwordDomain;
    }

    /**
     * @return the Realm for the Password
     */
    public String getRealm() {
        return realm;
    }

    /**
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password.
     */

    public String getPassword() {
        return password;
    }

    /**
     * Get the ID of this password. Only for use by DBDAO
     * 
     * @return the ID of this password
     */
    long getID() {
        return id;
    }

    /**
     * Set the ID of this password. Only for use by DBDAO.
     * 
     * @param newid
     *            the new ID of this password
     */
    void setID(long newid) {
        this.id = newid;
    }

    /**
     * Check if this password has an ID set yet (doesn't happen until the DBDAO
     * persists it).
     * 
     * @return true if this password has an ID set yet
     */
    boolean hasID() {
        return id != null;
    }

    /**
     * equals method overriding Object#equals. Autogenerated code.
     * 
     * @param o
     *            The object to compare with
     * @return Whether they are equal
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Password))
            return false;

        final Password password1 = (Password) o;

        if (comments != null ? !comments.equals(password1.comments) : password1.comments != null)
            return false;
        if (name != null ? !name.equals(password1.name) : password1.name != null)
            return false;
        if (password != null ? !password.equals(password1.password) : password1.password != null)
            return false;
        if (passwordDomain != null ? !passwordDomain.equals(password1.passwordDomain)
                : password1.passwordDomain != null)
            return false;
        if (realm != null ? !realm.equals(password1.realm) : password1.realm != null)
            return false;
        if (username != null ? !username.equals(password1.username) : password1.username != null)
            return false;

        return true;
    }

    /**
     * hashCode method overriding Object#hashCode. Autogenerated code.
     * 
     * @return hashcode
     */
    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (comments != null ? comments.hashCode() : 0);
        result = 29 * result + (passwordDomain != null ? passwordDomain.hashCode() : 0);
        result = 29 * result + (realm != null ? realm.hashCode() : 0);
        result = 29 * result + (username != null ? username.hashCode() : 0);
        result = 29 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

}
