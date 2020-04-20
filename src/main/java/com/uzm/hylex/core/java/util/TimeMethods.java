package com.uzm.hylex.core.java.util;

import java.util.concurrent.TimeUnit;

public class TimeMethods {
	
	public static long generateTimeCurrent(TimeUnit unit,int multiplier) {
		return System.currentTimeMillis() + unit.toMillis(multiplier);
	}
	public static long generateTime(TimeUnit unit,int multiplier) {
		return unit.toMillis(multiplier);
	}
	public static long getTime(String[]args){
		int modifier=0;
		String arg=args[2].toLowerCase();
		
		if(arg.startsWith("horas"))
			modifier=3600;
		else if(arg.startsWith("minutos"))
			modifier=60;
		else if(arg.startsWith("segundos"))
			modifier=1;
		else if(arg.startsWith("semanas"))
			modifier=604800;
		else if(arg.startsWith("dias"))
			modifier=86400;
		else if(arg.startsWith("anos"))
			modifier=31449600;
		else if(arg.startsWith("meses"))
			modifier=2620800;
		double time;

			time=Double.parseDouble(args[1]);
		return (long)(modifier*time)*1000;
	}

	  public static String getTimeUntil(long epoch)
	  {
	    epoch -= System.currentTimeMillis();
	    return getTime(epoch);
	  }
	  
	    public static String getTime(long ms){
	    	ms=(long)Math.ceil(ms/1000.0);
	    	StringBuilder sb=new StringBuilder(40);
	    	
	    	if(ms/31449600>0){
	    		long years=ms/31449600;    		
	    		sb.append(years).append(years == 1 ? " ano " : " anos ");
	    		ms-=years * 31449600;
	    	}
	    	if(ms/2620800>0){
	    		long months=ms/2620800;
	    		sb.append(months).append(months == 1 ? " mês " : " meses ");
	    		ms-=months*2620800;
	    	}
	    	if(ms/604800>0){
	    		long weeks=ms/604800;
	    		sb.append(weeks).append(weeks == 1 ? " semana " : " semanas ");
	    		ms-=weeks*604800;
	    	}
	    	if(ms/86400>0){
	    		long days=ms/86400;
	    		sb.append(days).append(days == 1 ? " dia " : " dias ");
	    		ms-=days*86400;
	    	}
	    	if(ms/3600>0){
	    		long hours=ms/3600;
	    		sb.append(hours).append(hours == 1 ? " hora " : " horas ");
	    		ms-=hours*3600;
	    	}
	    	if(ms/60>0){
	    		long minutes=ms/60;
	    		sb.append(minutes).append(minutes == 1 ? " minuto " : " minutos ");
	    		ms-=minutes*60;
	    	}
	    	if(ms>0){
	    		sb.append(ms).append(ms == 1 ? " segundo " : " segundos ");
	    	}
	    	if(sb.length()>1){
	    		sb.replace(sb.length()-1, sb.length(), "");
	    	}
	    	else{
	    		sb = new StringBuilder("Acabado");
	    	}
	    	
	    	return sb.toString();
	    }

}
