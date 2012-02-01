Nao Chuck IP 192.168.130.1 PORT 8080;
Nao Hans IP 192.168.130.2 PORT 8090;

context PingPong {
	activate for {
		Chuck c1; Hans h1;
	} when { true /* start immediately */} with bindings {
		c1 -> Ping; h1 -> Pong;
	} with settings {
        interval 500;
        // timeout can be left out, standard is 0
    }

	activate for {
        Chuck c2; Hans h2;
    } when { true } with bindings {
        c2 -> Stop; h2 -> Stop;
    } with settings {
        interval 500;
        /* stop immediately after 10sec */
        timeout 10000;
    }

	Ping prohibits Pong;
	Stop prohibits Ping;
	Stop prohibits Pong;

	val ping: Int = 0;
	val pong: Int = 1;

	// we start with ping
	var sound: Int = ping;

	role Sound playedBy NaoRobot {
    		behavior {}

    		void greet() {
    		    println("cu")
    		}
    }

	role Ping playedBy Sound {
		behavior {
		    println("ping")
		}
	}

	role Pong playedBy Sound {
        behavior {
            println("pong")
        }

        void greet() {
            println("bb")
        }
    }

    role Stop playedBy NaoRobot {
        behavior {
            println("aaand stop")
            c1.greet
            h1.greet
            System.exit(0)
        }
    }
}