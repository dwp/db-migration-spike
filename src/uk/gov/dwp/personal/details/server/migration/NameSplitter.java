package uk.gov.dwp.personal.details.server.migration;

import java.util.Objects;

public class NameSplitter {

    public Names split(String name) {
        String[] split = name.split("\\s");
        StringBuilder lastName = new StringBuilder(name.length());
        for (int i = 1; i < split.length; i++) {
            lastName.append(split[i]);
            if (i < (split.length-1)) {
                lastName.append(" ");
            }
        }
        return new Names(split[0], (lastName.length() > 0) ? lastName.toString() : null);
    }

    public static class Names {
        private final String firstName;
        private final String lastName;

        public Names(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Names)) {
                return false;
            }
            return Objects.equals(firstName, ((Names)obj).firstName) &&
                    Objects.equals(lastName, ((Names)obj).lastName);
        }

        @Override
        public int hashCode() {
            return 31 * firstName.hashCode() + (lastName != null ? lastName.hashCode() : 0);
        }

        @Override
        public String toString() {
            return String.format("%s %s", firstName, lastName);
        }
    }
}
