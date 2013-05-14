/* Based off of
 * the DropboxMirror project
 * by yetanotherx
 */

package io.github.techietotoro;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;

import com.yetanotherx.reddit.RedditPlugin;
import com.yetanotherx.reddit.api.modules.RedditCore;
import com.yetanotherx.reddit.api.modules.RedditLink;
import com.yetanotherx.reddit.util.LinkType;

/**
 * Hello world!
 *
 */
public class App extends RedditPlugin
{
	private Parameters params;

    public App(Parameters params) 
    {
        this.params = params;
    }

    public void run() throws InterruptedException
    {
		RedditCore.newFromUserAndPass(this, params.username, params.password).doLogin();
        params = null;
        
        RedditLink.doSubmit(this, "First post from bot!", "I'm trying to make a nice bot for some guys at futuristparty. Except I don't know python so I can't copy congressbot.", "techietotoro", LinkType.SELF);
        
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JavaCongressWatchBot";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
        Parameters params = new Parameters();
        JCommander jc = new JCommander(params, args);
        try {
            new App(params).run();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

