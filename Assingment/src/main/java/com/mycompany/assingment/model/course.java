package com.mycompany.assingment.model;

public class course {
	private int courseid;
	private String coursename;
	private int duration;
	private String category;
	private double fees;
	
	public double getFees() {
		return fees;
	}
	public void setFees(double fees) {
		this.fees = fees;
	}
	public int getCourseid() {
		return courseid;
	}
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "course [courseid=" + courseid + ", coursename=" + coursename + ", duration=" + duration + ", category="
				+ category + "]";
	}
	 
}
