# ACE: Lab-Web

## Overview

Here is a quick list of the Front End technologies utilized in the lab and they relate to the files at this level of the project.

### Node.js

  [Official Link](https://nodejs.org/en/)

  There are several components that make up Node, but basically it is server side Javascript. Check it [out](https://www.youtube.com/watch?v=pU9Q6oiQNd0).

  file/folder | Info
  ------------------- | ---------------------------
  package.json  | A list of your Node Dependencies. i.e. Node Modules you need for the project. You can download these dependencies via npm.
  /node_modules | This is where npm stores those dependencies.
  app.js | Simple Node Web Server. When run with `nodemon app.js` it allows you to make changes to the UI without having to re-deploy onto Tomcat.

  ** The Node aspects of the project are purely for local development and will not execute on Tomcat when deployed.


### Bower

  [Official Link](https://bower.io/)

  Bower is a package/dependency manager much like npm and Maven. It used for Angular and css components.

  file/folder | Info
  ------------------- | ---------------------------
  bower.json  | A list of your Bower Dependencies.
  /www/vendor | This is where Bower stores those dependencies.
  .bowerrc | A file used to configure Bower. Here we tell Bower where to shove it's dependencies (/www/vendor)


### Angular

  [Official Link](https://angularjs.org/)

  Angular is a Front End Javascript Framework. It is basically a set of libraries, syntax, and architecture rules that simplifies the construction of dynamic web apps.

  It binds HTML and Javascript in pretty cool ways and separates your views from your logic from your server communication to make your a life a lot easier.

### UI Bootstrap

  [Official Link](https://angular-ui.github.io/bootstrap/)

  Twitter Bootstrap for Angular.

### Ramda.js

  [Official Link](http://ramdajs.com/0.21.0/index.html)

  A Javascript library for Object/Array manipulation, validation, [function currying](https://en.wikipedia.org/wiki/Currying) and much much more.

### Atom

  [Official Link](https://atom.io/)

  Okay you don't have to use it but it is a pretty cool text editor/IDE.

  Great packages to install:

  git-blame
  highlight-selected
  minimap
  minimap-find-and-replace
  minimap-git-diff
  minimap-ighlight-selected
  terminal-plus
  tree-view-autoresize

### Oh My ZSH

  [Official Link](http://ohmyz.sh/)

  Terminal plugin that works swimmingly with Git. Also not required, but still awesome.

## Local Development

  Given that you have installed Node, have ran `npm install bower -g`, deployed the WAR onto Tomcat, and are in this directory in the terminal, this is how you deploy for local dev.

  ```
  npm install
  bower install
  nodemon app.js
  ```

  This connects to the locally running Java on Tomcat and allows you to make edits to your UI without having to constantly re-deploy onto Tomcat. Ask Josh how. He'll learn you some CORS and Server-Client Communication protocol.


## App Structure

  If you want to learn about Angular best practices check out John Papas [github](https://github.com/johnpapa/angular-styleguide/blob/master/a1/README.md).

  Otherwise just take my word for it. Traditionally people organize their Angular apps by the type of components. i.e. Controllers, Views, Services etc. This is fine for small intro projects but becomes VERY confusing the larger your project becomes. The better way to structure it is by more expressive groupings like we have in the "www" folder.

  folder | Info
  ------------------- | ---------------------------
  vendor  | This is where Bower stores those dependencies. If you didn't already know this, RTFM please.
  sections | These are the sections, or rather pages, of your App. i.e. the different URLs you will hit.
  core | These are your data services. These files communicate to the server via Ajax and REST. This is also where the app.js is located.
  components | These are the reusable directives. Think of them as mini sections of view and logic that can be reused on different pages. i.e. there can be several components in one section.

### Angular Components

Comp | Info
------------------- | ---------------------------
View  | This is the CSS and HTML that the user interacts with.
Controller | This is the Javascript logic that controls a view. Views and Controllers have a direct pairing and 1 to 1 relationship.
Service/Factory | These are your data services that communicate to the server. They are utilized by Controllers and other services for all heavy logic and data access.
