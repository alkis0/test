/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GAproject;

import java.text.DecimalFormat;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import org.omg.CORBA.FREE_MEM;
 

/**
 * Στατική κλάση operations
 * Περιέχει όλες τις λειτουργίες του γενετικού 
 * αλγορίθμου.Για το ποιά συνάρτηση θα εφαρμοστούν οι 
 * λειτουργίες ορίζει η μεταβλητή fx.Ισχύει fx=1 για f(x)=x^2
 * και fx=2 για f(x)=-x*sin(|x|^1/2)-y*sin(|y|^1/2).
 *	
 *
 * 
 * 
 *
 * 
 */


public class operations{
	
	static boolean pressed;
	
	
	/**
	 * Στατική μέθοδος createPopulation τύπου ArrayList<Chromosome>
	 * με ορίσματα την λίστα με τα χρωμοσώματα (chromosome_list),τα μέλη του πληθυσμού (members),
	 * και το μήκος χρωμοσώματος.Επιστρέφει μία λίστα με χρωμοσώματα που αποτελλεί τον αρχικό πληθυσμό, τα
	 * μέλη του οποίου παράγονται τυχαία.
	 * 
	 * 
	 * 
	 */
	public static ArrayList<Chromosome> createPopulation(ArrayList<Chromosome> chromosome_list,int members,int len){
		Random rand=new Random(System.currentTimeMillis());


			//Σε περίπτωση που έχουμε την δεύτερη συνάρτηση ο τρόπος δημιουργίας χρωμοσώματος
			//αλλάζει.Χρησιμοποιώ την μεταβλητή sign_selection που μπορεί να πάρει τις τιμές 0 η 1 (τυχαία)
			//Αν είναι 1 τότε θα παραχθεί θετικός αριθμός εύρους 0 500 ενώ αν είναι 0 θα παραχθεί αρνητικός 
			//αριθμός εύρους 0 -500.
			int k1=0,k2=0;
			int sign_selection=0;
			for(int i=0;i<members;i++){
				
				sign_selection=(int)(rand.nextDouble()*2);
				if(sign_selection==1)
					k1=(int)(rand.nextDouble()*500);
				else
					k1=(int)(rand.nextDouble()*-500);
				sign_selection=(int)(rand.nextDouble()*2);
				if(sign_selection==1)
					k2=(int)(rand.nextDouble()*500);
				else
					k2=(int)(rand.nextDouble()*-500);
				
				Chromosome x=new Chromosome(k1,k2,len);
				chromosome_list.add(x);
			
		}
		return chromosome_list;


	}


	
	/**
	 *	Στατική μέθοδος createNewPopulation τύπου ArrayList<Chromosome> με
	 *	ορίσματα τη λίστα χρωμοσωμάτων chromosome_list,τα μέλη του πληθυσμού members,
	 *	το μήκος του χρωμοσώματος len και ένα αντικείμενο τύπου Form για εκτύπωση.
	 * 
	 */
	
