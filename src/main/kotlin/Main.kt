import java.io.File
import kotlin.system.exitProcess

fun main(
    args : Array<String>
) {
    val timestamp = System.currentTimeMillis()
    var warnings = 0

    fun exit(
        reason : String
    ) {
        println("$reason\nUsage: \"srg mappings file\" \"field mappings file\" \"method mappings file\" \"tiny mappings file\"")
        exitProcess(0)
    }

    fun warning(
        message : String
    ) {
        println("Warning! $message")

        warnings++
    }

    if(args.size != 4) {
        exit("Not enough arguments!")
    }

    val srg = File(args[0])
    val field = File(args[1])
    val method = File(args[2])
    val output = File(args[3])

    if(!srg.exists()) {
        exit("Srg mappings file does not exist!")
    }

    if(!field.exists()) {
        exit("Field mappings file does not exist!")
    }

    if(!method.exists()) {
        exit("Method mappings file does not exist!")
    }

    if(srg.isDirectory) {
        exit("Srg mappings file is a directory!")
    }

    if(output.exists()) {
        println("Tiny mappings file will be overwritten!")

        output.delete()
    }

    output.createNewFile()

    val classes = mutableListOf<SrgEntry>()
    val fields = mutableListOf<SrgEntry>()
    val methods = mutableListOf<SrgEntry>()

    fun findEntries(
        entries : List<SrgEntry>,
        name : String,
        getter : (SrgEntry) -> String
    ) : Collection<SrgEntry> {
        val list = mutableListOf<SrgEntry>()

        for(entry in entries) {
            if(name == getter(entry)) {
                list.add(entry)
            }
        }

        return list
    }

    println()
    println("Parsing srg mappings!")

    for(line in srg.readLines()) {
        val split = line.split(" ")

        when (split[0]) {
            "PK:" -> warning("PK entries are unsupported!")

            "CL:" -> {
                val official = split[1]
                val named = split[2]

                classes.add(SrgEntry(official, named, named))
            }

            "FD:" -> {
                val intermediary = split[2].split("/").last()
                val official = split[2].removeSuffix("/$intermediary")

                fields.add(SrgEntry(official, intermediary, intermediary, "TYPE"))
            }

            "MD:" -> {
                val intermediary = split[3].split("/").last()
                val official = split[3].removeSuffix("/$intermediary")
                val type = split[4]

                methods.add(SrgEntry(official, intermediary, intermediary, type))
            }

            else -> warning("Unknown mapping type ${split[0]}")
        }
    }

    println("Parsing field mappings!")

    for(line in field.readLines()) {
        val split = line.split(",")

        if(split[0] == "searge") {
            continue
        }

        val intermediary = split[0]
        val named = split[1]

        val fieldEntries = findEntries(methods, intermediary) { it.intermediary }

        for(fieldEntry in fieldEntries) {
            fieldEntry.named = named
        }
    }

    println("Parsing method mappings!")

    for(line in method.readLines()) {
        val split = line.split(",")

        if(split[0] == "searge") {
            continue
        }

        val intermediary = split[0]
        val named = split[1]

        val methodEntries = findEntries(methods, intermediary) { it.intermediary }

        for(methodEntry in methodEntries) {
            methodEntry.named = named
        }
    }

    println("Parsed ${srg.readLines().size} lines of srg mappings!")
    println("Parsed ${field.readLines().size} lines of field mappings!")
    println("Parsed ${method.readLines().size} lines of method mappings!")
    println()
    println("Writing tiny mappings!")

    val writer = output.writer()

    writer.write("v1\t")
    writer.appendLine()

    println("Parsing ${classes.size} class entries!")

    for(entry in classes) {
        writer.write("CLASS\t${entry.intermediary}\t${entry.named}")
        writer.appendLine()
    }

    println("Parsing ${fields.size} field entries!")

    for(entry in fields) {
        writer.write("FIELD\t${entry.official}\t${entry.type}\t${entry.intermediary}\t${entry.named}")
        writer.appendLine()
    }

    println("Parsing ${methods.size} method entries!")

    for(entry in methods) {
        writer.write("METHOD\t${entry.official}\t${entry.type}\t${entry.intermediary}\t${entry.named}")
        writer.appendLine()
    }

    writer.close()

    println()
    println("Written ${classes.size + fields.size + methods.size} lines of tiny mappings!")
    println()
    println("Everything took ${System.currentTimeMillis() - timestamp} ms and got $warnings warnings!")
}

class SrgEntry(
    val official : String,
    val intermediary : String,
    var named : String,
    val type : String = ""
)