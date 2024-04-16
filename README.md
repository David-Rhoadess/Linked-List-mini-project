Author: David Rhoades

Purpose: an implementation of a circularly, doubly linked list with a dummy node

Github repo: https://github.com/David-Rhoadess/Linked-List-mini-project

Aknowledgements: Node2class, SimpleList, SimpleListExpt are all from https://github.com/Grinnell-CSC207/lab-linked-lists (modified) with permission

SimpleCDLL uses a template from https://github.com/Grinnell-CSC207/lab-linked-lists with permission.


Benefits of a circularly, doubly linked list with a dummy node:
In a circularly, doubly linked list, a dummy node at the beggining (or end depending on how you look at it) of the list removes the need for many "special case checks" in iterator methods such as add. Without a dummy node, add would need to check and see if the node was being added to the beginning of the list to avoid a NullPointerException which can occur when trying to access the node before the first node to update its "next" pointer. add would also need to update the list field pointer to the new first node. With a dummy node and circular linking, NullPointerExceptions are no longer an issue because there is always a node before the first node and after the last node, and the dummy node never moves so the list field never needs to be updated. These features also make hasNext and hasPrevious farily trivial with a simple check to see if the next or previous (respectively) node is the dummy node.