	public static ArrayList<Chromosome> createNewPopulation(ArrayList<Chromosome> chromosome_list,int members,int len,Form f){
		Chromosome x1,x2,store=null;
		boolean found=true;
		double best_val=0;	
		
		
		DecimalFormat df = new DecimalFormat("#.###");
		Random x=new Random(System.currentTimeMillis());
		
		int count=0;
		for(int i=0;i<members-1;i++)
			if(chromosome_list.get(i).value==chromosome_list.get(i+1).value)
				count++;
		if(count==members-1)
			return chromosome_list;
		
		
		
		
		//Ταξινόμηση πληθυσμού με βάση την τιμή της αξίας του χρωμοσώματος.
		while(found){
			found=false;
			for(int i=0;i<members-1;i++){
				x1=chromosome_list.get(i);
				x2=chromosome_list.get(i+1);
				if(x1.value>x2.value){
					found=true;
					Chromosome temp=x1;
					x1=x2;
					x2=temp;
					chromosome_list.set(i,x1);
					chromosome_list.set(i+1,x2); 
				}


			}
		}
		
		//best_val=max πχ 0,1,5 => best_val=5
		best_val=chromosome_list.get(members-1).value;
		
		
		//Σε περίπτωση που θέλουμε τη δεύτερη εξίσωση πρέπει να τροποποιήσουμε τις τιμές επιλογής
		//έτσι ώστε να δίνουμε πρωτεραιότητα στην πιό μικρή τιμή.Αυτό γίνεται αφαιρόντας από τη
		//μέγιστη τιμή διαδοχικά τις τιμές των στοιχείων τις λίστας.Έτσι αν έχω 3 τιμές 
		//πχ -3,0,4 οι τιμές επιλογής θα γίνουν 4+3=7,4-0=4,4+4=0 άρα 7,4,0.Οι κανονικές τιμές δεν αλλάζουν.
		
		for(int i=0;i<members;i++)
			chromosome_list.get(i).selection_value=best_val-chromosome_list.get(i).value; 

		//ταξινόμηση με βάση την τιμή επιλογής
		do{
			found=false;
			for(int i=0;i<members-1;i++){

				x1=chromosome_list.get(i);
				x2=chromosome_list.get(i+1);
				if(x1.selection_value>x2.selection_value){
					Chromosome temp;
					temp=x1;x1=x2;x2=temp;
					chromosome_list.set(i,x1);
					chromosome_list.set(i+1,x2);
					found=true;
				}
			}
		}while(found);
		
		
		//Σε περίπτωση ελιτισμού/ποσόστωσης αποθηκεύω την best_val τιμή επιλογής και το χρωμόσωμα που την έχει
		//Έπειτα κάνω ένα for για έλεγχο τιμών επιλογής που είναι μικρότερες απο το 15% του best_val
		//Όσες είναι τις κάνω ίσες με το 15%.Τέλος αποθηκεύω την κανονική τιμή του πιό δυνατού χρωμοσώματος 
		//στο best_val για να το χρησιμοποιήσω (αν χρειαστεί) στον ελιτισμό
		if(f.checkbox_elitism().getState() || f.checkbox_escalate().getState()){
			best_val=chromosome_list.get(members-1).selection_value;//best_val=max_selection_value
			store=chromosome_list.get(members-1);
			for(int i=0;i<members;i++){
				if(chromosome_list.get(i).selection_value<0.15*best_val){
					chromosome_list.get(i).selection_value=0.15*best_val;
				}
			}
			//πλέον στο best_Val βρίσκεται η πιο μικρή τιμή.
			best_val=chromosome_list.get(members-1).value;
			
		}
		
		//Η αθροιστική πιθανότητα του κάθε μέλους αποτελεί το κρητίριο επιλογής του για 
		//την επόμενη γενιά.Αν έχουμε τις τιμές επιλογής 0,1,5,20 τότε οι πιθανότητες τους
		//θα είναι αντίστοιχα 0,1,6,26.Αυτό σημαίνει ότι με ένα random()*26 θα μπορούμε να κάνουμε την επιλογή
		double total=0;
		for(int i=0;i<members;i++){
			chromosome_list.get(i).power=total+Math.abs(chromosome_list.get(i).selection_value);
			total+=Math.abs(chromosome_list.get(i).selection_value);
		}

		f.display(chromosome_list,members,0);
		f.results().append("===============================\n");

		
		//Επιλογή
		ArrayList<Chromosome> selection=new ArrayList<Chromosome>();
		int c=0;
		for(int i=0;i<members;i++){
			double k=x.nextDouble()*total;
		
			for(int j=0;j<members;j++){
				if(k<chromosome_list.get(j).power){
					
					selection.add(c++,new Chromosome(chromosome_list.get(j).x,chromosome_list.get(j).y,len));//
					break;
				}

			}


		}
		//Έλεγχος για ελιτισμό.Αν έχουμε τότε ψάχνουμε με ενα for για τη μικρότερη τιμή που έχουμε αποθηκεύσει.
		//Αν δεν υπάρχει την προσθέτουμε σαν extra μέλος στον πληθυσμό.Σε κάθε περίπτωση εκτυπώνω την ελάχιστη τιμή.
		if(f.checkbox_elitism().getState()){
			found=false;
			for(int i=0;i<members;i++)
				if(selection.get(i).value==best_val){
					found=true;
					f.min_text.setText(df.format(best_val));
					break;
				}
			if(!found){
				selection.add(members,new Chromosome(store.x,store.y,len));
				f.members++;
				f.min_text.setText(df.format(store.value));
			}
			
		}
		else{
			best_val=selection.get(0).value;
			for(int i=1;i<members;i++) if(selection.get(i).value<best_val) best_val=selection.get(i).value; 
			f.min_text.setText(df.format(best_val));
		}
		
		chromosome_list=selection;

		
		
		f.display(chromosome_list,chromosome_list.size(),1);

		return chromosome_list;

}

