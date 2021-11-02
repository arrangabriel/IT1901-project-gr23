open module rest {
    
    requires localpersistence;

    requires spring.boot;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;

    requires core;

    exports restapi;
    exports restserver;
}