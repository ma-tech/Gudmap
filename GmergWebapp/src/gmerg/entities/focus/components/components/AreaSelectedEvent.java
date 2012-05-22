/*
 * $Id: AreaSelectedEvent.java,v 1.1 2007/01/25 16:35:16 ycheng Exp $
 */

/*
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

package gmerg.entities.focus.components.components;


import javax.faces.event.ActionEvent;


/**
 * <p>An {@link ActionEvent} indicating that the specified {@link AreaComponent}
 * has just become the currently selected hotspot within the source
 * {@link MapComponent}.</p>
 */

public class AreaSelectedEvent extends ActionEvent {

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link AreaSelectedEvent} from the specified
     * source map.</p>
     *
     * @param map The {@link MapComponent} originating this event
     */
    public AreaSelectedEvent(MapComponent map) {
        super(map);
    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link MapComponent} of the map for which an area
     * was selected.</p>
     */
    public MapComponent getMapComponent() {
        return ((MapComponent) getComponent());
    }
}