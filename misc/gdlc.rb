#
# GDLC.rb
#
# Script to start the guideline compiler.
#

home = "m:/GDLC"
#bindir  = "#{home}/bin"
bindir  = "bin"

gdlc = "#{bindir}/gdlc.jar"

command = "java -jar #{gdlc}"

theArgs = "" 

ARGV.each do |arg|
  theArgs.concat(" ").concat(arg)
end

command.concat(" ").concat(theArgs)
#exec(command, theArgs)
exec(command)