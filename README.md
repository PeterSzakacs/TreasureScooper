Original game idea credit of Thunderbird games: http://www.thunderbird.cz/games/gold-...

This is a variety of this game developed for the purpose of teaching how to use stack and queue data structures as part of my bachelor thesis.

Made in Java with Intellij IDEA and libGDX. Graphics made in GIMP image editor.

# Introduction

The objective of this program and others in this group is to create a game environment, where students can program bots to play (and win) the game for them, with emphasis placed on learning new skills through the effort of coming up with ideas and testing game bot algorithms.

This particular game has the following scenario: The programmer controls a collecting pipe, which scoops up pieces of treasure in an underground system of caverns. The objective of the game is to collect all pieces of treasure in this underground network of caverns. Depending on the specific level and scenario however, enemy creatures, which populate these tunnels can emerge and damage the pipe, even destroy it, if they make contact with it.

However, you can destroy them using either a gun mounted at the head of this pipe (a long range weapon, but the bullets cost you score points) or simply eat them (short range, but does not cost anything).

# Role of stack and queue

The [`pipe`](core/src/main/szakacs/kpi/fei/tuke/arena/actors/pipe/Pipe.java) itself is represented as a stack of individual [`pipe segments`](core/src/main/szakacs/kpi/fei/tuke/arena/actors/pipe/PipeSegment.java) (minus the [`pipe head`](core/src/main/szakacs/kpi/fei/tuke/arena/actors/pipe/PipeHead.java), which connects to the top of this stack) and so the only way for it to even move around in these tunnels is through pushing segments onto or popping them off this stack. And the [`weapon`](core/src/main/szakacs/kpi/fei/tuke/arena/actors/pipe/Weapon.java) used by the player adds and removes bullets in a FIFO style, same as a queue (specifically, a circular buffer implementation of a queue) and this is directly shown on the game screen.

In addition to this, the game API features a custom interface for [`Stack<T>`](core/src/main/szakacs/kpi/fei/tuke/intrfc/misc/Stack.java) and [`Queue<T>`](core/src/main/szakacs/kpi/fei/tuke/intrfc/misc/Queue.java) types of collections as well as their array-based implementations [`ArrayStack<T>`](core/src/main/szakacs/kpi/fei/tuke/misc/ArrayStack.java) and [`ArrayQueue<T>`](core/src/main/szakacs/kpi/fei/tuke/misc/ArrayQueue.java) (circular array in case of queue). These can be used as part of the solution, for example by solving the game using a graph traversal algorithm (DFS or BFS) or using these structures in a custom algorithm for storing information about previous state, like where the pipe already was (for e.g. non-recursive variants of some solutions).

Both of these are generic collections with a type parameter determining the type of elements stored in them and are iterable in a read-only way. Also, they can be set at instatiation to either grow beyond their current capacity, as the number of elements increases ('dynamic'), or to have a fixed upper limit on the number of elements stored in them. These structures are already used for implementing the pipe and weapon.

# Game API

The API is currently still developed, so we present here only a brief overiview and some methods likely to be used in most or every solution and unlikely to be significantly refactored in the near- to mid-term future.

First the place, where the programmer implements his/her solution for the game is any class implementing the following interface: **[`szakacs.kpi.fei.tuke.intrfc.Player`](core/src/main/szakacs/kpi/fei/tuke/intrfc/Player.java)**. 
Additionaly, any such class must reside in the package `szakacs.kpi.fei.tuke.player` The interface is defined as follows:

```java
public interface Player {
    public void initialize(PlayerGameInterface gameInterface, Pipe pipe);
    public void deallocate();
    public void act();
}
```

The names`initialize` and `deallocate` should suggest, what these methods are for. The method `initialize()` is called when the player instance is created and `deallocate()` is called once the particular game level has ended, successfully or not. They can be used by a programmer to store away references to frequently accessed game objects as member variables of the implementing class, and for any cleanup required once the game level ends. The `initialize` method recieves a [`PlayerGameInterface`](core/src/main/szakacs/kpi/fei/tuke/intrfc/arena/proxies/PlayerGameInterface.java) type of object which is used to access all game functionality and a reference to the [`Pipe`](core/src/main/szakacs/kpi/fei/tuke/arena/actors/pipe/Pipe.java) object assigned to them for the current level.

