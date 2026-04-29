package mx.edu.cetys.software_quality_lab.validators;

import org.springframework.stereotype.Service;

@Service
public class EmailValidatorService {

    private static final int MAX_TOTAL_LENGTH = 47;
    private static final int MIN_DOMAIN_LENGTH = 1;
    private static final int MAX_DOMAIN_LENGTH = 5;

    private static final char HASH = '#';
    private static final char REQUIRED_DIGIT = '4';
    private static final char DOT = '.';

    private static final String VOWELS = "aeiou";

    public boolean isValid(String email) {

        if (!isPresent(email)) return false;

        if (!isWithinTotalLength(email)) return false;

        if (!containsRequiredDigit(email)) return false;

        int hashIndex = singleHashIndex(email);
        if (hashIndex < 0) return false;

        Parts parts = extractParts(email, hashIndex);
        if (parts == null) return false;

        if (!isValidDomainLength(parts.domain())) return false;

        if (!isValidUser(parts.user())) return false;

        if (!isValidProvider(parts.provider())) return false;

        if (!isValidDomain(parts.domain())) return false;

        if (hasDiphthong(email)) return false;

        return true;
    }

    private boolean isPresent(String email) {
        return email != null && !email.isBlank();
    }

    private boolean isWithinTotalLength(String email) {
        return email.length() <= MAX_TOTAL_LENGTH;
    }

    private boolean containsRequiredDigit(String email) {
        return email.indexOf(REQUIRED_DIGIT) >= 0;
    }

    private int singleHashIndex(String email) {
        int first = email.indexOf(HASH);
        if (first <= 0) return -1;
        if (first != email.lastIndexOf(HASH)) return -1;
        if (first >= email.length() - 1) return -1;
        return first;
    }

    private Parts extractParts(String email, int hashIndex) {
        String user = email.substring(0, hashIndex);
        String providerAndDomain = email.substring(hashIndex + 1);

        int lastDotIndex = providerAndDomain.lastIndexOf(DOT);
        if (lastDotIndex <= 0 || lastDotIndex >= providerAndDomain.length() - 1) return null;

        String provider = providerAndDomain.substring(0, lastDotIndex);
        String domain = providerAndDomain.substring(lastDotIndex + 1);

        return new Parts(user, provider, domain);
    }

    private boolean isValidDomainLength(String domain) {
        int len = domain.length();
        return len >= MIN_DOMAIN_LENGTH && len <= MAX_DOMAIN_LENGTH;
    }

    private boolean isValidUser(String user) {
        return user.chars().allMatch(cp -> isAllowedUserChar((char) cp));
    }

    private boolean isAllowedUserChar(char c) {
        if (isLowercaseLetter(c) || isDigit(c)) return true;

        return switch (c) {
            case DOT, '-', '_', '+' -> true;
            default -> false;
        };
    }

    private boolean isValidProvider(String provider) {
        for (int i = 0; i < provider.length(); i++) {
            char c = provider.charAt(i);
            if (isLowercaseLetter(c) || isDigit(c) || c == DOT) continue;
            return false;
        }
        return true;
    }

    private boolean isValidDomain(String domain) {
        for (int i = 0; i < domain.length(); i++) {
            char c = domain.charAt(i);
            if (isLowercaseLetter(c) || isDigit(c)) continue;
            return false;
        }
        return true;
    }

    private boolean hasDiphthong(String email) {
        for (int i = 0; i < email.length() - 1; i++) {
            if (isVowel(email.charAt(i)) && isVowel(email.charAt(i + 1))) {
                return true;
            }
        }
        return false;
    }

    private boolean isVowel(char c) {
        return VOWELS.indexOf(c) >= 0;
    }

    private boolean isLowercaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private record Parts(String user, String provider, String domain) {}
}