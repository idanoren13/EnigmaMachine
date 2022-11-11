package frontEnd.refreshers;

import com.google.gson.Gson;
import immutables.CandidateDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GET_CANDIDATES;

public class CandidateRefresher extends TimerTask {
    private final Consumer<CandidateDTO[]> candidateDTOConsumer;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public CandidateRefresher(Consumer<CandidateDTO[]> candidateDTOConsumer) {
        this.candidateDTOConsumer = candidateDTOConsumer;
    }

    @Override
    public void run() {
        String url = HttpUrl.parse(GET_CANDIDATES).newBuilder()
                .addQueryParameter("allyName", name)
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
                CandidateDTO[] candidateInfo = gson.fromJson(res, CandidateDTO[].class);
                System.out.println(candidateInfo);
                candidateDTOConsumer.accept(candidateInfo);

            }
        });
    }
}
