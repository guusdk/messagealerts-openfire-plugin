package nl.goodbytes.openfire.plugins.messagealerts.database;

import org.xmpp.packet.JID;

import java.util.Objects;

/**
 * Representation of one row in the database.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class Record
{
    private final long id;
    private final JID to;
    private final String text;

    public Record( long id, JID to, String text )
    {
        this.id = id;
        this.to = to;
        this.text = text;
    }

    public long getId()
    {
        return id;
    }

    public JID getTo()
    {
        return to;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final Record record = (Record) o;
        return id == record.id &&
            Objects.equals( to, record.to ) &&
            Objects.equals( text, record.text );
    }

    @Override
    public int hashCode()
    {

        return Objects.hash( id, to, text );
    }
}
