open module rest {
    

    requires spring.boot;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;

    requires transitive core;
    requires localpersistence;
    requires transitive org.json;

    exports restserver;
    
}