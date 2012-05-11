Nao Chuck IP 192.168.130.1 PORT 8080
Nao Hans IP 192.168.130.2 PORT 8090

context Football {
	activate for {
		Nao a; Nao b;
	} when { true } with bindings {
		a -> GoodPlayer; b -> BadPlayer;
	} with settings {
        interval 500;
        // after can be left out, standard is 0
    }

    activate for {
        Nao x;
    } when { true } with bindings {
        x -> Stop;
    } with settings {
        interval 500;
        /* stop immediately after 10sec */
        after 10000;
    }

    context GoalShot {
        activate for {
            GoodPlayer c;
        } when { c.foundTheBall() } with bindings {
            c -> Sender;
        } with settings {
            interval 500;
            after 1000;
        }

        role Sender {
            behavior {
                println("aaaand shot!")
            }
        }
    }

    role BadPlayer {
        behavior {
            println("Ok, lets play football.")
        }

        Boolean foundTheBall() {
            println("I am looking for it.")
            Thread.sleep(8000)
            println("There it is!")
            true
        }
    }

    role GoodPlayer {
        behavior {
            println("Ok, lets play football.")
        }

        Boolean foundTheBall() {
            println("I am looking for it.")
            Thread.sleep(2000)
            println("There it is!")
            true
        }
    }

    role Stop {
        behavior {
            println("and stop")
            System.exit(0)
        }
    }
}