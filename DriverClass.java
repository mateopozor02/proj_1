import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.stream.Stream;
import java.util.function.Function; 
import java.util.stream.Collectors; 
import java.util.Map;

public class DriverClass {
    public static void main(String[] args){

        List<Plato> platos = new ArrayList<>();

        try(Stream<String> stream = Files.lines(Paths.get("data.txt"))){
            stream.forEach(line -> platos.add(lineToPlato(line)));
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        //Lista de componentes ordenada alfabeticamente para cada plato 

        Function<Plato, String> byDescription = Plato::getDescripcion;
        Comparator<Plato> byDescriptionComparator = Comparator.comparing(byDescription);

        System.out.println("Platos ordenados por descripcion: ");
        platos.stream().sorted(byDescriptionComparator).forEach(p -> {
            p.getComponentes().sort((c1, c2) -> c1.compareTo(c2));
            System.out.println(p);
        });
        System.out.println();
        
        //Function that gets the price of a dish
        Function<Plato, Float> byPrice = Plato::getPrecio; 
        Comparator<Plato> byPriceComparator = Comparator.comparing(byPrice);

        System.out.println("Platos ordenados por precio: ");
        platos.stream().sorted(byPriceComparator).forEach(p -> {
            p.getComponentes().sort((c1, c2) -> c2.compareTo(c1));
            System.out.println(p);
        });
        System.out.println();

        //group Platos by year 
        System.out.println("Platos agrupados por año: ");

        Map<Integer, List<Plato>> groupedByYear = platos.stream().collect(Collectors.groupingBy(p -> p.getFechaInicio().get(Calendar.YEAR)));
        groupedByYear.forEach((year, platosList) -> {
            System.out.println(year);
            platosList.forEach(p -> System.out.println(p));
            System.out.println();
        });

    }

    public static Plato lineToPlato(String line){
        String[] parts = line.split(" "); 

        String description = parts[0];

        String day = parts[1].split("-")[0];
        String month = parts[1].split("-")[1];
        String year = parts[1].split("-")[2];

        Calendar date = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        float price = Float.parseFloat(parts[2]);

        List<String> componentsList = Arrays.asList(parts[3].split(","));

        return new Plato(description, date, price, componentsList);
    }
}
