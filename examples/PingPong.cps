Nao Chuck IP 192.168.130.1 PORT 8080

context PingPong {
	activate for {
		Nao a;
	} when { true } with bindings {
		a -> Ping;
	} with settings {
        interval 500;
    }

    activate for {
        Ping b;
    } when { true } with bindings {
        b -> Pong;
    } with settings {
        interval 1000;
        after 1000;
        continuously true;
    }

    activate for {
        Pong c;
    } when { true } with bindings {
        c -> Ping;
    } with settings {
        interval 1000;
        after 1000;
        continuously true;
    }

	activate for {
        Nao e;
    } when { true } with bindings {
        e -> Stop;
    } with settings {
        interval 500;
        after 10000;
    }

	Ping prohibits Pong;
	Stop prohibits Ping;
	Stop prohibits Pong;

	singleton role Ping {
		behavior {
		    println("ping")
		}
	}

	singleton role Pong {
        behavior {
            println("pong")
        }
    }

    role Stop {
        behavior {
            println("and stop")
            System.exit(0)
        }
    }
}