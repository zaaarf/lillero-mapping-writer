package ftbsc.lll.mapper.writer.impl;

import com.google.auto.service.AutoService;
import ftbsc.lll.mapper.tools.Mapper;
import ftbsc.lll.mapper.writer.IWriter;

import java.io.PrintWriter;

/**
 * An {@link IWriter} that writes in the TSRG format,
 * an intermediary format used by Forge.
 */
@AutoService(IWriter.class)
public class TSRGWriter implements IWriter {
	@Override
	public String uniqueId() {
		return "tsrg";
	}

	@Override
	public void write(Mapper mapper, PrintWriter writer, String... args) {
		if(args.length < 2)
			args = new String[] { "left", "right" };
		writer.printf("tsrg2 %s %s\n", args[0], args[1]);
		mapper.getRawMappings().forEach((name, data) -> {
			writer.printf("%s %s\n", name, data.nameMapped);
			data.getFields().forEach((fieldName, fieldData) ->
				writer.printf("\t%s %s\n", fieldName, fieldData.nameMapped));
			data.getMethods().forEach(((methodSignature, methodData) ->
				writer.printf("\t%s %s %s\n", methodSignature.name,
					methodSignature.descriptor, methodData.nameMapped)));
		});
	}
}
