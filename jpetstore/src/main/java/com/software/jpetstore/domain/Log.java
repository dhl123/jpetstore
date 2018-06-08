package com.software.jpetstore.domain;

public class Log {
    public Log(String username,String url,String cartitems) {
        this.username=username;
        this.cartitems=cartitems;
        this.url=url;
    }
    private String username;
    private String url;
    private String cartitems;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCartitems() {
        return cartitems;
    }

    public void setCartitems(String cartitems) {
        this.cartitems = cartitems;
    }
}
