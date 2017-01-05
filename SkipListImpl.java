import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

// Skeleton for skip list implementation.

public class SkipListImpl<T extends Comparable<? super T>> implements SkipList<T>, Iterable<T> {
	class Entry<T extends Comparable<? super T>> {
		T element;
		Entry[] nextArr;
		int chocieLevel;
		int index;
		Entry(T x, int level) {
			index=0;
			element = x;
			chocieLevel = level;
			nextArr = new Entry[level + 1];
		}
	}

	int size;	
	//int currentMaxLevel; // to see how the current max level in the list while
			// adding any element
	int maxLevel; // the allowed max level by the skip list
	Entry header; // -infinity
	Entry tail; // +infinity
	T last;

	SkipListImpl() {
		// TODO Auto-generated constructor stub
		//currentMaxLevel = 1;
		size = 0;
		maxLevel = 32;
		last=null;
		tail = new Entry(Long.MAX_VALUE, maxLevel);
		header = new Entry(Long.MIN_VALUE, maxLevel);
		for (int i = 0; i < maxLevel; i++) {
			// header.nextArr[i]=header;
			header.nextArr[i] = tail;
			tail.nextArr[i] = tail;
		}
	}

	public <T extends Comparable<? super T>> Entry[] find(T x) {
		// TODO Auto-generated method stub
		//Entry prev[] = new Entry[this.currentMaxLevel]; // create prev array of
														// size the current
		
		Entry prev[] = new Entry[this.maxLevel]; // create prev array of
		
		// maximum level in list
		Entry<T> p = this.header;

		/*for (int i = currentMaxLevel - 1; i >= 0; i--) {
			while (p.nextArr[i].element.compareTo(x) < 0) {
				p = p.nextArr[i];
			}
			prev[i] = p;
		}*///23 3252 47 130
		for (int i = maxLevel - 1; i >= 0; i--) {
			while (p.nextArr[i].element.compareTo(x) < 0) {
				p = p.nextArr[i];
			}
			prev[i] = p;
		}
		return prev;
	}

	@Override
	public boolean contains(T x) {
		Entry prev[] = find(x);
		if (prev[0].nextArr[0].element.compareTo(x) == 0) {
		//	System.out.println("contains done" + prev[0].nextArr[0].element);
			return true;
		} else {
			return false;
		}
	}//first last floor

	@Override
	public boolean add(T x) {
		Entry prev[] = find(x);
		/*if (prev[0].nextArr[0].element.compareTo(x) == 0||prev[0].element.compareTo(x) == 0)*/
		if(contains(x)){
			return false;
		} else {
			// new node is added to the skiplist
			int level = choice();
			// System.out.println("maximum level "+level);
			Entry newElement = new Entry(x, level);
			int i = 0;
			if(size==0)
			{
				last=x;
			}
			if(x.compareTo(last)>0)
			{
				last=x;
			}
			int min = (prev.length > level ? level : prev.length); // to choose
																	// the
																	// minimum
																	// to insert
		
			while (i < min) {/*
								 * System.out.println("current max "
								 * +currentMaxLevel);
								 * System.out.println(prev[i].nextArr[i].element
								 * +"flagg");
								 */
				// System.out.println(newElement.nextArr.length+"length"+prev.length+"prev
				// length");
				newElement.nextArr[i] = prev[i].nextArr[i];
				prev[i].nextArr[i] = newElement;
				
				// System.out.println("addition done inside");
				i++;
			}
			while (i < level) {
				this.header.nextArr[i] = newElement; // making infinite centals
														// length long
														// updating its next ,
														// newelement next
														// values
			                                    	//currentMaxLevel = level; // update current max level
				newElement.nextArr[i] = this.tail;
				this.tail.nextArr[i] = this.tail;
				i++;
			}
		/*	if(level>currentMaxLevel)
			{
				currentMaxLevel=level;
			}*/
			size++;
			return true;
		}
	//	System.out.println("addition done" + x);
	}

	public int choice() {
		int choiceVar = 0;
		int flag = this.maxLevel;
		boolean f;
		while (flag > 0) {
			f = Math.random() < 0.5;
			if (f) {
				choiceVar++;
			}
			flag--;
		}
		return choiceVar;
	}

