package jm;


/**
 * Sport Moment arrtubute
 * 对应的指标和特征
 *
 */
public class SMA {

	long T;
	double S;
	long t_a;
	long t_d;
	long t_i;
	long t_c;

	double v_mr;
	double v_m;
	double v_max;
	double v_sd;

	double a_max;
	double a_min;
	double a_a;
	double a_d;
	double a_sd;

	double Pc;
	double Pi;
	double Pd;
	double Pa;

	double P0_10;
	double P10_20;
	double P20_30;
	double P30_40;
	double P40_50;
	double P50_60;
	double P60_70;
	double P70;
	
	String flag;
	
	public long getT() {
		return T;
	}

	public void setT(long t) {
		T = t;
	}

	public double getS() {
		return S;
	}

	public void setS(double s) {
		S = s;
	}

	public long getT_a() {
		return t_a;
	}

	public void setT_a(long t_a) {
		this.t_a = t_a;
	}

	public long getT_d() {
		return t_d;
	}

	public void setT_d(long t_d) {
		this.t_d = t_d;
	}

	public long getT_i() {
		return t_i;
	}

	public void setT_i(long t_i) {
		this.t_i = t_i;
	}

	public long getT_c() {
		return t_c;
	}

	public void setT_c(long t_c) {
		this.t_c = t_c;
	}

	public double getV_mr() {
		return v_mr;
	}

	public void setV_mr(double v_mr) {
		this.v_mr = v_mr;
	}

	public double getV_m() {
		return v_m;
	}

	public void setV_m(double v_m) {
		this.v_m = v_m;
	}

	public double getV_max() {
		return v_max;
	}

	public void setV_max(double v_max) {
		this.v_max = v_max;
	}

	public double getV_sd() {
		return v_sd;
	}

	public void setV_sd(double v_sd) {
		this.v_sd = v_sd;
	}

	public double getA_max() {
		return a_max;
	}

	public void setA_max(double a_max) {
		this.a_max = a_max;
	}

	public double getA_min() {
		return a_min;
	}

	public void setA_min(double a_min) {
		this.a_min = a_min;
	}

	public double getA_a() {
		return a_a;
	}

	public void setA_a(double a_a) {
		this.a_a = a_a;
	}

	public double getA_d() {
		return a_d;
	}

	public void setA_d(double a_d) {
		this.a_d = a_d;
	}

	public double getA_sd() {
		return a_sd;
	}

	public void setA_sd(double a_sd) {
		this.a_sd = a_sd;
	}

	public double getPc() {
		return Pc;
	}

	public void setPc(double pc) {
		Pc = pc;
	}

	public double getPi() {
		return Pi;
	}

	public void setPi(double pi) {
		Pi = pi;
	}

	public double getPd() {
		return Pd;
	}

	public void setPd(double pd) {
		Pd = pd;
	}

	public double getPa() {
		return Pa;
	}

	public void setPa(double pa) {
		Pa = pa;
	}

	public double getP0_10() {
		return P0_10;
	}

	public void setP0_10(double p0_10) {
		P0_10 = p0_10;
	}

	public double getP10_20() {
		return P10_20;
	}

	public void setP10_20(double p10_20) {
		P10_20 = p10_20;
	}

	public double getP20_30() {
		return P20_30;
	}

	public void setP20_30(double p20_30) {
		P20_30 = p20_30;
	}

	public double getP30_40() {
		return P30_40;
	}

	public void setP30_40(double p30_40) {
		P30_40 = p30_40;
	}

	public double getP40_50() {
		return P40_50;
	}

	public void setP40_50(double p40_50) {
		P40_50 = p40_50;
	}

	public double getP50_60() {
		return P50_60;
	}

	public void setP50_60(double p50_60) {
		P50_60 = p50_60;
	}

	public double getP60_70() {
		return P60_70;
	}

	public void setP60_70(double p60_70) {
		P60_70 = p60_70;
	}

	public double getP70() {
		return P70;
	}

	public void setP70(double p70) {
		P70 = p70;
	}

	

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "SMA [T=" + T + ", S=" + S + ", t_a=" + t_a + ", t_d=" + t_d + ", t_i=" + t_i + ", t_c="
				+ t_c + ", v_mr=" + v_mr + ", v_m=" + v_m + ", v_max=" + v_max + ", v_sd=" + v_sd + ", a_max=" + a_max
				+ ", a_min=" + a_min + ", a_a=" + a_a + ", a_d=" + a_d + ", a_sd=" + a_sd + ", Pc=" + Pc + ", Pi=" + Pi
				+ ", Pd=" + Pd + ", Pa=" + Pa + ", P0_10=" + P0_10 + ", P10_20=" + P10_20 + ", P20_30=" + P20_30
				+ ", P30_40=" + P30_40 + ", P40_50=" + P40_50 + ", P50_60=" + P50_60 + ", P60_70=" + P60_70 + ", P70="
				+ P70 + "]";
	}

	public static double exchange(double d) {
		d = (double) Math.round(d * 1000) / 1000;
		return d;
	}

}