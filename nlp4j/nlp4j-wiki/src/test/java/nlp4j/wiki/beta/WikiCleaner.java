package nlp4j.wiki.beta;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WikiCleaner {
	public static String toPlainText(String wiki) {
        if (wiki == null || wiki.isEmpty()) {
            return wiki;
        }

        String text = wiki;

        // 先に Infobox を除去
        text = InfoboxParser.removeFirstInfobox(text);

        text = removeComments(text);
        text = removeRefTags(text);
        text = removeFileAndCategoryLinks(text);
        text = removeTables(text);
        text = replaceExternalLinks(text);
        text = replaceTemplatesWithHeuristics(text);
        text = replaceInternalLinks(text);
        text = removeHtmlTags(text);
        text = removeEmphasis(text);
        text = decodeBasicHtmlEntities(text);
        text = normalizeWhitespace(text);

        return text.trim();
    }

    public static String toPlainTextForInfoboxValue(String wiki) {
        if (wiki == null || wiki.isEmpty()) {
            return wiki;
        }

        String text = wiki;

        text = removeComments(text);
        text = removeRefTags(text);
        text = replaceTemplatesWithHeuristics(text);
        text = removeFileAndCategoryLinks(text);
        text = replaceExternalLinks(text);
        text = replaceInternalLinks(text);
        text = removeHtmlTags(text);
        text = removeEmphasis(text);
        text = decodeBasicHtmlEntities(text);
        text = normalizeWhitespace(text);

        return text.trim();
    }

    public static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (ParserUtil.startsWith(s, i, "<!--")) {
                int end = s.indexOf("-->", i + 4);
                if (end < 0) {
                    break;
                }
                i = end + 3;
            } else {
                out.append(s.charAt(i));
                i++;
            }
        }
        return out.toString();
    }

    public static String removeRefTags(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (ParserUtil.startsWithIgnoreCase(s, i, "<ref")) {
                int gt = s.indexOf('>', i);
                if (gt < 0) {
                    break;
                }

                // <ref ... />
                if (gt > i && s.charAt(gt - 1) == '/') {
                    i = gt + 1;
                    continue;
                }

                int end = ParserUtil.indexOfIgnoreCase(s, "</ref>", gt + 1);
                if (end < 0) {
                    i = gt + 1;
                } else {
                    i = end + 6;
                }
            } else {
                out.append(s.charAt(i));
                i++;
            }
        }
        return out.toString();
    }

    public static String removeFileAndCategoryLinks(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (ParserUtil.startsWith(s, i, "[[")) {
                int end = ParserUtil.findMatchingDoubleBracket(s, i);
                if (end < 0) {
                    out.append(s.charAt(i));
                    i++;
                    continue;
                }

                String content = s.substring(i + 2, end);
                String lower = content.toLowerCase();

                if (lower.startsWith("ファイル:")
                        || lower.startsWith("file:")
                        || lower.startsWith("image:")
                        || lower.startsWith("category:")
                        || lower.startsWith("カテゴリ:")) {
                    i = end + 2;
                    continue;
                } else {
                    out.append(s, i, end + 2);
                    i = end + 2;
                    continue;
                }
            }

            out.append(s.charAt(i));
            i++;
        }
        return out.toString();
    }

    public static String removeTables(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        int len = s.length();
        while (i < len) {
            if (ParserUtil.startsWith(s, i, "{|")) {
                int end = ParserUtil.findMatchingTableEnd(s, i + 2);
                if (end < 0) {
                    break;
                }
                i = end + 2;
            } else {
                out.append(s.charAt(i));
                i++;
            }
        }
        return out.toString();
    }

    public static String replaceExternalLinks(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '[' && !ParserUtil.startsWith(s, i, "[[")) {
                int end = s.indexOf(']', i + 1);
                if (end < 0) {
                    out.append(s.charAt(i));
                    i++;
                    continue;
                }

                String inside = s.substring(i + 1, end).trim();
                if (inside.startsWith("http://") || inside.startsWith("https://")) {
                    int space = inside.indexOf(' ');
                    if (space >= 0 && space + 1 < inside.length()) {
                        out.append(inside.substring(space + 1).trim());
                    }
                    i = end + 1;
                    continue;
                }
            }
            out.append(s.charAt(i));
            i++;
        }
        return out.toString();
    }

    public static String replaceInternalLinks(String s) {
        String prev;
        String cur = s;
        do {
            prev = cur;
            cur = replaceInternalLinksOnePass(cur);
        } while (!cur.equals(prev));
        return cur;
    }

    private static String replaceInternalLinksOnePass(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (ParserUtil.startsWith(s, i, "[[")) {
                int end = ParserUtil.findMatchingDoubleBracket(s, i);
                if (end < 0) {
                    out.append(s.charAt(i));
                    i++;
                    continue;
                }

                String content = s.substring(i + 2, end);
                out.append(extractDisplayTextFromInternalLink(content));
                i = end + 2;
            } else {
                out.append(s.charAt(i));
                i++;
            }
        }
        return out.toString();
    }

    private static String extractDisplayTextFromInternalLink(String content) {
        List<String> parts = ParserUtil.splitTopLevel(content, '|');
        if (parts.isEmpty()) {
            return "";
        }

        String target = parts.get(0).trim();
        String lower = target.toLowerCase();

        if (lower.startsWith("ファイル:")
                || lower.startsWith("file:")
                || lower.startsWith("image:")
                || lower.startsWith("category:")
                || lower.startsWith("カテゴリ:")) {
            return "";
        }

        String display = parts.size() == 1 ? target : parts.get(parts.size() - 1).trim();

        int hash = display.indexOf('#');
        if (parts.size() == 1 && hash >= 0 && hash + 1 < display.length()) {
            display = display.substring(hash + 1);
        }

        return display;
    }

    public static String replaceTemplatesWithHeuristics(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (ParserUtil.startsWith(s, i, "{{")) {
                int end = ParserUtil.findMatchingDoubleBrace(s, i);
                if (end < 0) {
                    break;
                }

                String tpl = s.substring(i + 2, end);
                String replacement = renderTemplateToText(tpl);
                out.append(replacement);
                i = end + 2;
            } else {
                out.append(s.charAt(i));
                i++;
            }
        }
        return out.toString();
    }

    private static String renderTemplateToText(String templateBody) {
        List<String> parts = ParserUtil.splitTopLevel(templateBody, '|');
        if (parts.isEmpty()) {
            return "";
        }

        String name = parts.get(0).trim().toLowerCase();

        if (name.equals("lang") || name.equals("lang-en") || name.equals("仮リンク")) {
            if (parts.size() >= 2) {
                return toPlainTextForInfoboxValue(parts.get(parts.size() - 1));
            }
        }

        if (name.equals("nowrap")) {
            if (parts.size() >= 2) {
                return toPlainTextForInfoboxValue(parts.get(1));
            }
        }

        if (name.equals("start date") || name.equals("start date and age")) {
            if (parts.size() >= 4) {
                return parts.get(1).trim() + "-" + parts.get(2).trim() + "-" + parts.get(3).trim();
            }
        }

        if (name.equals("convert")) {
            if (parts.size() >= 3) {
                return parts.get(1).trim() + " " + parts.get(2).trim();
            }
        }

        List<String> positional = new ArrayList<>();
        for (int i = 1; i < parts.size(); i++) {
            String p = parts.get(i).trim();
            int eq = ParserUtil.indexOfTopLevel(p, '=');
            if (eq < 0) {
                positional.add(toPlainTextForInfoboxValue(p));
            }
        }

        if (!positional.isEmpty()) {
            return String.join(" ", positional).trim();
        }

        return "";
    }

    public static String removeHtmlTags(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '<') {
                int end = s.indexOf('>', i + 1);
                if (end < 0) {
                    out.append(s.charAt(i));
                    i++;
                } else {
                    i = end + 1;
                }
            } else {
                out.append(s.charAt(i));
                i++;
            }
        }
        return out.toString();
    }

    public static String removeEmphasis(String s) {
        return s.replace("'''", "").replace("''", "");
    }

    public static String decodeBasicHtmlEntities(String s) {
        return s.replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");
    }

    public static String normalizeWhitespace(String s) {
        String text = s.replace("\r\n", "\n").replace('\r', '\n');
        text = text.replaceAll("[ \\t\\x0B\\f]+", " ");
        text = text.replaceAll(" *\\n *", "\n");
        text = text.replaceAll("\\n{3,}", "\n\n");
        return text.trim();
    }
}
