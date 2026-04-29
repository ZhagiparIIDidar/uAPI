import java.io.*;
import java.util.*;
package research;
/**
 * Represents a scientific research paper.
 */
public class ResearchPaper {

    private String title;
    private List<String> authors = new ArrayList<>();
    private String journal;
    private Date date;
    private String doi;
    private int citations;
    private int pages;

    public ResearchPaper() {}

    public ResearchPaper(String title, String journal, Date date, String doi, int citations, int pages) {
        this.title = title;
        this.journal = journal;
        this.date = date;
        this.doi = doi;
        this.citations = citations;
        this.pages = pages;
    }

    public String getTitle()              { return title; }
    public void setTitle(String title)    { this.title = title; }
    public List<String> getAuthors()      { return authors; }
    public void addAuthor(String author)  { authors.add(author); }
    public String getJournal()            { return journal; }
    public void setJournal(String j)      { this.journal = j; }
    public Date getDate()                 { return date; }
    public void setDate(Date date)        { this.date = date; }
    public String getDoi()                { return doi; }
    public void setDoi(String doi)        { this.doi = doi; }
    public void setCitations(int c)       { this.citations = c; }
    public void setPages(int p)           { this.pages = p; }

    public int getCitations() {
        return citations;
    }

    
    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return String.format("ResearchPaper{title='%s', journal='%s', citations=%d, pages=%d, doi=%s}",
                title, journal, citations, pages, doi);
    }
}
