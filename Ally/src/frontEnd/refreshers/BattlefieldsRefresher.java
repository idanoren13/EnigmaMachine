package frontEnd.refreshers;

import com.google.gson.Gson;
import immutables.ContestDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GET_BATTLEFIELDS;

public class BattlefieldsRefresher extends TimerTask {
    private final Consumer<ContestDTO[]> contestDTOConsumer;

    public BattlefieldsRefresher(Consumer<ContestDTO[]> contestDTOConsumer) {

        this.contestDTOConsumer = contestDTOConsumer;
    }

    @Override
    public void run() {
        String url = HttpUrl.parse(GET_BATTLEFIELDS).newBuilder()
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
                ContestDTO[] contests = gson.fromJson(res, ContestDTO[].class);
                System.out.println(contests);
                contestDTOConsumer.accept(contests);

            }
        });
    }
}
