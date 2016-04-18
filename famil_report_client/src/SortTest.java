import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SortTest {

	static Comparator<Map<String, String>> comparator = new Comparator<Map<String, String>>() {
		public int compare(Map<String, String> s1, Map<String, String> s2) {

			return -(s1.get("date").compareTo(s2.get("date")));

		}
	};
	
	public static void main(String[] args) {
		Map<String, String> mq1=new HashMap<String,String>();
		mq1.put("id", "1");
		mq1.put("date", "20130308");
		
		Map<String, String> mq2=new HashMap<String,String>();
		mq2.put("id", "2");
		mq2.put("date", "20110311");
		
		Map<String, String> mq3=new HashMap<String,String>();
		mq3.put("id", "3");
		mq3.put("date", "20151231");
		
		Map<String, String> mq4=new HashMap<String,String>();
		mq4.put("id", "4");
		mq4.put("date", "20180311");
		
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		list.add(mq3);
		list.add(mq2);
		list.add(mq1);
		Collections.sort(list,comparator);
		
		
	  Calendar calendar=	Calendar.getInstance();
	  int maxYear=calendar.get(Calendar.YEAR);
	  System.out.println(calendar.get(Calendar.YEAR));	
	  calendar.add(Calendar.YEAR, -5);
	  
	  int minYear=calendar.get(Calendar.YEAR);
	  System.out.println(calendar.get(Calendar.YEAR));
	  List<Map<String, String>> validList=new ArrayList<Map<String, String>>();
	  for(Map<String, String> m:list){
		    String scoreDate=m.get("date");
		    if(scoreDate!=null&&scoreDate.length()==8){
		    	int year=Integer.parseInt(scoreDate.substring(0,4));
		    	if(year<=maxYear&&year>=minYear){
		    		validList.add(m);
		    	}
		    }
			
		}
	  
	  for(Map<String, String> m:validList){
		  System.out.println(m);
	  }
	  
	  String s="1234";
	  System.out.println(s.substring(1,s.length()));
	  
	  
	  
	  
	  
	  
	}

	
	

}
