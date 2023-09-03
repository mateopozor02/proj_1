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
import java.util.function.Predicate;
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

        //Function that gets the date of a dish
        Function<Plato, Calendar> byDate = Plato::getFechaInicio;
        Comparator<Plato> byDateComparator = Comparator.comparing(byDate);

        System.out.println("Platos ordenados por precio: ");
        platos.stream().sorted(byPriceComparator).forEach(p -> {
            p.getComponentes().sort((c1, c2) -> c2.compareTo(c1));
            System.out.println(p);
        });
        System.out.println();

        //Platos agrupados por anio de incio
        System.out.println("Platos agrupados por año: ");

        Map<Integer, List<Plato>> groupedByYear = platos.stream().collect(Collectors.groupingBy(p -> p.getFechaInicio().get(Calendar.YEAR)));
        groupedByYear.forEach((year, platosList) -> {
            System.out.println(year);
            platosList.forEach(p -> System.out.println(p));
            System.out.println();
        });

        //Platos agrupados por rango de precio y ordenados por fecha de inicio 
        System.out.println("Platos agrupados por rango de precio: ");

        Map<String, List<Plato>> groupedByPriceRange = platos.stream().collect(Collectors.groupingBy(p -> {
            if(p.getPrecio() < 10){
                return "Menos de 10";
            }else if(p.getPrecio() >= 10 && p.getPrecio() < 20){
                return "Entre 10 y 20";
            }else{
                return "Mas de 20";
            }
        }));
        groupedByPriceRange.forEach((range, platosList) -> {
            System.out.println(range);
            platosList.stream().sorted(byDateComparator).forEach(p -> System.out.println(p));
            System.out.println();
        });
        System.out.println();

        //Platos agrupados por anio de inicio y ordenados por descripcion
        System.out.println("Platos agrupados por año de inicio y ordenados por descripcion: ");
        Map<Integer, List<Plato>> groupedByYearSortByDescription = platos.stream()
                                                                            .collect(Collectors.groupingBy(p -> p.getFechaInicio().get(Calendar.YEAR)));
        groupedByYearSortByDescription.forEach((year, platosList) -> {
            System.out.println(year);
            platosList.stream().sorted(byDescriptionComparator).forEach(p -> System.out.println(p));
            System.out.println();
        });
        System.out.println();

        //Platos con tres o mas componentes
        Predicate<Plato> hasThreeOrMoreComponents = p -> p.getComponentes().size() >= 3;

        System.out.println("Platos con tres o mas componentes: ");
        platos.stream().filter(hasThreeOrMoreComponents).sorted(byDescriptionComparator).forEach(p -> System.out.println(p));
        System.out.println();

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
