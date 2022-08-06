package luxo.core;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
Format  Data Type                                       Output
    %a	floating point (except BigDecimal)              Returns Hex output of floating point number.
    %b	Any type                                        "true" if non-null, "false" if null
    %c	character                                       Unicode character
    %d	integer (incl. byte, short, int, long, bigint)	Decimal Integer
    %e	floating point                                  decimal number in scientific notation
    %f	floating point                                  decimal number
    %g	floating point                                  decimal number, possibly in scientific notation depending on the precision and value.
    %h	any type                                        Hex String of value from hashCode() method.
    %n	none                                            Platform-specific line separator.
    %o	integer (incl. byte, short, int, long, bigint)	Octal number
    %s	any type                                        String value
    %t	Date/Time (incl. long, Calendar, Date and TemporalAccessor)	%t is the prefix for Date/Time conversions. More formatting flags are needed after this. See Date/Time conversion below.
    %x	integer (incl. byte, short, int, long, bigint)	Hex string.
*/

public class Log {
    
    private static final String CORE = "LUXO";
    private static final String CLIENT = "APP";
    
    /* Colors */
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";    
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    private static String format(String name, String txt, Object... args) {   
        return "[" + DATE_FORMAT.format(new Date()) + "] " + name + ": " + String.format(txt, args); 
    }
    
    /* Core Log Methods */
    public static void coreTrace(String txt, Object... args) {
        System.out.println(format(CORE, txt, args));
    }
    
    public static void coreInfo(String txt, Object... args) {
        System.out.println(GREEN + format(CORE, txt, args) + RESET);
    }
    
    public static void coreWarn(String txt, Object... args) {
        System.out.println(YELLOW + format(CORE, txt, args) + RESET);
    }
    
    public static void coreError(String txt, Object... args) {
        System.out.println(RED + format(CORE, txt, args) + RESET);
        System.exit(1);
    }
    public static void coreFatal(String txt, Object... args) {
        System.out.println(RED + format(CORE, txt, args) + RESET);
        System.exit(1);
    }
    
    public static void coreTrace(Object obj, Object... args) {
        System.out.println(format(CORE, obj.toString(), args));
    }
    
    public static void coreInfo(Object obj, Object... args) {
        System.out.println(GREEN + format(CORE, obj.toString(), args) + RESET);
    }
    
    public static void coreWarn(Object obj, Object... args) {
        System.out.println(YELLOW + format(CORE, obj.toString(), args) + RESET);
    }
    
    public static void coreError(Object obj, Object... args) {
        System.out.println(RED + format(CORE, obj.toString(), args) + RESET);
        System.exit(1);
    }
    public static void coreFatal(Object obj, Object... args) {
        System.out.println(RED + format(CORE, obj.toString(), args) + RESET);
        System.exit(1);
    }
    
    public static void coreAssert(boolean condition, String errorMsg) {
        if(!condition) coreError(errorMsg);
    }
    
    /* Client Log Methods */
    public static void trace(String txt, Object... args) {
        System.out.println(format(CLIENT, txt, args));
    }
    
    public static void info(String txt, Object... args) {
        System.out.println(GREEN + format(CLIENT, txt, args) + RESET);
    }
    
    public static void warn(String txt, Object... args) {
        System.out.println(YELLOW + format(CLIENT, txt, args) + RESET);
    }
    
    public static void error(String txt, Object... args) {
        System.out.println(RED + format(CLIENT, txt, args) + RESET);
        System.exit(1);
    }
    public static void fatal(String txt, Object... args) {
        System.out.println(RED + format(CLIENT, txt, args) + RESET);
        System.exit(1);
    }
    
    public static void trace(Object obj, Object... args) {
        System.out.println(format(CLIENT, obj.toString(), args));
    }
    
    public static void info(Object obj, Object... args) {
        System.out.println(GREEN + format(CLIENT, obj.toString(), args) + RESET);
    }
    
    public static void warn(Object obj, Object... args) {
        System.out.println(YELLOW + format(CLIENT, obj.toString(), args) + RESET);
    }
    
    public static void error(Object obj, Object... args) {
        System.out.println(RED + format(CLIENT, obj.toString(), args) + RESET);
        System.exit(1);
    }
    public static void fatal(Object obj, Object... args) {
        System.out.println(RED + format(CLIENT, obj.toString(), args) + RESET);
        System.exit(1);
    }
    
    public static void Assert(boolean condition, String errorMsg) {
        if(!condition) error(errorMsg);
    }
}
