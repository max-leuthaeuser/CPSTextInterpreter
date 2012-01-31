Nao Chuck IP 192.168.130.1 PORT 8080;
Nao Hans IP 192.168.130.2 PORT 8090;

context PingPong {
	activate for {
		Chuck chuck; Hans hans;
	} when { true /* start immediately */} with bindings {
		chuck -> Ping; hans -> Pong;
	} with settings {
        interval 500;
        // timeout can be left out, standard is 0
    }

	activate for {
        Chuck chuck; Hans hans;
    } when { true } with bindings {
        chuck -> Stop; hans -> Stop;
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
    }

	role Ping playedBy Sound {
		behavior {
		    while(true) {
                if (sound==pong) {
                    println("ping")
                    Thread.sleep(500)
                    sound = ping
                }
            }
		}
	}

	role Pong playedBy Sound {
        behavior {
            while(true) {
                if (sound==ping) {
                    println("pong")
                    Thread.sleep(500)
                    sound = pong
                }
            }
        }
    }

    role Stop playedBy NaoRobot {
        behavior {
            exit()
        }
    }
}