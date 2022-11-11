package frontEnd.refreshers;

import com.google.gson.Gson;
import immutables.AgentDataDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GET_AGENTS;


public class AgentDataRefresher extends TimerTask {
    private final Consumer<AgentDataDTO[]> agentDataDTOConsumer;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public AgentDataRefresher(Consumer<AgentDataDTO[]> agentDataDTOConsumer ){
        this.agentDataDTOConsumer = agentDataDTOConsumer;
    }
    @Override
    public void run() {
        String url = HttpUrl.parse(GET_AGENTS).newBuilder()
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
                AgentDataDTO[] agentsInfo = gson.fromJson(res, AgentDataDTO[].class);
                System.out.println(agentsInfo);
                agentDataDTOConsumer.accept(agentsInfo);

            }
        });
    }
}
