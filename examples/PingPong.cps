Nao Chuck IP 192.168.130.1 PORT 8080;
Nao Hans IP 192.168.130.2 PORT 8090;

context PingPong {
	activate for {
		Nao a; Nao b;
	} when { true /* start immediately */} with bindings {
		a -> Ping; b -> Pong;
	} with settings {
        interval 500;
        // timeout can be left out, standard is 0
    }

	activate for {
        Nao c;
    } when { true } with bindings {
        c -> Stop;
    } with settings {
        interval 500;
        /* stop immediately after 10sec */
        timeout 10000;
    }

	Ping prohibits Pong;
	Stop prohibits Ping;
	Stop prohibits Pong;

	role Ping {
		behavior {
		    println("ping")
		}

		void greet() {
            println("cu")
        }
	}

	role Pong {
        behavior {
            println("pong")
        }

        void greet() {
            println("bb")
        }
    }

    role Stop {
        behavior {
            println("and stop")
            a.greet
            b.greet
            System.exit(0)
        }
    }
}