package ca.weindex.common.util;

import java.util.regex.*;

public class Ubb2html
{
  public static String decode(String argString)
  {
    String tString = argString;
    if (!tString.equals(""))
    {
      Boolean tState = true;
      tString = tString.replace("&", "&amp;");
      tString = tString.replace(">", "&gt;");
      tString = tString.replace("<", "&lt;");
      tString = tString.replace("\"", "&quot;");
      tString = tString.replace("&amp;#91;", "&#91;");
      tString = tString.replace("&amp;#93;", "&#93;");
      tString = tString.replaceAll("\\[br\\]", "<br />");
      String[][] tRegexAry = {
        {"\\[p\\]([^\\[]*?)\\[\\/p\\]", "$1<br />"},
        {"\\[b\\]([^\\[]*?)\\[\\/b\\]", "<b>$1</b>"},
        {"\\[i\\]([^\\[]*?)\\[\\/i\\]", "<i>$1</i>"},
        {"\\[u\\]([^\\[]*?)\\[\\/u\\]", "<u>$1</u>"},
        {"\\[ol\\]([^\\[]*?)\\[\\/ol\\]", "<ol>$1</ol>"},
        {"\\[ul\\]([^\\[]*?)\\[\\/ul\\]", "<ul>$1</ul>"},
        {"\\[li\\]([^\\[]*?)\\[\\/li\\]", "<li>$1</li>"},
        {"\\[code\\]([^\\[]*?)\\[\\/code\\]", "<div class=\"ubb_code\">$1</div>"},
        {"\\[quote\\]([^\\[]*?)\\[\\/quote\\]", "<div class=\"ubb_quote\">$1</div>"},
        {"\\[color=([^\\]]*)\\]([^\\[]*?)\\[\\/color\\]", "<font style=\"color: $1\">$2</font>"},
        {"\\[hilitecolor=([^\\]]*)\\]([^\\[]*?)\\[\\/hilitecolor\\]", "<font style=\"background-color: $1\">$2</font>"},
        {"\\[align=([^\\]]*)\\]([^\\[]*?)\\[\\/align\\]", "<div style=\"text-align: $1\">$2</div>"},
        {"\\[url=([^\\]]*)\\]([^\\[]*?)\\[\\/url\\]", "<a href=\"$1\" target=\"_blank\">$2</a>"},
        //{"\\[img\\]([^\\[]*?)\\[\\/img\\]", "<a href=\"$1\" target=\"_blank\"><img src=\"$1\" onload=\"cls.img.tResize(this, 600, 1800);\" /></a>"}
        {"\\[img\\]([^\\[]*?)\\[\\/img\\]", "<img src=\"$1\" onload=\"cls.img.tResize(this, 600, 1800);\" />"}
      };
      while (tState)
      {
        tState = false;
        for (int ti = 0; ti < tRegexAry.length; ti ++)
        {
          String tvalue1, tvalue2;
          Pattern tPattern = Pattern.compile(tRegexAry[ti][0]);
          Matcher tMatcher = tPattern.matcher(tString);
          while (tMatcher.find())
          {
            tState = true;
            tvalue1 = tMatcher.group();
            tvalue2 = tRegexAry[ti][1];
            for (int tk = 1; tk < (tMatcher.groupCount() + 1); tk ++) tvalue2 = tvalue2.replace("$" + tk, tMatcher.group(tk));
            tString = tString.replace(tvalue1, tvalue2);
          }
        }
      }
    }
    return tString;
  }
}
