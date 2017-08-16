package eagle.jfaster.org.codec.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import de.javakaffee.kryoserializers.dexx.ListSerializer;
import de.javakaffee.kryoserializers.dexx.MapSerializer;
import de.javakaffee.kryoserializers.dexx.SetSerializer;
import eagle.jfaster.org.codec.Serialization;
import eagle.jfaster.org.spi.SpiInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * Created by fangyanpeng1 on 2017/7/29.
 */
@SpiInfo(name = "kryo")
public class KryoSerialization implements Serialization {

    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            return kryo;
        }
    };

    @Override
    public byte[] serialize(Object obj) throws IOException {
        Kryo kryo = kryos.get();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Output out = new Output(byteOut);
        kryo.writeObject(out,obj);
        out.flush();
        return byteOut.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        Kryo kryo = kryos.get();
        Input in = new Input(new ByteArrayInputStream(bytes));
        return kryo.readObject(in,clz);
    }
}