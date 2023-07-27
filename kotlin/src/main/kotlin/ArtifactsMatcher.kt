class ArtifactsMatcher {
    fun unmatchedArtifacts(artifacts: Array<GithubArtifact>): Array<GithubArtifact> {
        var incomingMap: MutableMap<Int, GithubArtifact> = mutableMapOf()
        var outcomingMap: MutableMap<Int, GithubArtifact> = mutableMapOf()

        for (artifact in artifacts) {
            var name = artifact.name

            if (!name.startsWith("test-")) {
                continue
            }

            name.removePrefix("test-")

            if (name.startsWith("start-")) {
                name.removePrefix("start-")
                val time = name.toIntOrNull() ?: continue

                if (outcomingMap[time] != null) {
                    outcomingMap.remove(time)
                } else {
                    incomingMap[time] = artifact
                }
            } else if (name.startsWith("finish-")) {
                name.removePrefix("finish-")
                val time = name.toIntOrNull() ?: continue

                if (incomingMap[time] != null) {
                    incomingMap.remove(time)
                } else {
                    outcomingMap[time] = artifact
                }
            }
        }

        return outcomingMap.values.toTypedArray()
    }
}