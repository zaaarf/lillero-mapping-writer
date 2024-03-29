package ftbsc.lll.mapper.writer.impl;

import com.google.auto.service.AutoService;
import ftbsc.lll.mapper.utils.Mapper;
import ftbsc.lll.mapper.writer.IWriter;

import java.io.PrintWriter;
import java.util.Optional;

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
	public void write(Mapper mapper, PrintWriter writer, String... args) {
		if(args.length < 2)
			throw new RuntimeException("Please provide the namespaces for the tiny format after the -a flag!");
		writer.printf("tiny\t2\t0\t%s\t%s", args[0], args[1]);
		mapper.getRawMappings().forEach((name, data) -> {
			writer.printf("c\t%s\t%s\n", name, data.nameMapped);
			data.getFields().forEach((fieldName, fieldData) ->
				writer.printf("\tf\t%s\t%s\t%s\n",
					Optional.ofNullable(fieldData.descriptor).orElse("?"),
					fieldName, fieldData.nameMapped));
			data.getMethods().forEach(((methodSignature, methodData) ->
				writer.printf("\tm\t%s\t%s\t%s\n", methodSignature.descriptor,
					methodSignature.name, methodData.nameMapped)));
		});
	}
}
