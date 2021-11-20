module integration {
    requires spring.boot;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires client;
    requires rest;

    opens integration to spring.core;
    exports integration;
}
