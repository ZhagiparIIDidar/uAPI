
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ResearchPaper {

    /**
     * Default constructor
     */
    public ResearchPaper() {
    }

    /**
     * 
     */
    public void title: String;

    /**
     * 
     */
    public void authors: List;

    /**
     * 
     */
    public void journal: String;

    /**
     * 
     */
    public void date: Date;

    /**
     * DOI — это уникальный идентификатор научной статьи в интернете. Как ссылка но короткая, например:
     * 10.1109/ACCESS.2022.123456
     * По этому коду можно найти статью в любой научной базе
     */
    public void doi: String;

    /**
     * Citations — это количество раз когда другие учёные сослались на эту статью в своих работах. Чем больше citations — тем важнее и популярнее статья в науке.
     * Именно из citations считается h-index исследователя — чем больше citations у его статей, тем выше h-index.
     */
    public void citations: int;

    /**
     * 
     */
    public void pages: int;



    /**
     * 
     */
    public void getCitations(): int() {
        // TODO implement here
    }

    /**
     * 
     */
    public void getPages(): int() {
        // TODO implement here
    }

    /**
     * 
     */
    public void toString(): String() {
        // TODO implement here
    }

}