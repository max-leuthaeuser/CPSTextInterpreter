import os
import subprocess

# number of objects for pattern matching
xl = range(2, 6, 1)
# on this positions
yl = range(2, 11)

def generate(x, y):
	# generation
	result = []

	for i in range(0, x):
		result.append("Nao C%s IP 192.168.0.%s PORT %s" %(str(i), str(i%255), str(i+1)))
		
	result.append("context Test {")	

	# activation block
	result.append("activate for {")
	for i in range(0, y):	
		result.append("Nao v%s;" % str(i))
	result.append("} when {")
		
	result.append(" true ")

	result.append("} with bindings {")
	for i in range(0, y):	
		result.append("v%s -> Role%s;" %(str(i), str(i%x)))
	result.append("} with settings { interval 0; after 0; continuously false; }")

	for i in range(0, x):	
		result.append("role Role%s {" % str(i))	
		result.append("behavior {}")
		result.append("}")

	result.append("}")

	# write to file
	f = open("out.cps", "wt")
	f.write('\n'.join(result))
	f.close()

	
def run(x, y):
	proc = subprocess.Popen(['java', '-jar', 'CPSTextInterpreter.jar', '-i', 'out.cps'], stdout=subprocess.PIPE)

	#mem = str(subprocess.Popen([r'C:\WINDOWS\system32\WindowsPowerShell\v1.0\powershell.exe', '-ExecutionPolicy', 'Unrestricted', 'Get-Process java* | select -exp ws'], stdout=subprocess.PIPE).communicate()[0])
	
	out = str(proc.communicate()[0]).split("#")
	time = (out[len(out)-2][:-4]).split(": ")[1]
	print("[n=%s, k=%s] Time: %s" %(str(x), str(y), time))
	

for x in xl:
	for y in yl:
		generate(x, y)
		run(x, y)