Nao Chuck IP 192.168.130.1 PORT 8080;
Nao Hans IP 192.168.130.2 PORT 8090;

context PingPong {
	activate for {
		Nao Chuck; Nao Hans;
	} when(500) { true /* start immediately */} with bindings {
		Chuck -> Ping; Hans -> Pong;
	}

	activate for {
        Nao Chuck; Nao Hans;
    } when(10000) { true /* stop immediately after 10sec*/} with bindings {
        Chuck -> Stop; Hans -> Stop;
    }

	Ping prohibits Pong;
	Stop prohibits Ping;
	Stop prohibits Pong;

	val ping: Int = 0;
	val pong: Int = 1;

	// we start with ping
	var sound: Int = ping;
	
	role Ping playedBy Nao {
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

	role Pong playedBy Nao {
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

    role Stop playedBy Nao {
        behavior {
            exit(0)
        }
    }
}