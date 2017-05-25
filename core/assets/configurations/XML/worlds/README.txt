This folder shall contain XML files describing individual game worlds.

Also in this folder is a document type definition (DTD) file describing
the valid structure of said documents.

Below is the documentation on how to write an XML file for a particular
game world. Currently, the structure of the XML data is designed to be
parsable by SAX XML parsers, meaning these files do not have a recursive
or particularly deep hierarchy of elements.

== XML configuration file for a particular game world ==

Entries here describe each horizontal tunnel in the game
world and all interconnections between these tunnels.
Also described are the entrances to the tunnel network.

Most attributes that are not ID or IDREF types describe
a dimension attribute in the form of a number. Note that
only @attribute{ offsetX } and @attribute{ offsetY } are
attributes that describe an absolute dimension as number
of pixels. All other attributes just describe the number
of offsetX or offsetY lengths (see @elm <ht> @attribute
width) or a position relative to the bottom left corner
of the screen in number of offsetX or offsetY lengths,
respectively.

The basic idea here is that one can write the tunnel
attributes without having to recalculate them every time
one changes the values of offsetX and offsetY. This is
because conceptually, the game world and screen is treated
as a rectangular grid, where each rectangle represents
one position where a piece of treasure can occur.

It is basically the same coordinate system, only the offsets
are not individual pixels but cells of a predefined width
and height in pixels. It is for this reason we shall refer
to these attributes when used as cell coordinates.

Also of note is that actors are allowed to move arbitrary
distances within these cells, however, they all keep and
update a reference to their current tunnel cell. Their
actual positions within these cells are within a range:

x: cellX+0, cellX+1, ..., cellX+offsetX-1
y: cellY+0, cellY+1, ..., cellY+offsetY-1

where the position at coordinates (cellX, cellY) refers
to the bottom left corner of the cell.

Example:

<world offsetX="128" offsetY="64" ... >
...
<ht id="some_id" x="10" y="9" width="20" />
...

The actual position of the tunnel during rendering is
(relative to the bottom left corner of the screen):

X: x * offsetX = 10 * 128 = 1280 px;
Y: y * offsetY = 9 * 64 = 576 px;

The number of cells of said tunnel equals the width attribute.
The length of the tunnel during rendering is:

Width: width * offsetX = 20 * 128 = 2560 px;



@elm <world>: root element, attributes hold configuration for attributes
              pertaining to the game world directly
    @attributes width: the width of the game world
                       (number of cells)
    @attributes height: the height of the game world
                        (number of cells)
    @attributes offsetX: the number of pixels of one cell in the horizontal direction
                         (absolute)
    @attributes offsetY: the number of pixels of one cell in the vertical direction
                         (absolute)

@elm <ht>: a horizontal tunnel in the game
    @attributes id: the identifier of the tunnel, theoretically can be arbitrary, as long as it is unique within the file,
                    (custom for writing these IDs is to be determined later).
    @attributes x: The horizontal coordinate of the leftmost edge of the tunnel
                   (cell coordinate)
    @attributes y: The vertical coordinate of the tunnel
                   (cell coordinate)
    @attributes width: The width of the tunnel,
                       (number of cells)

@elm <ic>: an interconnection (vertical tunnel) between two horizontal tunnels
    @attributes x: the horizontal coordinate of the interconnection
                   (cell coordinate)
    @attributes from: the identifier of the horizontal tunnel connecting
                      to the upper side of the interconnection
                      (IDREF)
    @attributes to: the identifier of the horizontal tunnel connecting
                    to the bottom side of the interconnection
                    (IDREF)

@elm <entrance>: an entrance point from the surface into the underground tunnel network
    @attributes x: the horizontal position of the entrance point
                   (cell coordinate)
    @attributes y: the vertical position of the entrance point
                   (cell coordinate)
    @attributes to: the identifier of the first horizontal tunnel
                    directly reachable from the entrance point
                    (IDREF)
    @attributes id: the identifier of the entrance, theoretically can be arbitrary, as long as it is unique within the file,
                    (custom for writing these IDs is to be determined later).