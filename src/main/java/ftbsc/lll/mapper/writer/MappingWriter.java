package ftbsc.lll.mapper.writer;

import ftbsc.lll.mapper.MapperProvider;
import ftbsc.lll.mapper.tools.Mapper;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Writes a mapping to a certain format.
 */
public class MappingWriter {

	/**
	 * The main function.
	 * @param args the command line arguments
	 * @throws IOException if something goes wrong while writing the file
	 * @throws ParseException if something goes wrong while parsin arguments
	 */
	public static void main(String[] args) throws IOException, ParseException {
		Options options = new Options()
			.addOption("r", "reverse", false, "Writes down inverted mappings");
		DefaultParser parser = new DefaultParser();
		CommandLine cmdLine = parser.parse(options, args);
		args = cmdLine.getArgs();

		//separate normal args from custom args
		int cursor;
		for(cursor = 0; cursor < args.length; cursor++)
			if(args[cursor].equals("-a"))
				break;
		String[] customArgs;
		if(cursor != args.length) {
			int len = args.length - cursor - 1;
			customArgs = new String[len];
			System.arraycopy(args, cursor + 1, customArgs, 0, len);
			String[] newArgs = new String[cursor];
			System.arraycopy(args, 0, newArgs, 0, cursor);
			args = newArgs;
		} else customArgs = new String[0];

		if(args.length < 3) {
			System.err.println("Bad argument count!");
			System.err.println("java -jar mapping-writer.jar [-r] <location> <format> <output> [-a <custom args]");
			return;
		}

		//load the mapper
		List<String> lines = MapperProvider.fetchFromLocalOrRemote(args[0]);
		Mapper mapper = !cmdLine.hasOption("reverse")
			? MapperProvider.getMapper(lines).getMapper(lines, false)
			: MapperProvider.getMapper(lines).getInvertedMapper(lines, false);

		//load the writers
		Map<String, IWriter> writerMap = new HashMap<>();
		for(IWriter w : ServiceLoader.load(IWriter.class))
			writerMap.put(w.uniqueId(), w);

		//get the one we need
		IWriter writer = writerMap.get(args[1]);
		if(writer == null) {
			System.err.printf("%s was not recognised as a valid format!", args[1]);
			return;
		}

		//now for the file
		File targetFile = new File(args[2]);

		if(!targetFile.createNewFile()) {
			System.err.println("File already exists!");
			return;
		}

		if(!targetFile.canWrite()) {
			System.err.println("Failed to write to file: access denied.");
			return;
		}

		//call the writer
		PrintWriter printWriter = new PrintWriter(new FileWriter(targetFile));
		writer.write(mapper, printWriter, customArgs);
		printWriter.close();
	}
}
