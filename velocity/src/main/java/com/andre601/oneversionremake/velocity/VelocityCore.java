/*
 * Copyright 2020 Andre601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.andre601.oneversionremake.velocity;

import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.enums.ProxyPlatform;
import com.andre601.oneversionremake.core.files.ConfigHandler;
import com.andre601.oneversionremake.core.interfaces.PluginCore;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import com.andre601.oneversionremake.velocity.commands.CmdOneVersionRemake;
import com.andre601.oneversionremake.velocity.listener.VelocityLoginListener;
import com.andre601.oneversionremake.velocity.listener.VelocityPingListener;
import com.andre601.oneversionremake.velocity.logging.VelocityLogger;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class VelocityCore implements PluginCore{
    
    private final ProxyLogger logger;
    private final ProxyServer proxy;
    private final Path path;
    
    private OneVersionRemake core;
    
    private ConfigHandler configHandler = null;
    
    @Inject
    public VelocityCore(ProxyServer proxy, @DataDirectory Path path){
        this.logger = new VelocityLogger(LoggerFactory.getLogger("OneVersionRemake"));
        this.proxy = proxy;
        this.path = path;
    }
    
    @Subscribe
    public void initialize(ProxyInitializeEvent event){
        this.core = new OneVersionRemake(this);
    }
    
    // PluginCore stuff
    
    @Override
    public void loadCommands(){
        CommandMeta commandMeta = getProxy().getCommandManager().metaBuilder("oneversionremake")
                .aliases("ovr")
                .build();
        
        getProxy().getCommandManager().register(commandMeta, new CmdOneVersionRemake(this));
    }
    
    @Override
    public void loadEventListeners(){
        new VelocityLoginListener(this);
        new VelocityPingListener(this);
    }
    
    @Override
    public void setConfigHandler(ConfigHandler configHandler){
        this.configHandler = configHandler;
    }
    
    @Override
    public boolean reloadConfig(){
        return core.reloadConfig();
    }
    
    @Override
    public Path getPath(){
        return path;
    }
    
    @Override
    public ProxyPlatform getProxyPlatform(){
        return ProxyPlatform.VELOCITY;
    }
    
    @Override
    public ProxyLogger getProxyLogger(){
        return logger;
    }
    
    @Override
    public ConfigHandler getConfigHandler(){
        return configHandler;
    }
    
    public String getVersion(){
        return core.getVersion();
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
}
