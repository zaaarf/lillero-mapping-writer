package ftbsc.lll.mapper.writer.impl;

import com.google.auto.service.AutoService;
import ftbsc.lll.mapper.IMapper;
import ftbsc.lll.mapper.writer.IWriter;

import java.io.PrintWriter;

/**
 * Writes to TSRG, an intermediary format used by Forge.
 */
@AutoService(IWriter.class)
public class TSRGWriter implements IWriter {
	@Override
	public String uniqueId() {
		return "tsrg";
	}

	@Override
	public void write(IMapper mapper, PrintWriter writer) {
		writer.println("tsrg2 left right");
		mapper.getRawMappings().forEach((name, data) -> {
			writer.printf("%s %s\n", data.name, data.nameMapped);
			data.getFields().forEach((fieldName, fieldData) ->
				writer.printf("\t%s %s\n", fieldName, fieldData.nameMapped));
			data.getMethods().forEach(((methodSignature, methodData) ->
				writer.printf("\t%s %s %s\n", methodSignature.name,
					methodSignature.descriptor, methodData.nameMapped)));
		});
	}
}
