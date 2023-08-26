package ftbsc.lll.mapper.writer;

import ftbsc.lll.mapper.IMapper;
import ftbsc.lll.mapper.MapperProvider;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Writes a mapping to a certain format.
 */
public class MappingWriter {

	/**
	 * The main function, must be passed exactly two arguments
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

		if(args.length != 3) {
			System.err.println("Bad argument count!");
			System.err.println("java -jar mapping-writer.jar [-r] <location> <format> <output>");
			return;
		}

		//load the mapper
		List<String> lines = MapperProvider.fetchFromLocalOrRemote(args[0]);
		IMapper mapper = MapperProvider.getMapper(lines);
		mapper.populate(lines, false);
		if(cmdLine.hasOption("reverse"))
			mapper = mapper.getInverted();

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
			System.out.println("File already exists!");
			return;
		}

		if(!targetFile.canWrite()) {
			System.out.println("Failed to write to file: access denied.");
			return;
		}

		//call the writer
		PrintWriter printWriter = new PrintWriter(new FileWriter(targetFile));
		writer.write(mapper, printWriter);
		printWriter.close();
	}
}
