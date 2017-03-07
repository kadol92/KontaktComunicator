package app.main;

import controllers.MainController;

/**
 * Created by Kamil on 2016-12-10.
 */
public class TestClass implements Runnable {

     private MainController mainController;

    public TestClass(MainController mainController){
        this.mainController = mainController;

    }

    @Override
    public void run() {
        while(true){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

   /* public void showMessage(){
        mainController.showMessage("Gówno");
    }*/

}
