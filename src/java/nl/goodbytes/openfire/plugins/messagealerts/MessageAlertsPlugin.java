/*
 * Copyright (C) 2017 Guus der Kinderen. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.goodbytes.openfire.plugins.messagealerts;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.Timer;

/**
 * An Openfire plugin for the MessageAlerts plugin.
 *
 * @author Guus der Kinderen, guus.der.kinderen@gmail.com
 */
public class MessageAlertsPlugin implements Plugin, PropertyEventListener
{
    private final Logger Log = LoggerFactory.getLogger( MessageAlertsPlugin.class );

    private Timer timer;

    @Override
    public void initializePlugin( PluginManager manager, File pluginDirectory )
    {
        Log.debug( "Initializing plugin..." );
        reschedule();
        PropertyEventDispatcher.addListener( this );
        Log.debug( "Plugin initialized." );
    }

    @Override
    public void destroyPlugin()
    {
        Log.debug( "Destroying plugin..." );
        PropertyEventDispatcher.removeListener( this );
        if ( timer != null ) {
            timer.cancel();
            timer = null;
        }
        Log.debug( "Plugin destroyed." );
    }

    private void reschedule()
    {
        long frequency = JiveGlobals.getLongProperty( "messagealerts.frequency", 60 );
        if ( frequency <= 0 )
        {
            Log.warn( "The property value for 'messagealerts.frequency' should be a positive value. Using the default value of '60' instead of '{}'.", frequency );
            frequency = 60;
        }

        Log.debug( "Scheduling message alerts with a frequence of {} seconds.", frequency );
        if ( timer != null ) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule( new AlertTimerTask(), frequency*1000, frequency*1000 );
    }

    @Override
    public void propertySet( String property, Map<String, Object> params )
    {
        if ( "messagealerts.frequency".equals( property ) )
        {
            reschedule();
        }
    }

    @Override
    public void propertyDeleted( String property, Map<String, Object> params )
    {
        if ( "messagealerts.frequency".equals( property ) )
        {
            reschedule();
        }
    }

    @Override
    public void xmlPropertySet( String property, Map<String, Object> params )
    {

    }

    @Override
    public void xmlPropertyDeleted( String property, Map<String, Object> params )
    {

    }
}
