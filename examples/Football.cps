import {
    java.util.Date;
    java.util.StringBuilder;
    java.util._;
    java.util.*;
}

Nao Chuck IP 192.234.1.22 PORT 8080;
Nao Hans IP 192.168.1.1 PORT 8090;

context Football {
	activate for {
		Nao a; Nao b;
	} when(200) { bla() && foo() } with bindings {
		a -> SomeRole; b -> OtherRole;
	}

	FieldPlayer prohibits Keeper;
	
	role FieldPlayer playedBy Hans {
		behavior {
			// not implemented yet
		}
		
		var x: Float;
		var y: Float;
	}
	
	role Keeper playedBy Chuck {
		behavior {
			while(true) {
			    watchTheGoal()
			    bla()
			    foo()
			    if(true) { print() }
			}
		}

		void watchTheBall() {
			// some code here
			magic();
		}

		val dis: Int = 10;

		void anotherMethod() {
		    if(true) {
			    bla();
			}
		}
	}
	
	val blub: Int = 11;
	
	context Pass {
		activate for {
			Nao r; Nao s;
		} when { passPossible() } with bindings {
			r -> Reciever;
			s -> Sender;
		}

        activate for {
            Nao a; Nao b;
        } when { !other() } with bindings {
            a -> AnotherRole; b -> SomeOtherRole;
        }
		
		role Sender playedBy Chuck {
			behavior {
				if(possible())
					pass()
			}
			
			void pass() {
				shootTheBall();
			}
 
			void look() {
				// magic or so
			}
		}
		
		Sender prohibits Receiver;
		Receiver prohibits Sender;
		
		role Receiver playedBy Hans {
			behavior {
				while(true)
					waitForPass()
			}
			
			var test: Int = 10;
			
			void waitForPass() {
				// waiting here
			}
		}
	}
}