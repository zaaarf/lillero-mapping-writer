package ftbsc.lll.mapper.writer;

import ftbsc.lll.mapper.IMapper;

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
	 * Writes in a {@link PrintWriter} the contents of a {@link IMapper}.
	 * @param mapper the mapper
	 * @param writer the writer
	 * @param args various arguments which the writers may need
	 */
	void write(IMapper mapper, PrintWriter writer, String... args);
}
