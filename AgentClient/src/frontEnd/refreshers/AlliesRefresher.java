package frontEnd.refreshers;

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

import static utils.Constants.GET_ALLIES;

public class AlliesRefresher extends TimerTask {

    private final Consumer<AllyDTO[]> alliesDTOConsumer;

    public AlliesRefresher(Consumer<AllyDTO[]> alliesDTOConsumer) {
        this.alliesDTOConsumer = alliesDTOConsumer;
    }


    @Override
    public void run() {
        String url = HttpUrl.parse(GET_ALLIES).newBuilder()
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                System.out.println(res);

                Gson gson = new Gson();
                AllyDTO[] contests = gson.fromJson(res, AllyDTO[].class);
                System.out.println(contests);
                alliesDTOConsumer.accept(contests);

            }
        });

    }
}
