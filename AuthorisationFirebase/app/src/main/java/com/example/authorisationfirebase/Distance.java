package com.example.authorisationfirebase;

public class Distance {

    /**
     * A simple abstraction of a city. This class maintains Cartesian coordinates
     * and also knows the Pythagorean theorem.
     *
     * @author bkanber
     *
     */
        private double xAndY;

        /**
         * Initalize a city
         *
         *
         */
        public Distance(double xAndY) {
            this.xAndY = xAndY;

        }

        public Distance(){}


        public double distanceValues(Distance distance){


            return 0;

        }

    /**
     * Get x position of city
     *
     * @return x X position of city
     */
    public double getXandY() {
        return this.xAndY;
    }


    // Возвращает следующую в лексикографическом порядке перестановку элементов массива Permutation
    public static double[] NextPermutation(double[] permutation) throws Exception
    {
        //алгоритм Нарайаны

        //шаг 1
        int j = permutation.length - 2;
        while (permutation[j] >= permutation[j + 1])
        {
            j--;

            if (j == -1)
                throw new Exception("Текущая перестановка является максимальной");
        }

        //шаг 2
        int l = permutation.length - 1;
        while (permutation[j] >= permutation[l])
            l--;
        double temp = permutation[j];
        permutation[j] = permutation[l];
        permutation[l] = temp;

        //шаг 3
        j++;
        l = permutation.length - 1;
        while (j < l)
        {
            temp = permutation[j];
            permutation[j] = permutation[l];
            permutation[l] = temp;
            j++;
            l--;
        }

        return permutation;
    }

    public static int Factorial(int n) throws Exception
    {
        if (n < 0)
            throw new Exception("Факториал отрицательного числа не может быть вычислен");

        if (n == 0)
            return 1;

        int f = 1;
        while (n > 0)
        {
            f *= n;
            n--;
        }

        return f;
    }



    public static double[][] GetAllPermutations(double[] permutation)
    {
        double[][] allPermutations=new double[1][1];
        try {
            allPermutations = new double[Factorial(permutation.length)][permutation.length];
        }
        catch (Exception exc)
        {
            System.out.println(exc);
        }

        for (int j = 0; j < permutation.length; j++)
        {
            allPermutations[0][ j] = permutation[j];
        }

        for (int i = 1; i < allPermutations.length; i++)
        {
            try {
                permutation = NextPermutation(permutation);
            }
            catch (Exception exc)
            {
                System.out.println(exc);
            }
            for (int j = 0; j < permutation.length; j++)
            {
                allPermutations[i][ j] = permutation[j];
            }
        }

        return allPermutations;
    }


    // Возвращает длину пути в графе. Если path состоит из {0, 1, 2, 3, 4 }, то будет вычислена длина пути 0->1->2->3->4
    public static double LengthOfPath(Double[][] adjacencyMatrix, double[] path)
    {
        int length = 0;

        for (int i=0; i<path.length-1; i++)
        {
            length += adjacencyMatrix[(int) path[i]][(int) path[i + 1]];
        }

        return length;
    }

    /// Возвращает все пути, по которым коммивояжёр может выйти из города 0, обойти все остальные города (без повторений) и вернуться обратно в город 0.
    public static double[][] GetAllPaths(Double[][] adjacencyMatrix)
    {
        double[] intermediateVertexes = new double[adjacencyMatrix[0].length-1];
        for (int i = 0; i < intermediateVertexes.length; i++)
            intermediateVertexes[i] = i+1;

        double[][] intermediatePaths = GetAllPermutations(intermediateVertexes);

        double[][] allPath = new double[intermediatePaths.length][ intermediatePaths[0].length + 2];
        for (int i=0; i<intermediatePaths.length; i++)
        {
            for (int j=0; j<intermediatePaths[i].length; j++)
            {
                allPath[i][ j + 1] = intermediatePaths[i][ j];
            }
        }

        return allPath;
    }

    /// Возвращает лучший путь для коммивояжёра среди всех возможных и его длину
    public static double[] GetBestPath(Double[][] adjacencyMatrix)
    {
        double[][] allPaths = GetAllPaths(adjacencyMatrix);
        int minPathIndex = 0;
        double minPathLength = Integer.MAX_VALUE;
        double[] currentPath = new double[allPaths[0].length];
        double currentLength;

        for (int i=0; i<allPaths.length; i++)
        {
            for (int j=0; j< allPaths[i].length; j++)
            {
                currentPath[j] = allPaths[i][ j];
            }

            currentLength = LengthOfPath(adjacencyMatrix, currentPath);

            if (currentLength<minPathLength)
            {
                minPathLength = currentLength;
                minPathIndex = i;
            }
        }

        double[] bestPath = new double[allPaths[0].length];
        for (int j=0; j< allPaths[0].length; j++)
        {
            bestPath[j] = allPaths[minPathIndex][ j];
        }

        return bestPath;
    }

}
