# Lillero-mapping-writer
Simple CLI program for converting and reversing mappings. Because apparently Enigma can't.

## Usage
Follow the format:
```
java -jar mapping-writer.jar [-r] <location> <format> <output>
```

where `<location>` is where to find the mapping to convert, `<format>` is a supported format, `<output>` is the name of the output file, and `-r` makes it so that the mapping is inverted. 