	/**
	 *	Στατική μέθοδος Mutation τύπου void με
	 *	ορίσματα τη λίστα χρωμοσωμάτων chromosome_list,τα μέλη του πληθυσμού members,
	 *	το μήκος του χρωμοσώματος len ,ένα αντικείμενο τύπου Form για εκτύπωση και την πιθανότητα 
	 *  μετάλλαξης.
	 * 
	 */
	
	public static void Mutation(ArrayList<Chromosome> chromosome_list,int members,int len,Form f,double chance){
		int mutation_number=(int)(members*chance);
		Random k=new Random(System.currentTimeMillis());
		if(mutation_number==0) return;
		
		if(pressed){
			f.results().append("\n");
			f.results().append("elements for mutation\n");
			f.results().append("===============================\n");
		}
	
		double min=chromosome_list.get(0).value;
		Chromosome store=new Chromosome(chromosome_list.get(0).x,chromosome_list.get(0).y,len);
		

		
		for(int i=0;i<mutation_number;i++){	
			
			for(int j=0;j<members;j++)
				if(chromosome_list.get(j).value<min){
					min=chromosome_list.get(j).value;
					store=new Chromosome(chromosome_list.get(j).x,chromosome_list.get(j).y,len);
				}
		
			
			int member=k.nextInt(members);
			if(pressed) f.results().append("member "+member+" selected: "+chromosome_list.get(member).Binary_Form+"\n");
			int pos1,pos2;
			
			
			char bf[]=chromosome_list.get(member).Binary_Form.toCharArray();
			
		
				
			pos1=k.nextInt(len);
		
		
				
			//debug text
			if(pressed){
				f.results().append("attributes selected: "+pos1+"\n");
				System.out.println("member "+member+" selected: "+chromosome_list.get(member).Binary_Form+"\n");
				System.out.println("attr selected: "+pos1);
			}
			if(bf[pos1]=='1') bf[pos1]='0';
			else bf[pos1]='1';
	
		
			chromosome_list.get(member).Binary_Form=new String(bf);
		
			chromosome_list.get(member).toDecimal_evaluate_fx2(len);
			
			
			//debug text
			if(pressed) System.out.println("member "+member+" became: "+chromosome_list.get(member).Binary_Form+"\n");
			if(f.checkbox_elitism().getState()){
				boolean found=false;
				DecimalFormat df=new DecimalFormat("#.###");
				for(int j=0;j<members;j++){
					if(chromosome_list.get(j).value<=min){
						found=true;
						f.min_text.setText(df.format(chromosome_list.get(j).value));
						break;
					}
				}
				
				if(!found){
					chromosome_list.add(members,new Chromosome(store.x,store.y,len));
					members=++f.members;
					f.min_text.setText(df.format(store.value));
				}
			}

		}

		//debug text
		if(pressed) f.display(chromosome_list, members, 1);
	}




