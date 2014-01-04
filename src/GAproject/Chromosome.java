
package GAproject;

import javax.swing.JOptionPane;




public class Chromosome {
	String Binary_Form="";//binary
	int chrom_len;//default len
	int x;
	int y;
	double value;
	double selection_value;
	double power;
	




	//Κατασκευαστής για τη συνάρτηση f(x,y)=-x*sin(|x|^1/2)-y*sin(|y|^1/2)
	public Chromosome(int num1,int num2,int len){
		x=num1;
		y=num2;
	
		
		chrom_len=len;
		double x_absolute=Math.abs((double)x);
		double y_absolute=Math.abs((double)y);
		
		double x_absolute_square=Math.sqrt(x_absolute);
		double y_absolute_square=Math.sqrt(y_absolute);
		
		value=-x*Math.sin(x_absolute_square)-y*Math.sin(y_absolute_square);
		selection_value=value;
		toBinary_fx2();
	
		
	}
	
	

	
	/**
	 *	Μέθοδος για την κωδικοποίηση των αριθμών x,y
	 *	σε δυαδικό.
	 * 
	 */
	public void toBinary_fx2(){
		//binary form for 2 integers will be like this:
		//example x=2 y=3 and length=8
		//then Binary_Form=00000010;00000011
		
	
		int n1,n2;
		int signx,signy=signx=1;
		
		if(x>=0)
			n2=x;
		else{
			n2=-x;
			signx=-1;
		}
		if(y>=0)
			n1=y;
		else{
			n1=-y;
			signy=-1;
		}
		
		
		int i;
		String temp="";
		
		//y code
		while(n1!=0){
			int remain=n1%2;
			n1/=2;
			if(remain==1)
				temp+="1";
			else
				temp+="0";
			
		}
		
		
		for(i=temp.length();i<chrom_len/2-1;i++)
			temp+="0";
		if(signy==-1) temp+="1";
		else temp+="0";
		
		//temp+=";";
		while(n2!=0){
			int remain=n2%2;
			n2/=2;
			if(remain==1)
				temp+="1";
			else
				temp+="0";
			
		}
		
		for(i=temp.length();i<chrom_len-1;i++)
			temp+="0";
		if(signx==-1) temp+="1";
		else temp+="0";
		
		char s[]=temp.toCharArray();
	
		
		for(i=temp.length()-1;i>=0;i--)
			Binary_Form+=String.valueOf(s[i]);
		
		//System.out.println(Binary_Form);
	
	}
	
	

	/**
	 *	Evaluate. 
	 * 
	 */
	public void toDecimal_evaluate_fx2(int len){
		int signx=1;
		int signy=1;
		x=y=0;

		char c[]=Binary_Form.toCharArray();
		int i;
		if(c[0]=='1') signx=-1;
		
		if(c[len/2]=='1')signy=-1;
		
		int k=0;
		//System.out.println("debugx:");
		for(i=0;i<len/2-1;i++){
			//System.out.println(c[i+1]);
			if(c[i+1]=='1')
				x+=(int)(Math.pow(2,len/2.-2-i));
			//if len=10 then i will become 0,1,2,3
			//so 2^3-i
		}

		//System.out.println("x= "+x);
	
		//System.out.println("debugy");
		for(i=len/2;i<len-1;i++){
			//System.out.println(c[i+1]);
			if(c[i+1]=='1')
				y+=Math.pow(2,(double)(len/2.)-2-(double)(i-len/2.)) ;
			//if len=10 then i will become 5,6,7,8
			//so 2^3-i-5
		}
		//System.out.println("y= "+y);
		//JOptionPane.showMessageDialog(null, y);
		
		x=signx*x;
		y=signy*y;

		double x_absolute=Math.abs((double)x);
		double y_absolute=Math.abs((double)y);
		
		double x_absolute_square=Math.sqrt(x_absolute);
		double y_absolute_square=Math.sqrt(y_absolute);
		
		selection_value=value=-x*Math.sin(x_absolute_square)-y*Math.sin(y_absolute_square);
		
		//System.out.println("result= "+value);
				
	}
	
	
}
