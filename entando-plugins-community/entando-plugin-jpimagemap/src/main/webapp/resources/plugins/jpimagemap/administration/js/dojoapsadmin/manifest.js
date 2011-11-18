dojo.provide("dojoapsadmin.manifest");
  dojo.require("dojo.string.extras");

  dojo.registerNamespaceResolver("dojoapsadmin",
    function(name){ 
      return "dojoapsadmin.widget."+dojo.string.capitalize(name);
    }
  ); 
