package sunlib.turtle.handler;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;
import sunlib.turtle.models.CachedManifest;
import sunlib.turtle.models.CachedText;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ManifestRequestHandler implements RequestHandler {

    static Logger logger = LogManager.getLogger(ManifestRequestHandler.class.getName());
    static Map<String, CachedManifest> cache = new HashMap<String, CachedManifest>();

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        logger.trace("manifest request,{}", request.toString());
        // get manifest from act=status API

        ManifestResponse ret = null;

        if (request.params.get("act").equals("cache")) {
            CachedManifest cached = cache.get(request.getCacheId());
            if (cached == null) {
                //TODO add tasks to queue and download one by one
                cache.put(request.getCacheId(), new CachedManifest());
                ret = new ManifestResponse(false, 0);
            } else {
                ret = new ManifestResponse(cached.is_cached, cached.progress);
            }
        } else if (request.params.get("act").equals("status")) {
            if (cache.get(request.getCacheId()) != null) {
                CachedManifest cached = cache.get(request.getCacheId());
                ret = new ManifestResponse();
                if (!cached.is_cached) {
                    cached.progress += 20;
                    if (cached.progress == 100) {
                        cached.is_cached = true;
                    }
                }
                ret.is_cached = cached.is_cached;
                ret.progress = cached.progress;
            } else {
                ret = new ManifestResponse();
            }
        }
        return new ApiResponse(true,
                new CachedText(request.getCacheId(), new Gson().toJson(ret)));
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        return null;
    }

    @Override
    public void stop() {

    }

    public static class ManifestResponse {
        public Boolean is_cached;
        public Integer progress;

        public ManifestResponse() {
        }

        public ManifestResponse(Boolean is_cached, Integer progress) {
            this.is_cached = is_cached;
            this.progress = progress;
        }
    }
}
