package br.com.conecta;

import java.util.ArrayList;
import java.util.List;

public class TrackerPosList {

	List<TrackerPos> trackerPosList = new ArrayList<>();

	public List<TrackerPos> getTrackerPosList() {
		return trackerPosList;
	}

	public void setTrackerPosList(List<TrackerPos> trackerPosList) {
		this.trackerPosList = trackerPosList;
	}

	@Override
	public String toString() {
		return "TrackerPosList [trackerPosList=" + trackerPosList + "]";
	}
	
}