	public static void Crossover(ArrayList<Chromosome> chromosome_list,int members,int len,Form f,double chance){

		Random x=new Random(System.currentTimeMillis());

		//int crossover_pairs=(int)(Math.ceil(chance*members));
		int crossover_pairs=(int)(chance*members);
		if(crossover_pairs==0) return;
		if(pressed) System.out.println("Crossover pairs: "+crossover_pairs);
		int a_member,b_member;
		int pos1,pos2,pos3,pos4;
		Chromosome a,b;
		char b1[],b2[];

		//crossover_pairs=1;
		
		int count=0;
		for(int i=0;i<members-1;i++)
			if(chromosome_list.get(i).value==chromosome_list.get(i+1).value)
				count++;
		if(count==members-1) return;
	
		
		double min=chromosome_list.get(0).value;
		Chromosome store=new Chromosome(chromosome_list.get(0).x,chromosome_list.get(0).y,len);
		
		
		for(int i=0;i<crossover_pairs;i++){
			count=0;
			for(int j=0;j<members-1;j++)
				if(chromosome_list.get(j).value==chromosome_list.get(j+1).value)
					count++;
			if(count==members-1) return;
			
			for(int j=0;j<members;j++)
				if(chromosome_list.get(j).value<min){
					min=chromosome_list.get(j).value;
					store=new Chromosome(chromosome_list.get(j).x,chromosome_list.get(j).y,len);
				}
			
			
		
			do{
				a_member=x.nextInt(members);
				b_member=x.nextInt(members);

			}while(a_member==b_member || (chromosome_list.get(a_member).x==chromosome_list.get(b_member).x && chromosome_list.get(a_member).y==chromosome_list.get(b_member).y));
				
			if(pressed)f.results().append("selected members for "+i+" crossover: "+a_member+" "+b_member+"\n");
			
			//compute Crossover elements posittions
			
			do{
				pos1=x.nextInt(len);
				pos2=x.nextInt(len);
			}while(pos1==pos2);
			

			a=chromosome_list.get(a_member);
			b=chromosome_list.get(b_member);
			
			
			
			b1=a.Binary_Form.toCharArray();
			b2=b.Binary_Form.toCharArray();
			

			
			//debug text
			if(pressed){
				f.results().append(pos1+" "+pos2+"\n");
				f.results().append("before crossover\n");
				f.results().append(a.Binary_Form+"\n"+b.Binary_Form+"\n");
			}
			
			char temp;
			temp=b1[pos1];
			b1[pos1]=b2[pos1];
			b2[pos1]=temp;

			temp=b1[pos2];
			b1[pos2]=b2[pos2];
			b2[pos2]=temp;

		
			

			a.Binary_Form=new String(b1);
			b.Binary_Form=new String(b2);
			if(pressed){
				System.out.println(a.Binary_Form);
				System.out.println(b.Binary_Form);
			}
		

			a.toDecimal_evaluate_fx2(len);
			b.toDecimal_evaluate_fx2(len);

		
			
			
			if(f.checkbox_elitism().getState()){
				boolean found=false;
				DecimalFormat df=new DecimalFormat("#.###");
				for(int j=0;j<members;j++){
					if(chromosome_list.get(j).value<=min){
						found=true;
						f.min_text.setText(df.format(chromosome_list.get(j).value));
						break;
					}
				}
				
				if(!found){
					chromosome_list.add(members,new Chromosome(store.x,store.y,len));
					members=++f.members;
					
					//JOptionPane.showMessageDialog(f, chromosome_list.get(members-1).value);
				
					f.min_text.setText(df.format(store.value));	
				}
			}
			
			if(pressed){
				System.out.println("val1 "+a.Binary_Form);
				System.out.println("val2 "+b.Binary_Form);
			}
			//debug text
			if(pressed){
				f.results().append("after Crossover\n");
				f.results().append(a.Binary_Form+"\n"+b.Binary_Form+"\n");
				f.display(chromosome_list,members,1);
			}
			
			
			
			
			
	
		}
	}

