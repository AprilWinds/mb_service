package chat.hala.app.restful;

import chat.hala.app.entity.AppMaterial;
import chat.hala.app.library.Qiniu;
import chat.hala.app.library.util.Http;
import chat.hala.app.repository.AppMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by astropete on 2017/12/27.
 */

@RestController
@RequestMapping("/util")
public class UtilResource {

    @Autowired
    AppMaterialRepository amRepo;

    @RequestMapping(value = "/alive", method = RequestMethod.GET, produces = "application/json")
    public Object alive(){
        Map<String, String> m = new HashMap<>();
        m.put("alive", "yes");
        return m;
    }

    @RequestMapping(value = "/qtoken", method = RequestMethod.GET, produces = "application/json")
    public Object getQToken(){
        return Http.standardResponse(Qiniu.issueToken());
    }

    @RequestMapping(value = "/material", method = RequestMethod.GET, produces = "application/json")
    public Object getMaterialByCategory(@RequestParam("category") AppMaterial.MaterialCtg category){
        List<AppMaterial> re = amRepo.findByCategoryOrderBySortBy(category.ordinal());
        return re;
    }
}
