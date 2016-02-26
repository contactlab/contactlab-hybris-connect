package com.contactlab.services;

import java.io.File;

/**
 * @author openmind
 */
public interface SFTPService
{

    /**
     * Outbound transfer.
     *
     * @param host
     *            the host
     * @param port
     *            the port
     * @param user
     *            the user
     * @param password
     *            the password
     * @param remoteDir
     *            the remote dir
     * @param file
     *            the file
     * @return true, if successful
     */
    boolean outboundTransfer(String host, int port, String user, String password, final String remoteDir, File file,
            String pathDestination);

}
