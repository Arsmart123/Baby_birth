
/**
 * Write a description of test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.io.File;

public class test {
    public void printNames(){
        FileResource fr = new FileResource();
        for (CSVRecord rec:fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100){
            System.out.println("Name " + rec.get(0)+
                               "Gender " + rec.get(1)+
                               "Num Born " + rec.get(2));
            }
        }
    }
    public void totalBirths(FileResource fr){
        int totalBirths = 0; 
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")){
                totalBoys += numBorn;
            }
            else{
                totalGirls += numBorn;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("total girls = " + totalGirls);
        System.out.println("total boys = " + totalBoys);
    }
    public void totalGirls(FileResource fr){
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){
            if (rec.get(1).equals("M")){
                totalGirls += 1;
            }
        }
        System.out.println("total girls number= " + totalGirls);
    }
    public int numGirls(FileResource fr){
        int countGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){
            if (rec.get(1).equals("F")){
                countGirls ++;
            }
        }
        return countGirls;
    }
    public int getRank(int year,String Name, String gender){
        //假设的前提是文件都是先列女再列男
        int count = 0;
        int none = 0;
        //注意下面的文件是属于testing文件夹的 us_babynames_by_year/yob1905.csv
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+ ".csv");
        for (CSVRecord rec : fr.getCSVParser(false)){
            count ++;
            if (gender.equals("F") && rec.get(1).equals("F") && rec.get(0).equals(Name)){
                return count;
            }
            else if (gender.equals("M") && rec.get(1).equals("M")&& rec.get(0).equals(Name)){
                int GirlsNum = numGirls(fr);
                return (count-GirlsNum);
            }
        }
        if (none == 0){
            return -1;
        }
        return 222;
    }
    public String getName(int year, int rank, String sex){
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+ ".csv");
        //us_babynames_by_year/yob"+year+ ".csv
        if (sex.equals("F")){
            int count_F = 0;
            for (CSVRecord rec : fr.getCSVParser(false)){
                count_F ++;
                if(count_F==rank && rec.get(1).equals("F")){
                    return rec.get(0);
                }
            }
        }
        else if(sex.equals("M")){
            int count_M = 0;
            for (CSVRecord rec : fr.getCSVParser(false)){
                if(rec.get(1).equals("M")){
                    count_M ++;
                    if (count_M == rank && rec.get(1).equals("M")){
                        return rec.get(0);
                    }
                }
            }
        }
        return "NO NAME";
    }
    public String whatIsNameInYear(String name, int year,int newYear, String gender){
        int rank = getRank(year,name,gender); //找到旧年的rank
        String name_return = getName(newYear,rank,gender); //输入新的年
        return name_return;
    }
    public int yearOfHighestRank(String name, String gender){
        int rank0 = 999999999;
        int cc = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f:dr.selectedFiles()){
            //FileResource fr = new FileResource(f);
            String year = f.getName().substring(3,7);
            System.out.println("year "+year);
            int rank = getRank(Integer.parseInt(year),name,gender);  //找不到会返回-1
            System.out.println("rank "+rank);
            if (rank < rank0 && rank > 0){
                rank0 = rank;
            }
            else if(rank == -1){
                cc -= 1;
            }
        }
        System.out.println(rank0);
        if (cc == -3){
            return -1;
        }
        for (File f:dr.selectedFiles()){
            //FileResource fr = new FileResource(f);
            String year = f.getName().substring(3,7);
            int rank = getRank(Integer.parseInt(year),name,gender);  //找不到会返回-1
            if (rank == rank0){
                return Integer.parseInt(year);
            }
        }
        return 999999999;
    }
    public double getAverageRank(String name, String gender){
        double rank0 = 0;
        double count = 0;
        int elsee = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f:dr.selectedFiles()){
            //FileResource fr = new FileResource(f);
            String year = f.getName().substring(3,7);
            int rank = getRank(Integer.parseInt(year),name,gender);  //找不到会返回-1
            if (rank != -1){
                rank0 += rank;
                count ++ ;
            }
            else{
                elsee += -1;
            }
        }
        if (elsee == -3){
            return -1;
        }
        
        return rank0/count;
    }
    public int getTotalBirthsRankedHigher(int year,String name,String sex){
        int rank = getRank(year,name,sex);
        System.out.println(rank);
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+ ".csv");
        if (sex.equals("F")){
            int rank_count = 0;
            int total = 0;
            for (CSVRecord rec : fr.getCSVParser(false)){
                if (rank_count < rank-1 && rec.get(1).equals("F")){
                    rank_count ++;
                    total += Integer.parseInt(rec.get(2));
                }
            }
            return total;
        }
        else if(sex.equals("M")){
            int rank_count = 0;
            int total = 0;
            for (CSVRecord rec : fr.getCSVParser(false)){
                if (rank_count < rank-1 && rec.get(1).equals("M")){
                    total += Integer.parseInt(rec.get(2));
                    rank_count ++;
                }
            }
            return total;
        }
        return 9999;
    }
    public void testgetTotalBirthsRankedHigher(){
        int total = getTotalBirthsRankedHigher(1990,"Drew","M");
        System.out.println(total);
    }
    public void testgetAverageRank(){
        double avg = getAverageRank("Robert", "M");
        System.out.println(avg);
    }
    public void testyearOfHighestRank(){
        int year = yearOfHighestRank("Mich", "M");
        System.out.println(year);
    } 
    public void testTotalBirths(){
        FileResource fr = new FileResource("testing/yob2012short.csv");
        totalBirths(fr);
    }
    public void testtotalGirls(){
        FileResource fr = new FileResource("us_babynames_by_year/yob1905.csv");
        totalGirls(fr);
    }
    public void testgetRank(){
        int rank = getRank(1971,"Frank","M");
        System.out.println(rank);
    }
    public void testgetName(){
        String name = getName(1982, 450, "M");
        System.out.println(name);
    }
    public void testwhatIsNameInYear(){
        String old_name = "Owen";
        int old_year = 1974;
        int new_year = 2014;
        String sex = "M";
        String new_name = whatIsNameInYear(old_name,old_year,new_year,sex);
        System.out.println(old_name + " born in " + old_year + " would be " + new_name
                + " if it was born in " + new_year);
    }
}
