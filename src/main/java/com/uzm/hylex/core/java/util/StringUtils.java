package com.uzm.hylex.core.java.util;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe com utilitários relacionado a {@link String}.
 *
 * @author Maxter
 */
public class StringUtils {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

  private static final int MAX_LENGTH = 4;

  private static final String[] suffix = new String[] {"", "k", "m", "b", "t"};

  /**
   * Formata um número "#,###" através do {@link DecimalFormat}
   *
   * @param number Para formatar.
   * @return O número formatado.
   */
  public static String formatNumber(int number) {
    return DECIMAL_FORMAT.format(number);
  }

  /**
   * Formata um número "#,###" através do {@link DecimalFormat}
   *
   * @param number Para formatar.
   * @return O número formatado.
   */
  public static String formatNumber(long number) {
    return DECIMAL_FORMAT.format(number);
  }

  /**
   * Formata um número "#,###" através do {@link DecimalFormat}
   *
   * @param number Para formatar.
   * @return O número formatado.
   */
  public static String formatNumber(double number) {
    return DECIMAL_FORMAT.format(number);
  }

  private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(§)[0-9A-FK-OR]");

  /**
   * Remove todas as cores de uma String.<br/>
   * Color code: §
   *
   * @param input A string para remover as cores.
   * @return A string sem cores.
   */
  public static String stripColors(final String input) {
    if (input == null) {
      return null;
    }

    return COLOR_PATTERN.matcher(input).replaceAll("");
  }

  /**
   * Formata os {@code &} para o color code {@code §}.
   *
   * @param textToFormat A string para formatar as cores.
   * @return A string com as cores formatadas.
   */
  public static String formatColors(String textToFormat) {
    return translateAlternateColorCodes('&', textToFormat);
  }

  /**
   * Desformata o color code {@code §} para {@code &}.
   *
   * @param textToDeFormat A string para desformatar as cores.
   * @return A string com as cores desformatadas.
   */
  public static String deformatColors(String textToDeFormat) {
    Matcher matcher = COLOR_PATTERN.matcher(textToDeFormat);
    while (matcher.find()) {
      String color = matcher.group();
      textToDeFormat = textToDeFormat.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("&" + color.substring(1)));
    }

