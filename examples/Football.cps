Nao Chuck IP 192.234.1.22 PORT 8080;
Mindstorm Hans IP 192.168.1.1 PORT 8090;

context Football {
	activate for {
		Nao a; Nao b;
	} when { bla() && foo() } with bindings {
		a -> SomeRole; b -> OtherRole;
	}
	
	var test: Int = 10;
	
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
			while(true) watchTheGoal()
		}

		void watchTheBall() {
			// some code here
			magic();
		}

		val dis: Int = 10;

		void anotherMedthod() {
			bla();
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
		
		Sender prohibits Reciever;
		Reciever prohibits Sender;
		
		role Receiver playedBy Hans {
			behavior {
				while(true)
					waitForPass()
			}
			
			void waitForPass() {
				// waiting here
			}
		}
	}
}