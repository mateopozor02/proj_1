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

    public static Plato lineToPlato(String line){
        String[] parts = line.split(";");

        String description = parts[0];

        String day = parts[1].split("-")[0];
        String month = parts[1].split("-")[1];
        String year = parts[1].split("-")[2];

        Calendar date = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        float price = Float.parseFloat(parts[2]);

        List<String> componentsList = Arrays.asList(parts[3].split(","));

        return new Plato(description, date, price, componentsList);
    }


    public static void main(String[] args){

        List<Plato> platos = new ArrayList<>();

        try(Stream<String> stream = Files.lines(Paths.get("data.txt"))){
            stream.forEach(line -> platos.add(lineToPlato(line)));
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        // display de todos los platos
        //platos.stream().forEach(System.out::println);


        // 4) Lista de componentes ordenada alfabeticamente, lista de componente ordenada alfabéticamente
        Function<Plato, String> byDescription = Plato::getDescripcion;
        Comparator<Plato> byDescriptionComparator = Comparator.comparing(byDescription);

        System.out.println("4) Platos ordenados por descripcion y componentes ordenados alfabeticamente: ");
        platos.stream().sorted(byDescriptionComparator).forEach(p -> {
            p.getComponentes().sort((c1, c2) -> c1.compareTo(c2));
            System.out.println(p);
        });
        System.out.println();


        // 5) Lista de platos ordenada por precio, lista de componente ordenada alfabéticamente en sentido inverso
        // Function that gets the price of a dish
        Function<Plato, Float> byPrice = Plato::getPrecio;
        Comparator<Plato> byPriceComparator = Comparator.comparing(byPrice);

        // Function that gets the date of a dish
        Function<Plato, Calendar> byDate = Plato::getFechaInicio;
        Comparator<Plato> byDateComparator = Comparator.comparing(byDate);

        System.out.println("5) Platos ordenados por precio, lista de componente ordenada alfabeticamente en sentido inverso : ");
        platos.stream().sorted(byPriceComparator).forEach(p -> {
            p.getComponentes().sort((c1, c2) -> c2.compareTo(c1));
            System.out.println(p);
        });
        System.out.println();


        // 6) Platos agrupados por anio de incio
        System.out.println("6) Platos agrupados por anio de inicio: ");

        Map<Integer, List<Plato>> groupedByYear = platos.stream().collect(Collectors.groupingBy(p -> p.getFechaInicio().get(Calendar.YEAR)));
        groupedByYear.forEach((year, platosList) -> {
            System.out.println(year);
            platosList.forEach(p -> System.out.println(p));
            System.out.println();
        });


        // 7) Platos agrupados por rango de precio y ordenados por fecha de inicio
        System.out.println("7) Platos agrupados por rango de precio y ordenados por fecha de inicio : ");

        Map<String, List<Plato>> groupedByPriceRange = platos.stream().collect(Collectors.groupingBy(p -> {
            if(p.getPrecio() < 5){
                return "Menos de 5";
            }else if(p.getPrecio() >= 5 && p.getPrecio() < 10){
                return "Entre 5 y 10";
            }else{
                return "Mas de 10";
            }
        }));
        groupedByPriceRange.forEach((range, platosList) -> {
            System.out.println(range);
            platosList.stream().sorted(byDateComparator).forEach(p -> System.out.println(p));
            System.out.println();
        });
        System.out.println();


        // 8) Platos agrupados por anio de inicio y ordenados por descripcion
        System.out.println("8) Platos agrupados por año de inicio y ordenados por descripcion: ");
        Map<Integer, List<Plato>> groupedByYearSortByDescription = platos.stream()
                .collect(Collectors.groupingBy(p -> p.getFechaInicio().get(Calendar.YEAR)));
        groupedByYearSortByDescription.forEach((year, platosList) -> {
            System.out.println(year);
            platosList.stream().sorted(byDescriptionComparator).forEach(p -> System.out.println(p));
            System.out.println();
        });
        System.out.println();


        // 9) Platos con tres o mas componentes ordenados por descripcion
        Predicate<Plato> hasThreeOrMoreComponents = p -> p.getComponentes().size() >= 3;

        System.out.println("9) Platos con tres o mas componentes ordenados por descripcion: ");
        platos.stream().filter(hasThreeOrMoreComponents).sorted(byDescriptionComparator).forEach(p -> System.out.println(p));
        System.out.println();


        // 10) Platos que no tienen arroz ni frejol, ordenados por descripcion

        System.out.println("10) Platos que no tienen arroz ni frejol, ordenados por descripcion: ");

        Predicate<Plato> acceptedComponents= p -> !p.getComponentes().contains("arroz") &&  !p.getComponentes().contains("frejol");
        platos.stream().filter(acceptedComponents).sorted(byDescriptionComparator).forEach(p -> System.out.println(p));

    }


}
