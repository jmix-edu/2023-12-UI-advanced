package com.company.jmixpm.screen.userinfo;

import com.company.jmixpm.app.PostService;
import com.company.jmixpm.entity.UserInfo;
import com.google.common.collect.ImmutableMap;
import io.jmix.core.LoadContext;
import io.jmix.ui.action.Action;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.navigation.UrlIdSerializer;
import io.jmix.ui.navigation.UrlParamsChangedEvent;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("UserInfoScreen")
@UiDescriptor("user-info-screen.xml")
@Route("user-info")
public class UserInfoScreen extends Screen {
    @Autowired
    private UrlRouting urlRouting;
    private Long userId;
    @Autowired
    private PostService postService;

    public void setUserId (Long userId) {
        this.userId = userId;
    }

    @Subscribe
    public void onAfterShow(final AfterShowEvent event) {
        String serializedId = UrlIdSerializer.serializeId(userId);
        urlRouting.replaceState(this, ImmutableMap.of("id", serializedId));
    }

    @Subscribe
    public void onUrlParamsChanged(final UrlParamsChangedEvent event) {
        String serializedId = event.getParams().get("id");
        userId = (Long) UrlIdSerializer.deserializeId(Long.class, serializedId);
    }
    
    

    @Install(to = "userInfoDl", target = Target.DATA_LOADER)
    private UserInfo userInfoDlLoadDelegate(final LoadContext<UserInfo> loadContext) {
        return postService.fetchUserInfo(userId);
    }

    @Subscribe("windowClose")
    public void onWindowClose(final Action.ActionPerformedEvent event) {
        closeWithDefaultAction();
    }
    
    
}