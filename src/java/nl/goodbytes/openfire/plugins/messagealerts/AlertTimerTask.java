package nl.goodbytes.openfire.plugins.messagealerts;

import nl.goodbytes.openfire.plugins.messagealerts.database.DAO;
import nl.goodbytes.openfire.plugins.messagealerts.database.Record;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import java.util.List;
import java.util.TimerTask;

/**
 * A task that sends a message for every row in the database, deleting every row for which a message was sent.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class AlertTimerTask extends TimerTask
{
    private static final Logger Log = LoggerFactory.getLogger( AlertTimerTask.class );

    @Override
    public void run()
    {
        final JID from;
        final String propValue = JiveGlobals.getProperty( "messagealerts.from", XMPPServer.getInstance().getServerInfo().getXMPPDomain() );
        try
        {
            from = new JID( propValue );
        }
        catch ( IllegalArgumentException e )
        {
            Log.warn( "Cannot send messages! Unable to create a valid JID from the value that is in property 'messagealerts.from': '{}'.", propValue, e );
            return;
        }

        final List<Record> records = DAO.getAllRecords();
        if ( records.isEmpty() )
        {
            Log.trace( "Database is empty. No messages to send." );
            return;
        }

        Log.debug( "Sending {} messages...", records.size() );
        for ( final Record record : records )
        {
            Log.debug( "Sending a message (for database id '{}') to '{}'... ", record.getId(), record.getTo() );

            // Construct the message.
            final Message message = new Message();
            message.setFrom( from );
            message.setTo( record.getTo() );
            message.setBody( record.getText() );
            message.setType( Message.Type.chat );

            // Send the message.
            XMPPServer.getInstance().getMessageRouter().route( message );

            // Delete the record from the database.
            DAO.deleteRecord( record.getId() );
        }
        Log.debug( "Done sending messages." );
    }
}
