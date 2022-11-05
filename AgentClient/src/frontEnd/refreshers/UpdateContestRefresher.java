package frontEnd.refreshers;

import com.google.gson.Gson;
import immutables.ContestDataDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GET_CONTEST_STATUS;

public class UpdateContestRefresher extends TimerTask {
    private Consumer<ContestDataDTO> contestDataDTOConsumer;
    private String allyName;

    public UpdateContestRefresher(Consumer<ContestDataDTO> contestDataDTOConsumer) {
        this.contestDataDTOConsumer = contestDataDTOConsumer;

    }

    @Override
    public void run() {
        String url = HttpUrl.parse(GET_CONTEST_STATUS).newBuilder()
                .addQueryParameter("allyName", allyName)
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
                ContestDataDTO contestDataDTO = gson.fromJson(res, ContestDataDTO.class);
                System.out.println(contestDataDTO);
                contestDataDTOConsumer.accept(contestDataDTO);

            }
        });

    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }
}
