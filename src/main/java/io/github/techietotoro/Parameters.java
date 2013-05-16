package io.github.techietotoro;

import com.beust.jcommander.Parameter;

public class Parameters {
    
    @Parameter(names = { "-user", "-username" }, description = "Username")
    public String username;
    
    @Parameter(names = { "-pass", "-password" }, description = "Password")
    public String password;
    
    @Parameter(names = { "-sub", "-subreddit" }, description = "Subreddit")
    public String subreddit;
    
    @Parameter(names = { "-feed", "-url" }, description = "Feed URL")
    public String feed;
    
}
