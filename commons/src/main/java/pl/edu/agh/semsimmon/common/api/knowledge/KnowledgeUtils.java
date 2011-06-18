package pl.edu.agh.semsimmon.common.api.knowledge;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Tadeusz Kozak
 *
 * <p/>
 * Created on: 2011-06-18 12:31
 * <br/>
 * <a href="http://www.saymama.com">http://www.saymama.com</a>
 *
 * @TODO description
 */
public class KnowledgeUtils {

  public static byte[] serializeOntology(InputStream input) throws IOException {
    ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream((int)(input.available() * 0.5));
    GZIPOutputStream gzipStream = new GZIPOutputStream(byteArrayOutput);
    IOUtils.copy(input, gzipStream);
    gzipStream.finish();
    gzipStream.close();
    return byteArrayOutput.toByteArray();
  }

  public static InputStream deserializeOntology(byte[] input) throws IOException {
    return new GZIPInputStream(new ByteArrayInputStream(input));
  }

}