	@Override
	public T remove(T x) {
		Entry prev[] = find(x);
		if (prev[0].nextArr[0].element.compareTo(x) != 0) {
			return null;
		} else {
			Entry dummy = prev[0].nextArr[0];
			for (int i = 0; i < dummy.chocieLevel; i++) {
				if (prev[i].nextArr[i] == dummy) {
					prev[i].nextArr[i] = dummy.nextArr[i];
				} else {
					break;
				}
			}
			size--;
			//System.out.println("remove done" + dummy.element);
			return x;
		}
	}

	@Override
	public T first() {
		return (T) this.header.nextArr[0].element;
	}

	@Override
	public T last() {
		Entry dummy = this.header;
		Entry prev=this.header;/*
		while (dummy.nextArr[0] != this.tail) {
			dummy = dummy.nextArr[currentMaxLevel - 1];
		}
		 System.out.println("Last elemetn "+ dummy.element);*///add contains first last ceiling remove floor
		/*Iterator i=this.iterator();
		T lastelem = null;
		while(i.hasNext())
		{
			if(i.next()!=tail)
			{
				lastelem=(T) i.next();
			}
		}*/
		/*
		while (dummy.nextArr[0].element.compareTo( this.tail.element)!=0) {
			dummy = dummy.nextArr[0];
			
		}
		*/
		return last;
	}

	public int calculateGap(Entry<T> head,Entry<T> tail,int i)
	{
		Entry h1=head;
		Entry h2=tail;
		int maxlevel=i-1;
		if(maxlevel<0)
		{
			return 1;
		}
		while(maxlevel>=0&&h1.nextArr[maxlevel]!=h2)
		{
			maxlevel--;
		}
		int leftGap=calculateGap(h1, h1.nextArr[maxlevel], maxlevel);
		int rightGap=calculateGap(h1.nextArr[maxlevel],h2, maxlevel);
		head.nextArr[maxlevel].index=leftGap+rightGap;
		return leftGap+rightGap;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	@Override
	public T ceiling(T x) {
		Entry[] dummy = find(x);
		int cmp = dummy[0].nextArr[0].element.compareTo(x);
		if (cmp == 0 || cmp > 0) {
		//System.out.println(dummy[0].nextArr[0].element+" ceiling internediate result of "+x);
			return (T) dummy[0].nextArr[0].element;
		}
		return null;
	}

	@Override
	public T floor(T x) {
		Entry[] dummy = find(x);
		int cmp = dummy[0].nextArr[0].element.compareTo(x);
		if (cmp == 0) {
			//System.out.println("floor value inside"+x +"is"+(T) dummy[0].nextArr[0].element);
			return (T) dummy[0].nextArr[0].element;
		}
		else{
		//	cmp = dummy[0].element.compareTo(x);
			//if(cmp<0)
			{
				//System.out.println("floor value inside"+x +"is"+(T) dummy[0].element);
				return (T) dummy[0].element;
			}
		}
		//return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return new ImplIterator();
	}

	class ImplIterator<T extends Comparable<? super T>> implements Iterator<T> {
		int index = 1;
		Entry<T> cursor = header;
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return (cursor.nextArr[0].element.compareTo(tail.element) != 0);
		}
		@Override
		public T next() {
			// TODO Auto-generated method stub
			cursor = cursor.nextArr[0];
			return cursor.element;
		}
	}
	@Override
	public void rebuild() {
		Entry[] input=new Entry[size];
		int log=(int) (Math.log(size)/Math.log(2));
		rebuildSize(input, 0, size,log );
		Entry<T> dumy=header.nextArr[0];
		int j=0;
		while(dumy.element!=tail.element)
		{
			input[j].element=dumy.element;		
			if(input[j].nextArr.length<dumy.nextArr.length)
			{
				input[j].nextArr[j]=dumy.nextArr[j];
		     	j++;
			}
			dumy= dumy.nextArr[0];
			j++;
		}
	}
	public void rebuildSize(Entry[] inp,int p,int r,int k)
	{	int q=0;
		if((p<=r))
		{
			if(k==0)
			{
				for(int i=p;i<=r;i++)
				{
					inp[i]=new Entry(null,0);
				}
				
			}
			else{
				q=(p+r)/2;
				inp[q]=new Entry(null,k);
				rebuildSize(inp, p, q, k-1);
				rebuildSize(inp, q+1, r, k-1);
			}
		}
	}
	@Override
	public T findIndex(int n) {
		int i = -1;
		T result = null;
		Iterator iter = this.iterator();
		while (i < n) {
			result = (T) iter.next();
			i++;
		}
		//System.out.println(result + " is value at index  2");
		return result;
	}
}