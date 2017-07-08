/*
 * $Id$
 *
 * Copyright 2014 Valentyn Kolesnikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.declinationofnames;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Declinates of Russian names and surnames.
 *
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public final class RussianNameProcessor {
    public static final String SEX_M = "m";
    public static final String SEX_F = "f";
    // именительный
    public static final String CASE_NOM = "nom";
    // родительный
    public static final String CASE_GEN = "gen";
    // дательный
    public static final String CASE_DAT = "dat";
    // винительный
    public static final String CASE_ACC = "acc";
    // творительный
    public static final String CASE_INS = "ins";
    // предложный
    public static final String CASE_PRE = "pre";

    private RussianNameProcessor() {
    }

    static Map<String, Map<String, List<Object>>> rules = new HashMap<String,
            Map<String, List<Object>>>() {{
        put("lastName", new HashMap<String, List<Object>>() {{
            put("exceptions", new ArrayList<Object>() {{
                add("	дюма,тома,дега,люка,ферма,гамарра,петипа,шандра . . . . .");
                add("	гусь,ремень,камень,онук,богода,нечипас,долгопалец,маненок,рева,кива . . . . .");
                add("	вий,сой,цой,хой -я -ю -я -ем -е");
            }});
            put("suffixes", new ArrayList<Object>() {{
                add("f	б,в,г,д,ж,з,й,к,л,м,н,п,р,с,т,ф,х,ц,ч,ш,щ,ъ,ь . . . . .");
                add("f	ска,цка  -ой -ой -ую -ой -ой");
                add("f	щая,шая,чая       --ей --ей --ую --ей --ей");
                add("f	ая       --ой --ой --ую --ой --ой");
                add("	ская     --ой --ой --ую --ой --ой");
                add("f	на       -ой -ой -у -ой -ой");

                add("	иной -я -ю -я -ем -е");
                add("	уй   -я -ю -я -ем -е");
                add("	ца   -ы -е -у -ей -е");

                add("	рих  а у а ом е");

                add("	ия                      . . . . .");
                add("	иа,аа,оа,уа,ыа,еа,юа,эа . . . . .");
                add("	их,ых                   . . . . .");
                add("	о,е,э,и,ы,у,ю           . . . . .");

                add("	ова,ева            -ой -ой -у -ой -ой");
                add("	га,ка,ха,ча,ща,жа  -и -е -у -ой -е");
                add("	ца  -и -е -у -ей -е");
                add("	а   -ы -е -у -ой -е");

                add("	ь   -я -ю -я -ем -е");

                add("	ия  -и -и -ю -ей -и");
                add("	я   -и -е -ю -ей -е");
                add("	ей  -я -ю -я -ем -е");

                add("	ян,ан,йн   а у а ом е");

                add("	ынец,обец  --ца --цу --ца --цем --це");
                add("	онец,овец  --ца --цу --ца --цом --це");

                add("	ц,ч,ш,щ   а у а ем е");

                add("	ай  -я -ю -я -ем -е");
                add("	гой,кой  -го -му -го --им -м");
                add("	ой  -го -му -го --ым -м");
                add("	ах,ив   а у а ом е");

                add("	ший,щий,жий,ний  --его --ему --его -м --ем");
                add("	кий,ый   --ого --ому --ого -м --ом");
                add("	ий       -я -ю -я -ем -и");

                add("	ок  --ка --ку --ка --ком --ке");
                add("	ец  --ца --цу --ца --цом --це");

                add("	в,н   а у а ым е");
                add("	б,г,д,ж,з,к,л,м,п,р,с,т,ф,х   а у а ом е");
            }});
        }});
        put("firstName", new HashMap<String, List<Object>>() {{
            put("exceptions", new ArrayList<Object>() {{
                add("	^лев    --ьва --ьву --ьва --ьвом --ьве");
                add("	^павел  --ла  --лу  --ла  --лом  --ле");
                add("m	^шота   . . . . .");
                add("m	^пётр   ---етра ---етру ---етра ---етром ---етре");
                add("f	[оёеэую]ль   . . . . .");
            }});
            put("suffixes", new ArrayList<Object>() {{
                add("	е,ё,и,о,у,ы,э,ю   . . . . .");
                add("f	б,в,г,д,ж,з,й,к,л,м,н,п,р,с,т,ф,х,ц,ч,ш,щ,ъ   . . . . .");

                add("f	ь   -и -и . ю -и");
                add("m	ь   -я -ю -я -ем -е");

                add("	га,ка,ха,ча,ща,жа  -и -е -у -ой -е");
                add("	а   -ы -е -у -ой -е");
                add("	ия  -и -и -ю -ей -и");
                add("	я   -и -е -ю -ей -е");
                add("	ей  -я -ю -я -ем -е");
                add("	ий  -я -ю -я -ем -и");
                add("	й   -я -ю -я -ем -е");
                add("	б,в,г,д,ж,з,к,л,м,н,п,р,с,т,ф,х,ц,ч	 а у а ом е");
            }});
        }});
        put("middleName", new HashMap<String, List<Object>>() {{
            put("suffixes", new ArrayList<Object>() {{
                add("	ич   а  у  а  ем  е");
                add("	на  -ы -е -у -ой -е");
            }});
        }});
    }};

    static {
        prepareRules();
    }

    private static void prepareRules() {
        for (Map.Entry<String, Map<String, List<Object>>> type : rules.entrySet()) {
            for(Map.Entry<String, List<Object>> key : rules.get(type.getKey()).entrySet()) {
                for(int i = 0, n = rules.get(type.getKey()).get(key.getKey()).size(); i < n; i++) {
                    rules.get(type.getKey()).get(key.getKey()).set(i, rule(rules.get(type.getKey()).get(key.getKey()).get(i)));
                }
            }
        }
    }

    private static Map<String, List<String>> rule(Object rule) {
        final Matcher matcher = Pattern.compile(
                "^\\s*([fm]?)\\s*(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*$").matcher(rule.toString());
        if (matcher.matches()) {
            return new HashMap<String, List<String>>() {{
                put("sex", Arrays.asList(matcher.group(1)));
                put("test", Arrays.asList(matcher.group(2).split(",")));
                put("mods", Arrays.asList(matcher.group(3), matcher.group(4),
                        matcher.group(5), matcher.group(6), matcher.group(7)));
            }};
        }
        return Collections.emptyMap();
    }

    // склоняем слово по указанному набору правил и исключений
    public static String word(String word, String sex, String wordType, String gcase) {
        RussianCase russianCase = RussianCase.valueOf(RussianCase.class, gcase.toUpperCase());
        return word(word, sex, wordType, russianCase);
    }

    // склоняем слово по указанному набору правил и исключений
    public static String word(String word, String sex, String wordType, RussianCase wordCase) {
        if(word == null)
            return "";

        if (wordCase == RussianCase.NOM) {
            return word;
        }

        // составные слова
        if (word.contains("-")) {
            String[] list = word.split("-");
            return Arrays.stream(list)
                    .map(w -> word(w, sex, wordType, wordCase))
                    .reduce((a,b) -> a + "-" + b)
                    .get();
        }

        // Иванов И. И.
        if (Pattern.compile("^[А-ЯЁ]\\.?$", Pattern.CASE_INSENSITIVE).matcher(word).matches()) {
            return word;
        }

        Map<String, List<Object>> localRules = rules.get(wordType);

        if (localRules.get("exceptions") != null) {
            String pick = pick(word, sex, wordCase, localRules.get("exceptions"));
            if (pick != null) {
                return pick;
            }
        }
        String pick = pick(word, sex, wordCase, localRules.get("suffixes"));
        return pick != null ? pick : word;
    }

    // выбираем из списка правил первое подходящее и применяем
    private static String pick(String word, String sex, RussianCase gcase, List<Object> rules) {
        String wordLower = word == null ? "" : word.toLowerCase();
        for (Object rule : rules) {
            if (ruleMatch(wordLower, sex, rule)) {
                return applyMod(word, gcase, rule);
            }
        }
        return null;
    }

    // проверяем, подходит ли правило к слову
    // checks that given rule matches given word ending
    private static boolean ruleMatch(String word, String sex, final Object rule) {
        final Map<String, List<String>> localRule = (Map<String, List<String>>) rule;
        String ruleSex = localRule.get("sex").get(0).trim();
        if (!ruleSex.isEmpty() && !ruleSex.equals(sex)) {
            return false;
        }

        List<String> tests = localRule.get("test");
        for (String test : tests) {
            if (word.matches(".*" + test)) {
                return true;
            }
        }
        return false;
    }

    // склоняем слово (правим окончание)
    private static String applyMod(final String word, RussianCase wordCase, final Object rule) {
        final Map<String, List<String>> localRule = (Map<String, List<String>>) rule;
        final String mod;

        int ord = wordCase.ordinal() - 1;
        if (ord < 0) {
            mod = ".";
        } else {
            mod = localRule.get("mods").get(ord);
        }

        String localWord = word;
        for (int i = 0, n = mod.length(); i < n; i++) {
            String c = mod.substring(i, i + 1);
            switch (c) {
                case ".":
                    break;
                case "-":
                    localWord = localWord.substring(0, localWord.length() - 1);
                    break;
                default:
                    localWord = localWord + c;
                    break;
            }
        }
        return localWord;
    }
}
