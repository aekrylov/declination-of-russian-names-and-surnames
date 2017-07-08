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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.declinationofnames.RussianNameProcessor.SEX_F;
import static com.github.declinationofnames.RussianNameProcessor.SEX_M;

/**
 * Declinates Russian names and surnames.
 *
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class RussianName {
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String sex;
    private final boolean fullNameSurnameLast;
    // new RussianName("Козлов Евгений Павлович")      // годится обычная форма
    // new RussianName('Евгений Павлович Козлов")      // в таком виде тоже
    // new RussianName("Козлов", "Евгений")        // можно явно указать составляющие
    // new RussianName("Кунтидия", "Убиреко", "", "f") // можно явно указать пол ('m' или 'f')
    public RussianName(final String fullName) {
        Matcher m = Pattern.compile("^\\s*(\\S+)(\\s+(\\S+)(\\s+(\\S+))?)?\\s*$").matcher(fullName);
        if (!m.matches()) {
            throw new IllegalArgumentException("Cannot parse supplied name");
        }
        if (m.group(5) != null && m.group(3).matches(".*(ич|на)$") && !m.group(5).matches(".*(ич|на)$")) {
            // Иван Петрович Сидоров
            this.lastName = m.group(5);
            this.firstName = m.group(1);
            this.middleName = m.group(3);
            this.fullNameSurnameLast = true;
        } else {
            // Сидоров Иван Петрович
            this.lastName = m.group(1);
            this.firstName = m.group(3);
            this.middleName = m.group(5);
            this.fullNameSurnameLast = false;
        }
        this.sex = getSex();
    }
    
    public RussianName(final String lastName, final String firstName, final String middleName, final String sex) {
        this.lastName = lastName;
        this.firstName = firstName == null ? "" : firstName;
        this.middleName = middleName == null ? "" : middleName;
        this.fullNameSurnameLast = false;
        this.sex = sex == null ? getSex() : sex;
    }

    private String getSex() {
        if (this.middleName != null && this.middleName.length() > 2) {
            if ("ич".equals(this.middleName.substring(this.middleName.length() - 2))) {
                return SEX_M;
            } else if ("на".equals(this.middleName.substring(this.middleName.length() - 2))) {
                return SEX_F;
            }
        }
        return "";
    }
    
    public String fullName(String gcase) {
            return (
                    (fullNameSurnameLast ? "" : lastName(gcase) + " ")
                    + firstName(gcase) + " " + middleName(gcase) +
                    (fullNameSurnameLast ? " " + lastName(gcase) : "")
            ).trim();
    }
    
    public String lastName(String gcase) {
        return RussianNameProcessor.word(lastName, sex, "lastName", gcase);
    }
    public String firstName(String gcase) {
        return RussianNameProcessor.word(firstName, this.sex, "firstName", gcase);
    }
    public String middleName(String gcase) {
        return RussianNameProcessor.word(middleName, this.sex, "middleName", gcase);
    }
}
