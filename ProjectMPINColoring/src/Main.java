import mpi.MPI;

public class Main {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        if (rank == 0) {
            MPINColoring.root();
        } else {
            MPINColoring.child();
        }
        MPI.Finalize();
    }
}
