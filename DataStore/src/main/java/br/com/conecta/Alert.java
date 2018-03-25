package br.com.conecta;

public class Alert {
	String pos;
	String giro;
	String mov;
	String time;
	String data;
	
	public Alert() {}
		
	public Alert(String pos, String giro, String mov, String time, String data) {
		this.pos = pos;
		this.giro = giro;
		this.mov = mov;
		this.time = time;
		this.data = data;
		
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getGiro() {
		return giro;
	}

	public void setGiro(String giro) {
		this.giro = giro;
	}

	public String getMov() {
		return mov;
	}

	public void setMov(String mov) {
		this.mov = mov;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Alert [pos=" + pos + ", giro=" + giro + ", mov=" + mov + ", time=" + time + ", data=" + data + "]";
	}

	



	
}