package uBoatApp.refreshers;

import com.google.gson.Gson;
import immutables.AllyDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GET_SIGNED_UP_ALLIES;

public class AlliesRefresher extends TimerTask {
    private final Consumer<AllyDTO[]> allyDTOConsumer;
    private final String uboatName;

    public AlliesRefresher(Consumer<AllyDTO[]> contestDTOConsumer, String uboatName) {
        this.allyDTOConsumer = contestDTOConsumer;
        this.uboatName = uboatName;
    }

    @Override
    public void run() {
        String url = HttpUrl.parse(GET_SIGNED_UP_ALLIES).newBuilder()
                .addQueryParameter("name",uboatName)
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                System.out.println(res);

                Gson gson = new Gson();
                AllyDTO[] allies = gson.fromJson(res, AllyDTO[].class);
                System.out.println(allies);
                allyDTOConsumer.accept(allies);
            }
        });
    }
}
