The PDS4 Model component is the umbrella (and web site) for the PDS4 ontology,
data, and information model.  It also includes the Maven POM inherited by the
PDS4 Model sub-componentns.  This site should be online at
https://pds-cm.jpl.nasa.gov/pds4/model/.  If not, run "mvn site:run" and you
can view it locally at http://localhost:8080/.

Because this is a POM project, it should be deployed to the Maven repository 
so that it can be referenced by the child projects. This can be accomplished 
with the following command:

% mvn deploy --non-recursive

Using the "--non-recursive" option will by typical for most commands 
executed against this project so that they are not applied against the 
child projects or modules. The site can be deployed as follows: 

% mvn site-deploy --non-resursive
