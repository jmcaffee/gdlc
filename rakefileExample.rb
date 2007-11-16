task :generate do
# execute code generators
end

task :compile => [:generate] do
# execute javac ant-task
end

task :test => [:generate, :compile] do
# execute junit ant-task
end

task :jar => [:generate, :compile] do
# execute jar ant-task
end

task :all => [:test, :jar]

