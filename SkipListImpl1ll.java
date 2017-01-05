import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.text.AbstractDocument.LeafElement;

// Skeleton for skip list implementation.

public class SkipListImpl1ll<T extends Comparable<? super T>> implements SkipList<T>, Iterable<T> {
	class Entry<T extends Comparable<? super T>> {
		T element;
		Entry[] nextArr;
		int[] gap;
		int chocieLevel;
		int index;
		Entry(T x, int level) {
			element = x;
			index=0;
			gap=new int[level+1];
			chocieLevel = level;
			nextArr = new Entry[level + 1];
		}
	}
	Iterator skipListItercurr;
	int iterIndex;
	
	int size;
	          //int currentMaxLevel; // to see how the current max level in the list while
							// adding any element
	int maxLevel; // the allowed max level by the skip list
	Entry header; // -infinity
	Entry tail; // +infinity
	T last;

	SkipListImpl1ll() {
		// TODO Auto-generated constructor stub
		//currentMaxLevel = 1;
		size = 0;
		maxLevel = 16;
		last=null;
		tail = new Entry(null, maxLevel);
		header = new Entry(null, maxLevel);
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
			while (p.nextArr[i]!=tail&&p.nextArr[i].element.compareTo(x) < 0) {
				p = p.nextArr[i];
			}
			prev[i] = p;
		}
		return prev;
	}
	public int calculateGap(Entry<T> head,int i)
	{
		int maxlevel=i;		
		if(maxlevel<0)
		{
			return 0;
		}
		Entry h1=head;
		//Entry h2=tail;
		int count=0;
		while(maxlevel>=0&&h1.nextArr[maxlevel]==h1.nextArr[i])
		{
			maxlevel--;
		}
		if(maxlevel>0)
		{
			/*h1.gap[i]++;*/
			int j=i;
			while(j<maxlevel)
			{
				h1.gap[j]=h1.gap[j]+1;
				j++;
			}
		}
		if(maxlevel==-1)
		{
			return 0;
		}
	/*	int leftGap=calculateGap(h1, h1.nextArr[maxlevel], maxlevel);
		int rightGap=calculateGap(h1.nextArr[maxlevel],h2, maxlevel);
		*
		*
		*///h1.nextArr[i].index=leftGap+rightGap;
		int leftGap=calculateGap(h1, maxlevel);
		int rightGap=calculateGap(h1.nextArr[maxlevel], maxlevel);
		
		h1.gap[i]=h1.gap[i]+leftGap+rightGap;
		return leftGap+rightGap;
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
        {
		Entry prev[] = find(x);
		/*if (prev[0].nextArr[0].element.compareTo(x) == 0||prev[0].element.compareTo(x) == 0)*/
		if(prev[0].nextArr[0]!=tail&&contains(x)){
			return false;
		} else {
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
			//logic starts here//gap[maxlevel] is a 0 always
			//newElem
			  int k=prev.length;
		       int entrylevel=newElement.chocieLevel;
		       while(k>entrylevel)//outgoing links which are above node newElement
		       {
		    	   prev[k-1].gap[k-1]++;
		    	   k--;
		       }//increasing its gap till it reaches current level
		       
		       //when level less or equal
		       //to count number of nodes between them
		       while(k>0)
		       {
		    	   //going to its base of pre[k-1] and counting one by one nodes btwn newnode and prev[k-1]
		    	   //below if not needed not sure but?
		    	   if(prev[k-1].nextArr[k-1].element==null||prev[k-1].nextArr[k-1].element.compareTo(x)>0)
		    	   {
		    		   int countbtween=0;
		    		   Entry<T> dummy=prev[k-1];
		    		   //System.out.println(x);
		    		   //System.out.println(dummy.nextArr[0].element);
		    		   while(dummy.nextArr[0].element!=null&&dummy.nextArr[0].element.compareTo(x)<0)
		    		   {
		    			   dummy=dummy.nextArr[0];
		    			  // System.out.println("uyuyu");
		    	     		countbtween++;   
		    		   }//count is known
		    		   int oldvalue=prev[k-1].gap[k-1]; //old value is noted to update the gap ofnewly inserted element
		    		   newElement.gap[k-1]=oldvalue-countbtween;//newelement gap is old-countbetween
		    		   prev[k-1].gap[k-1]=countbtween;//countbtwen is assigned
		    	   }
		    	   k--;
		       }
			
			//logic ends and add starts
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
			Entry newElement = prev[0].nextArr[0];
			
			 int k=prev.length;
		       int entrylevel=newElement.chocieLevel;
		       while(k>entrylevel)//outgoing links which are above node newElement
		       {
		    	   prev[k-1].gap[k-1]--;
		    	   k--;
		       }//deccreasing its gap till it reaches current level
		       
		       //when level less or equal
		       //to count number of nodes between them
		       while(k>0)
		       {
		    	   //going to its base of pre[k-1] and counting one by one nodes btwn newnode and prev[k-1]
		    	   //below if not needed not sure but?
		    	
		    	   prev[k-1].gap[k-1]=prev[k-1].gap[k-1]+prev[0].nextArr[0].gap[k-1];
		    	    k--;
		       }
			
			//logic ends and delete starts
			
			
			if(x.compareTo(last)==0)
			{
				last=(T) prev[0].element;
			}
			for (int i = 0; i < newElement.chocieLevel; i++) {
				if (prev[i].nextArr[i] == newElement) {
					prev[i].nextArr[i] = newElement.nextArr[i];
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
		
		return last;
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
			return (cursor.nextArr[0].element!=null);
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
		while(dumy.element!=null)
		{
			input[j].element=dumy.element;		
			if(input[j].nextArr.length<=dumy.nextArr.length)
			{
				int h=0;
				while(h<input[j].nextArr.length)
				{
			     	input[j].nextArr[h]=dumy.nextArr[h];
			     	h++;
				}
			}
			else{
				int h=0;
				while(h<dumy.nextArr.length)
				{
			     	input[j].nextArr[h]=dumy.nextArr[h];
			     	h++;
				}
				
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
	//size + index if element is before it else index
	public void DummyIterator(Entry<T> x)
	{
       Entry[] prev=find(x.element); 
		//
       int k=prev.length;
       int entrylevel=x.chocieLevel;
       while(k>entrylevel)
       {
    	   prev[k-1].gap[k-1]++;
    	   k--;
       }
         while(k>0)
       {
    	   if(prev[k-1].nextArr[k-1].element.compareTo(x)>0)
    	   {
    		   int countbtween=0;
    		   Entry<T> dummy=prev[k-1];
    		   while(dummy.nextArr[0].element.compareTo(x)<0)
    		   {
    			   dummy=dummy.nextArr[0];
    	     		countbtween++;   
    		   }
    		   int oldvalue=prev[k-1].gap[k-1];
    		   x.nextArr[k-1].gap[k-1]=oldvalue-countbtween;
    		   prev[k-1].gap[k-1]=prev[k-1].gap[k-1]+countbtween;
    	   }
    	   k--;
       }
	}
	@Override
	public T findIndex(int n) {
	 	Entry<T> start=header;
		/*	int i=header.chocieLevel-1;
			boolean headerflag=true;
			n=n+1;
		
		while(i>0)
		{
			while(n>=start.gap[i])
			{
				n=n-start.gap[i];
				start=start.nextArr[i];
			}
		}
			
		return start.element;*/
	 	//if(this.==null||)
	 	Iterator iter=null;
	 	int i=-1;
	 	if(this.skipListItercurr==null||this.iterIndex>=n)
	 	{
	 		iter=this.iterator();
		 		
	 	}
	 	else{
	 		iter=this.skipListItercurr;
	 		i=iterIndex;
	 	}
	 	
	 	T result=null;
	 	while(i<n)
	 	{
	 		i++;
	 		result=(T) iter.next();
	 	}
	 	this.iterIndex=i-1;
		return result;
	}
	/*
	 * 	Entry<T> start=header;
		int i=header.chocieLevel-1;
		boolean headerflag=true;
		//n=n+1;
		if(n==size)
		{
			return (T) tail;
		}
		while(n<start.gap[i])
		{
			i=i-1;
			if(n>start.gap[i]+1)
			{
				n=n-1-start.gap[i];
				start=start.nextArr[i];
				i=start.chocieLevel-1;
			}
			else if(n==start.gap[i]+1){
				return (T) start.nextArr[i].element;
			}
			else if(n==0)
			{
				return (T) start.element;
				
			}
		}
		if(n==start.gap[i])
		{
			return (T) start.nextArr[i].element;
		}
		else if(n==0)
		{
			return (T) start.element;
		}
	
	return null;
	
	 */
}