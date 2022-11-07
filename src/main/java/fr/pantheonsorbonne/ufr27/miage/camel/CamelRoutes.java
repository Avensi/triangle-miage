package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.model.Triangle;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {
    @Override
    public void configure() {
        from("file:./target/data/triangles?noop=true")
                .unmarshal().csv()
                .process(new TriangleProcessor())
                .process(new EquilateralProcessor())
                .choice()
                .when(header("isEquilateral").isEqualTo("true")).marshal().jacksonXml(Triangle.class)
                .to("jms:queue/miage.LI.equilateral")
                .otherwise().marshal().jacksonXml(Triangle.class)
                .to("jms:queue/miage.LI.autre");
        from("jms:queue/miage.LI.autre")
                .unmarshal().jacksonXml(Triangle.class)
                .process(new PerimeterComputer())
                .marshal().json()
                .to("file:./target/data/calcs");
        from("jms:queue/miage.LI.equilateral")
                .unmarshal().jacksonXml(Triangle.class)
                .process(new PerimeterComputer())
                .marshal().json()
                .to("file:./target/data/calcs");

    }

    private static class TriangleProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {

            List<List<String>> points = (List<List<String>>) exchange.getMessage().getBody();
            Triangle triangle = new Triangle();
            triangle.setPoint(triangle.parsePoints(points));
            exchange.getMessage().setBody(triangle);


        }
    }

    private static class EquilateralProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            Triangle triangle = exchange.getMessage().getBody(Triangle.class);

            if (triangle.isEquilateral()){
                exchange.getMessage().setHeader("isEquilateral","true");
            }else{
                exchange.getMessage().setHeader("isEquilateral","false");
            }


        }
    }

    private static class PerimeterComputer implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            Triangle triangle = exchange.getMessage().getBody(Triangle.class);
            Map<String, Double> perimetre = new HashMap<>();
            perimetre.put("perimetre", triangle.getPerimeter());
            exchange.getMessage().setBody(perimetre);

        }
    }


}
