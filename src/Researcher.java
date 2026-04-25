
import java.io.*;
import java.util.*;

/**
 * 
 */
public interface Researcher {



    /**
     * 
     */
    public void getHIndex(): int();

    /**
     * 
     */
    public void printPapers(c: Comparator): void();

    /**
     * 
     */
    public void addPaper(): void();

    /**
     * 
     */
    public void joinProject(): void();

}