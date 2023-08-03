class ArtifactsMatcher {
    fun unmatchedArtifacts(artifacts: Array<GithubArtifact>): Array<GithubArtifact> {
        var incomingMap: MutableMap<Int, GithubArtifact> = mutableMapOf()
        var outcomingMap: MutableMap<Int, GithubArtifact> = mutableMapOf()

        for (artifact in artifacts) {
            var name = artifact.name

            if (!name.startsWith("test-")) {
                continue
            }

            name = name.removePrefix("test-")

            if (name.startsWith("start-")) {
                name = name.removePrefix("start-")
                val time = name.toIntOrNull() ?: continue

                if (outcomingMap.containsKey(time)) {
                    outcomingMap.remove(time)
                } else {
                    incomingMap[time] = artifact
                }
            } else if (name.startsWith("finish-")) {
                name = name.removePrefix("finish-")
                val time = name.toIntOrNull() ?: continue

                if (incomingMap.containsKey(time)) {
                    incomingMap.remove(time)
                } else {
                    outcomingMap[time] = artifact
                }
            }
        }

        return incomingMap.values.toTypedArray()
    }
}