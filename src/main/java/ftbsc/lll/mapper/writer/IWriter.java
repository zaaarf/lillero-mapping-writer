package ftbsc.lll.mapper.writer;

import ftbsc.lll.mapper.utils.Mapper;

import java.io.PrintWriter;

/**
 * The common interface for all mapping writers.
 */
public interface IWriter {
	
	/**
	 * @return a unique identifier for this writer
	 */
	String uniqueId();

	/**
	 * Writes in a {@link PrintWriter} the contents of a {@link Mapper}.
	 * @param mapper the mapper
	 * @param writer the writer
	 * @param args various arguments which the writers may need
	 */
	void write(Mapper mapper, PrintWriter writer, String... args);
}
