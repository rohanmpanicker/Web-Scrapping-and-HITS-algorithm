package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScrapper {
	
	static Set<String> expanded = new HashSet<String>();
	static Set<String> seed = new HashSet<String>();
	static ArrayList<String> nodes = new ArrayList<String>();
	static Map<String,String> linkMapping = new HashMap<>();
	static int nGraph[][];
	static double hubScore[];
	static double authScore[];
	static double tempHubScore[];
	static double tempAuthScore[];
	static double maxHubValues[];
	static double maxAuthValues[];
	static String maxAuthPages[];
	static String maxHubPages[];

	public WebScrapper() {
		// TODO Auto-generated constructor stub
	}
	
	static void descriptionExtracter() throws IOException {
		
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\description.txt"));
		
		Iterator<String> iterator = seed.iterator();
		
		while( iterator.hasNext()) {
			
			try {
				
				String link = iterator.next();
				System.out.println(link);
				Document doc = Jsoup.connect(link).get();  
				String title = doc.title(); 
				System.out.println(title);
				
				
				out.write("Link : " + link);
				out.newLine();
				out.write("Description : " + title);
				out.newLine();

				
			}
			catch(Exception e) {
				continue;
			}
			
		}

		out.close();
        System.out.println("Description File created successfully");


	}
	
	static void topValueExtraction(int numberOfValues) throws IOException {
		
		maxAuthValues = new double[numberOfValues];
		maxHubValues = new double[numberOfValues];
		maxAuthPages = new String[numberOfValues];
		maxHubPages = new String[numberOfValues];
		String maxAuthPage = "";
		String maxHubPage = "";
		
		
		for(int i=0; i < authScore.length; i++) {
			
			tempAuthScore[i]=authScore[i];
			tempHubScore[i]=hubScore[i];
		}
		
		int counter=0;
		double topHubValue=-1.0;
		double topAuthValue=-1.0;
		int hubPosition=0;
		int authPosition=0;
		
		
		while(counter < numberOfValues ) {
			
			for(int i=0; i<tempAuthScore.length; i++) {
				
				if( i == 0 && tempAuthScore[i]!=-1.0) {
					
					topAuthValue = tempAuthScore[i];
					authPosition = i;
					maxAuthPage=nodes.get(i);
					
				}
				else {
					
					if( topAuthValue == -1.0 ) {
						topAuthValue = tempAuthScore[i];
						authPosition = i;
						maxAuthPage=nodes.get(i);
					}
					else if( tempAuthScore[i] > topAuthValue ){
						topAuthValue = tempAuthScore[i];
						authPosition = i;
						maxAuthPage=nodes.get(i);
						
					}
					
				}
				
				if( i == 0 && tempHubScore[i]!=-1.0) {
					
					topAuthValue = tempHubScore[i];
					hubPosition = i;
					maxHubPage=nodes.get(i);
				}
				else {
					
					if( topHubValue == -1.0 ) {
						topHubValue = tempHubScore[i];
						hubPosition = i;
						maxHubPage=nodes.get(i);
					}
					else if( tempHubScore[i] > topHubValue ){
						topHubValue = tempHubScore[i];
						hubPosition = i;
						maxHubPage=nodes.get(i);
					}
					
				}
				
			}//for loop ends
			
			maxAuthValues[counter]=topAuthValue;
			maxHubValues[counter]=topHubValue;
			maxAuthPages[counter]=maxAuthPage;
			maxHubPages[counter]=maxHubPage;
			counter++;
			topAuthValue=-1.0;
			topHubValue=-1.0;
			tempAuthScore[authPosition]=-1.0;
			tempHubScore[hubPosition]=-1.0;
			authPosition=0;
			hubPosition=0;
			
		}
		
		File file = new File("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\maxValues.txt");
		BufferedWriter max = new BufferedWriter(new FileWriter(file));
		max.write(numberOfValues + " Hub Score Values : ");
		max.newLine();
		for(int r=0; r<maxHubPages.length; r++) {
			max.write(maxHubPages[r] + " : " + maxHubValues[r]);
			max.newLine();
		}
		
		max.newLine();
		
		max.write(numberOfValues + " Authority Score Values : ");
		max.newLine();
		for(int r=0; r<maxAuthPages.length; r++) {
			max.write(maxAuthPages[r] + " : " + maxAuthValues[r]);
			max.newLine();
		}
		
		max.close();
	}
	
	static void computeHITS() throws IOException {
		
		File file = new File("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\output.txt");
		BufferedWriter op = new BufferedWriter(new FileWriter(file));
		
		//System.out.print("Base : 0 :");
		op.write("Base : 0 :");
		for(int i = 0 ; i < authScore.length ; i++ ) {
			//System.out.printf("A/H[%d] = %f/%f",i,authScore[i],hubScore[i]);
			op.write("A/H["+i+"] = "+authScore[i]+"/"+hubScore[i]);
		}
		
		op.newLine();
		
		int noOfIterations = 15;
		
		for( int r=0; r<noOfIterations ; r++ ) {
			
			//Authority Score Calculation
			for(int j=0; j < nodes.size(); j++) {
				
				boolean enter = true;
				
				for(int i=0 ; i < nodes.size(); i++) {
					
					if(nGraph[i][j]==1 && enter) {
						tempAuthScore[j] = hubScore[i];
						enter = false;
					}
					else if(nGraph[i][j]==1 && !enter) {
						tempAuthScore[j] = tempAuthScore[j] + hubScore[i];
					}
					
					
				}
				
				authScore[j] = tempAuthScore[j];
				
			}
			
			//Hub Score Calculation
			for(int i = 0 ; i < nodes.size() ; i++) {
				
				boolean enter=true;
				
				for(int j=0;j<nodes.size();j++) {
					
					if(nGraph[i][j]==1 && enter) {
						tempHubScore[i] = hubScore[j];
						enter = false;
					}
					else if(nGraph[i][j]==1 && !enter) {
						tempHubScore[i] = tempHubScore[i] + hubScore[j]; 
					}	
				}
				
				hubScore[i] = tempHubScore[i];
			}
			
			
			//Normalization of the values
			 
			double sumAuthScore = 0.0;
			double sumHubScore = 0.0;
			
			for(int i=0 ; i<authScore.length ; i++) {
				sumAuthScore = sumAuthScore + authScore[i];
				sumHubScore = sumHubScore + hubScore[i];
			}
			
			for(int i=0 ; i<authScore.length ; i++) {
				authScore[i]=authScore[i]/Math.sqrt(sumAuthScore);
				hubScore[i]=hubScore[i]/Math.sqrt(sumHubScore);
			}
			
			//Normalization ends
			
			//System.out.printf("Iter : %d :",(r+1));
			op.write("Iter : "+(r+1)+" :");
			for(int k = 0 ; k < authScore.length ; k++ ) {
				//System.out.printf("A/H[%d] = %f/%f",k,authScore[k],hubScore[k]);
				op.write("A/H["+k+"] = "+authScore[k]+"/"+hubScore[k]);
			}
			System.out.println();
			op.newLine();
				
		}//iteration for loop
		
		op.close();
		
	}
	
	static void intialHubAndAuth() {
		
		hubScore = new double[nodes.size()];
		authScore = new double[nodes.size()];
		tempHubScore = new double[nodes.size()];
		tempAuthScore = new double[nodes.size()];
		
		for(int i=0; i < nodes.size() ; i++) {
			
			hubScore[i]=1;
			authScore[i]=1;
			
		}
		
	}
	
	static void neighborhoodGraphGenerator(int threshold) throws IOException {
		
		int capacity = 30 * threshold;
		int tracker=0;
		Iterator<String> i = seed.iterator();
		int noOfEdges = 0;
		
		while(i.hasNext()) {
			tracker++;
			nodes.add(i.next());
			
			if(tracker == capacity ) {
				break;
			}
			
		}
		
		Iterator<String> i2 = expanded.iterator();
		
		while(i2.hasNext()) {
			tracker++;
			nodes.add(i2.next());
			
			if(tracker == capacity ) {
				break;
			}
			
		}
		
		File mappingFile = new File("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\mapping.txt");
		File graph = new File("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\graph.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(mappingFile));
		
		String st;
		
		nGraph= new int[nodes.size()][nodes.size()];
		
		System.out.println("The total number of nodes considered after threshold : " + nodes.size());
		
		while ((st = br.readLine()) != null) { 
			
			//System.out.println(st);
			String links[] = st.split(",");
			int pos1=-1;
			int pos2=-1;
			
			boolean enter1 = true;
			boolean enter2 = true;
			
			//System.out.println(links[1]+"->"+links[0]);
			
			for(int r = 0 ; r < nodes.size() ; r++) {
				
				//Finding the parent node
				if( enter1 && links[1].equals(nodes.get(r))) {
					pos1=r;
					enter1=false;
					
					if(!enter2) {
						break;
					}
					
				}
				
				//Finding the child node
				if( enter2 && links[0].equals(nodes.get(r))) {
					pos2=r;
					enter2=false;
					 
					if(!enter1) {
						break;
					}
				}
				
			}
			
			//System.out.println(pos1 + " " + pos2);
			
			if(pos1!=-1 && pos2!=-1) {
				nGraph[pos1][pos2]=1;
				noOfEdges++;
			}
			
			
			
			
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(graph));
		
		for(int r=0; r < nGraph.length ; r++) {
			for(int s=0; s < nGraph[r].length ; s++) {
				bw.write(nGraph[r][s] + " ");
			}
			bw.newLine();
		}
		
		bw.close();
		
		System.out.println("The number of edges are : " + noOfEdges);
		
	}//neighborhoodGraph() ends
	
	static void LinkExtracter() {
		
		try {
			
			File file = new File("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\seed.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			Pattern p = Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\expanded.txt"));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\mapping.txt"));
			
			String st;
			while ((st = br.readLine()) != null) { 
				
				try {
					
					Document doc = Jsoup.connect(st).get();  
			        String title = doc.title();  
			        //System.out.println(title);
			        Elements links = doc.select("a[href]");
			        
			        for(Element l : links) {
			        	
			        	Matcher m = p.matcher(l.attr("href"));
						while (m.find()) { 
				            expanded.add(m.group());
				            linkMapping.put(m.group(),st);
				        }
						
			        }
			        
				}
				catch(Exception e) {
					continue;
				}
				
				
			}
			
			for ( Map.Entry<String,String> entry : linkMapping.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    bw2.write(key+","+value);
			    bw2.newLine(); 
			}
			
			
			Iterator<String> itr = expanded.iterator();
			
			while(itr.hasNext()) {
				bw.write(itr.next());
		        bw.newLine();
			}
			
			bw.close();
			bw2.close();
			
			
	        System.out.println("Expanded File created successfully");
	        System.out.println("Mapping File created successfully");
	        System.out.println("The total number of expanded nodes : " + expanded.size());
	        System.out.println("The total number of mappings : " + linkMapping.size());
		}
		catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
        
		
	}//LinkExtracter() ends	

	static void seedFileGenerator(String query) throws IOException {
		
		Pattern p = Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
		int i=0, counter = 0;
		int start=10;
		
		while(counter<30) {
			
			if( i == 0) {
				
				Document doc = Jsoup.connect("https://www.google.com/search?q="+query).get();  
		        String title = doc.title();  
		        System.out.println(title);
		        Elements links = doc.select(".r , .ad_cclk[href]");
		        i++;
		        
		        for (Element link : links) {
		        	
		            if(counter > 29) {
		            	break;
		            }
		            
		            Matcher m = p.matcher(link.text());
					
					while (m.find()) {
						seed.add(m.group());
			            counter++;
			        }
		            
		            

		        }
		        
		        if(counter > 29) {
	            	break;
	            }
		        
		        
		        
			}
			else if( i > 0 ) {
				
				Document doc = Jsoup.connect("https://www.google.com/search?q="+query+"&start="+start).get();  
		        Elements links = doc.select(".r , .ad_cclk[href]");
		        
		        for (Element link : links) {
		        	
		        	if(counter > 29) {
		            	break;
		            }
		        	
		        	Matcher m = p.matcher(link.text());
					
					while (m.find()) {
						seed.add(m.group());
			            counter++;
			        }
		            
		            
		        }
		        
		        start=start+10;
		        
		        if(counter > 29) {
	            	break;
	            }
				
			}
			
		}//while ends
		
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Rohan Panicker\\Desktop\\NJIT\\Spring 2019\\CS 634 - Data Mining\\Advanced Project\\seed.txt"));
		
		
		Iterator<String> iterator = seed.iterator();
		
		while(iterator.hasNext()) {
			
			Matcher m = p.matcher(iterator.next());
			
			while (m.find()) {
	            out.write(m.group());
	            out.newLine();
	        } 
			
		}
		
		out.close();
        System.out.println("Seed File created successfully");
        
        System.out.println("The total number of seed nodes : " + seed.size());

	}//seedFileGenerator() ends

	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter your search query keyword");
			
			String keyword = br.readLine();
			String words[] = keyword.split(" ");
			
			String query="";
			
			for(int i=0; i<words.length; i++) {
				
				if( i == words.length - 1 ) {
					query=query+"+"+words[i];
				}
				else {
					query=query+words[i]+"+";
				}
			}
			
			
			WebScrapper ws = new WebScrapper();
			ws.seedFileGenerator(query);
			
			System.out.println("Enter the threshold value of k(>=10) : ");
			int k = Integer.parseInt(br.readLine());
			
			System.out.println("Enter the number of values you want to extract");
			int noOfValues = Integer.parseInt(br.readLine());
			
			
			ws.LinkExtracter();
			ws.neighborhoodGraphGenerator(k);
			ws.intialHubAndAuth();
			ws.computeHITS();
			ws.topValueExtraction(noOfValues);
			ws.descriptionExtracter();
			
		}
		catch (NullPointerException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		catch(HttpStatusException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
				

	}//main ends

}//class ends
