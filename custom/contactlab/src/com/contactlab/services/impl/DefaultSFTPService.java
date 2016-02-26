package com.contactlab.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contactlab.services.SFTPService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DefaultSFTPService implements SFTPService // NO_UCD (unused code)
{

    private Logger log = LoggerFactory.getLogger(DefaultSFTPService.class);

    private int timeout;
    private String transferredPath;
    private boolean enablesftp;

    protected int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    protected String getTransferredPath()
    {
        return transferredPath;
    }

    public void setTransferredPath(String transferredPath)
    {
        this.transferredPath = transferredPath;
    }

    protected boolean isEnablesftp()
    {
        return enablesftp;
    }

    public void setEnablesftp(boolean enablesftp)
    {
        this.enablesftp = enablesftp;
    }

    public boolean outboundTransfer(String host, int port, String user, String password, String remoteDir, File file,
            String pathDestination)
    {

        Exception exception = null;
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        boolean isSuccess = false;

        moveFile(file, pathDestination);

        if (!enablesftp)
        {
            log.warn("Sftp upload is disabled - not uploading file {} to {} {}", file.getName(), host, remoteDir);
            return true;
        }

        if (isInputsValid(host, port, user, password, remoteDir, file))
        {
            try
            {
                JSch jsch = new JSch();

                session = jsch.getSession(user, host, port);
                session.setPassword(password);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                log.info("Connecting to host {}:{}", host, host);

                session.connect(timeout);

                log.info("Opening SFTP channel....");
                channel = session.openChannel("sftp");
                channel.connect(timeout);

                channelSftp = (ChannelSftp) channel;
                log.info("Opening directory location {}", remoteDir);
                channelSftp.cd(remoteDir);

                log.info("Writing file " + file.getAbsolutePath() + " to directory location...." + remoteDir);
                channelSftp.put(new FileInputStream(file), file.getName());
                log.info("Moved file " + file.getAbsolutePath() + " to directory location...." + remoteDir);

                isSuccess = true;
            }
            catch (Exception e)
            {
                exception = e;
                log.error(ClassUtils.getShortClassName(e.getClass()) + " occurred while uploading file "
                        + file.getAbsolutePath() + " to SFTP " + host + ": " + e.getMessage(), e);
            }
            finally
            {
                if (channelSftp != null && channelSftp.isConnected())
                {
                    channelSftp.exit();
                    log.debug("Disconnected channel");
                }
                if (session.isConnected())
                {
                    session.disconnect();
                    log.debug("Disconnected session");
                }
            }
        }
        if (!isSuccess || exception != null)
        {
            log.error("Error uploading file " + file.getAbsolutePath() + " to sftp", exception);
        }

        return isSuccess;
    }

    private boolean moveFile(File file, String pathDestination)
    {
        try
        {
            File localDir = new File(transferredPath, pathDestination);

            // sposto file nella cartella files inviati
            boolean createDir = localDir.mkdirs();
            log.debug("Crete directory {} {}", localDir.getAbsolutePath(), createDir);

            FileUtils.copyFileToDirectory(file, localDir, false);
            log.info("File {} copied to archive directory", file.getAbsolutePath());
            return true;
        }
        catch (IOException e)
        {
            log.error("errore {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean isInputsValid(String host, int port, String user, String password, String remoteDir, File file)
    {
        boolean isParamsValid = true;

        if (StringUtils.isBlank(host))
        {
            log.error(">>>> Host address is empty");
            isParamsValid = false;
        }
        if (StringUtils.isBlank(String.valueOf(port)))
        {
            log.error(">>>> Port number is empty");
            isParamsValid = false;
        }
        if (StringUtils.isBlank(String.valueOf(user)))
        {
            log.error(">>>> Host username is empty");
            isParamsValid = false;
        }
        if (StringUtils.isBlank(String.valueOf(user)))
        {
            log.error(">>>> Host password is empty");
            isParamsValid = false;
        }
        if (StringUtils.isBlank(String.valueOf(remoteDir)))
        {
            log.error(">>>> Directory path is empty");
            isParamsValid = false;
        }
        if (file == null)
        {
            log.error(">>>> Files to be uploaded is empty");
            isParamsValid = false;
        }

        return isParamsValid;
    }
}
