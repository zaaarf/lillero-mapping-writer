package ftbsc.lll.mapper.writer.impl;

import com.google.auto.service.AutoService;
import ftbsc.lll.mapper.utils.Mapper;
import ftbsc.lll.mapper.utils.MappingUtils;
import ftbsc.lll.mapper.data.FieldData;
import ftbsc.lll.mapper.data.MethodData;
import ftbsc.lll.mapper.writer.IWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IWriter} that writes in the SRG format.
 */
@AutoService(IWriter.class)
public class SRGWriter implements IWriter {
	@Override
	public String uniqueId() {
		return "srg";
	}

	@Override
	public void write(Mapper mapper, PrintWriter writer, String... ignored) {
		List<FieldData> fieldData = new ArrayList<>();
		List<MethodData> methodData = new ArrayList<>();

		//print classes and save rest for later
		mapper.getRawMappings().forEach((name, data) -> {
			writer.printf("CL: %s %s\n", name, data.nameMapped);
			fieldData.addAll(data.getFields().values());
			methodData.addAll(data.getMethods().values());
		});

		//print fields
		fieldData.forEach(data -> writer.printf("FD: %s/%s %s/%s\n",
			data.parentClass.name, data.name, data.parentClass.nameMapped, data.nameMapped));

		//print methods
		methodData.forEach(data -> writer.printf("MD: %s/%s %s %s/%s %s\n",
			data.parentClass.name, data.signature.name, data.signature.descriptor,
			data.parentClass.nameMapped, data.nameMapped, MappingUtils.mapMethodDescriptor(
				data.signature.descriptor, mapper, false)));
	}
}
