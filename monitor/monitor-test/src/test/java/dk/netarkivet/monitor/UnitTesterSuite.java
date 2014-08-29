/*
 * #%L
 * Netarchivesuite - monitor - test
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

package dk.netarkivet.monitor;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import dk.netarkivet.monitor.jmx.MonitorJMXTesterSuite;
import dk.netarkivet.monitor.logging.MonitorLoggingTesterSuite;
import dk.netarkivet.monitor.registry.MonitorRegistryTesterSuite;
import dk.netarkivet.monitor.webinterface.MonitorWebinterfaceTesterSuite;

/**
 * This class runs all the monitor module unit tests.
 */
public class UnitTesterSuite {
    public static void addToSuite(TestSuite suite) {
        MonitorTesterSuite.addToSuite(suite);
        MonitorLoggingTesterSuite.addToSuite(suite);
        MonitorJMXTesterSuite.addToSuite(suite);
        MonitorRegistryTesterSuite.addToSuite(suite);
        MonitorWebinterfaceTesterSuite.addToSuite(suite);
    }

    public static Test suite() {
        TestSuite suite;
        suite = new TestSuite(UnitTesterSuite.class.getName());

        addToSuite(suite);

        return suite;
    }

    public static void main(String[] args) {
        String[] args2 = {"-noloading", UnitTesterSuite.class.getName()};
        TestRunner.main(args2);
    }
}
