
This project is dependent on all the other projects by design.
Since the main class, MojoApplication, has to import each 'extension' (we'll call them),
this project has to be dependent on those projects.

For example, for Mojo to be wired into the rest-api extension, this component has
to import the rest-api component. 

# Homebrew Support

A Homebrew formula exists for Mojo. The archive fetched by homebrew is manually created.

## Steps to create the archive:

```[bash]
# Assemble the application's artifacts
# The version property is optional; this is the syntax when used.
gw clean build -Pversion=x.y.z

# Navigate to the build/libs directory.
# This is where the uber jar is written.
cd build/libs

# Create a tar of the mojo-**-all.jar
tar cvf mojo-application-x.y.z.tar mojo-application-x.y.z-all.jar

# GZip the resulting tar file
gzip --best mojo-application-x.y.z.tar

# Copy the gzip file to the homebrew-mojo repository
cp mojo-application-x.y.z.tar.gz ~/workspace/homebrew-mojo

# Navigate to the homebrew-mojo/Formula directory
cd ~/workspace/homebrew-mojo/Formula

#
# -- The remaining steps happen in the homebrew-mojo repository.
#

# Calculate the sha256 of the update gzip file
shasum -a 256 ../mojo-application-x.y.z.tar.gz

# Edit the mojo.rb formula
vi mojo.rb
# - update the version info in the file to match the new version
# - fix the sha256 value to have the new sha256
# - save these changes

# Commit the new gzip file and Formula to git:
git add -f ../mojo-application-x.y.z.tar.gz
git add mojo.rb
git commit -m "publishing version x.y.z"
git push

# Update the local homebrew formula
./update-tap.sh

# Update the homebrew version of mojo
brew upgrade mojo
