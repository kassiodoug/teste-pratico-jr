package utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormataNumeroBr {
  public static String format(BigDecimal valor) {

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
    symbols.setDecimalSeparator(',');
    symbols.setGroupingSeparator('.');
    DecimalFormat df = new DecimalFormat("###,###,##0.00", symbols);

    return df.format(valor);
  }
}
