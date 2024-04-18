import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Simple doubly-linked lists.
 *
 * @author David Rhoades
 * @author Sam Rebelsky
 */
public class SimpleCDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The front of the list
   */
  Node2<T> dummy;

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * Iteretor fail-fast check
   */
  long iterVerstion;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCDLL() {
    this.dummy = new Node2<T>(null);
    this.dummy.next = this.dummy;
    this.dummy.prev = this.dummy;
    this.size = 0;
    this.iterVerstion = 0;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node2<T> prev = dummy;
      Node2<T> next = SimpleCDLL.this.dummy.next;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there is no such value.
       */
      Node2<T> update = null;

      /**
       * A value to be updates when this iterator makes changes to 
       * a list and to be compared to the lists version before any action,
       * throwing a ConcurrentModificationException if not
       */
      long myVersion = SimpleCDLL.this.iterVerstion;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      /**
       * Add val to the list
       */
      public void add(T val) throws UnsupportedOperationException, ConcurrentModificationException{
        failFastCheck();

        this.prev = this.prev.insertAfter(val);

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;

        // Update fail-fast counter
        this.myVersion = ++SimpleCDLL.this.iterVerstion;
      } // add(T)

      /**
       * Returns true is the list has a valid next value, returns false otherwise
       */
      public boolean hasNext() throws ConcurrentModificationException{ 
        failFastCheck();
        return (this.next != SimpleCDLL.this.dummy);
      } // hasNext()

      /**
       * Returns true is the list has a valid previous value, returns false otherwise
       */
      public boolean hasPrevious() throws ConcurrentModificationException{
        failFastCheck();
        return (this.prev != SimpleCDLL.this.dummy);
      } // hasPrevious()

      /**
       * Returns the next value in the list and advances the iterator
       */
      public T next() throws ConcurrentModificationException{
        failFastCheck();
        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;
        // And return the value
        return this.update.value;
      } // next()

      /**
       * returns the index of the next value in the list (even if it does not exist)
       */
      public int nextIndex() throws ConcurrentModificationException{
        failFastCheck();
        return this.pos;
      } // nextIndex()

      /**
       * returns the index of the previous value in the list (even if it does not exist)
       */
      public int previousIndex() throws ConcurrentModificationException{
        failFastCheck();
        return this.pos - 1;
      } // prevIndex

      /**
       * Returns the previous value in the list and moves the iterator back one value
       */
      public T previous() throws NoSuchElementException, ConcurrentModificationException {
        failFastCheck();
        if (!this.hasPrevious()) { 
          throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.prev;
        // Move the cursor
        this.next = this.prev;
        this.prev = this.prev.prev;
        this.pos--;
        return this.update.value;
      } // previous()

      /**
       * removes the last value returned by previous or next. If remove has already been called since the last
       * call of one of these, an IllegalStateException is thrown.
       */
      public void remove() throws IllegalStateException, ConcurrentModificationException{
        failFastCheck();
        if (this.update == SimpleCDLL.this.dummy) { 
          throw new NoSuchElementException();
        } // if
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if

        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;

        // Note that no more updates are possible
        this.update = null;

        // Update fail-fast counter
        this.myVersion = ++SimpleCDLL.this.iterVerstion;
      } // remove()

      /**
       * Sets the last value returned by previous or next to val, same restrictions as remove apply.
       */
      public void set(T val) throws ConcurrentModificationException{
        failFastCheck();
        if (!this.hasPrevious()) { 
          throw new NoSuchElementException();
        } // if
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
        // Note that no more updates are possible
        this.update = null;
      } // set(T)

      /**
       * Checks to see if this iterator is up-to-date (no structural modifcations have been made since its creation b another iterator)
       * @throws ConcurrentModificationException
       */
      public void failFastCheck() throws ConcurrentModificationException{
        // Fail-fast?
        if(this.myVersion != SimpleCDLL.this.iterVerstion) {
          throw new ConcurrentModificationException();
        } // if
      }
    };
  } // listIterator()

} // class SimpleDLL<T>
