package com.example.rationalfunctions;

import java.util.*;

class Mulpoly extends Hashtable<String,LinkedList<Integer>> { //Calculation: used to return values to plot on graphs.
	//Hashtable : A set of key-value pairs, each polynomial is a key, and the values are its indices
	//String: The input polynomial
	//LinkedList<Integer>: The exponents of that particular key
	LinkedList<Integer> tmp; //temporary storage for the values for each key
	
	Mulpoly(String str) {  //str = full input polynomial
		int oldind=0,exp=1,i,j;	//oldind = the initial index of each polynomial in the string; exp = to store the exponents for each polynomial; i,j = looping variables;  		
		char c;			//c = to check for the beginning and end of each polynomial
		String poly;	//poly = to store the polynomials at each iteration
		double m=1,sl,cold;		//m = outer slope;  sl =attached slope to x; cold = old intercept
		
		for(i=0;i<str.length();i++) {
			c=str.charAt(i);
			if(i==0 && c!='(') {
				try {m=Double.parseDouble(str.substring(0,str.indexOf('(')));} 
				catch(Exception e) {		
					if(str.substring(0,str.indexOf('(')).equals("-"))
						m=-1;
				}
			  i=str.indexOf('(')-1;
			}
				
			if(c=='(') 
				oldind=i+1;	
			else if(c==')') {	
			// if ) is encountered, then one polynomial has just been finished iterating through
				poly=str.substring(oldind,i);	
				
				//making the new term
				try {
					sl=Double.parseDouble(poly.substring(0,poly.indexOf('x')));
				}  
				catch(Exception e) {
					sl=1; 
					if(poly.substring(0,poly.indexOf('x')).equals("-")) 
						sl=-1;
				}
				sl*=m;
				cold=Double.parseDouble(poly.substring(poly.indexOf('x')+2))*m;  
				poly=sl+"x"+poly.charAt(poly.indexOf('x')+1)+cold;
				m=1;				
				
				//save the polynomial.
				j=i+1;	//looking for the next (
				try {
				  for(;j<str.length();j++)
					if(str.charAt(j)=='(') {
						//if ( is found then the substring between the previous ) (at i) and ( (at j) will give us the exponent.
						//the exponent will start from (i+2) position because )^ takes the ith and (i+1)th position.
						exp=Integer.parseInt(str.substring(i+2,j)); 
						break;
					}
				  if(j==str.length()) exp=Integer.parseInt(str.substring(i+2));
				  //in case of the last polynomial, the entire substring till the end of the string is the exponent.
			    }
				catch(Exception e) {
					exp=1; //if there is no exponent, eg. (x-5), then the exponent=1.
				}	
				i=j-1; //to iterate from the ( again.
				
				//looking for the value of the key stored in 'poly'
				if((tmp=get(poly))==null)		//if this term has not been added at least once, make a new LinkedList for the polynomial.
				   tmp=new LinkedList<Integer>();
				tmp.add(exp);	//add the new exponent to the end of the list.
				put(poly,tmp);  //putting back this key-value to the Hashtable
			}
		}	
	}		
	  
	Number[][] getArr() {
		//getArr() is used to return all the poles and zeroes of the input function.
		
		Double[][] x= new Double[3][100]; 
		//The array has 3 rows. Values will be stored in [0]th row and [2]th row for the exponents -1 and 1 respectively.
		//This means, every pole will be saved in the [0]th row and every zero in the [2]th row.
		
		Enumeration<String> e = keys(); //e: enumeration containing all the polynomials in the equation	
		String str; //temporary location to store key at each iteration 
		Iterator<Integer> it; //to iterate through the linked list of exponents for each polynomial
		int i=0,c;	//c: to index and save in x
		double m;  //slope
		
		while (e.hasMoreElements()) {
			tmp=get(str=e.nextElement());
			//tmp stores the linkedlist of exponents
			m=Double.parseDouble(str.substring(0,str.indexOf('x')));
			//m stores the slope. it is the value of the polynomial till before the 'x'
			
			it = tmp.iterator(); 	//to iterate through the linkedlist
			 while (it.hasNext()) { 	 
				x[(c=it.next()+1)][i]=Double.parseDouble(str.substring(str.indexOf('x')+2))/m; 
				//x[][] = c. c in this case will be c/m. y = m(x+c/m)
				if(str.charAt(str.indexOf('x')+1)=='+') 
					x[c][i]=-x[c][i];
				//if the value of c is positive, then the x-intercept will be negative.
				i++;
				x[c][i++]=0.0;  //saving 0 for the y coordinate so that it gets placed on the x-axis.
			 }
		}
		return x;
	}
		
	Double[] getGraph(String key, int exp) {	//Get the coordinates for the graph for (key)^exp and stores the (x,y) values for the graph
		double intr=Double.parseDouble(key.substring(key.indexOf('x')+2)),m=Double.parseDouble(key.substring(0,key.indexOf('x'))),x=-50;	
		//x: initial value of x
		//intr: value of the intercept.
		//m: slope
		Double[] pts= new Double[1000];
		intr=(key.charAt(key.indexOf('x')+1)=='-')?-intr:intr;  //saving the intercept with the sign.
		int i,j;
		switch(exp) {
			case -1:   //for terms of the type (x-a)^-1
				for(i=0;i<500;i+=2,x+=.5)
					if(m*(pts[i]=x)!=-intr) 	//mx!=c
						pts[i+1]=1/(m*x+intr);  //y=1/(mx-c)
					 else {
					 	for(j=0,x-=.5;j<5;pts[i+1]=1/(m*(pts[i]=x)+intr),j++,i+=2,x+=0.1);
					 	x-=0.4; }//as it approaches infinity, the graph should be more precise.	
				break;
				
			case 1:		//for terms of the type (x-a)
				x=-10;
				for(i=0;i<3;pts[i]=((pts[i+1]=x)-intr)/m,i+=2,x+=20);			 //straight lines with y=-20 and y=20;  x=(y-c)/m
				break;
		}
		return pts;
	}
}