The method `act()` is the focus for the programmer. It is a method called on every iteration of the game loop, and thus any actions the player wants to perform (move the pipe by a small increment, shoot, anticipate next move etc.) have to be done here, or in helper methods called from here.

The player is restricted to accessing only those methods he is authorized to use, as it would potentially make any solution to the game trivial and thus be considered cheating if he were given access to them. These allowed methods are grouped in the [`PlayerGameInterface`]((core/src/main/szakacs/kpi/fei/tuke/intrfc/arena/proxies/PlayerGameInterface.java)) type of object received on initialization, which serves as a proxy to their actual implementations in the game backend classes. This proxy thus serves a dual role of both preventing cheats by not exposing backend classes and their methods directly, but also by grouping methods from several classes it allows the player to easily see all available functionality in the API from one class. 


Some methods however, could not be hidden this way without making related existing code rather complicated and in such cases, they are exposed, but require an authentication token in the form of a specific object instance that is never exposed to the player, and which needs to be passed in order for the method to execute its functionality. These methods have a `Object authToken` as the last formal parameter in their signatures.

## Game World

The network of underground tunnels is represented as linked node graph structure, with each node (equal to the [`TunnelCell`](core/src/main/szakacs/kpi/fei/tuke/arena/game/world/TunnelCell.java) object) representing a position where exactly one piece of treasure can be. Every node also has a pointer to any adjacent nodes in each [`direction`](core/src/main/szakacs/kpi/fei/tuke/enums/Direction.java) around it, or null, if there is no such node (i.e., if there is a wall). To avoid having to check each node's configuration of neighbors, a special [`TunnelCellType`](core/src/main/szakacs/kpi/fei/tuke/enums/TunnelCellType.java) enum value storedi in it indicating the particular configuration of nodes in any direction.
They are currently as follows:
`ENTRANCE` - nodes to left, right, up 
`EXIT` - nodes to left, right, down
`LEFT_EDGE` - only a node to the right
`RIGHT_EDGE` - only a node to the left
`TUNNEL` - nodes to left and right
`INTERCONNECT` - nodes to up and down

Strictly speaking, this graph structure would be enough to represent the world, but in order to make decision-making in the game easier, pieces of treasure can only be found in the horizontal tunnels (tunnels stretching from right to left) which have their own [`class`](core/src/main/szakacs/kpi/fei/tuke/arena/game/world/HorizontalTunnel.java) to represent them. The game world only keeps references to a collection of these `tunnel` objects and any `TunnelCells` that serve as entrances to the tunnel system itself (that are immediately below the surface, they are assigned the value `INTERCONNECT` although they have no neighbor in the upwards direction). Interconnecting tunnels so far can't have treasures, just like in the original games. 
## The pipe

As was mentioned, the pipe is represented as a stack of smaller segments with the collecting head connected to the top of the stack (though the head is not identical to the top of the stack). This stack of segments can be pushed to, and the pipe class contains a method `calculateNextSegment(Direction dir)` which creates a segment of a type required to move the pipe head in the supplied direction, unless there is a wall, in chich case, null is returned. Collection of treasure by the head is done automatically on pushing a segment to the stack and thus moving the head. The pipe also has a weapon, which can fire() bollets or load() new ones into its ammunition storage.

The pipe also keeps a health value ranging from 0 to 100, and as can be guessed, a pipe with a value of 0 is not usable anymore (it is this value the enemy creatures lower upon contact). It can however be repaired during the game, by asking a [`GameShop`](core/src/main/szakacs/kpi/fei/tuke/arena/game/GameShop.java) object to repair it (it is also used to buy bullets).

## Actors

Every pipe segment, the pipe head, enemy creatures, even bullets and others implement a common [`Actor`](core/src/main/szakacs/kpi/fei/tuke/intrfc/arena/actors/Actor.java) interface, which is also the type of reference which the backend code uses for storing and keeping track of them. Just like the player, they also have an `act()` method that is called on every iteration of the game loop, and can return their current position (which is actually the `tunnel cell` they are currently in) as well as their actual X, Y coordinates.  Currently, besides the ones mentioned above, there is a wall actor, which is basically an actor that blocks passage to other tunnel cells, by disconnecting the references they have to each other. This actor can be destroyed by firing a bullet at it, and then it restores the references these cells have to each other to their original state.
