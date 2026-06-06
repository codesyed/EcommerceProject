package com.bank.rough;
import java.util.*;

class SubsequenceIdealPairs {

    static int maxDiversity(int[] A, int M) {
        int s=0;
        int e=0;

        if(M <= 0 || M>A.length)return -1;

        Map<Integer, Integer>map=new HashMap<>();
        int ans = 1;

        //1st window done
        while(e<M){
            int elem = A[e];
            map.put(elem, map.getOrDefault(elem, 0)+1);
            e++;
        }
        ans = Math.max(ans, map.size());

        //remaining windows
        while(e<A.length){

            //add
            int End = A[e];
            map.put(End, map.getOrDefault(End, 0)+1);

            int Start = A[s];
            map.put(Start, map.getOrDefault(Start, 0)-1);

            if(map.get(Start) <= 0)map.remove(Start);

            ans = Math.max(ans, map.size());
            s++;
            e++;
        }
        return ans;
    }

    static int compatiblePairs(int[] A, int[] B) {
        Arrays.sort(A);

        //Sorting int array in reverse order using stream
        B=Arrays
                .stream(B) //Stream of int
                .boxed() //Stream of Int
                .sorted(Collections.reverseOrder()) //Stream of sorted in reverseOrder
                .mapToInt(a->a) //Int -> to 'int' IntStream
                .toArray(); //array

        int cnt = 0;
        for(int i=0; i<A.length; i++){
            int a = A[i];
            int b = B[i];
            if((a%b==0 && b!=0) ||(b%a==0 && a!=0))cnt++;
        }

        if(cnt==0)return -1;
        return cnt;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int M = sc.nextInt();

        int[] A = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = sc.nextInt();
        }

        int[] B = new int[N];
        for (int i = 0; i < N; i++) {
            B[i] = sc.nextInt();
        }

        sc.close();

        int diversityScore = maxDiversity(A, M);
        int compatiblePairsCount = compatiblePairs(A, B);

        System.out.println(diversityScore);
        System.out.println(compatiblePairsCount);
    }
}