package ar.edu.utn.dds.k3003.cliente;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.model.HechoDTO;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;
import java.util.List;


public interface FuentesRetrofitClient {

    @GET("colecciones/{coleccionId}/hechos")
    Call<List<HechoDTO>> get(@Path("coleccionId") String coleccionId);
}
