package nl.goodbytes.openfire.plugins.messagealerts.database;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Database interaction.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class DAO
{
    private final static Logger Log = LoggerFactory.getLogger( DAO.class );

    /**
     * Returns a record for every row that is currently in the database.
     *
     * @return A list of records, never null, but possibly empty.
     */
    public static List<Record> getAllRecords()
    {
        Log.debug( "Retrieving all rows from the database..." );
        final List<Record> results = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement( "SELECT * FROM ofmsgalerts" );
            rs = pstmt.executeQuery();
            while ( rs.next() )
            {
                final long id = rs.getLong( 1 );
                final String to = rs.getString( 2 );
                final String message = rs.getString( 3 );
                final JID toJID;
                try
                {
                    toJID = new JID( to );
                }
                catch ( IllegalArgumentException e )
                {
                    Log.warn( "Unable to create a JID from the value in database row with identifier '" + id + "': '" + to + "'", e );
                    continue;
                }

                results.add( new Record( id, toJID, message ) );
            }
        }
        catch ( SQLException e )
        {
            Log.error( "An exception occurred while reading from the database.", e );
        }
        finally
        {
            DbConnectionManager.closeConnection( rs, pstmt, con );
        }

        Log.debug( "Retrieved {} row(s) from the database.", results.size() );
        return results;
    }

    /**
     * Deletes rows from the database that match the id. This method does nothing when there is no row with the
     * provided database id.
     *
     * @param id The database id for the record to be deleted.
     */
    public static void deleteRecord( long id )
    {
        Log.debug( "Deleting row with id '{}' from the database...", id );
        Connection con = null;
        PreparedStatement pstmt = null;
        try
        {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement( "DELETE FROM ofmsgalerts WHERE id = ?" );
            pstmt.setLong( 1, id );
            final int affectedRows = pstmt.executeUpdate();
            Log.debug( "Deleted {} row(s) with id '{}' from the database.", affectedRows, id );
        }
        catch ( SQLException e )
        {
            Log.error( "An exception occurred while trying to delete record with id '"+id+"' from the database.", e );
        }
        finally
        {
            DbConnectionManager.closeConnection( pstmt, con );
        }
    }
}
