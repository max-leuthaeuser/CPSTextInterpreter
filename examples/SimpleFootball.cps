Nao Chuck IP 192.168.130.1 PORT 8080 with priorities {
    Sender -> 1;
    GoodPlayer -> 2;
}
Nao Hans IP 192.168.130.2 PORT 8090 with priorities {
    Sender -> 1;
    GoodPlayer -> 1;
}

context Football {
	activate for {
		Chuck c1; Hans h1;
	} when { true /* start immediately */} with bindings {
		c1 -> GoodPlayer; h1 -> BadPlayer;
	} with settings {
        interval 500;
        // timeout can be left out, standard is 0
    }

    activate for {
        Chuck c3; Hans h3;
    } when { true } with bindings {
        c3 -> Stop; h3 -> Stop;
    } with settings {
        interval 500;
        /* stop immediately after 10sec */
        timeout 10000;
    }

    context GoalShot {
        activate for {
            Chuck c2;
        } when { c1.foundTheBall() } with bindings {
            c2 -> Sender;
        } with settings {
            interval 500;
            timeout 1000;
        }

		activate for {
            Chuck h2;
        } when { h1.foundTheBall() } with bindings {
            h2 -> Sender;
        } with settings {
            interval 500;
            timeout 1000;
        }

        role Sender playedBy NaoRobot {
            behavior {
                println("aaaand shot!")
            }
        }
    }

    role BadPlayer playedBy NaoRobot {
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

    role GoodPlayer playedBy NaoRobot {
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

    role Stop playedBy NaoRobot {
        behavior {
            println("and stop")
            System.exit(0)
        }
    }
}