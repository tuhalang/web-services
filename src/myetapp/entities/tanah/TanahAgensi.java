package myetapp.entities.tanah;

import java.io.Serializable;

import myetapp.entities.gis.Tanah;

public class TanahAgensi extends Tanah implements Serializable {
	private static final long serialVersionUID = -4787301040910626439L;
	private String kod;
	private String nama;
	private int bilangan;
	private double luas;
	
	public String getKod() {
		return kod;
	}
	public void setKod(String kod) {
		this.kod = kod;
	}
	
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public int getBil() {
		return bilangan;
	}
	public void setBil(int bilangan) {
		this.bilangan = bilangan;
	}	
	
	public double getLuas() {
		return luas;
	}
	public void setLuas(double luas) {
		this.luas = luas;
	}
	
	
}
