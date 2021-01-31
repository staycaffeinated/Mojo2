
This project is dependent on all the other projects by design.
Since the main class, MojoApplication, has to import each 'extension' (we'll call them),
this project has to be dependent on those projects.

For example, for Mojo to be wired into the rest-api extension, this component has
to import the rest-api component. 