    return textToDeFormat;
  }

  public static String removeNumbers(String string) {
    return string.replaceAll("[0-9]", "");
  }


  /**
   * Formata os {@link altColorChar} para o color code {@code §}.
   *
   * @param altColorChar    O caractere que é definido como color code.
   * @param textToTranslate A string para formatar as cores.
   * @return A string com as cores formatadas.
   */


  public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
    Pattern pattern = Pattern.compile("(?i)(" + String.valueOf(altColorChar) + ")[0-9A-FK-OR]");

    Matcher matcher = pattern.matcher(textToTranslate);
    while (matcher.find()) {
      String color = matcher.group();
      textToTranslate = textToTranslate.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("§" + color.substring(1)));
    }

    return textToTranslate;
  }

  public static String center(String s, int size) {
    return center(s, size, ' ');
  }

  public static String center(String s, int size, char pad) {
    if (s == null || size <= s.length())
      return s;

    StringBuilder sb = new StringBuilder(size);
    for (int i = 0; i < (size - s.length()) / 2; i++) {
      sb.append(pad);
    }
    sb.append(s);
    while (sb.length() < size) {
      sb.append(pad);
    }
    return sb.toString();
  }

  public static String progressDataBar(float current, float max, int amplifier) {
    String c = "■";
    StringBuilder prog = new StringBuilder("&8[");
    float percentageDone = (current / max) * 100;
    float percentageRemaining = 100 - percentageDone;

    float quantitySquare1 = (percentageDone * amplifier) / 100;
    float quantitySquare2 = (percentageRemaining * amplifier) / 100;
    if (Math.round(quantitySquare1) >= 1) {
      prog.append("&b");
    }
    while (Math.round(quantitySquare1) > 0) {
      quantitySquare1--;
      prog.append(c);
    }
    if (Math.round(quantitySquare1) == 0 && Math.round(quantitySquare2) >= 1) {
      prog.append("&7");
    }
    while (Math.round(quantitySquare2) > 0) {
      quantitySquare2--;
      prog.append(c);
    }
    prog.append("&8]");

    return prog.toString();
  }

  public static String formatNumberUS(double number) {
    String r = new DecimalFormat("##0E0").format(number);
    r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
    while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
      r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
    }
    return r;
  }

  /**
   * Pega a primeira cor de uma {@code String}.
   *
   * @param input A string para pegar a cor.
   * @return A primeira cor ou {@code ""(vazio)} caso não encontre nenhuma.
   */
  public static String getFirstColor(String input) {
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String first = "";
    if (matcher.find()) {
      first = matcher.group();
    }

    return first;
  }

  /**
   * Pega a última cor de uma {@code String}.
   *
   * @param input A string para pegar a cor.
   * @return A última cor ou {@code ""(vazio)} caso não encontre nenhuma.
   */
  public static String getLastColor(String input) {
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String last = "";
    while (matcher.find()) {
      last = matcher.group();
    }

    return last;
  }

  /**
   * Repete uma String várias vezes.
   *
   * @param repeat A string para repetir.
   * @param amount A quantidade de vezes que será repetida.
   * @return Resultado da repetição.
   */
  public static String repeat(String repeat, int amount) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      sb.append(repeat);
    }

    return sb.toString();
  }

  /**
   * Junta uma Array em uma {@code String} utilizando um separador.
   *
   * @param array     A array para juntar.
   * @param index     O ínicio da iteração da array (0 = toda a array).
   * @param separator O separador da junção.
   * @return Resultado da junção.
   */
  public static <T> String join(T[] array, int index, String separator) {
    StringBuilder joined = new StringBuilder();
    for (int slot = index; slot < array.length; slot++) {
      joined.append(array[slot].toString() + (slot + 1 == array.length ? "" : separator));
    }

    return joined.toString();
  }

  /**
   * Junta uma Array em uma {@code String} utilizando um separador.
   *
   * @param array     A array para juntar.
   * @param separator O separador da junção.
   * @return Resultado da junção.
   */
  public static <T> String join(T[] array, String separator) {
    return join(array, 0, separator);
  }

  /**
   * Junta uma Coleção em uma {@code String} utilizando um separador.
   *
   * @param collection A coleção para juntar.
   * @param separator  O separador da junção.
   * @return Resultado da junção.
   */
  public static <T> String join(Collection<T> collection, String separator) {
    return join(collection.toArray(new Object[collection.size()]), separator);
  }

  /**
   * Quebra uma {@code String} várias vezes para criar linhas com o tamanho máximo definido.<br/>
   * <b>Observação:</b> Esse método é uma variação do {@link StringUtils#split(String, int, boolean)}
   * com o parâmetro {@code ignoreCompleteWords} definido como {@code false}.
   *
   * @param toSplit String para quebrar.
   * @param length  Tamanho máximo das linhas.
   * @return Resultado da separação.
   */
  public static String[] split(String toSplit, int length) {
    return split(toSplit, length, false);
  }

  /**
   * "Capitaliza" uma String Exemplo: MAXTER se torna Maxter
   *
   * @param toCapitalise String para capitalizar
   * @return Resultado capitalizado.
   */
  public static String capitalise(String toCapitalise) {
    StringBuilder result = new StringBuilder();

    String[] splittedString = toCapitalise.split(" ");
    for (int index = 0; index < splittedString.length; index++) {
      String append = splittedString[index];
      result.append(append.substring(0, 1).toUpperCase() + append.substring(1).toLowerCase() + (index + 1 == splittedString.length ? "" : " "));
    }

    return result.toString();
  }


  /**
   * Quebra uma {@code String} várias vezes para criar linhas com o tamanho máximo definido.
   *
   * @param toSplit               String para quebrar.
   * @param length                Tamanho máximo das linhas.
   * @param ignoreIncompleteWords Se irá ignorar a quebra de palavras ou não (caso esteja desativado e
   *                              for quebrar uma palavra, ela irá ser removida da linha atual e adicionar na próxima).
   * @return Resultado da separação.
   */
  public static String[] split(String toSplit, int length, boolean ignoreIncompleteWords) {
    StringBuilder result = new StringBuilder(), current = new StringBuilder();

    char[] arr = toSplit.toCharArray();
    for (int charId = 0; charId < arr.length; charId++) {
      char character = arr[charId];
      if (current.length() == length) {
        if (!ignoreIncompleteWords) {
          List<Character> removedChars = new ArrayList<>();
          for (int l = current.length() - 1; l > 0; l--) {
            if (current.charAt(l) == ' ') {
              current.deleteCharAt(l);
              result.append(current.toString() + "\n");
              Collections.reverse(removedChars);
              current = new StringBuilder(join(removedChars, ""));
              break;
            }

            removedChars.add(current.charAt(l));
            current.deleteCharAt(l);
          }

          removedChars.clear();
          removedChars = null;
        } else {
          result.append(current.toString() + "\n");
          current = new StringBuilder();
        }
      }

      current.append(current.length() == 0 && character == ' ' ? "" : character);
      if (charId + 1 == arr.length) {
        result.append(current.toString() + "\n");
      }
    }

    return result.toString().split("\n");
  }



  private static String[] DAY_OF_WEEK = new String[] {"domingo","segunda-feira", "terça-feira", "quarta-feira", "quinta-feira", "sexta-feira", "sábado"};

  private static String[] MONTHS = new String[] {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};

  public static String formatDateBR(long ms) {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date(ms));
    String hour = String.valueOf((c.get(Calendar.HOUR_OF_DAY) > 12 ? (c.get(Calendar.HOUR_OF_DAY) - 12) : c.get(Calendar.HOUR_OF_DAY)));
    String minute = (c.get(Calendar.HOUR_OF_DAY) > 12 ? c.get(Calendar.MINUTE) + " da tarde" : c.get(Calendar.MINUTE) + " da manhã");
    String day_of_week = DAY_OF_WEEK[c.get(Calendar.DAY_OF_WEEK) - 1 ];
    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
    String month = MONTHS[c.get(Calendar.MONTH)];
    String year = String.valueOf(c.get(Calendar.YEAR));

    return day_of_week + ", " + day + " de " + month + " de " + year + " às " + (hour.length() == 1 ? "0" + hour : hour) + ":" + (c.get(Calendar.MINUTE) < 10 ?
      "0" + minute :
      minute);
  }
}
