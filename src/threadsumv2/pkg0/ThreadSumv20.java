/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsumv2.pkg0;

import java.util.Random;

/**
 *
 * @author Stefan-IonutCreanga
 */
public class ThreadSumv20 extends Thread {

        private int[] arr;

        private int low, high, partial;

        //cu acest constructor o sa instantiem fiecare thread in parte 
        public ThreadSumv20(int[] arr, int low, int high)
        {
            this.arr = arr;
            this.low = low;
            this.high = Math.min(high, arr.length);
        }
        
        //getter pt campul partial
        public int getPartialSum()
        {
            
            return partial;
            
        }
        
        public static int sum(int[] arr, int low, int high)
        {
            int total = 0;

            for (int i = low; i < high; i++) {
                total += arr[i];
            }

            return total;
        }
        
        //VIP-ul
         public static int parallelSum(int[] arr, int threads)
        {
            //100 000 000/4 = 25 000 000
            //Math.ceil - rotunjeste prin adaos 
            int size = (int) Math.ceil(arr.length * 1.0 / threads);

            //asta este posibil datorita faptului ca mostenim clasa Thread si folosim contructorul ei
            //practic pot sa declar un grup de threduri
            ThreadSumv20[] sums = new ThreadSumv20[threads];

            //pornim thredurile, fiecare in parte, si impartim treaba (numarul)
            for (int i = 0; i < threads; i++) {
                //aici se intampla magia cu impartitul
                //eg. (arr, 25 000 000*0, 25 000 000*1)
                sums[i] = new ThreadSumv20(arr, i * size, (i + 1) * size);
                
                //practic apelam methoda run()
                sums[i].start();
            }
            
            //luam fiecare thread in parte si ii dam join sa nu se simta singure  
            try {
                for (ThreadSumv20 sum : sums) {
                   
                    sum.join();
                }
            } catch (InterruptedException e) { System.out.println(":( a dat fail :("); }

            int total = 0;
            
            //adunam sumele partiale pe care thredurile le-au calculat in methoda run() cand am apelat start()
            for (ThreadSumv20 sum : sums) {
                total += sum.getPartialSum();
            }

            return total;
        }
        
      
        public static int parallelSum(int[] arr)
        {
            return parallelSum(arr, Runtime.getRuntime().availableProcessors());
        }
        
        
        //asta se apeleaza pt fiecare thread in parte dupa ce methoda nume_thread.start() este apelata
        //fiecare thread lucareza cu instanta ei de clasa, in felul asta nu avem nevoie de mutex 
        @Override
        public void run()
        {
            partial = sum(arr, low, high);
            //nu stiu sa adaug si nume thredului care se ocupa de treaba :( si printez ceva generic 
            System.out.println("Suma partiala: "+ partial);
        }
    
    
    
    public static void main(String[] args) {
    
    //geneream un numar aleator    
    Random rand = new Random();

    int[] arr = new int[100000000];
    
    for (int i = 0; i < arr.length; i++) {
        arr[i] = rand.nextInt(101) + 1; // 1..100
       
    }

    System.out.println("Suma toatala:" + ThreadSumv20.parallelSum(arr));
    

    }
}
