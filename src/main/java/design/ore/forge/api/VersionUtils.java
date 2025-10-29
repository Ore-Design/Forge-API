package design.ore.forge.api;

public class VersionUtils
{
    /**
     * Compares two version strings.
     *
     * @param currentVersion   the currentVersion version string (e.g. "2.3")
     * @param candidateVersion the version string to compare against (e.g. "2.3.1")
     * @return true if candidateVersion is later than currentVersion, false otherwise
     */
    public static boolean isLaterVersion(String currentVersion, String candidateVersion)
    {
        if(currentVersion == null || candidateVersion == null) return false;
        if(currentVersion.equals(candidateVersion)) return false;

        String[] currentParts = currentVersion.split("\\.");
        String[] candidateParts = candidateVersion.split("\\.");

        // Max length is whichever has more segments
        int maxLength = Math.max(currentParts.length, candidateParts.length);

        for (int i = 0; i < maxLength; i++)
        {
            int curVal = (i < currentParts.length) ? Integer.parseInt(currentParts[i]) : 0;
            int candVal = (i < candidateParts.length) ? Integer.parseInt(candidateParts[i]) : 0;

            if (candVal > curVal) return true;  // Candidate is later
            else if (candVal < curVal) return false; // Candidate is earlier
            // Otherwise keep checking
        }

        // Versions are equal
        return false;
    }
}
