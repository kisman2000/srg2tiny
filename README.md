<div align="center">

# srg2tiny

Minecraft srg mappings to recaf tiny mapping format converter

Originally made for `refmapper`/`Curse`

# Building

To build srg2tiny you need to have [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) installed

</div>

1. Download srg2tiny source code by clicking `Code -> Download ZIP` on [the main page](https://github.com/kisman2000/refmapper)
2. Extract the source code somewhere and open cmd (on Windows) or Terminal
3. Execute `gradlew build` (if you're on Windows) or `chmod +x ./gradlew && ./gradlew build` (if you're on Linux) and wait until the building process finishes
4. After that you should have a file called `srg2tiny-VERSION.jar` inside `<srg2tiny src>/build/libs` folder
5. Use it anywhere you need

<div align="center">

# Downloading

You can download stable prebuilt JARs from [the releases page](https://github.com/kisman2000/srg2tiny/releases)

# Usage via CLI
To use CLI you need to have minecraft `srg` and `fields` and `methods` mappings

</div>

Getting `srg`/`fields`/`method` mappings:

1. Download MCP for version you need from [here](http://www.modcoderpack.com/)
2. Extract `conf` folder from `mcpXXX.zip`
3. Use `conf/joined.srg` as `srg` mappings
4. Use `conf/fields.csv` as `fields` mappings
5. Use `conf/methods.csv` as `methods` mappings

Getting `tiny` mappings:

1. Execute `java -jar srg2tiny.jar "srg" "fields" "methods" "tiny"`
2. Use `"tiny"` as `tiny` mappings
3. Enjoy!

<div align="center">

# Special thanks to
[**KuroHere**](https://github.com/KuroHere)/Curse for idea of srg2tiny

</div>