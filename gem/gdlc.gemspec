# -*- encoding: utf-8 -*-
# ft: ruby
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'gdlc/version'

file_list =  Dir['.*'] + Dir['*']
file_list += Dir['bin/*']
file_list += Dir['docs/*']
file_list += Dir['lib/**/*.rb']
file_list += Dir['res/*']
file_list += Dir['spec/*']

file_list.reject! { |fn| File.directory?(fn) }

Gem::Specification.new do |gem|
  gem.name          = "gdlc"
  gem.version       = Gdlc::VERSION
  gem.authors       = ["Jeff McAffee"]
  gem.email         = ["jeff@ktechsystems.com"]
  gem.description   = %q{Ruby support for GDLC compiler}
  gem.summary       = gem.description
  gem.homepage      = "https://github.com/jmcaffee/gdlc"
  gem.license       = "GPL-3.0"

  gem.files         = file_list #`git ls-files`.split($/)
  gem.executables   = gem.files.grep(%r{^bin/}).map{ |f| File.basename(f) }
  gem.test_files    = gem.files.grep(%r{^(test|spec|features)/})
  gem.require_paths = ["lib"]

  gem.add_development_dependency 'rspec'
end

