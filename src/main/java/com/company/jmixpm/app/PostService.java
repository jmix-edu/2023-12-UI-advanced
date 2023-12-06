package com.company.jmixpm.app;

import com.company.jmixpm.entity.Post;
import com.company.jmixpm.entity.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PostService {

    public List<Post> fetchPosts() {
        RestTemplate restTemplate = new RestTemplate();
        Post[] posts = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts", Post[].class);
        return posts != null ? Arrays.asList(posts) : Collections.emptyList();
    }

    public UserInfo fetchUserInfo (Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://jsonplaceholder.typicode.com/users/{id}",
                UserInfo.class, id);
    }

    public List<Post> fetchPosts(int firstResult, int maxResult) {
        RestTemplate rest = new RestTemplate();
        Post[] posts = rest.getForObject("https://jsonplaceholder.typicode.com/posts?_start={start}&_end={end}",
                Post[].class, firstResult, firstResult + maxResult);
        return posts != null ? Arrays.asList(posts) : Collections.emptyList();
    }

    public int getTotalCount() {
        return fetchPosts().size();
    }
}