	/**
	 *	Στατική μέθοδος GAInversion τύπου void με
	 *	ορίσματα τη λίστα χρωμοσωμάτων chromosome_list,τα μέλη του πληθυσμού members,
	 *	το μήκος του χρωμοσώματος len , ένα αντικείμενο τύπου Form για εκτύπωση και
	 *	την πιθανότητα για αναστροφή.
	 * 
	 */
	
	
	public static void GAInversion(ArrayList<Chromosome> chromosome_list,int members,int len,Form f,double chance){
		Random x=new Random(System.currentTimeMillis());
		int inversion_members=(int)(chance*members);
		int pos1;
		int pos2;
		int member;

		if(inversion_members==0) return;
			
		double min=chromosome_list.get(0).value;
		Chromosome store=new Chromosome(chromosome_list.get(0).x,chromosome_list.get(0).y,len);
		
		
		for(int i=0;i<inversion_members;i++){
			
			for(int j=0;j<members;j++)
				if(chromosome_list.get(j).value<min){
					min=chromosome_list.get(j).value;
					store=new Chromosome(chromosome_list.get(j).x,chromosome_list.get(j).y,len);
				}
		

			//κάθε φορά στη μεταβλητή member επιλέγεται τυχαία ένα μέλος
			//από το συνολικό πληθυσμό
			member=x.nextInt(members);
		
			
			//επανάληψη για επιλογή τυχαίων θέσεων για αναστροφή
			//έτσι ώστε να μην είναι ίδιες			
			do{
				pos1=x.nextInt(len);
				pos2=x.nextInt(len);
			}while(pos1==pos2);
		

			
			if(pressed){
				f.results().append("selected: "+member+"\n");
				f.results().append("pos: "+pos1+" "+pos2+"\n");
				f.results().append("before: "+chromosome_list.get(member).Binary_Form+"\n");

			}
			//Σπάω το String που υπάρχει η δυαδική μορφή του
			//χρωμοσώματος που επιλέχτηκε σε πίνακα χαρακτήρων
			//για να κάνω τις αλλαγές
			char a[]=chromosome_list.get(member).Binary_Form.toCharArray();
			char temp[]=new char[Math.abs(pos1-pos2)+1];

			int p=0;
			int t;

			//εφαρμογή του GAinversion
			if(pos1>pos2){
				t=pos1;
				pos1=pos2;
				pos2=t;
			}
			for(int j=pos2;j>=pos1;j--) temp[p++]=a[j];
			p=0;
			for(int j=pos1;j<=pos2;j++)	a[j]=temp[p++];
			

			//αποθήκευση του String που προκύπτει σαν νεα δυαδική μορφή
			//του χρωμοσώματος και υπολογισμός της νέας τιμής του
			chromosome_list.get(member).Binary_Form=new String(a);
			chromosome_list.get(member).toDecimal_evaluate_fx2(len);
		
			
			if(f.checkbox_elitism().getState()){
				boolean found=false;
				DecimalFormat df=new DecimalFormat("#.###");
				for(int j=0;j<members;j++){
					if(chromosome_list.get(j).value<=min){
						found=true;
						f.min_text.setText(df.format(chromosome_list.get(j).value));
						break;
					}
				}
				
				if(!found){
					chromosome_list.add(members,new Chromosome(store.x,store.y,len));
					members=++f.members;
					f.min_text.setText(df.format(store.value));
				}
			}
			
			
			
			if(pressed){
				f.results().append("after: "+chromosome_list.get(member).Binary_Form+"\n");
				f.display(chromosome_list,members,1);
			}
			
			
		}
		
	}

}