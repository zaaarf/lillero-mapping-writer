package ftbsc.lll.mapper.writer.impl;

import com.google.auto.service.AutoService;
import ftbsc.lll.mapper.IMapper;
import ftbsc.lll.mapper.writer.IWriter;

import java.io.PrintWriter;

/**
 * An {@link IWriter} that writes in the Tiny v2 format.
 */
@AutoService(IWriter.class)
public class TinyV2Writer implements IWriter {
	@Override
	public String uniqueId() {
		return "tinyv2";
	}

	@Override
	public void write(IMapper mapper, PrintWriter writer, String... args) {
		writer.printf("tiny\t2\t0\t%s\t%s", args[0], args[1]);
		mapper.getRawMappings().forEach((name, data) -> {
			writer.printf("c\t%s\t%s\n", name, data.nameMapped);
			data.getFields().forEach((fieldName, fieldData) ->
				writer.printf("\tf\t?\t%s\t%s\n", fieldName, fieldData.nameMapped)); //TODO field descriptors
			data.getMethods().forEach(((methodSignature, methodData) ->
				writer.printf("\tm\t%s\t%s\t%s\n", methodSignature.descriptor,
					methodSignature.name, methodData.nameMapped)));
		});
	}
}
