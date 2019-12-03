package excel;

import java.util.ArrayList;
import java.util.List;

public class DoforExcle {

	public static void main(String[] args) {

		List<Sport> listS = GetCa.readexcel(0, "");
		listS = doforList(listS);


	}

	public static List<Sport> doforList(List<Sport> listS) {
		double maxSpeed = 0;
		List<Long> timel = new ArrayList<Long>();
		long intime = listS.get(0).time;
		for (Sport sport : listS) {
			sport.time = sport.time-intime;
			if(sport.speed>maxSpeed) {
				timel.clear();
				timel.add(sport.time);
			}else if(sport.speed==maxSpeed){
				timel.add(sport.time);
			}
		}
		long midtime = 0;
		for (int i = 0; i < timel.size(); i++) {
			midtime+=timel.get(i);
		}
		midtime = midtime/timel.size();
		
		List<Sport> reList = new ArrayList<>();
		
		for (Sport sport : reList) {
			reList.add(new Sport(sport.speed, sport.time-midtime));
		}
	
		return reList;
	}
}
