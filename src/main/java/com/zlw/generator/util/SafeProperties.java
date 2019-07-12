package com.zlw.generator.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class SafeProperties extends java.util.Properties {
    private static final long serialVersionUID = 5011694856722313621L;
    private static final String keyValueSeparators = "=: \t\r\n\f";
    private static final String strictKeyValueSeparators = "=:";
    private static final String specialSaveChars = "=: \t\r\n\f#!";
    private static final String whiteSpaceChars = " \t\r\n\f";
    public static String DEFAULT_ENCODING = "utf-8";
    private SafeProperties.PropertiesContext context = new SafeProperties.PropertiesContext();
    private static final char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public SafeProperties() {
    }

    public SafeProperties.PropertiesContext getContext() {
        return this.context;
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, DEFAULT_ENCODING));

        while (true) {
            String line = in.readLine();
            String intactLine = line;
            if (line == null) {
                return;
            }

            if (line.length() > 0) {
                int len = line.length();

                int keyStart;
                for (keyStart = 0; keyStart < len && " \t\r\n\f".indexOf(line.charAt(keyStart)) != -1; ++keyStart) {
                    ;
                }

                if (keyStart != len) {
                    char firstChar = line.charAt(keyStart);
                    if (firstChar != 35 && firstChar != 33) {
                        while (this.continueLine(line)) {
                            String separatorIndex = in.readLine();
                            intactLine = intactLine + "\n" + separatorIndex;
                            if (separatorIndex == null) {
                                separatorIndex = "";
                            }

                            String valueIndex = line.substring(0, len - 1);

                            int key;
                            for (key = 0; key < separatorIndex.length() && " \t\r\n\f".indexOf(separatorIndex.charAt(key)) != -1; ++key) {
                                ;
                            }

                            separatorIndex = separatorIndex.substring(key, separatorIndex.length());
                            line = valueIndex + separatorIndex;
                            len = line.length();
                        }

                        int var13;
                        for (var13 = keyStart; var13 < len; ++var13) {
                            char var12 = line.charAt(var13);
                            if (var12 == 92) {
                                ++var13;
                            } else if ("=: \t\r\n\f".indexOf(var12) != -1) {
                                break;
                            }
                        }

                        int var14;
                        for (var14 = var13; var14 < len && " \t\r\n\f".indexOf(line.charAt(var14)) != -1; ++var14) {
                            ;
                        }

                        if (var14 < len && "=:".indexOf(line.charAt(var14)) != -1) {
                            ++var14;
                        }

                        while (var14 < len && " \t\r\n\f".indexOf(line.charAt(var14)) != -1) {
                            ++var14;
                        }

                        String var15 = line.substring(keyStart, var13);
                        String value = var13 < len ? line.substring(var14, len) : "";
                        var15 = this.loadConvert(var15);
                        value = this.loadConvert(value);
                        this.put(var15, value, intactLine);
                    } else {
                        this.context.addCommentLine(line);
                    }
                }
            } else {
                this.context.addCommentLine(line);
            }
        }
    }

    private String loadConvert(String theString) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);
        int x = 0;

        while (x < len) {
            char aChar = theString.charAt(x++);
            if (aChar == 92) {
                aChar = theString.charAt(x++);
                if (aChar == 117) {
                    int value = 0;

                    for (int i = 0; i < 4; ++i) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - 48;
                                break;
                            case ':':
                            case ';':
                            case '<':
                            case '=':
                            case '>':
                            case '?':
                            case '@':
                            case 'G':
                            case 'H':
                            case 'I':
                            case 'J':
                            case 'K':
                            case 'L':
                            case 'M':
                            case 'N':
                            case 'O':
                            case 'P':
                            case 'Q':
                            case 'R':
                            case 'S':
                            case 'T':
                            case 'U':
                            case 'V':
                            case 'W':
                            case 'X':
                            case 'Y':
                            case 'Z':
                            case '[':
                            case '\\':
                            case ']':
                            case '^':
                            case '_':
                            case '`':
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 65;
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 97;
                        }
                    }

                    outBuffer.append((char) value);
                } else if (aChar == 116) {
                    outBuffer.append('\t');
                } else if (aChar == 114) {
                    outBuffer.append('\r');
                } else if (aChar == 110) {
                    outBuffer.append('\n');
                } else if (aChar == 102) {
                    outBuffer.append('\f');
                } else {
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }

    @Override
    public synchronized void store(OutputStream out, String header) throws IOException {
        BufferedWriter awriter = new BufferedWriter(new OutputStreamWriter(out, DEFAULT_ENCODING));
        if (header != null) {
            writeln(awriter, "#" + header);
        }

        List entrys = this.context.getCommentOrEntrys();
        Iterator iter = entrys.iterator();

        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj.toString() != null) {
                writeln(awriter, obj.toString());
            }
        }

        awriter.flush();
    }

    private static void writeln(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    private boolean continueLine(String line) {
        int slashCount = 0;

        for (int index = line.length() - 1; index >= 0 && line.charAt(index--) == 92; ++slashCount) {
            ;
        }

        return slashCount % 2 == 1;
    }

    private String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);

        for (int x = 0; x < len; ++x) {
            char aChar = theString.charAt(x);
            switch (aChar) {
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case ' ':
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }

                    outBuffer.append(' ');
                    break;
                case '\\':
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;
                default:
                    if (aChar >= 32 && aChar <= 126) {
                        if ("=: \t\r\n\f#!".indexOf(aChar) != -1) {
                            outBuffer.append('\\');
                        }

                        outBuffer.append(aChar);
                    } else {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex(aChar >> 12 & 15));
                        outBuffer.append(toHex(aChar >> 8 & 15));
                        outBuffer.append(toHex(aChar >> 4 & 15));
                        outBuffer.append(toHex(aChar & 15));
                    }
            }
        }

        return outBuffer.toString();
    }

    private static char toHex(int nibble) {
        return hexDigit[nibble & 15];
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        this.context.putOrUpdate(key.toString(), value.toString());
        return super.put(key, value);
    }

    public synchronized Object put(Object key, Object value, String line) {
        this.context.putOrUpdate(key.toString(), value.toString(), line);
        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        this.context.remove(key.toString());
        return super.remove(key);
    }

    public void addComment(String comment) {
        if (comment != null) {
            this.context.addCommentLine("#" + comment);
        }

    }

    class PropertiesContext {
        private List commentOrEntrys = new ArrayList();

        PropertiesContext() {
        }

        public List getCommentOrEntrys() {
            return this.commentOrEntrys;
        }

        public void addCommentLine(String line) {
            this.commentOrEntrys.add(line);
        }

        public void putOrUpdate(SafeProperties.PropertiesContext.PropertyEntry pe) {
            this.remove(pe.getKey());
            this.commentOrEntrys.add(pe);
        }

        public void putOrUpdate(String key, String value, String line) {
            SafeProperties.PropertiesContext.PropertyEntry pe = new SafeProperties.PropertiesContext.PropertyEntry(key, value, line);
            this.remove(key);
            this.commentOrEntrys.add(pe);
        }

        public void putOrUpdate(String key, String value) {
            SafeProperties.PropertiesContext.PropertyEntry pe = new SafeProperties.PropertiesContext.PropertyEntry(key, value);
            this.remove(key);
            this.commentOrEntrys.add(pe);
        }

        public void remove(String key) {
            for (int i = 0; i < this.commentOrEntrys.size(); ++i) {
                Object obj = this.commentOrEntrys.get(i);
                if (obj instanceof SafeProperties.PropertiesContext.PropertyEntry && obj != null && key.equals(((SafeProperties.PropertiesContext.PropertyEntry) obj).getKey())) {
                    this.commentOrEntrys.remove(obj);
                }
            }

        }

        class PropertyEntry {
            private String key;
            private String value;
            private String line;

            public String getLine() {
                return this.line;
            }

            public void setLine(String line) {
                this.line = line;
            }

            public PropertyEntry(String key, String value) {
                this.key = key;
                this.value = value;
            }

            public PropertyEntry(String key, String value, String line) {
                this(key, value);
                this.line = line;
            }

            public String getKey() {
                return this.key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return this.value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                if (this.line != null) {
                    return this.line;
                } else if (this.key != null && this.value != null) {
                    String k = SafeProperties.this.saveConvert(this.key, true);
                    String v = SafeProperties.this.saveConvert(this.value, false);
                    return k + "=" + v;
                } else {
                    return null;
                }
            }
        }
    }
}
